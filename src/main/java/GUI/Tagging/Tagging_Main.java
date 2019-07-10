package GUI.Tagging;

/*
 * Get values (Main)
 * version: July 06, 2019 15:03 PM
 * Last revision: July 11, 2019 07:16 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;

public class Tagging_Main 
{
	// Storage 
	FileOutputStream writer;
	PrintStream ps;
		
	// Parameters
		private String fileName_index = "";
		private String artileID_index = "";
	
	private boolean filestartPoint = false;
	private boolean startPoint = false;
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
		
	// article content
	private String articleId;
	private String author;
	private String title;
	private String content;
	private String date;
	private int messagesCount;
	
	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	// Regular expression
	Pattern pattern;
	Matcher matcher;
	String regexTitle = "標的?";
	// display
	Vector companyIdDisplay = new Vector();
	Vector companyNameDisplay = new Vector();
	Vector valueDisplay = new Vector();
	
	// Testing
	private String contentTmp = "1. 標的：6558興能高 2. 分類：短、中多 3. 分析/正文： 貿易戰暫告一段落，行動裝置鋰電池應會再度回到熱門市場中。貿易戰之前這支已經拉了 一波，前高75。隨著貿易戰進行，穿戴裝置市場保守，掉到50底，後轉強。 昨天貿易戰中場嘉年華，這支獲得跳空缺口（66.5跳69），60ma強勢上揚，搭配之前就已 擺好的5ma、10ma、20ma，均線皆已上揚且依序排列。 K值雖已達87.5，但高檔鈍化可能性高。 4. 進退場機制：(非長期投資者，必須有停損機制) 今早洗盤68已進 分段停利：73起 加碼區：66.568.5 停損：55";
	
	public Tagging_Main() throws Exception
	{
		// automatic tagging
			// Company info.
			ReadCompany();		
		// articles related
		//Read history
//		ReadHistory();
//		// Find start point by history record 
//		ReadAllArticles(fileName_index, artileID_index);
//		// Get article content
//			for(int i=0; i<filenameVec.size(); i++) {
//				articleId = "";
//				author = "";
//				title = "";
//				content = "";
//				date = "";
//				messagesCount = 0;
//				companyIdDisplay.clear();
//				companyNameDisplay.clear();
//				valueDisplay.clear();
//				
//				GetContentByArticleId(filenameVec.get(i).toString(), articleIdVec.get(i).toString());
//				// Filter
//				
//				pattern = Pattern.compile(regexTitle, Pattern.MULTILINE);
//				matcher = pattern.matcher(title);
//				if(matcher.find()){
//					
//				}
//				
//				System.out.println(filenameVec.get(i)+"	"+articleId+"	"+date+"	"+title+"	"+author+"	"+messagesCount);
//			}
		
		// Pattern Recognition
			PatternCheck(contentTmp);	
			
		// Save history
		//StoragedHistory(aa, bb);
	}
	
	// Read History
	private void ReadHistory() throws Exception 
	{
		File file = new File(Units.historyFolder + Units.historyName);
		if(file.exists()) {
			fileName_index = "";
			artileID_index = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String Line;
			String temp[];
			while((Line = bfr.readLine())!=null)
			{
				temp = Line.split("\\t");
				fileName_index = temp[0];
				artileID_index = temp[1];
			}
			
			bfr.close();
		}
	}
	
	private void ReadAllArticles(String historyfileName, String historyarticleId) throws Exception
	{
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	
		    	if((filestartPoint == true) && (startPoint == true)) {
		    		//System.out.println(file.getName());
		    		StartCoolection(file.getName());
		    	}
		    	
		    	if(file.getName().equalsIgnoreCase(historyfileName)) {
	    			//System.out.println(file.getName());
	    			filestartPoint = true;
	    			articleIndex(historyfileName, historyarticleId, file.getName());
	    		}
		    }
		}
	}
	
	private void articleIndex(String historyfileName, String historyarticleId, String currentFileName) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + historyfileName);
		BufferedReader bfr = new BufferedReader(fr);
		
		String strTmp = "";
		while((Line = bfr.readLine())!=null)
		{	
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		if(isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if(obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for(int i=0; i<jsonarray.length(); i++)
				{
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if(articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						
						if((filestartPoint == true) && (startPoint == true)) {
							// ==== Collection
							//System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
							filenameVec.add(currentFileName);
							articleIdVec.add(idTmp);
						}
						
						if((filestartPoint == true) && (idTmp.equalsIgnoreCase(historyarticleId))) {
							startPoint = true;
						}
					}
				}
			}
		}
	}
	
	private void StartCoolection(String currentFileName) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + currentFileName);
		BufferedReader bfr = new BufferedReader(fr);
		
		String strTmp = "";
		while((Line = bfr.readLine())!=null)
		{	
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		if(isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if(obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for(int i=0; i<jsonarray.length(); i++)
				{
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					
					if(articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						// ==== Collection
						//System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
						filenameVec.add(currentFileName);
						articleIdVec.add(idTmp);
					}
				}
			}
		}
	}
	
	private void GetContentByArticleId(String filenameIndex, String articleIdIndex) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);
		
		String strTmp = "";
		while((Line = bfr.readLine())!=null)
		{	
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		if(isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if(obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for(int i=0; i<jsonarray.length(); i++)
				{
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if(articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						articleId = idTmp;
						if(idTmp.equalsIgnoreCase(articleIdIndex)) {
							
							// author
							if(articleobj.has("author")) {
								author = articleobj.getString("author");
							}
							// title
							if(articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}		
							// content
							if(articleobj.has("content")) {
								content = articleobj.getString("content");
							}
							// date
							if(articleobj.has("date")) {
								date = articleobj.getString("date");
							}
							// message
							if(articleobj.has("messages")) {
								//System.out.println(articleobj.getJSONArray("messages"));
								JSONArray mesarray = new JSONArray(articleobj.getJSONArray("messages").toString());
								messagesCount = mesarray.length();
							}
							
							break;
						}
					}
				}
			}
		}
	}
	
	private void StoragedHistory(String articleFileName, String articleId) throws Exception
	{
		writer = new FileOutputStream(Units.historyFolder + Units.historyName, true);
		
		Date date = new Date();
	    //System.out.println(date.toString());
	    
		ps = new PrintStream(writer); 
		ps.print(articleFileName+"	"+articleId+"	"+date.toString()+"\n");
		ps.close();
	}
	
	private boolean isJSONValid(String jsonInString) {
		
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check; 
		check = jsonele.isJsonObject();
		return check;
	}
	
	// Read company name & id
	private void ReadCompany() throws Exception
	{
		// TWSE
		ReadCompany(Units.sourceFolder, Units.twsefile);
		// TPEX
		ReadCompany(Units.sourceFolder, Units.tpexfile);
	}
	
	private void ReadCompany(String pathName, String fileName) throws Exception
	{
		File file = new File(pathName + fileName);
		if(file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			while((Line = bfr.readLine())!=null)
			{
				temp = Line.split("\\t");
				companyId.add(temp[0]);
				companyName.add(temp[1]);
			}
			
			bfr.close();
		}
	}
	
	// Regular Expression
	private void PatternCheck(String strTmp)
	{
		// Company Name and ID
		String regexName = "";
		String regexId = "";
		String tmpName;
		String patternName;
		String patternId;
		boolean namecheck = false;
		boolean idcheck = false;
		
		pattern = Pattern.compile(regexTitle, Pattern.MULTILINE);
		matcher = pattern.matcher(strTmp);
		if(matcher.find()){
			System.out.println(matcher.group());
		}
		
		for(int i=0; i<companyId.size(); i++)
		{
			patternName = "";
			patternId = "";
			
			// Name
			tmpName = companyName.get(i).toString().replace("-KY", "");
			tmpName = tmpName.replace("-DR", "");
			regexName = "("+tmpName+")+";
			pattern = Pattern.compile(regexName, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
	        if(matcher.find()){
	        	//System.out.println(companyId.get(i)+"	"+companyName.get(i)+"	"+matcher.group());
	        	patternName = matcher.group();
	        	companyNameDisplay.add(patternName);
	        	namecheck = true;
	        }
	        // Id
	        regexId = companyId.get(i).toString();
	        pattern = Pattern.compile(regexId, Pattern.MULTILINE);
	        matcher = pattern.matcher(strTmp);
	        if(matcher.find()){
	        	patternId = matcher.group();
	        	idcheck = true;
	        	companyIdDisplay.add(patternId);
	        }
	        
//	        if((patternName.isEmpty() == false) || (patternId.isEmpty() == false)) {
//	        	System.out.println(companyId.get(i)+"	"+companyName.get(i)+"	"+patternName+"	"+patternId);
//	        }     
	        
		}
		
		// Values
		String regexValue = "([0-9]+\\.?[0-9]+)";
		pattern = Pattern.compile(regexValue, Pattern.MULTILINE);
        matcher = pattern.matcher(strTmp);
        while(matcher.find()){
        	//System.out.println(matcher.group(0));
        	valueDisplay.add(matcher.group(0));
        }
	}
	
	public static void main(String args[])
	{
		try {
			Tagging_Main tagging = new Tagging_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package GUI.Tagging;

/*
 * Get values (Main)
 * version: July 06, 2019 15:03 PM
 * Last revision: July 08, 2019 00:20 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

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
	
		
	public Tagging_Main() throws Exception
	{
		// Read history
		ReadHistory();
			//System.out.println(fileName_index+"	"+artileID_index);
		// Find start point by history record 
		ReadAllArticles(fileName_index, artileID_index);
			//System.out.println(filenameVec.size()+"	"+articleIdVec.size());
		// Get article content
			for(int i=0; i<filenameVec.size(); i++) {
				articleId = "";
				author = "";
				title = "";
				content = "";
				date = "";
				messagesCount = 0;
				GetContentByArticleId(filenameVec.get(i).toString(), articleIdVec.get(i).toString());
				
				System.out.println(filenameVec.get(i)+"	"+articleId+"	"+date+"	"+title+"	"+author+"	"+messagesCount);
			}
		
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

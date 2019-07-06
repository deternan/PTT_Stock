package GUI.Tagging;

import java.io.BufferedReader;

/*
 * Get values (Main)
 * version: July 06, 2019 15:03 PM
 * Last revision: July 07, 2019 00:22 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */


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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;

public class Tagging_Main 
{
	String aa = "Stock_0002.json";
	String bb = "M.1441549887.A.0FC";
	
	FileOutputStream writer;
	PrintStream ps;
	
//	private String articleIdIndex;	
//	private String nextfileName;
	
	
	// Parameters
		private String fileName_index = "";
		private String artileID_index = "";
		private String fileName_latest = "";
		//private String content_index;
	
	private boolean filestartPoint = false;
	private boolean startPoint = false;
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
		
		
	public Tagging_Main() throws Exception
	{
		// Read history
		ReadHistory();
		//System.out.println(fileName_index+"	"+artileID_index);
		
		ReadAllArticles(fileName_index, artileID_index);
		
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
				//System.out.println(temp[0]+"	"+temp[1]);
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
		
		int listOfFilesSize = listOfFiles.length;
		int index = 0;
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	index++;
		    	
		    	if((filestartPoint == true) && (startPoint == true)) {
		    		System.out.println(file.getName());
		    		StartCoolection(file.getName());
		    	}
		    	
		    	if(file.getName().equalsIgnoreCase(historyfileName)) {
	    			System.out.println(file.getName());
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
		String article_id;
		boolean indexcheck;
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
							System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
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
		String article_id;
		boolean indexcheck;
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
						System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
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

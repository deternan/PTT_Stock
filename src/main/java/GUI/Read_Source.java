package GUI;

/*
 * Sotck GUI
 * b. Read articls
 * 
 * version: June 22, 2019 09:29 PM
 * Last revision: June 22, 2019 10:01 PM
 * 
 * Author : Chao-Hsuan Ke
 * Email: phelpske.dev at gmail dot com
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Read_Source {

	private String folder_source = "/data/git/DataSet/ptt/Stock data/";
	private String fileName = "Stock-5015-5015.json";		// Testing
	
	private BufferedReader bfr;
	// Parsing
	JSONParser parser = new JSONParser();
		
	// attribute
	private String articleId;
	private String author;
	private String title;
	private int messageCount;
	private String Date;
	
	// Date
	String year;
	String month;
	String day;
	
	public Read_Source() throws Exception 
	{
//		File[] listOfFiles = folder.listFiles();		
//		Arrays.sort(listOfFiles);
//		
//		for (File file : listOfFiles) {
//		    if (file.isFile()) {
//		        System.out.println(file.getName());
//		        		        
//		        // Check extension file name
//		        checkResponse = ExtensionCheck(folder_source + file.getName());
//		        if(checkResponse) {
//		        	 // Read files
//			        ReadFile(folder_source + file.getName());
//			        
//			        //System.out.println(file.getName()+"	"+outputBase);
//		        }
//		    }
//		}
		
		ReadFile(folder_source + fileName);
	}
	
	private void ReadFile(String path) throws Exception
	{
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;
		String allText = "";
		while((Line = bfr.readLine())!=null)
		{								
			allText += Line;
		}
		fr.close();
		bfr.close();	
			
		year = "";
		month = "";
		day = "";
		
		// Parsing
		Parsing(allText);		
	}
	
	private void Parsing(String lineStr) throws Exception
	{
		JSONObject json = (JSONObject) parser.parse(lineStr);
		JSONArray msg = (JSONArray) json.get("articles");
	
		
		for(int i=0; i<msg.size(); i++) 
		{
			articleId = "";
			author = "";
			title = "";
			messageCount = 0;
			
			JSONObject articlejson = (JSONObject) parser.parse(msg.get(i).toString());
			
			// article_id
			if(articlejson.containsKey("article_id") && articlejson.get("article_id")!=null) {
				articleId = articlejson.get("article_id").toString().trim();
			}else {
				articleId = "";
			}
			
			// author
			if(articlejson.containsKey("author")) {
				if(articlejson.get("author") != null) {
					if(articlejson.get("author").toString().indexOf("(") > 0) {
						author = articlejson.get("author").toString().substring(0, articlejson.get("author").toString().indexOf("(")).trim();
					}else {
						author = articlejson.get("author").toString();
					}			
				}				
			}else {
				author = "";
			}
				
			
			// title
			if(articlejson.containsKey("article_title") && articlejson.get("article_title")!= null) {
				title = articlejson.get("article_title").toString();
			}else {
				title = "";
			}
			
			// Message (Push)
			if(articlejson.containsKey("message_count")) {
				if(articlejson.get("message_count") != null) {
					//System.out.println(articlejson.get("message_count"));
					JSONObject messagejson = (JSONObject) parser.parse(articlejson.get("message_count").toString());
					messageCount = Integer.parseInt(messagejson.get("all").toString().trim());
				}
			}else {
				messageCount = 0;
			}
			
			// Date
			if(articlejson.containsKey("date")) {
				Date_Split(articlejson.get("date").toString());
			}
			
			System.out.println(title+"	"+year+"-"+month+"-"+day);
		}
	}
	
	private void Date_Split(String dateStr)
	{
		//"date":"Tue Aug 30 13:38:20 2016",
		String temp[];
		String monthStr;
		String dayStr;
		temp = dateStr.split(" ");
		if(temp.length == 6) {
			month = temp[1];
			monthStr = MonthTranslation(month);
			day = temp[3];
			if(day.length() == 1) {
				dayStr = "0"+String.valueOf(day);
			}else {
				dayStr = String.valueOf(day);
			}
			year = temp[5];
		}
	}
	
	private String MonthTranslation(String inputStr) {
		
		String monthInt = ""; 
		switch(inputStr)
		{
		case "Jan":
			monthInt = "01";
			break;
		case "Feb":
			monthInt = "02";
			break;
		case "Mar":
			monthInt = "03";
			break;
		case "Apr":
			monthInt = "04";
			break;
		case "May":
			monthInt = "05";
			break;
		case "Jun":
			monthInt = "06";
		break;
		case "Jul":
			monthInt = "07";
		break;
		case "Aug":
			monthInt = "08";
		break;
		case "Sep":
			monthInt = "09";
		break;
		case "Oct":
			monthInt = "10";
		break;
		case "Nov":
			monthInt = "11";
		break;
		case "Dec":
			monthInt = "12";
		break;
		}
		
		return monthInt;
	}
	
	public static void main(String args[])
	{
		try {
			Read_Source rs = new Read_Source(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

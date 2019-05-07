package ptt.statistics;

/*
 * Message (Push) Statistical
 * version: May 08, 2019 05:56 AM
 * Last revision: May 08, 2019 06:20 AM
 * 
 * Author : Chao-Hsuan Ke
 * Institute: Delta Research Center
 * Company : Delta Electronics Inc. (Taiwan)
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Statistical_articlePush {
	
	// Read source
	private String folder_source = "/Users/phelps/temp/";
	//private String folder_source = "/data/git/DataSet/ptt/Stock data/";
	private BufferedReader bfr;
	// Output files
	private String folder_output = "/Users/phelps/Desktop/";
	private BufferedWriter writer;
	// Parsing
	JSONParser parser = new JSONParser();
	
	// File Check
	String extension_Json = "json";
	// Date
	String year;
	String month;
	String day;
	// output
	private String outputBase = "";
	
	public Statistical_articlePush() throws Exception {
		
		
		boolean checkResponse;
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	outputBase = "";
		        //System.out.println(file.getName());
		        		        
		        // Check extension file name
		        checkResponse = ExtensionCheck(folder_source + file.getName());
		        if(checkResponse) {
		        	 // Read files
			        ReadFile(folder_source + file.getName());
			        
			        //System.out.println(file.getName()+"	"+outputBase);
		        }
		    }
		}
	}

	private boolean ExtensionCheck(String path)
	{
		boolean checkResponse = false;
		        		        
		        String Getextension = getFileExtension(new File(path));
		        String extension = Getextension.substring(1, Getextension.length());
		        if(extension.equalsIgnoreCase(extension_Json)) {
		        	checkResponse = true;
		        }
		return checkResponse;
	}
	
	private static String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
 
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
			
		String author;
		
		for(int i=0; i<msg.size(); i++) {
			JSONObject articlejson = (JSONObject) parser.parse(msg.get(i).toString());
			
			// article_id
			//System.out.println(i+"	"+articlejson.get("article_id"));
			
			// author
//			if(articlejson.containsKey("author")) 
//			{
//				if(articlejson.get("author") != null) {
//					if(articlejson.get("author").toString().indexOf("(") > 0) {
//						author = articlejson.get("author").toString().substring(0, articlejson.get("author").toString().indexOf("(")).trim();
//					}else {
//						author = articlejson.get("author").toString();
//					}			
//				}				
//			}			
				
			
			// title
			//System.out.println(i+"	"+articlejson.get("title"));
			
			// content
			//System.out.println(i+"	"+articlejson.get("content"));
			
			// Message (Push)
			if(articlejson.containsKey("message_count")) 
			{
				if(articlejson.get("message_count") != null) {
					//System.out.println(articlejson.get("message_count"));
					JSONObject messagejson = (JSONObject) parser.parse(articlejson.get("message_count").toString());
					System.out.println(articlejson.get("article_id")+"	"+messagejson.get("all"));
				}
			}
			
			// Date
			if(articlejson.containsKey("date")) {
				Date_Split(articlejson.get("date").toString());
			}
			
			//System.out.println(i+"	"+articlejson.get("article_id")+"	"+articlejson.get("article_title"));
		}
		
		
	}
	
	private void Date_Split(String dateStr)
	{
		//"date":"Tue Aug 30 13:38:20 2016",
		String temp[];
		temp = dateStr.split(" ");
		if(temp.length == 6) {
			month = temp[1];
			day = temp[3];
			year = temp[5];

			outputBase = String.valueOf(year)+"_"+String.valueOf(month)+"_"+String.valueOf(day);
		}
	}
	
	public static void main(String args[]) {
		try {
			Statistical_articlePush sap = new Statistical_articlePush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

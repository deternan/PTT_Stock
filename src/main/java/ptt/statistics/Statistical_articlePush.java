package ptt.statistics;

/*
 * Message (Push) Statistical
 * version: May 08, 2019 05:56 AM
 * Last revision: May 08, 2019 10:33 PM
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	// ArrayList
	int countIndex = 0;
		ArrayList<ArrayList> all_array_temp = new ArrayList<ArrayList>();
		//ArrayList<String[]> all_array_temp = new ArrayList<String[]>();
		//ArrayList<ArrayList<String>> all_array_temp = new ArrayList<ArrayList<String>>();
		// Map
		Map<String, Integer> duplicates = new HashMap<String, Integer>();
	
	// atribute
	private String articleId;
	private String author;
	private String title;
	private int messageCount;
	private String Date;
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
		
		System.out.println(msg.size());
		for(int i=0; i<msg.size(); i++) 
		{
			articleId = "";
			author = "";
			title = "";
			messageCount = 0;
			outputBase= "";
			
			JSONObject articlejson = (JSONObject) parser.parse(msg.get(i).toString());
			
			// article_id
			if(articlejson.containsKey("article_id")) {
				articleId = articlejson.get("article_id").toString().trim();
			}else {
				articleId = "";
			}
			//System.out.println(i+"	"+articlejson.get("article_id"));
			
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
			if(articlejson.containsKey("article_title")) {
				title = articlejson.get("article_title").toString();
			}else {
				title = "";
			}
			
			
			// content
			//System.out.println(i+"	"+articlejson.get("content"));
			
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
				Date = outputBase;
			}
			
			ArrayList listArray = new ArrayList();
			listArray.add(articleId);
			listArray.add(author);
			listArray.add(String.valueOf(messageCount));
			
			all_array_temp.add(listArray);
			
			//ArrayList aa = all_array_temp.get(countIndex);
			//System.out.println(i+"	"+countIndex+"	"+articleId+"	"+messageCount+"	"+author+"	"+title+"	"+listArray.size()+"	"+aa.size()+"	"+all_array_temp.get(countIndex));
			//System.out.println(articleId+"	"+messageCount+"	"+author+"	"+title+"	"+listTemp[2]+"	"+listArray.size()+"	"+aa.get(2));
			
			listArray.clear();
			countIndex++;
		}
		
		//ArrayList<String>a = all_array_temp.get(0);
		//System.out.println(a);
		//System.out.println(all_array_temp.get(0));
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
	
	private void CountDuplicatedList() {
		
		//Map<String, Integer> duplicates = new HashMap<String, Integer>();
		String str;
		for(int i=0; i<all_array_temp.size(); i++) {
			//str = all_array_temp.;
			//if() 
			{
				
			}
		}
//		
//		for (String str : all_array_temp) {
//			if (duplicates.containsKey(str)) {
//				duplicates.put(str, duplicates.get(str) + 1);
//			} else {
//				duplicates.put(str, 1);
//			}
//		}
		
//		for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
//			//System.out.println(entry.getKey() + " = " + entry.getValue());
//			allAuthor_array.add(entry.getKey());
//			allAuthorStastic_array.add(entry.getValue());
//		}		
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

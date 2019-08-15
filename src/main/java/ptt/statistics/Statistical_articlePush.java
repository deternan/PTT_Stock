package ptt.statistics;

/*
 * Message (Push) Statistical
 * version: May 08, 2019 05:56 AM
 * Last revision: August 16, 2019 07:31 PM
 * 
 * Author : Chao-Hsuan Ke
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Statistical_articlePush {
	
	// Read source
	private String folder_source = "/data/git/DataSet/ptt/Stock data/";
	private BufferedReader bfr;
	// Output files
	private String folder_output = "/Users/phelps/Desktop/";
	private BufferedWriter writer;
	// Parsing
	JSONParser parser = new JSONParser();
	// ArrayList
	int countIndex = 0;
		// Data Storage
		// Type 1
	ArrayList<String> articleId_Array = new ArrayList<String>(); 
	ArrayList<Integer> messageCount_Array = new ArrayList<Integer>(); 
	ArrayList<String> author_Array = new ArrayList<String>();
	ArrayList<String> title_Array = new ArrayList<String>();
	ArrayList<String> date_Array = new ArrayList<String>();
		// Type 2
		ArrayList<ArrayList> all_array_temp = new ArrayList<ArrayList>();
		// Map
		Map<String, Integer> duplicates = new HashMap<String, Integer>();
	
	// attribute
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
	// Statistical
		private String outputMessageStatistical = "MessageStatistical";
	
	public Statistical_articlePush() throws Exception {
		
		boolean checkResponse;
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        		        
		        // Check extension file name
		        checkResponse = ExtensionCheck(folder_source + file.getName());
		        if(checkResponse) {
		        	 // Read files
			        ReadFile(folder_source + file.getName());
		        }
		    }
		}
		
		// Sort
		BubbleSort();

		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputMessageStatistical+"_"+outputBase+".txt"), "utf-8"));
		for(int i=0;i<author_Array.size();i++)  {
			writer.write(articleId_Array.get(i)+"	"+author_Array.get(i)+"	"+messageCount_Array.get(i)+"	"+title_Array.get(i)+"	"+date_Array.get(i)+"\n");
		}
		writer.close();
	}

	private boolean ExtensionCheck(String path)
	{
		boolean checkResponse = false;
		        		        
		String Getextension = getFileExtension(new File(path));
		String extension = Getextension.substring(1, Getextension.length());
		if (extension.equalsIgnoreCase(extension_Json)) {
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
		while((Line = bfr.readLine())!=null){								
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
		
		ArrayList listArray = new ArrayList();
		
		for(int i=0; i<msg.size(); i++) 
		{
			articleId = "";
			author = "";
			title = "";
			messageCount = 0;
			outputBase= "";
			
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
			
			
			// Message (Push count)
			if(articlejson.containsKey("message_count")) {
				if(articlejson.get("message_count") != null) {
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
			
			
			// Type 1
			articleId_Array.add(articleId);
			author_Array.add(author);
			messageCount_Array.add(messageCount);
			title_Array.add(title);
			date_Array.add(Date);
			// Type 2
			listArray.add(articleId);						// 0
			listArray.add(author);							// 1
			listArray.add(String.valueOf(messageCount));	// 2
			
			all_array_temp.add(listArray);
			
			listArray.clear();
			countIndex++;
		}
	}
	
	private void Date_Split(String dateStr)
	{
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

			outputBase = String.valueOf(year)+monthStr+dayStr;
		}
	}
	
	private void BubbleSort()
    {
    	int lenD = articleId_Array.size();
		int j = 0;
		int tmp = 0;
		String idtmp = "";
		String authortmp = "";
		String titletmp = "";
		String datetmp = "";
		
		for(int i=0; i<lenD; i++)
		{
		    j = i;
		    for(int k=i; k<lenD; k++)
		    {
		    	if(messageCount_Array.get(j) < messageCount_Array.get(k)){
		        j = k;		        
		      }
		    }
		    
		    tmp = messageCount_Array.get(i);		    		  
		    messageCount_Array.set(i, messageCount_Array.get(j));
		    messageCount_Array.set(j, tmp);
		    
		    idtmp = articleId_Array.get(i);
		    articleId_Array.set(i, articleId_Array.get(j));
		    articleId_Array.set(j, idtmp);
		    
		    authortmp = author_Array.get(i);
		    author_Array.set(i, author_Array.get(j));
		    author_Array.set(j, authortmp);
		    
		    titletmp = title_Array.get(i);
		    title_Array.set(i, title_Array.get(j));
		    title_Array.set(j, titletmp);
		    
		    datetmp = date_Array.get(i);
		    date_Array.set(i, date_Array.get(j));
		    date_Array.set(j, datetmp);
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
	
	public static void main(String args[]) {
		try {
			Statistical_articlePush sap = new Statistical_articlePush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package ptt.statistics;

/*
 * Published number in article by Authors
 * version: May 20, 2019 08:40 PM
 * Last revision: August 18, 2019 02:47 AM
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Statistical_AuthorsPushedNumber 
{
	// Read source
	private String folder_source = "/data/git/DataSet/ptt/Stock data/";
	private BufferedReader bfr;
	private String output_source = "/Users/phelps/Documents/github/PTT_Stock/output/";
	// output
	private String folder_output = "/Users/phelps/Desktop/";
	private BufferedWriter writer;	
		private String outputBase = "";
		String finalFileName = "";
	// check latest file
	int checkNum;
	// Statistical
	private String outputAuthorList = "AuthorList";
	private String outputAuthorPushedNumber = "AuthorPushedNumber";
	// Parsing
	JSONParser parser = new JSONParser();
	// File Check
		String extension_Json = "json";
		// Date 
		String year;
		String month;
		String day;
	// Author List
	ArrayList<String> allAuthor_array = new ArrayList<String>();	
	// author pushed count
	ArrayList<Integer> allAuthorPushedCount_array = new ArrayList<Integer>();	
		
	public Statistical_AuthorsPushedNumber() throws Exception
	{
		AllAuthorList();
		
		boolean checkResponse;
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	outputBase = "";
		        		        
		        // Check extension file name
		        checkResponse = ExtensionCheck(folder_source + file.getName());
		        if(checkResponse) {
		        	 // Read files
			        ReadFile(folder_source + file.getName());
			        
			        System.out.println(file.getName()+"	"+outputBase);
		        }
		    }
		}
		
		// Sort
		BubbleSort();
		
		// output
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputAuthorPushedNumber+"_"+outputBase+".txt"), "utf-8"));
		for(int i=0; i<allAuthor_array.size(); i++) {
			writer.write(allAuthor_array.get(i)+"	"+allAuthorPushedCount_array.get(i)+"\n");
		}
		writer.close();
		
	}
	
	private void AllAuthorList() throws Exception {

		String finalFile;

		File folder = new File(output_source);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);

		checkNum = 0;
		finalFileName = "";

		// To get the latest file
		for (File file : listOfFiles) {

			if (file.getName().indexOf(outputAuthorList) == 0) {
				finalFile = file.getName();
				// Check Latest file
				finalFileName = LatestFileCheck(outputAuthorList, finalFile);
			}
		}

		finalFileName = outputAuthorList + "_" + finalFileName + ".txt";

		// Import
		ImportAuthorList(output_source + finalFileName);
	}
	
	private void ImportAuthorList(String path) throws Exception {

		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;

		while ((Line = bfr.readLine()) != null) {
			allAuthor_array.add(Line.trim());
			allAuthorPushedCount_array.add(0);
		}
		fr.close();
		bfr.close();
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
			
			// author
			if(articlejson.containsKey("author")) 
			{
				if(articlejson.get("author") != null) {
					if(articlejson.get("author").toString().indexOf("(") > 0) {
						author = articlejson.get("author").toString().substring(0, articlejson.get("author").toString().indexOf("(")).trim();
					}else {
						author = articlejson.get("author").toString();
					}			
				}				
			}			
				
			// title
			//System.out.println(i+"	"+articlejson.get("title"));
			
			// content
			//System.out.println(i+"	"+articlejson.get("content"));
			
			// Message (Push)
			if (articlejson.containsKey("message_count")) {
				if (articlejson.get("message_count") != null) {
					//JSONObject messagejson = (JSONObject) parser.parse(articlejson.get("message_count").toString());
					JSONArray jsonarray = (JSONArray) parser.parse(articlejson.get("messages").toString());
					
					Message_Parsing(jsonarray);
				}
			} 
			
			// Date
			if(articlejson.containsKey("date")) {
				Date_Split(articlejson.get("date").toString());
			}
		}
		
	}
	
	private void Message_Parsing(JSONArray jsonarray) throws Exception
	{
		String pushAuthor;
		for(int i=0; i<jsonarray.size(); i++) {
			JSONObject messagejson = (JSONObject) parser.parse(jsonarray.get(i).toString());
			pushAuthor = messagejson.get("push_userid").toString();
			Statistical(pushAuthor);
		}
	}
	
	private void Statistical(String authorTag) {
		
		int countTemp;
		for(int i=0; i<allAuthor_array.size(); i++) {
			countTemp = allAuthorPushedCount_array.get(i);
			if(authorTag.trim().equalsIgnoreCase(allAuthor_array.get(i).toLowerCase().trim())) {
				allAuthorPushedCount_array.set(i, countTemp+1);
				break;
			}
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

			outputBase = String.valueOf(year)+monthStr+dayStr;
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
	
	private String LatestFileCheck(String TagStr, String fileName) {
		String fileNameDate = fileName.substring(TagStr.length() + 1, fileName.indexOf(".txt"));
		String finalfileNameDate = "";
		
		if(fileNameDate.length() > 0) {
			
			if(Integer.parseInt(fileNameDate) > checkNum) {
				finalfileNameDate = fileNameDate;
				checkNum = Integer.parseInt(finalfileNameDate);
			}
			
		}
		
		return finalfileNameDate;
	}
	
	private void BubbleSort()
    {
    	int lenD = allAuthor_array.size();
		int j = 0;
		int tmp = 0;
		String name = "";
		
		for(int i=0; i<lenD; i++)
		{
		    j = i;
		    for(int k=i; k<lenD; k++)
		    {
		      
		    	if(allAuthorPushedCount_array.get(j) < allAuthorPushedCount_array.get(k)){
		        j = k;		        
		      }
		    }
		    
		    tmp = allAuthorPushedCount_array.get(i);		   							 	name = allAuthor_array.get(i);
		    allAuthorPushedCount_array.set(i, allAuthorPushedCount_array.get(j));			allAuthor_array.set(i, allAuthor_array.get(j));
		    allAuthorPushedCount_array.set(j, tmp);										allAuthor_array.set(j, name);
		}
		
    }
	
	public static void main(String args[])
	{
		try {
			Statistical_AuthorsPushedNumber sts_apn = new Statistical_AuthorsPushedNumber();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

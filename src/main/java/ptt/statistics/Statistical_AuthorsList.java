package ptt.statistics;

/*
 * Authors Statistical
 * version: May 02, 2019 07:00 PM
 * Last revision: August 15, 2019 11:32 PM
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Statistical_AuthorsList {

	// Read source
	private String folder_source = "/data/git/DataSet/ptt/Stock data/";		//  PTT 文章檔案路徑位置
	private BufferedReader bfr;
	// Output files
	private String folder_output = "/Users/phelps/Desktop/";				// 	輸出位置
	private BufferedWriter writer_1;
	private BufferedWriter writer_2;
	// Parsing
	JSONParser parser = new JSONParser();
	
	// ArrayList
	ArrayList<String> allAuthor_array_temp = new ArrayList<String>();
	// Map
	Map<String, Integer> duplicates = new HashMap<String, Integer>();
	
	// File Check
	String extension_Json = "json";
	// Date 
	String year;
	String month;
	String day;
	// output
	private String outputBase = "";
		// Statistical
		private String outputAuthorList = "AuthorList";
		private String outputAuthorStatistical = "AuthorStatistical";
		// author array
		ArrayList<String> allAuthor_array = new ArrayList<String>();
		ArrayList<Integer> allAuthorStastic_array = new ArrayList<Integer>();
		
	public Statistical_AuthorsList() throws Exception
	{
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
		
		// 
		CountDuplicatedList();
		// ArrayList Sort
		MapSort_byValue();
		
		// output (author list)
		writer_1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputAuthorList+"_"+outputBase+".txt"), "utf-8"));
		for(int i=0; i<allAuthor_array.size(); i++) {
			writer_1.write(allAuthor_array.get(i)+"\n");
		}
		writer_1.close();
		// output (author and published count)
		writer_2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputAuthorStatistical+"_"+outputBase+".txt"), "utf-8"));
		for(int i=0; i<allAuthor_array.size(); i++) {
			writer_2.write(allAuthor_array.get(i)+"	"+allAuthorStastic_array.get(i)+"\n");
		}
		writer_2.close();
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

					// ArrayList
					allAuthor_array_temp.add(author.trim());
				}				
			}			
			
			// Date
			if(articlejson.containsKey("date")) {
				Date_Split(articlejson.get("date").toString());
			}
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
	
	private void CountDuplicatedList() {
		
		for (String str : allAuthor_array_temp) {
			if (duplicates.containsKey(str)) {
				duplicates.put(str, duplicates.get(str) + 1);
			} else {
				duplicates.put(str, 1);
			}
		}		
	}
	
	private void MapSort_byValue() {

		LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
		duplicates.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		// Display
		for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {
			allAuthor_array.add(entry.getKey());
			allAuthorStastic_array.add(entry.getValue());
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
			Statistical_AuthorsList st_au = new Statistical_AuthorsList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

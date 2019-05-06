package ptt.statistics;

/*
 * Authors Statistical
 * version: May 02, 2019 07:00 PM
 * Last revision: May 07, 2019 00:20 AM
 * 
 * Author : Chao-Hsuan Ke
 * Institute: Delta Research Center
 * Company : Delta Electronics Inc. (Taiwan)
 * 
 */

/*
 * 
 * [Author]
	1.  哪些 author (id) 常發表文章 ?
	2. 那些人最常推文 (不管 推/噓)
	3. 那些 author的文章被推文最多
   [Company]
	1. 那些公司被討論最多
	2. 那些公司被說看漲最多
	3. 那些公司被說看跌最多
 *
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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Statistical_Authors {

	// Read source
//	private String folder_source = "/Users/phelps/temp/";
	private String folder_source = "/data/git/DataSet/ptt/Stock data/";
	private BufferedReader bfr;
	// Output files
	private String folder_output = "/Users/phelps/Desktop/";
	private BufferedWriter writer;
	// Parsing
	JSONParser parser = new JSONParser();
	
	// Vector
		// All (temp)
		Vector allAuthor_temp = new Vector();	
		// Non-duplication
		Vector allAuthor = new Vector();
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
		private String outputAuthorStatistical = "AuthorStatistical";
		// author array
		ArrayList<String> allAuthor_array = new ArrayList<String>();
		ArrayList<Integer> allAuthorStastic_array = new ArrayList<Integer>();
		
	public Statistical_Authors() throws Exception
	{
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
			        
			        System.out.println(file.getName()+"	"+outputBase);
		        }
		    }
		}
		
		// Vector
//		System.out.println(allAuthor_temp.size());
		// Remove duplication
//		allAuthor = new Vector(new LinkedHashSet(allAuthor_temp));
//		System.out.println(allAuthor.size());
		
		// ArrayList
//		System.out.println(allAuthor_array_temp.size());
		// 
		CountDuplicatedList();
		// ArrayList Sort
		MapSort_byValue();
		
		// output
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputAuthorStatistical+"_"+outputBase+".txt"), "utf-8"));
		for(int i=0; i<allAuthor_array.size(); i++) {
			writer.write(allAuthor_array.get(i)+"	"+allAuthorStastic_array.get(i)+"\n");
		}
		writer.close();
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
			if(articlejson.containsKey("author")) 
			{
				if(articlejson.get("author") != null) {
					if(articlejson.get("author").toString().indexOf("(") > 0) {
						author = articlejson.get("author").toString().substring(0, articlejson.get("author").toString().indexOf("(")).trim();
					}else {
						author = articlejson.get("author").toString();
					}			
//					System.out.println(author);
					// Vector
//					allAuthor_temp.add(author.trim());
					// ArrayList
					allAuthor_array_temp.add(author.trim());
				}				
			}			
				
			
			// title
			//System.out.println(i+"	"+articlejson.get("title"));
			
			// content
			//System.out.println(i+"	"+articlejson.get("content"));
			
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
	
	private void CountDuplicatedList() {
		
		//Map<String, Integer> duplicates = new HashMap<String, Integer>();

		for (String str : allAuthor_array_temp) {
			if (duplicates.containsKey(str)) {
				duplicates.put(str, duplicates.get(str) + 1);
			} else {
				duplicates.put(str, 1);
			}
		}

		
		for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
			//System.out.println(entry.getKey() + " = " + entry.getValue());
			allAuthor_array.add(entry.getKey());
			allAuthorStastic_array.add(entry.getValue());
		}		
	}
	
	private void MapSort_byValue() {
		//Map<String, Integer> unSortedMap = getUnSortedMap();

		LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
		duplicates.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		//System.out.println("Reverse Sorted Map   : " + reverseSortedMap);

		// Display
		for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {
//			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	public static void main(String args[])
	{
		try {
			Statistical_Authors st_au = new Statistical_Authors();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

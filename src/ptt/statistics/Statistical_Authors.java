package ptt.statistics;

/*
 * Authors Statistical
 * version: May 02, 2019 07:00 PM
 * Last revision: May 03, 2019 06:45 PM
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
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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

//	private String folder_source = "D:\\Phelps\\GitHub\\PTT_Stock\\source\\Stock data\\";
	private String folder_source = "C:\\Users\\barry.ke\\Desktop\\PTT\\";
	private BufferedReader bfr;
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
	
		
	public Statistical_Authors() throws Exception
	{
		
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        		        
		        // Read files
		        ReadFile(folder_source + file.getName());
		    }
		}
		
		// Vector
//		System.out.println(allAuthor_temp.size());
		// Remove duplication
//		allAuthor = new Vector(new LinkedHashSet(allAuthor_temp));
//		System.out.println(allAuthor.size());
		
		// ArrayList
		System.out.println(allAuthor_array_temp.size());
		// 
		CountDuplicatedList();
		// ArrayList Sort
		MapSort_byValue();
		
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
			//System.out.println(i+"	"+articlejson.get("author"));
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
			
			//System.out.println(i+"	"+articlejson.get("article_id")+"	"+articlejson.get("article_title"));
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

//		for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
//			System.out.println(entry.getKey() + " = " + entry.getValue());
//		}		
	}
	
	private void MapSort_byValue() {
		//Map<String, Integer> unSortedMap = getUnSortedMap();

		LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
		duplicates.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		//System.out.println("Reverse Sorted Map   : " + reverseSortedMap);

		for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
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

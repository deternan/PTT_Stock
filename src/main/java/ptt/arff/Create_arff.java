package ptt.arff;

/*
 * create arff file
 * 
 * version: September 01, 2019 09:54 PM
 * Last revision: September 01, 2019 09:54 PM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;

public class Create_arff 
{
	// tagging record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String file = "tagging.txt";
	// Articles
	private String articleFolder = "/data/git/DataSet/ptt/Stock data/";
	// article content
		private String articleFile;
		private String articleId;
		private String author;
		private String title;
		private String content;
		private String date;
		private int messagesCount;
	Vector titlecontentVec = new Vector();
	
	public Create_arff() throws Exception
	{
		// Read tagging txt
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		BufferedReader bfr = new BufferedReader(fr);
				
		int index = 0;
		String temp[];
		while((Line = bfr.readLine())!=null)
		{					
			temp = Line.split("	");
			//System.out.println(temp[0]+"	"+temp[1]);
			
			// content
			ReadSourceFile(temp[0], temp[1]);
			
			if(index == 1) 
			{
				SentenceSplit(title, content);
				for(int i=0; i<titlecontentVec.size(); i++)
				{
					ChineseWordParser(titlecontentVec.get(i).toString());
				}
			}
			
			index++;
			Clean();
		}
		fr.close();
		bfr.close();
	}

	private void ReadSourceFile(String filenameIndex, String articleIdIndex) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) {
			strTmp += Line;
		}
		fr.close();
		bfr.close();

		String idTmp;
		if (isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if (obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if (articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						articleId = idTmp;
						if (idTmp.equalsIgnoreCase(articleIdIndex)) {

							// author
							if (articleobj.has("author")) {
								author = articleobj.getString("author");
							}
							// title
							if (articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}
							// content
							if (articleobj.has("content")) {
								content = articleobj.getString("content");
							}
							// date
							if (articleobj.has("date")) {
								date = articleobj.getString("date");
							}
							// message
							if (articleobj.has("messages")) {
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
	
	private boolean isJSONValid(String jsonInString) {

		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}
	
	private void SentenceSplit(String titleStr, String contentStr)
	{
		System.out.println(contentStr);
		String titletmpStr[];
		String contenttmpStr[];
		if(titleStr.contains("，")) {
			titletmpStr = titleStr.split("，");
		}else {
			titlecontentVec.add(titleStr);
		}
		
//		if(contentStr.contains("，"))
		if(contentStr.trim().length() > 0)
		{
			contenttmpStr = contentStr.split("，");
			if(contenttmpStr.length > 0) {
				for(int i=0; i<contenttmpStr.length; i++) {
					titlecontentVec.add(contenttmpStr[i]);
					//System.out.println(contenttmpStr[i]);
				}
			}
		}
	}
	
	private void ChineseWordParser(String input_str) 
	{
		String sentence = "";
		for(int i=0; i<input_str.length();i++)
		{  
		    String test = input_str.substring(i, i+1);  
		    if(test.matches("[\\u4E00-\\u9FA5]+")){  
		        //System.out.printf("\t[Info] %s -> 中文!\n", test);
		    	sentence += test;
		    }  
		      
		}
		
		if(sentence.length() > 0){
			System.out.println(sentence);
		}
	}
	
	private void Clean()
	{
		articleFile = "";
		articleId = "";
		author = "";
		title = "";
		content = "";
		date = "";
		messagesCount = 0;
		titlecontentVec.clear();
	}
	
	public static void main(String args[]) 
	{
		try {
			Create_arff ca = new Create_arff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

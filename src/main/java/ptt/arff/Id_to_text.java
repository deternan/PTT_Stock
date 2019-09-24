package ptt.arff;

/*
 * PTT articles saved as new files
 * 
 * version: September 24, 2019 10:00 PM
 * Last revision: September 24, 2019 10:22 PM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;

public class Id_to_text 
{
	// tagging record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String file = "tagging.txt";
	// Articles
	private String articleFolder = "/data/git/DataSet/ptt/Stock data/";
	// article content
	private String title;
	private String content;
	private String allTitleContent = "";
	// file index
	String fileNameStr;
	String articleIdStr;
	String tagCategoryStr;
	
	// output
	private BufferedWriter writer;
	private String arfffolder = sourceFolder;
	private String arfffilename = "tagging_TEXT.txt";
	private String allweValueStr = "";
	
	public Id_to_text() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		BufferedReader bfr = new BufferedReader(fr);
		// output arff
		output_Initialize();

		// int index = 0;
		String temp[];
		while ((Line = bfr.readLine()) != null) 
		{
			temp = Line.split("	");
			fileNameStr = temp[0];
			articleIdStr = temp[1];
			tagCategoryStr = temp[3];
			// content
			ReadSourceFile(temp[0], temp[1]);
			
			allTitleContent += title + " " + content;

			System.out.println(fileNameStr+"	"+articleIdStr+"	"+allTitleContent.length());
			// output
			
			writer.write(fileNameStr+"	"+articleIdStr+"	"+allTitleContent + "\n");
			
			Clean();
		}
		
		fr.close();
		bfr.close();
		
		writer.close();
	}
	
	private void ReadSourceFile(String filenameIndex, String articleIdIndex) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) 
		{
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

						if (idTmp.equalsIgnoreCase(articleIdIndex)) {
							
							// title
							if (articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}
							// content
							if (articleobj.has("content")) {
								content = articleobj.getString("content");
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
	
	private void Clean()
	{
		title = "";
		content = "";
		allTitleContent = "";
		fileNameStr = "";
		articleIdStr = "";
		tagCategoryStr = "";
	}
	
	private void output_Initialize() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
	}
	
	public static void main(String[] args) 
	{
		try {
			Id_to_text id2text = new Id_to_text();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

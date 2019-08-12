package GUI.Tagging;

/*
 * Get values (Main)
 * version: August 10, 2019 00:01 AM
 * Last revision: August 12, 2019 11:00 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;


public class TaggingMain_Sample {

	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String alllist = "articlelist.txt";
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	
	// File Check
		String extension_Json = "json";
	// articlelist
		BufferedWriter writerarticlelist;
		
		
	public TaggingMain_Sample() throws Exception
	{
		
		writerarticlelist = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Units.sourceFolder + Units.alllist), "utf-8"));
		ReadAllArticlesList();
		writerarticlelist.close();
		
		//ReadAllArticles();
	}
	
	private void ReadAllArticlesList() throws Exception 
	{
		boolean checkResponse;
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);

		String articlenameTmp;
		String idTmp = "";
		String strTmp = "";
		String authorTmp = "";
			int authorTagIndex;
		
		String Line = "";
		for (File file : listOfFiles) {
			strTmp = "";
			
			if (file.isFile()) {
				articlenameTmp = file.getName();
				checkResponse = ExtensionCheck(Units.articleFolder + articlenameTmp);
				if(checkResponse) {
					FileReader fr = new FileReader(Units.articleFolder + articlenameTmp);
					BufferedReader bfr = new BufferedReader(fr);
					while ((Line = bfr.readLine()) != null) {
						strTmp += Line;
					}
					fr.close();
					bfr.close();
					
					if (isJSONValid(strTmp)) {
						idTmp = "";
						authorTmp = "";
						JSONObject obj = new JSONObject(strTmp);
						if (obj.has("articles")) {
							JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
							for (int i = 0; i < jsonarray.length(); i++) {
								JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
								if (articleobj.has("article_id")) {
									idTmp = articleobj.get("article_id").toString();							
								}
								if (articleobj.has("author")) {
									authorTmp = articleobj.get("author").toString();
									if(authorTmp.trim().length() == 0) {
										authorTmp = "null";
									}
									if(authorTmp.indexOf("(")>0) {
										authorTagIndex = authorTmp.indexOf("(");
										authorTmp = authorTmp.substring(0, authorTagIndex).trim();
									}
								}
								
								writerarticlelist.write(articlenameTmp+"	"+idTmp+"	"+authorTmp+"\n");
							}
						}
					}
				}				
			}
		}
	}
	
	private void ReadAllArticles() throws Exception
	{
		File file = new File(sourceFolder + alllist);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		boolean startcheck = false;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				if(startcheck) {
					filenameVec.add(temp[0]);
					articleIdVec.add(temp[1]);
				}
				
			}
		}
		bfr.close();
	}
	
	private boolean ExtensionCheck(String path) {
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
	
	private boolean isJSONValid(String jsonInString) {
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}
	
	public static void main(String args[])
	{
		try {
			TaggingMain_Sample sample = new TaggingMain_Sample();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

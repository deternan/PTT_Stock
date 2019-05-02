package ptt.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Statistical_Authors {

	private String folder_source = "D:\\Phelps\\GitHub\\PTT_Stock\\source\\Stock data\\";
	private BufferedReader bfr;
	// Parsing
	JSONParser parser = new JSONParser();
	
	Vector allAuthor = new Vector();
	
	
	
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
				if(articlejson.get("author").toString().indexOf("(") > 0) {
					author = articlejson.get("author").toString().substring(0, articlejson.get("author").toString().indexOf("(")).trim();
				}else {
					author = articlejson.get("author").toString();
				}			
				System.out.println(author);
			}			
			
			
			// title
			//System.out.println(i+"	"+articlejson.get("title"));
			
			// content
			//System.out.println(i+"	"+articlejson.get("content"));
			
			//System.out.println(i+"	"+articlejson.get("article_id")+"	"+articlejson.get("article_title"));
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

package ptt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Json_Parser {

	private String path = "D:\\Phelps\\GitHub\\PTT_Stock\\source\\Stock data\\";
	private String filename = "Stock-5000-5000.json";
	private BufferedReader bfr;	
	
	// Json
	private String article_id;
	private String article_title;
	private String author;
	private String content;
	private String date;
	
	
	private Vector authorVec = new Vector();
	
	public Json_Parser() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(path + filename);
		bfr = new BufferedReader(fr);
		
		String allText = "";
		while((Line = bfr.readLine())!=null)
		{					
			//System.out.println(Line);
			allText += Line;
			
		}				
		fr.close();
		bfr.close();
		
		
		JsonTag(allText);
	}
	
	private void JsonTag(String lineStr) throws Exception
	{
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(lineStr);
				
		JSONArray msg = (JSONArray) json.get("articles");
		//System.out.println(msg.size());
		
		for(int i=0; i<msg.size(); i++) {
			JSONObject articlejson = (JSONObject) parser.parse(msg.get(i).toString());
			
			// article_id
			System.out.println(i+"	"+articlejson.get("article_id"));
			// author
			
		}
		
	}
	
	public static void main(String args[])
	{
		try {
			Json_Parser jp = new Json_Parser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

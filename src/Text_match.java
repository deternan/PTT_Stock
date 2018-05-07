import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Json parser
 * 
 * version: April 25, 2018 11:22 PM
 * Last revision: May 08, 2018 00:51 AM
 * 
 * 
 */

public class Text_match 
{

	private String read_file_path = "C:\\Users\\Barry\\Desktop\\";
	private String read_file_name = "Stock-3002-3002.json";
	private BufferedReader bfr;
	// type
	final List<String> pageTypes = new ArrayList<>(Arrays.asList("標的", "新聞", "心得", "請益", "投顧", "其他", "公告"));
	
	private String article_id;
	private String article_title;
	private String author;
	private String content;
	
	// Get
	//TWSE
	private Vector twse_id = new Vector();
	private Vector twse_name = new Vector();
	// TPEX
	private Vector tpex_id = new Vector();
	private Vector tpex_name = new Vector();
	
	public Text_match(Vector twse_id, Vector twse_name, Vector tpex_id, Vector tpex_name) throws Exception
	{
		this.twse_id = twse_id;
		
		System.out.println(this.twse_id.size());
	}
	
	private void Read_Source() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + read_file_name);
		BufferedReader bfr = new BufferedReader(fr);
		
		JSONObject obj;
		while ((Line = bfr.readLine()) != null) 
		{			
			obj = new JSONObject(bfr.readLine());			
			//article_id = obj.get("article_id").toString();
			//article_title = obj.get("article_title").toString();
			Id(obj);
			Title(obj);
			Content(obj);
			//System.out.println(article_id+"	"+content);			
		}
		
		fr.close();
		bfr.close();
	}
	
	private void Id(JSONObject obj) throws Exception
	{
		article_id = obj.get("article_id").toString();
	}
	
	private void Title(JSONObject obj) throws Exception
	{
		article_title = obj.get("article_title").toString();	
	}
	
	private void Content(JSONObject obj) throws Exception
	{
		content = obj.get("content").toString();		
	}
	
	private void Id_match()
	{
		
	}
	
//	public static void main(String args[])
//	{
//		try {
//			Json_parser JP = new Json_parser();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}

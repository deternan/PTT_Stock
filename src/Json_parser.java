import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Json parser
 * 
 * version: April 25, 2018 11:22 PM
 * Last revision: April 25, 2018 11:22 PM
 * 
 * 
 */

public class Json_parser 
{

	private String read_file_path = "C:\\Users\\Barry\\Desktop\\";
	private String read_file_name = "Stock-3002-3002.json";
	private BufferedReader bfr;
	
	private String article_id;
	private String article_title;
	private String author;
	private String content;
	
	public Json_parser() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + read_file_name);
		BufferedReader bfr = new BufferedReader(fr);
		
		JSONObject obj;
		while ((Line = bfr.readLine()) != null) {
			//System.out.println(Line);
			obj = new JSONObject(bfr.readLine());			
			article_id = obj.get("article_id").toString();
			article_title = obj.get("article_title").toString();
			System.out.println(article_title);
			//JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
		}
		
		fr.close();
		bfr.close();
	}
	
	public static void main(String args[])
	{
		try {
			Json_parser JP = new Json_parser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

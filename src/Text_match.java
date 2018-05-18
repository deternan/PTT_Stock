import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Json parser
 * 
 * version: April 25, 2018 11:22 PM
 * Last revision: May 19, 2018 01:17 AM
 * 
 * 
 */

public class Text_match 
{

	private String read_file_path = "C:\\Users\\Barry\\Desktop\\";
	private String read_file_name = "Stock-3002-3002.json";
	private BufferedReader bfr;
	// type
	//final List<String> pageTypes = new ArrayList<>(Arrays.asList("標的", "新聞", "心得", "請益", "投顧", "其他", "公告"));
	final List<String> pageTypes_filter = new ArrayList<>(Arrays.asList("標的", "心得", "請益", "投顧"));
	
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
	
	// Regular expression 
	Pattern p_id;
	Matcher m;
	
	public Text_match(Vector twse_id, Vector twse_name, Vector tpex_id, Vector tpex_name) throws Exception
	{
		this.twse_id = twse_id;
		this.twse_name = twse_name;
		this.tpex_id = tpex_id;
		this.tpex_name = tpex_name;		
		//System.out.println(this.twse_id.size());
		
		Read_TextSource();
		
		
	}
	
	private void Read_TextSource() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + read_file_name);
		BufferedReader bfr = new BufferedReader(fr);
		
		JSONObject obj;
		boolean title_check;
		while ((Line = bfr.readLine()) != null) 
		{			
			title_check = false;
			obj = new JSONObject(bfr.readLine());			
			//article_id = obj.get("article_id").toString();
			//article_title = obj.get("article_title").toString();
			
			Title(obj);
			// Filter
			title_check = Title_Filter(article_title); 
			if(title_check == true) 
			{
				Id(obj);								
				Content(obj);				
				System.out.println(article_id+"	"+article_title+"	"+content);
				
				// matching
				TWSE_id_match(article_title + " "+content);
				
			}			
			
			//
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
	
	private boolean Title_Filter(String title)
	{
		boolean tag = false;
		for(int i=0; i<pageTypes_filter.size(); i++)
		{
			if(title.contains(pageTypes_filter.get(i).toString())) {
				tag = true;
				break;
			}
		}
		
		return tag;
	}
	
	private void Content(JSONObject obj) throws Exception
	{
		content = obj.get("content").toString();		
	}
	
	// Pattern
		// name & id
		// (id)
		// name(id)
	// 
	
	private void TWSE_id_match(String inputtext)
	{
		for(int i=0; i<twse_id.size(); i++)
		{
			if(inputtext.contains(twse_id.get(i).toString())) {
				System.out.println(twse_id.get(i)+"	"+twse_name.get(i));				
			}
		}
		
	}

	private void TPEX_id_match(String inputtext)
	{
		
		for(int i=0; i<tpex_id.size(); i++)
		{
			
		}
	}
	
}

package Data;

/*
 * create standard dataset
 * 
 * version: June 02, 2018 12:12 PM
 * Last revision: June 03, 2018 01:21 AM
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class StandardData_Text_Testing extends Parameters
{	
	//TWSE
	private Vector twse_id = new Vector();
	private Vector twse_name = new Vector();
	// TPEX
	private Vector tpex_id = new Vector();
	private Vector tpex_name = new Vector();
	// id & value
	private Vector id = new Vector();
	private Vector value = new Vector();
	// value tag
	private String Tag;
	// value regular
	private String regex = "[0-9]+\\.{1}[0-9]+";
	private Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	Vector all_value_temp = new Vector();
	// other info.
	private String articleid;
	private String date_str;
	private String author_str;
	// Current value
	private String current_value;
	
	
	
	public StandardData_Text_Testing(Vector twse_id, Vector twse_name, Vector tpex_id, Vector tpex_name, Vector id, Vector value) throws Exception
	{		
		this.twse_id = twse_id;
		this.twse_name = twse_name;
		this.tpex_id = tpex_id;
		this.tpex_name = tpex_name;		
		this.id = id;
		this.value = value;
		
		// Read PTT Text & matching
		Read_PTTText();
		
	}
	
	private void Read_PTTText() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(ppt_path + ppt_filename);
		BufferedReader bfr = new BufferedReader(fr);
		
		String array_temp[];
		
		while ((Line = bfr.readLine()) != null) 
		{
			Tag = "";
			current_value = "";
			all_value_temp.clear();
			
			array_temp = Line.split("\t");
			articleid = array_temp[0];
			author_str = array_temp[1];
			date_str = array_temp[2];
			
//			System.out.println(array_temp[0]+"		"+date_str);
			// TWSE
			TWSE_id_match(array_temp[3]);
			// TPEX
			TPEX_id_match(array_temp[3]);
			
			if(all_value_temp.size() <= 3) {
				for(int i=0; i<all_value_temp.size(); i++)
				{
					System.out.println(all_value_temp.get(i));
				}
			}
		}
	}
	
	private void TWSE_id_match(String input_content)
	{
		
		for(int i=0; i<twse_id.size(); i++)
		{
			// ID
			if(input_content.contains(twse_id.get(i).toString())) {
				// Name
				if(input_content.contains(twse_name.get(i).toString())){
					Tag = Tag_matching(input_content);
					Value_matching(input_content);
					current_value = Current_value_matching(twse_id.get(i).toString());
					System.out.println(articleid+"	"+date_str+"	"+twse_id.get(i)+"	"+twse_name.get(i)+"	"+Tag+"	"+current_value);					
				}
			}			
		}		
	}
		
	private void TPEX_id_match(String input_content) 
	{
		for (int i = 0; i < tpex_id.size(); i++) 
		{
			// ID
			if (input_content.contains(tpex_id.get(i).toString())) {
				// Name
				if (input_content.contains(tpex_name.get(i).toString())) {					
					Tag = Tag_matching(input_content);
					Value_matching(input_content);
					current_value = Current_value_matching(tpex_id.get(i).toString());
					System.out.println(articleid+"	"+date_str+"	"+tpex_id.get(i) + "	" + tpex_name.get(i)+"	"+Tag+"	"+current_value);

				}
			}
		}
	}
		
	private String Tag_matching(String input_content)
	{
		String tag_temp = "";
		for(int i=0; i<TagsTypes_filter.size(); i++)
		{
			if(input_content.contains(TagsTypes_filter.get(i))) {
				tag_temp = TagsTypes_filter.get(i);
			}
		}
		return tag_temp;
	}

	private void Value_matching(String input_content)
	{
		// Parsing
		Matcher matcher = pattern.matcher(input_content);
		while (matcher.find()) 
		{
			all_value_temp.add(matcher.group());
			//System.out.println(matcher.group());
		}
	}
	
	private String Current_value_matching(String id_input)
	{
		String return_value = "";
		for(int i=0; i<id.size(); i++)
		{
			if(id_input.equalsIgnoreCase(id.get(i).toString())) {
				return_value = value.get(i).toString();
				break;
			}
			
		}
		
		return return_value;
	}
	
}

package Data;

/*
 * create standard dataset
 * 
 * version: June 02, 2018 12:12 PM
 * Last revision: June 02, 2018 01:19 PM
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class StandardData_Text extends Parameters
{	
	//TWSE
	private Vector twse_id = new Vector();
	private Vector twse_name = new Vector();
	// TPEX
	private Vector tpex_id = new Vector();
	private Vector tpex_name = new Vector();
	// value tag
	private String Tag;
	// value regular
	private String regex = "[0-9]+\\.?[0-9]+";
	private Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	
	
	public StandardData_Text(Vector twse_id, Vector twse_name, Vector tpex_id, Vector tpex_name) throws Exception
	{		
		this.twse_id = twse_id;
		this.twse_name = twse_name;
		this.tpex_id = tpex_id;
		this.tpex_name = tpex_name;		
		
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
			
			array_temp = Line.split("\t");
			System.out.println(array_temp[0]+"	"+array_temp[1]);
			
			// TWSE
			TWSE_id_match(array_temp[3]);
			// TPEX
			TPEX_id_match(array_temp[3]);
			
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
					
					System.out.println(twse_id.get(i)+"	"+twse_name.get(i)+"	"+Tag);					
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
					
					System.out.println(tpex_id.get(i) + "	" + tpex_name.get(i)+"	"+Tag);

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
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}
	
}

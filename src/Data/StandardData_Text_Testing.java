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
	private String regex_point = "[0-9]+\\.{1}[0-9]+";
	private Pattern pattern_point;	
	//private Pattern pattern = Pattern.compile(regex_point, Pattern.MULTILINE);
	private String regex = "[0-9]+";
	private Pattern pattern;
	Vector all_value_temp = new Vector();
	// other info.
	private String articleid;
	private String date_str;
	private String author_str;
	// Current value
	private double current_value;
//	private double rangevalue_up;
//	private double rangevalue_down;
	private boolean range_check;
	private double rate = 1.5;
	
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
			current_value = 0;
			all_value_temp.clear();
			range_check = false;
			
			array_temp = Line.split("\t");
			articleid = array_temp[0];
			author_str = array_temp[1];
			date_str = array_temp[2];
			
			// TWSE
			TWSE_id_match(array_temp[3]);
			// TPEX
			TPEX_id_match(array_temp[3]);
			
			if(all_value_temp.size() <= 10) 
			{
				//range_check = Ranger_filetr(current_value);
				for(int i=0; i<all_value_temp.size(); i++)
				{
					range_check = Ranger_filetr(current_value, Double.parseDouble(all_value_temp.get(i).toString()));
					System.out.println(all_value_temp.get(i)+"	"+range_check);
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
					break;
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
					break;
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
		pattern_point = Pattern.compile(regex_point, Pattern.MULTILINE);
		Matcher matcher_point = pattern_point.matcher(input_content);
		while (matcher_point.find()) 
		{
			all_value_temp.add(matcher_point.group());
			//System.out.println(matcher_point.group());
		}
		
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(input_content);
		while (matcher.find()) 
		{
			all_value_temp.add(matcher.group());
			//System.out.println(matcher.group());
		}
		
	}
	
	private double Current_value_matching(String id_input)
	{
		double return_value = 0;
		for(int i=0; i<id.size(); i++)
		{
			if(id_input.equalsIgnoreCase(id.get(i).toString())) {
				return_value = Double.parseDouble(value.get(i).toString());
				break;
			}
			
		}
		
		return return_value;
	}
	
	private boolean Ranger_filetr(double value, double testing_value)
	{
		double up_temp = value * rate;
		double down_temp = value - (up_temp - value);
		boolean check = false;		
		//System.out.println(value+"	"+up_temp+"	"+down_temp);
		if((testing_value >= down_temp) && (testing_value <= up_temp)) {
			check = true;
		}
		
		return check;
	}
	
	private void class_Tagging()
	{
		
	}
	
}

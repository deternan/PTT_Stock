package Data;

/*
 * create standard dataset & evaluation
 * 
 * version: June 02, 2018 12:12 PM
 * Last revision: June 07, 2018 01:28 AM
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
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
	private String tag_regex = "(2\\.){1}(.*)?(3\\.){1}\\s?分析{1}";
	private String Tag_reg;	
	// value regular	
	private String val_regex = "([0-9]+\\.?[0-9]+)";
	private Pattern pattern;		
	Vector all_value_temp = new Vector();
	// other info.
	private String articleid;
	private String date_str;
	private String author_str;
	private String PTTtext;
	// Current value
	private double current_value;
	private boolean range_check;
	private boolean numerical_check;
	private double rate = 1.5;
	// Range value 	
	//private double [] rangevalue = new double[10];
	private Vector rangevalue_vec = new Vector();
	
	// Index
	private String comid;
	private String comname;
	// Evaluation
	private int totalCount = 0;
	private double correct_num = 0;
	private double error_num = 0;
	
	// output
	BufferedWriter writer;
	
	public StandardData_Text_Testing(Vector twse_id, Vector twse_name, Vector tpex_id, Vector tpex_name, Vector id, Vector value) throws Exception
	{		
		this.twse_id = twse_id;
		this.twse_name = twse_name;
		this.tpex_id = tpex_id;
		this.tpex_name = tpex_name;		
		this.id = id;
		this.value = value;
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(text_path + tex_filename), "utf-8"));
		
		// Read PTT Text & matching
		Read_PTTText();		
		
		writer.close();
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
			Tag_reg = "";			
			current_value = 0;
			all_value_temp.clear();
			PTTtext = "";
			range_check = false;			
			//Arrays.fill(rangevalue, 0);
			rangevalue_vec.clear();
			comid = "";
			comname = "";
			
			array_temp = Line.split("\t");
			articleid = array_temp[0];
			author_str = array_temp[1];
			date_str = array_temp[2];
			PTTtext = array_temp[3];
			
			// TWSE
			TWSE_id_match(array_temp[3]);
			// TPEX
			TPEX_id_match(array_temp[3]);
			
			//if(all_value_temp.size() <= 10) 
			{
				//range_check = Ranger_filetr(current_value);
				for(int i=0; i<all_value_temp.size(); i++)
				{
					numerical_check = false;
					range_check = Ranger_filetr(current_value, Double.parseDouble(all_value_temp.get(i).toString()));
					if(all_value_temp.get(i).toString().subSequence(all_value_temp.get(i).toString().length() -1, all_value_temp.get(i).toString().length()).toString().equalsIgnoreCase(".") == false){
						numerical_check = true;
					}
					 
					if((range_check == true) && (numerical_check == true)){
						rangevalue_vec.add(all_value_temp.get(i));
					}					
					//System.out.println(all_value_temp.get(i)+"	"+range_check+"	"+numerical_check);
				}				
							
			}
			// Tagging
			//System.out.println(articleid+"	"+current_value+"	"+rangevalue_vec.size());
			class_Tagging(articleid, current_value, rangevalue_vec);
		}
		
		// Accuracy
		System.out.println("-------------------------------------------");
		System.out.println(totalCount+"	"+correct_num+"	"+error_num);
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
					current_value = Current_value_matching(twse_id.get(i).toString());
					Value_matching(input_content);
					Tag_reg = Tag_matching_Regular(input_content);
					comid = twse_id.get(i).toString();
					comname = twse_name.get(i).toString();
					
					//System.out.println(articleid+"	"+date_str+"	"+tpex_id.get(i) + "	" + tpex_name.get(i)+"	"+Tag+"	"+current_value);
//					if(Tag_reg.trim().length() == 1){						
//						System.out.println(articleid+"	"+date_str+"	"+twse_id.get(i) + "	" + twse_name.get(i)+"	"+Tag_reg+"	"+current_value);
//					}					
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
					current_value = Current_value_matching(tpex_id.get(i).toString());					
					Value_matching(input_content);
					Tag_reg = Tag_matching_Regular(input_content);
					comid = tpex_id.get(i).toString();
					comname = tpex_name.get(i).toString();
					
					//System.out.println(articleid+"	"+date_str+"	"+tpex_id.get(i) + "	" + tpex_name.get(i)+"	"+Tag+"	"+current_value);
					if(Tag_reg.trim().length() == 1){
						
						//System.out.println(articleid+"	"+date_str+"	"+tpex_id.get(i) + "	" + tpex_name.get(i)+"	"+Tag_reg+"	"+current_value);
					}
					
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

	private String Tag_matching_Regular(String input_content)
	{				
		String temp = "";
		String pattern_str;
		int start_point;
		int end_point;
		// Parsing
		pattern = Pattern.compile(tag_regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(input_content);		
		while (matcher.find()) 
		{			
			//System.out.println(matcher.group());
			if(matcher.group().toString().length() < 20){
				pattern_str = matcher.group().toString();
				start_point = pattern_str.indexOf("分類"); 
				end_point = pattern_str.indexOf("3.");
				//System.out.println(pattern_str);
				
				temp = pattern_str.substring(start_point+3, end_point);
				if(temp.contains(":")){
					temp = temp.substring(0, temp.indexOf(":"));
				}else if(temp.contains("/")){
					temp = temp.substring(0, temp.indexOf("/"));
				}				
			}
		}		
		
		return temp;
	}
	
	private void Value_matching(String input_content)
	{		
		// Parsing
		pattern = Pattern.compile(val_regex, Pattern.MULTILINE);
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
	
	private void class_Tagging(String articleid, double realValue, Vector rangeValue) throws Exception
	{
		//if((comid.length() > 0) && (Tag.length() > 0))
		//if((comid.length() > 0) && (Tag_reg.length() > 0))
		{
			//System.out.println(comid+"	"+comname+"	"+realValue+"	"+rangeValue.size());
			if(rangeValue.size() == 1){
				//System.out.println(comid+"	"+comname+"	"+realValue+"	"+rangeValue.size());
				//System.out.println("1:	"+rangeValue.get(0));
				
				// Evaluation	// 
				//oneValue_accuracy(Tag, Double.parseDouble(rangeValue.get(0).toString()), current_value);
				oneValue_accuracy(Tag_reg, Double.parseDouble(rangeValue.get(0).toString()), current_value);
				//totalCount++;
			}else if(rangeValue.size() == 2){				
				//System.out.println(comid+"	"+comname+"	"+realValue+"	"+rangeValue.size());
				//System.out.println("2:	"+rangeValue.get(0)+"	"+rangeValue.get(1));
				
				// Evaluation
				//twoValue_accuracy(Tag, Double.parseDouble(rangeValue.get(0).toString()), Double.parseDouble(rangeValue.get(1).toString()), current_value);
				twoValue_accuracy(Tag_reg, Double.parseDouble(rangeValue.get(0).toString()), Double.parseDouble(rangeValue.get(1).toString()), current_value);
				//totalCount++;
			}else if((rangeValue.size() > 2) && (rangeValue.size() < 10)){
				//Vector_Sort(rangeValue, Tag_reg, current_value);				
			}
		}
		
	}
	
	private void Vector_Sort(Vector rangeValue, String Texttag, double realValue)
	{
		double max = -1;
		double min = 3000;
		double [] temp = new double[rangeValue.size()];
		for(int i=0; i<rangeValue.size(); i++)
		{
			temp[i] = Double.parseDouble(rangeValue.get(i).toString());
		}
		//System.out.println(temp.length);
		Arrays.sort(temp);
//		System.out.println("M:	"+temp[0]+"	"+temp[temp.length-1]);		// small	large
		
		// Accuracy evaluation
		double X = (min + max) / 2;
		if (Texttag.trim().equalsIgnoreCase("多")) {
			if(realValue >= X) {
				correct_num++;
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
			}else {
				error_num++;
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		} else if (Texttag.equalsIgnoreCase("空")) {
			if(realValue < X) {
				correct_num++;
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
			}else {
				error_num++;
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		}
	}
	
	private void oneValue_accuracy(String Texttag, double textValue, double realValue) throws Exception
	{		
		double X = textValue;
		//System.out.println(Texttag+"	"+X+"	"+realValue);
		//System.out.println(articleid+"	"+date_str+"	"+comid + "	" + comname+"	"+Tag_reg+"	"+current_value);
		if(Texttag.trim().equalsIgnoreCase("多")) {
			if(realValue >= X) {  				
				correct_num++;
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
				output_processing("1");
			}else {
				error_num++;
				output_processing("-1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		}else if(Texttag.equalsIgnoreCase("空")) {
			if(realValue < X) {
				correct_num++;
				output_processing("1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
			}else {
				error_num++;
				output_processing("-1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		}
	}
	
	private void twoValue_accuracy(String Texttag, double min, double max, double realValue) throws Exception
	{
		double X = (min + max) / 2;
		//System.out.println(Texttag+"	"+X+"	"+realValue);
		//System.out.println(articleid+"	"+date_str+"	"+comid + "	" + comname+"	"+Tag_reg+"	"+current_value);
		if (Texttag.trim().equalsIgnoreCase("多")) {
			if(realValue >= X) {
				correct_num++;
				output_processing("1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
			}else {
				error_num++;
				output_processing("-1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		} else if (Texttag.equalsIgnoreCase("空")) {
			if(realValue < X) {
				correct_num++;
				output_processing("1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	correct"+"	"+PTTtext.length());
			}else {
				error_num++;
				output_processing("-1");
				System.out.println(articleid+"	"+date_str+"	"+comid+"	"+comname+"	"+Texttag+"	"+realValue+"	"+X+"	error"+"	"+PTTtext.length());
			}
			totalCount++;
		}
	}
	
	private void output_processing(String classTag) throws Exception
	{
		writer.write(classTag+"\t"+PTTtext.replaceAll("\\[標的\\]", "")+"\n");
	}
	
	
}

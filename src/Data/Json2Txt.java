package Data;

/*
 * PTT data, Json to text 
 * 
 * version: May 23, 2018 00:59 AM
 * Last revision: June 01, 2018 09:14 PM
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

public class Json2Txt 
{
	private String read_file_path = "C:\\Users\\Barry.Ke\\Desktop\\ptt\\";	
	//private String read_file_name = "Stock-4273-4273.json";
	private String read_file_name;
	private BufferedReader bfr;
	
	final List<String> pageTypes_filter = new ArrayList<>(Arrays.asList("標的", "心得", "投顧"));
	
	private String article_id;
	private String article_title;
	private String author;
	private String content;
	private String date;
	
	// Date
	Calendar today;
	Date s_date;	
		// time gap
		Date dateline;	
		int month_gap = 3;
	int setting_month = 6;
	int setting_day = 1;
	
	// output
	private String output_folder = "C:\\Users\\Barry.Ke\\Desktop\\";
	private String output_file = "ptt_text.txt";
	BufferedWriter writer;
	
	public Json2Txt() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_folder + output_file), "utf-8"));
		
		Setting_Date();			
		Read_folder();

		writer.close();
	}
	
	private void Setting_Date() throws Exception
	{
		// Today
		today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		//System.out.println(today.getTime());
				
		// specific date
		//s_date = new Date(2018-1900, setting_month-1-month_gap, setting_day);
		s_date = new Date(2018-1900, setting_month-1, setting_day);
		//System.out.println(s_date);
		// deadline
		dateline = s_date;		
		dateline.setMonth(dateline.getMonth() - month_gap);
		//System.out.println(dateline);		
	}
	
	private void Read_folder() throws Exception
	{
		File folder = new File(read_file_path);		
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) 
		{
		    if (file.isFile()) {
		        //System.out.println(file.getName());
		        read_file_name = file.getName();
		        Read_TextSource(read_file_name);
		    }
		}
	}
	
	private void Read_TextSource(String name) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + name);
		//System.out.println(read_file_path + name);
		BufferedReader bfr = new BufferedReader(fr);
		
		//JSONObject obj;
		boolean title_check;
		boolean date_check;		
		while ((bfr.readLine()) != null) 
		{			
			Line = bfr.readLine();
			title_check = false;
			date_check = false;
						
			if(Line != null)
			{				
				if(Line.toString().length() > 1000)
				{					
					JSONObject obj = new JSONObject(Line.toString());
										
					//article_id = obj.get("article_id").toString();
					date = obj.get("date").toString();
					article_title = obj.get("article_title").toString();
					
					//System.out.println(read_file_path + name+"	"+date);
					// Filter
					// Date 
					if((date != null) && (date.length() >0)){
						date_check = Date_comparison(date);
						
						// title
						title_check = Title_Filter(article_title); 
						if((title_check == true) && (date_check == true)) 
						{
							//System.out.println(date);
							Id(obj);
							Title(obj);
							author(obj);
							Content(obj);
											
							//System.out.println(article_id+"	"+author+"	"+date+"	"+article_title+"	"+content);
							System.out.println(article_id+"	"+author+"	"+date);
							writer.write(article_id+"	"+author+"	"+date+"	"+article_title+" "+content+"\n");
						}	
					}
				}
			}
		}
		
		fr.close();
		bfr.close();
	}
	
	private boolean Date_comparison(String input_date) throws Exception 
	{		
		//System.out.println(input_date);
		boolean data_check;
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy", Locale.ENGLISH);
		Date pttdate = formatter.parse(input_date);		
		//System.out.println(pttdate);
		
		if(dateline.before(pttdate)) {			
			data_check = true;
		}else {			
			data_check = false;
		}
		
		return data_check;		
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
	
	private void Id(JSONObject obj) throws Exception
	{
		article_id = obj.get("article_id").toString();
	}
	
	private void Title(JSONObject obj) throws Exception
	{
		article_title = obj.get("article_title").toString();	
	}
	
	private void author(JSONObject obj) throws Exception
	{
		author = obj.get("author").toString();
	}
	
	private void Content(JSONObject obj) throws Exception
	{
		content = obj.get("content").toString();
	}
	
	public static void main(String[] args) 
	{
		try {
			Json2Txt ca = new Json2Txt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

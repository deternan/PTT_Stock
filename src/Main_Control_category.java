
/*
 * date category
 * 
 * version: May 23, 2018 00:59 AM
 * Last revision: May 23, 2018 00:59 AM
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

public class Main_Control_category 
{
	private String read_file_path = "C:\\Users\\Barry\\Desktop\\";
	private String read_file_name = "Stock-4273-4273.json";
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
		
	
	public Main_Control_category() throws Exception
	{
		Setting_Date();
		
		Read_TextSource();
		//System.out.println(s_date);
		//Date dateline = s_date;
		
		//dateline.setMonth(dateline.getMonth() - month_gap);
		//System.out.println(dateline);
		
		//System.out.println(dateline.after(s_date));
		
//		if(dateline.compareTo(s_date) < 0) {
//			System.out.println(dateline);
//		}else {
//			System.out.println(s_date);
//		}
	}
	
	private void Setting_Date()
	{
		// Today
		today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		//System.out.println(today.getTime());
		
		// specific date
		s_date = new Date(2018-1900, 05-1, 22);		
		//System.out.println(s_date);
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
			article_title = obj.get("article_title").toString();
			date = obj.get("date").toString();
			
			//Title(obj);
			// Filter
			title_check = Title_Filter(article_title); 
			if(title_check == true) 
			{
				//Id(obj);								
				//Content(obj);				
				//System.out.println(article_id+"	"+article_title+"	"+content);
				
				Date_comparison(date);
				
				//System.out.println(date+"	"+article_title);
			}						
		}
		
		fr.close();
		bfr.close();
	}
	
	private void Date_comparison(String input_date) throws ParseException
	{
		// Wed May 16 11:32:45 2018
		System.out.println(input_date);
		DateFormat formatter = new SimpleDateFormat("E MMM dd hh:mm:ss yyyy", Locale.TAIWAN);
		Date pttdate = formatter.parse(input_date);
//		System.out.println(pttdate);
		//dateline
		//Date parse = SimpleDateFormat.parse(input_date);
		
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
	
	public static void main(String[] args) 
	{
		try {
			Main_Control_category ca = new Main_Control_category();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

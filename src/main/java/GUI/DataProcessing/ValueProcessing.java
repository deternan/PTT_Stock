package GUI.DataProcessing;



/*
 * "fields":[
"日期",
"成交股數",
"成交金額",
"開盤價",
"最高價",
"最低價",
"收盤價",
"漲跌價差",
"成交筆數"
]
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import GUI.httpGet.Units;

public class ValueProcessing 
{
	// Temp (Read specific JSON)
	private String ssfolder = "/Users/phelps/Desktop/2388_201906.txt";
	private BufferedReader bfr;
	String sourceLine = "";
	
	// Date
	private int monthGap;
	
	// Storage
	FileWriter fw;
	

	
	public ValueProcessing() throws Exception
	{
		StorageInitial("2388");
		
		// Date
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdf  = new SimpleDateFormat(Units.basic_pattern);
		String todayStr = sdf.format(today);
		String specificDateStr = Units.startYear + Units.startMonth + Units.startDay;		
		monthGap = getMonthGap(todayStr, specificDateStr);
		System.out.println(monthGap);
		
		// Processing and Storage
//		sourceData();
//		Processing(sourceLine);
		
		fw.close();
	}
	
	private void StorageInitial(String inputId) throws Exception
	{
		File file = new File(Units.value_folder + inputId + Units.extension);
		if(file.exists()) {
			fw = new FileWriter(Units.value_folder + inputId + Units.extension, true);
			
		}else {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Units.value_folder + inputId + Units.extension), "utf-8"));
			writer.close();
		}
	}
	
	private void sourceData() throws Exception
	{	
		FileReader fr = new FileReader(ssfolder);
		bfr = new BufferedReader(fr);
		String Line = "";		
		while((Line = bfr.readLine())!=null)
		{	
			sourceLine = Line;
			//System.out.println(Line);			
		}
		fr.close();
		bfr.close();
	}
	
	private void Processing(String jsonStr) throws Exception
	{
		JSONObject obj = new JSONObject(jsonStr);			
		//System.out.println(obj.get("data"));
		JSONArray jsonarray = new JSONArray(obj.get("data").toString());
		for(int i=0; i<jsonarray.length(); i++)
		{
			JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
			//System.out.println(arrayData.get(0)+"	"+arrayData.get(6));
			Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
		}
	}
	
	private void Storage(String inputDate, String inputValue) throws Exception
	{
		fw.write("\n"+inputDate.replaceAll("/", "")+"	"+inputValue);
	}
	
	// Date Processing
	private int getMonthGap(String d1, String d2) throws Exception
	{
		SimpleDateFormat sdf  = new SimpleDateFormat(Units.basic_pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(d1));
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);
         
        c.setTime(sdf.parse(d2));
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);
         
        int result;
        if(year1 == year2) {
            result = month1 - month2;
        } else {
            result = 12*(year1 - year2) + month1 - month2;
        }
        //System.out.println(result);
        
        return result;
	}
	
	public static void main(String args[])
	{
		try {
			ValueProcessing vp = new ValueProcessing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

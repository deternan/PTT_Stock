package GUI.DataProcessing;

/*
 * Parser values
 * version: June 30, 2019 07:23 PM
 * Last revision: July 02, 2019 00:52 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import GUI.httpGet.Units;

public class GetValueandProcessing 
{
	// Temp (Parameter)
	private String ID = "2388";
	// Get JsonResponse
	String sourceLine = "";
	
	// Date
	private int monthGap = 0;
	
	// Read data
	File file;
	// Storage
	BufferedWriter writer;
	
	// check
	boolean dataData_check;
	
	public GetValueandProcessing() throws Exception
	{
		dataData_check = true;
		file = new File(Units.value_folder + ID + Units.extension);
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		
		// Date
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdf  = new SimpleDateFormat(Units.basic_pattern);
		String todayStr = sdf.format(today);
		String specificDateStr = Units.startYear + Units.startMonth + Units.startDay;
		monthGap = getMonthGap(todayStr, specificDateStr);
		//System.out.println(monthGap);
		
		List<String> monthList = MonthIncrement(Units.startYear + Units.startMonth, monthGap);
		
		String existDate = "";
		for(int i=0; i<monthList.size(); i++)
		{
			sourceLine = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			existDate = convertTWDate(monthList.get(i));
			existDate = existDate.substring(0, existDate.length()-2);
			//System.out.println(monthList.get(i)+"	"+existDate);
			// check whether data is exist ?
			checkExistDate(existDate, bfr);
		
			// Save new values to file
			if(dataData_check == true) {
				// Processing and Storage
					// get data from URL
					GetValues(monthList.get(i) + Units.startDay);
					// Processing
					Processing(sourceLine);
					
				// Thread sleep
				Thread.sleep((int) Units.sleepTime);
			}
			bfr.close();
		}

		writer.close();
		
		// Message
		System.out.println(ID+"	Finished");
	}
	
	private void GetValues(String DateStr)
	{
		String URL = Units.valueUrl + DateStr + "8&stockNo=" + ID;
		try {
			HttpsGet https = new HttpsGet();
			sourceLine = https.responseJSON(URL);					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void Processing(String jsonStr) throws Exception
	{
		JSONObject obj = new JSONObject(jsonStr);
		//System.out.println(obj.get("data"));
		if(obj.has("data")) {
			JSONArray jsonarray = new JSONArray(obj.get("data").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				//System.out.println(arrayData.get(0)+"	"+arrayData.get(6));
				Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
			}
		}
	}
	
	private void Storage(String inputDate, String inputValue) throws Exception
	{
		//fw.write("\n"+inputDate.replaceAll("/", "")+"	"+inputValue);
		writer.write(inputDate.replaceAll("/", "")+"	"+inputValue+"\n");
	}
	
	// avoid duplication
	private void checkExistDate(String DateStr, BufferedReader bfr) throws Exception
	{
		String Line = "";
		String temp[];
		String YYMM;
		
		while((Line = bfr.readLine())!=null)
		{					
			temp = Line.split("	");
			if(temp.length == 2) {	
				YYMM = temp[0].substring(0, temp[0].length()-2);
				//System.out.println(DateStr+"	"+YYMM);
				if(DateStr.trim().equalsIgnoreCase(YYMM)) {
					dataData_check = false;
					break;
				}
			}
		}
	}
	
	private List<String> MonthIncrement(String startDate, int addMonths) throws Exception
	{
		List<String> monthList = new ArrayList();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
        
        for(int i=0; i<=addMonths; i++){
        	//Thread.sleep(10000);
        	Date dt=sdf.parse(startDate);
        	Calendar rightNow = Calendar.getInstance();
        	rightNow.setTime(dt);
    		if(i != 0){
    			rightNow.add(Calendar.MONTH,1);
    		}
        	Date dt1=rightNow.getTime();
        	String reStr = sdf.format(dt1);
        	//System.out.println(reStr);
        	startDate = reStr;
        	monthList.add(reStr);
        }
        
        return monthList;
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
        
        return result;
	}
	
	private String convertTWDate(String AD) throws Exception 
	{
	    SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
	    SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
	    Calendar cal = Calendar.getInstance();
	    String TWDate = "";
	    
	    cal.setTime(df4.parse(AD));
        cal.add(Calendar.YEAR, -1911);
        TWDate = Integer.toString(cal.get(Calendar.YEAR)) + df2.format(cal.getTime());
        return TWDate;
	}
	
	public static void main(String args[])
	{
		try {
			GetValueandProcessing vp = new GetValueandProcessing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
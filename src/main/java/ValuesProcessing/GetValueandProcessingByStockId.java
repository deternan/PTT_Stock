package ValuesProcessing;

/*
 * Parser values
 * version: June 30, 2019 07:23 PM
 * Last revision: July 04, 2019 00:12 AM
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;

public class GetValueandProcessingByStockId 
{
	String value_folder = "/Users/phelps/Documents/github/PTT_Stock/output/Values/";
	String extension = ".txt";
	
	// Temp (Parameter)
	private String ID;
	private String tag;
	// Get JsonResponse
	String sourceLine = "";
	
	// Date
	private int monthGap = 0;
	
	// Read data
	File file;
		boolean filecheck;
	// Storage
	BufferedWriter writer;
	
	// check
	boolean dataData_check;
	
	// Timer
	TimeZone tz = TimeZone.getTimeZone("Asia/Taipei");		
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	String nowAsISO;
	
	public GetValueandProcessingByStockId(String ID, String tag) throws Exception
	{
		this.ID = ID;
		this.tag = tag;
		filecheck = false;
		
		dataData_check = true;
		file = new File(value_folder + this.ID + extension);
		if(file.exists() == false) {
			filecheck = true;
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		}else {
			FileOutputStream fos = new FileOutputStream(Units.value_folder + this.ID + Units.extension, true);
		}
		
		// Date
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdf  = new SimpleDateFormat(Units.basic_pattern);
		String todayStr = sdf.format(today);
		String specificDateStr = Units.startYear + Units.startMonth + Units.startDay;
		monthGap = getMonthGap(todayStr, specificDateStr);
		
		List<String> monthList = MonthIncrement(Units.startYear + Units.startMonth, monthGap);
		
		String existDate = "";
		for(int i=0; i<monthList.size(); i++)
		{
			sourceLine = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			existDate = convertTWDate(monthList.get(i));
			existDate = existDate.substring(0, existDate.length()-2);
			// check whether data is exist ?
			checkExistDate(existDate, bfr);
			
			// Save new values to file
			if(dataData_check == true) {
				// Processing and Storage
					// get data from URL
				if("twse".equalsIgnoreCase(this.tag)) {
					GetValues(monthList.get(i) + Units.startDay, this.tag);
					if(isJSONValid(sourceLine)) {
						Processing_TWSE(sourceLine);
					}
				}else if("tpex".equalsIgnoreCase(this.tag)) {
					GetValues(existDate.substring(0, 3) + "/"+ existDate.substring(3, 5), this.tag);
					if(isJSONValid(sourceLine)) {
						Processing_TPEX(sourceLine);
					}
				}			
			}
			
			bfr.close();
			
			if(filecheck == true) {
				// Thread sleep
				Thread.sleep((int) Units.sleepTime);
			}
			
		}

		if(filecheck == true) {
			writer.close();
		}
		
		// Message
		System.out.println(this.ID+"	Finished");
	}
	
	private void GetValues(String DateStr, String tag)
	{
		sourceLine = "";
		String URL = "";
		if("twse".equalsIgnoreCase(tag)) {
			// TWSE
			// https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190101&stockNo=2388
			URL = Units.TWSEvalueUrl + DateStr + "8&stockNo=" + ID;
		}else if("tpex".equalsIgnoreCase(tag)) {
			// TPEX
			// https://www.tpex.org.tw/web/stock/aftertrading/daily_trading_info/st43_result.php?d=107/08&stkno=3105
			URL = Units.TPEXvalueUrl + DateStr + "&stkno=" + ID;
		}
		
		System.out.println(DateStr);
		
		try {
			HttpsGet https = new HttpsGet();
			
			if(https.responseJSON(URL).isEmpty() == false) {
				sourceLine = https.responseJSON(URL);
			}
								
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void Processing_TWSE(String jsonStr) throws Exception
	{
		JSONObject obj = new JSONObject(jsonStr);
		if(obj.has("data")) {
			JSONArray jsonarray = new JSONArray(obj.get("data").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				System.out.println(arrayData.get(0)+"	"+arrayData.get(6));
				Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
			}
		}
	}
	
	private void Processing_TPEX(String jsonStr) throws Exception
	{
		JSONObject obj = new JSONObject(jsonStr);
		if(obj.has("aaData")) {
			JSONArray jsonarray = new JSONArray(obj.get("aaData").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
			}
		}
	}
	
	private void Storage(String inputDate, String inputValue) throws Exception
	{
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

	private boolean isJSONValid(String jsonInString) {
		
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check; 
		check = jsonele.isJsonObject();
		return check;
	}
	
}

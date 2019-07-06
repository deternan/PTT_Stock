package GUI.test;

/*
 * Testing 
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
import java.io.PrintStream;
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
import GUI.DataProcessing.HttpsGet;

public class Testing 
{
	// Temp (Parameter)
	private String ID;
	// Get JsonResponse
	String sourceLine = "{\"stkNo\":\"3105\",\"stkName\":\"\\u7a69\\u61cb\",\"showListPriceNote\":false,\"showListPriceLink\":false,\"reportDate\":\"107\\/08\",\"iTotalRecords\":23,\"aaData\":[[\"107\\/08\\/01\",\"7,031\",\"1,090,434\",\"148.50\",\"159.00\",\"148.50\",\"156.00\",\"8.00\",\"4,696\"],[\"107\\/08\\/02\",\"4,052\",\"628,318\",\"158.00\",\"158.00\",\"152.00\",\"156.00\",\"0.00\",\"2,937\"],[\"107\\/08\\/03\",\"5,463\",\"875,457\",\"158.50\",\"163.00\",\"157.00\",\"162.00\",\"6.00\",\"3,615\"],[\"107\\/08\\/06\",\"4,183\",\"688,618\",\"164.00\",\"166.00\",\"162.00\",\"165.00\",\"3.00\",\"2,966\"],[\"107\\/08\\/07\",\"6,501\",\"1,097,972\",\"166.00\",\"170.50\",\"166.00\",\"170.50\",\"5.50\",\"4,490\"],[\"107\\/08\\/08\",\"6,309\",\"1,087,182\",\"173.00\",\"174.00\",\"169.00\",\"169.00\",\"-1.50\",\"4,208\"],[\"107\\/08\\/09\",\"3,503\",\"576,150\",\"169.00\",\"169.00\",\"162.50\",\"165.00\",\"-4.00\",\"2,536\"],[\"107\\/08\\/10\",\"5,617\",\"926,131\",\"165.00\",\"168.50\",\"161.00\",\"165.00\",\"0.00\",\"3,840\"],[\"107\\/08\\/13\",\"6,649\",\"1,034,815\",\"162.50\",\"164.00\",\"149.00\",\"153.50\",\"-11.50\",\"4,757\"],[\"107\\/08\\/14\",\"3,927\",\"610,701\",\"156.00\",\"158.00\",\"152.50\",\"156.00\",\"2.50\",\"2,960\"],[\"107\\/08\\/15\",\"10,765\",\"1,583,501\",\"155.50\",\"155.50\",\"144.00\",\"145.50\",\"-10.50\",\"7,441\"],[\"107\\/08\\/16\",\"9,297\",\"1,299,501\",\"136.50\",\"144.00\",\"134.50\",\"144.00\",\"-1.50\",\"6,389\"],[\"107\\/08\\/17\",\"7,691\",\"1,142,877\",\"147.00\",\"152.00\",\"145.50\",\"146.50\",\"2.50\",\"5,404\"],[\"107\\/08\\/20\",\"6,703\",\"1,005,302\",\"149.00\",\"153.00\",\"147.00\",\"151.00\",\"4.50\",\"4,448\"],[\"107\\/08\\/21\",\"7,300\",\"1,141,418\",\"152.00\",\"159.50\",\"152.00\",\"158.50\",\"7.50\",\"5,298\"],[\"107\\/08\\/22\",\"13,326\",\"2,197,711\",\"162.50\",\"168.00\",\"161.50\",\"162.50\",\"4.00\",\"9,095\"],[\"107\\/08\\/23\",\"7,703\",\"1,236,515\",\"162.50\",\"163.00\",\"158.00\",\"160.00\",\"-2.50\",\"5,391\"],[\"107\\/08\\/24\",\"7,552\",\"1,199,562\",\"158.00\",\"163.00\",\"155.00\",\"162.00\",\"2.00\",\"5,374\"],[\"107\\/08\\/27\",\"5,327\",\"864,448\",\"164.00\",\"164.00\",\"160.00\",\"162.50\",\"0.50\",\"3,675\"],[\"107\\/08\\/28\",\"6,926\",\"1,141,321\",\"163.00\",\"167.00\",\"161.00\",\"161.00\",\"-1.50\",\"4,613\"],[\"107\\/08\\/29\",\"3,290\",\"531,782\",\"162.00\",\"164.00\",\"159.50\",\"161.00\",\"0.00\",\"2,348\"],[\"107\\/08\\/30\",\"7,243\",\"1,198,638\",\"162.50\",\"167.00\",\"162.50\",\"166.00\",\"5.00\",\"4,891\"],[\"107\\/08\\/31\",\"4,071\",\"674,216\",\"165.00\",\"167.00\",\"163.50\",\"166.50\",\"0.50\",\"2,685\"]]}";
	
	// Date
	private int monthGap = 0;
	
	// Read data
	File file;
		boolean filecheck;
	// Storage
//	BufferedWriter writer;
	FileOutputStream writer;
	PrintStream ps;
	
	// check
	boolean dataData_check;
	
	// Timer
	TimeZone tz = TimeZone.getTimeZone("Asia/Taipei");		
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	String nowAsISO;
	
	public Testing() throws Exception
	{
		this.ID = ID;
		filecheck = false;
		
		dataData_check = true;
		file = new File(Units.value_folder + this.ID + Units.extension);
//		if(file.exists() == false) {
//			filecheck = true;
//			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
//		}else {
//			FileOutputStream fos = new FileOutputStream(Units.value_folder + this.ID + Units.extension, true);
//		}
		writer = new FileOutputStream(Units.value_folder + this.ID + Units.extension, true);
		
		// Date
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdf  = new SimpleDateFormat(Units.basic_pattern);
		String todayStr = sdf.format(today);
		String specificDateStr = Units.startYear + Units.startMonth + Units.startDay;
		monthGap = getMonthGap(todayStr, specificDateStr);
		
		List<String> monthList = MonthIncrement(Units.startYear + Units.startMonth, monthGap);
		
		
		//for(int i=0; i<monthList.size(); i++)
		{
						
			// Save new values to file
			//if(dataData_check == true) 
			{
				// Processing and Storage
					
					if(isJSONValid(sourceLine))
					{
						System.out.println("Processing");
						// Processing
						Processing_TPEX(sourceLine);
						//System.out.println(this.ID+"	"+monthList.get(i)+Units.startDay);
					}		
			}
			
		}

		writer.close();

	}

	
	private void Processing_TPEX(String jsonStr) throws Exception
	{
		JSONObject obj = new JSONObject(jsonStr);
		//System.out.println(obj.get("data"));
		if(obj.has("aaData")) {
			JSONArray jsonarray = new JSONArray(obj.get("aaData").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				System.out.println(arrayData.get(0)+"	"+arrayData.get(6));
				//Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
			}
		}
	}
	
	private void Storage(String inputDate, String inputValue) throws Exception
	{
		
		//writer.write(inputDate.replaceAll("/", "")+"	"+inputValue+"\n");
		ps = new PrintStream(writer); 
		ps.println(inputDate.replaceAll("/", "")+"	"+inputValue);
		ps.close();
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

	private boolean isJSONValid(String jsonInString) {
		
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check; 
		check = jsonele.isJsonObject();
		return check;
	}
	
	public static void main(String args[])
	{
		try {
			Testing tt = new Testing();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

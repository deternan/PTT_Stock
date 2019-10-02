package ValuesProcessing;

/*
 * download values (Main)
 * version: July 03, 2019 07:23 PM
 * Last revision: October 02, 2019 09:10 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetValues_Main
{
	String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	
	String basic_pattern = "yyyyMMdd";
	String TWSE_outputTag = "TWSE";
	String TPEX_outputTag = "TPEX";
	String extension = ".txt";
	
	DateFormat df = new SimpleDateFormat(basic_pattern, Locale.getDefault());
	private String todayStr = "";

	// 
	Vector companyId = new Vector();
	
	GetValueandProcessingByStockId value;
	
	public GetValues_Main() throws Exception
	{
		// get Stock Value
		GetStockValue();
		// https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20191001&type=IND
		// 發行量加權股價指數
		
		// by stock id
//		GetStockValuebyId();
		
	}
	
	private void GetStockValue() throws Exception
	{
		String startDate = "20180101";
		GetValueandProcessing_StockValue stockvalue = new GetValueandProcessing_StockValue(startDate);
		
		
//		BufferedReader bfr;
//		String Line = "";
//		FileReader fr = new FileReader("/Users/phelps/Desktop/" + "20191002.txt");
//		bfr = new BufferedReader(fr);
//				
//		while((Line = bfr.readLine())!=null)
//		{			
//			JSONObject obj = new JSONObject(Line);
//			
//			// Value
//			if(obj.has("params")) {
//				JSONObject objPara = new JSONObject(obj.get("params").toString());
//				if(objPara.has("date")) {
//					System.out.println(objPara.get("date"));
//				}
//			}
//			
//			
//			if(obj.has("data3")) {
//				JSONArray jsonarray = new JSONArray(obj.get("data1").toString());
//				for(int i=0; i<jsonarray.length(); i++)
//				{
//					//System.out.println(jsonarray.get(i));
//					JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
//					if(arrayData.get(0).toString().equalsIgnoreCase("發行量加權股價指數")) {
//						System.out.println(arrayData.get(i));
//					}
//				}
//			}
//		}
//		fr.close();
//		bfr.close();
		
	}
	
	
	private void GetStockValuebyId() throws Exception
	{
		Today();
		
		// TWSE
		String twseStr = sourceFolder + TWSE_outputTag +"_" + todayStr + extension;
		ReadCompany(twseStr);
		for(int i=0; i<companyId.size(); i++)
		{
			GetValueandProcessingByStockId value = new GetValueandProcessingByStockId(companyId.get(i).toString(), "twse");
		}
		
		// TPEX
		companyId.clear();
		String tpexStr = sourceFolder + TPEX_outputTag +"_" + todayStr + extension;
		ReadCompany(tpexStr);
		for(int i=0; i<companyId.size(); i++)
		{
			GetValueandProcessingByStockId value = new GetValueandProcessingByStockId(companyId.get(i).toString(), "tpex");
		}
	}
	
	private void ReadCompany(String pathfile) throws Exception
	{
		File file = new File(pathfile);
		if(file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String Line;
			String temp[];
			while((Line = bfr.readLine())!=null)
			{
				temp = Line.split("\\t");
				//System.out.println(temp[0]+"	"+temp[1]);
				companyId.add(temp[0]);
			}
			
			bfr.close();
		}else {
			System.out.println(pathfile + " the file is not exist");
		}
		
	}
	
	private void Today()
	{
		Date today = Calendar.getInstance().getTime();
		todayStr = df.format(today.getTime());
	}
	
	public static void main(String args[])
	{
		try {
			GetValues_Main gv = new GetValues_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

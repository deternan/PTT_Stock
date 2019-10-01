package ValuesProcessing;

/*
 * download values (Main)
 * version: July 03, 2019 07:23 PM
 * Last revision: October 01, 2019 11:28 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

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
	
	GetValueandProcessing value;
	
	public GetValues_Main() throws Exception
	{
		// by stock id
		GetStockValue();
		
		// Stock Index
			//https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20191001&type=IND
			//發行量加權股價指數
			
		
	}
	
	private void GetStockValue() throws Exception
	{
		Today();
		
		// TWSE
		String twseStr = sourceFolder + TWSE_outputTag +"_" + todayStr + extension;
		ReadCompany(twseStr);
		for(int i=0; i<companyId.size(); i++)
		{
			GetValueandProcessing value = new GetValueandProcessing(companyId.get(i).toString(), "twse");
		}
		
		// TPEX
		companyId.clear();
		String tpexStr = sourceFolder + TPEX_outputTag +"_" + todayStr + extension;
		ReadCompany(tpexStr);
		for(int i=0; i<companyId.size(); i++)
		{
			GetValueandProcessing value = new GetValueandProcessing(companyId.get(i).toString(), "tpex");
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

package GUI.DataProcessing;

/*
 * download values (Main)
 * version: July 03, 2019 07:23 PM
 * Last revision: July 06, 2019 11:15 AM
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

import GUI.Units;

public class GatValues_Main
{
	DateFormat df = new SimpleDateFormat(Units.basic_pattern, Locale.getDefault());
	private String todayStr = "";

	// 
	Vector companyId = new Vector();
	
	GetValueandProcessing value;
	
	public GatValues_Main() throws Exception
	{
		Today();
		
		// TWSE
		String twseStr = Units.sourceFoder + Units.TWSE_outputTag +"_" + todayStr + Units.extension;
		ReadCompany(twseStr);
		for(int i=0; i<companyId.size(); i++)
		{
			// TWSE
			GetValueandProcessing value = new GetValueandProcessing(companyId.get(i).toString(), "twse");
		}
		// TPEX
		companyId.clear();
		String tpexStr = Units.sourceFoder + Units.TPEX_outputTag +"_" + todayStr + Units.extension;
		ReadCompany(tpexStr);
		for(int i=0; i<companyId.size(); i++)
		{
			//System.out.println(companyId.get(i));
			// TPEX
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
			GatValues_Main gv = new GatValues_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

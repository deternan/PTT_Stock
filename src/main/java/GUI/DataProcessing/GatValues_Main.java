package GUI.DataProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	public GatValues_Main() throws Exception
	{
		Today();
		
		// TWSE
		String twseStr = Units.sourceFoder + Units.TWSE_outputTag +"_" + todayStr + Units.extension;
		ReadCompany(twseStr);
		// TPEX
		String tpexStr = Units.sourceFoder + Units.TPEX_outputTag +"_" + todayStr + Units.extension;
		ReadCompany(tpexStr);
		
		for(int i=0; i<companyId.size(); i++)
		{
			GetValueandProcessing value = new GetValueandProcessing(companyId.get(i).toString());
		}
		
	}
	
	private void ReadCompany(String pathfile) throws Exception
	{
		File file = new File(pathfile);
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

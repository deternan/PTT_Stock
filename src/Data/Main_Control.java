package Data;
/*
 * PTT Stock - Main Control
 * 
 * version: April 29, 2018 02:23 AM
 * Last revision: May 21, 2018 00:54 AM
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Main_Control extends Parameters 
{
	
	//TWSE
	private Vector twse_id = new Vector();
	private Vector twse_name = new Vector();
	// TPEX
	private Vector tpex_id = new Vector();
	private Vector tpex_name = new Vector();
	// id & value
	private Vector id = new Vector();
	private Vector value = new Vector();
	
	//StandardData_Text SD;
	StandardData_Text_Testing SD_testing;
	
	public Main_Control() throws Exception
	{		
		Read_TWSE_Info();
		Read_TPEX_Info();
		
		Read_Current_value();
		
//		System.out.println(twse_id.size());
//		System.out.println(twse_name.size());		
//		System.out.println(tpex_id.size());
//		System.out.println(tpex_name.size());
//		System.out.println(id.size());
//		System.out.println(value.size());
		
		//SD = new StandardData_Text(twse_id, twse_name, tpex_id, tpex_name);
		SD_testing = new StandardData_Text_Testing(twse_id, twse_name, tpex_id, tpex_name, id, value);
		
		
		// ------------
//		Text_match tm = new Text_match(twse_id, twse_name, tpex_id, tpex_name); 
	}
	
	private void Read_TWSE_Info() throws Exception 
	{
		String Line = "";
		FileReader fr = new FileReader(path + twse);
		BufferedReader bfr = new BufferedReader(fr);
		
		String array_temp[];
		
		while ((Line = bfr.readLine()) != null) 
		{
			//System.out.println(Line);
			array_temp = Line.split("\t");
			//System.out.println(array_temp[0]+"	"+array_temp[1]);
			twse_id.add(array_temp[0]);
			twse_name.add(array_temp[1]);			
		}
		
		fr.close();
		bfr.close();
	}
	
	private void Read_TPEX_Info() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(path + tpex);
		BufferedReader bfr = new BufferedReader(fr);
		
		String array_temp[];
		
		while ((Line = bfr.readLine()) != null) 
		{
			//System.out.println(Line);
			array_temp = Line.split("\t");
			//System.out.println(array_temp[0]+"	"+array_temp[1]);
			tpex_id.add(array_temp[0]);
			tpex_name.add(array_temp[1]);			
		}
		
		fr.close();
		bfr.close();
	}
	
	private void Read_Current_value() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(value_path + value_filename);
		BufferedReader bfr = new BufferedReader(fr);
		
		String array_temp[];
		while ((Line = bfr.readLine()) != null)
		{
			array_temp = Line.split("\t");
			id.add(array_temp[0]);
			value.add(array_temp[2]);
		}
		
		fr.close();
		bfr.close();
	}
	
	public static void main(String[] args)
	{
		try {
			Main_Control main = new Main_Control();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

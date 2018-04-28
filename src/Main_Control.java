/*
 * PTT Stock - Main Control
 * 
 * version: April 29, 2018 02:23 AM
 * Last revision: April 29, 2018 02:23 AM
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Main_Control 
{
	// Read file
	private String path = "";	
	private String twse = "TWSE_2018.txt";
	private String tpex = "TPEX_2018.txt";
	
	//TWSE
	private Vector twse_id = new Vector();
	private Vector twse_name = new Vector();
	// TPEX
	private Vector tpex_id = new Vector();
	private Vector tpex_name = new Vector();
	
	public Main_Control() throws Exception
	{
		
		Read_TWSE_Info();
		Read_TPEX_Info();
		
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
			System.out.println(array_temp[0]+"	"+array_temp[1]);
			tpex_id.add(array_temp[0]);
			tpex_name.add(array_temp[1]);			
		}
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

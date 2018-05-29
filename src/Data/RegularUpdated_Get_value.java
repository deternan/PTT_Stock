package Data;

/*
 * 
 * Get current stock value 
 * Copyright (C) 2018 Phelps Ke, phelpske.dev at gmail dot com
 * 
 * divided information source
 * https://goodinfo.tw/StockInfo
 * 
 * Last revision: May 30, 2018 00:55 PM
 * 
 * JAR
 * jsoup-1.10.2.jar
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class RegularUpdated_Get_value 
{
	// Source
	private String path = "";	
	private String twse = "TWSE_2018.txt";
	private String tpex = "TPEX_2018.txt";	
	//TWSE
	private Vector twse_id = new Vector();	
	// TPEX
	private Vector tpex_id = new Vector();			
	
	// Parser
	private String stock_url = "https://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=";
	private String stock_path = "";		
	private double value;
	
	// Save	
	private String output_folder = "";	
	private String TWSE_output_filename;
	private String TPEX_output_filename;
	private Vector Save_TWSE_value = new Vector();
	private Vector Save_TPEX_value = new Vector();
	
	public RegularUpdated_Get_value() throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		TWSE_output_filename = "TWSE_"+dateFormat.format(date).toString()+".txt";
		TPEX_output_filename = "TPEX_"+dateFormat.format(date).toString()+".txt";
		
		// Get TWSE value
		Read_TWSE_Info();
		for(int i=0; i<twse_id.size(); i++)
		{
			stock_path = stock_url + twse_id.get(i).toString();
			value(Integer.parseInt(twse_id.get(i).toString()));
			//System.out.println(twse_id.get(i)+"	"+value);
			Save_TWSE_value.add(value);
		}
		Output_file(TWSE_output_filename, twse_id, Save_TWSE_value);
		
		// Get TPEX value
		Read_TPEX_Info();
		for(int i=0; i<tpex_id.size(); i++)
		{
			stock_path = stock_url + tpex_id.get(i).toString();
			value(Integer.parseInt(tpex_id.get(i).toString()));
			//System.out.println(twse_id.get(i)+"	"+value);
			Save_TPEX_value.add(value);
		}
		Output_file(TPEX_output_filename, tpex_id, Save_TPEX_value);
		
	}
	
	private void Read_TWSE_Info() throws Exception 
	{
		String Line = "";
		FileReader fr = new FileReader(path + twse);
		BufferedReader bfr = new BufferedReader(fr);
		
		String array_temp[];
		
		while ((Line = bfr.readLine()) != null) 
		{			
			array_temp = Line.split("\t");
			//System.out.println(array_temp[0]+"	"+array_temp[1]);
			twse_id.add(array_temp[0]);					
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
			array_temp = Line.split("\t");			
			tpex_id.add(array_temp[0]);					
		}
	}
	
	private void value(int code) throws Exception
	{
		Document doc = Jsoup.connect(stock_path).get();
		Elements tr = doc.select("td[style]");
		
		value = Double.parseDouble(tr.get(16).text());	
	}
	
	private void Output_file(String outputname, Vector id, Vector value) throws Exception
	{
		BufferedWriter writer;		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_folder + outputname), "utf-8"));
		for(int i=0; i<value.size(); i++)
		{
			writer.write(id.get(i)+"	"+value.get(i)+"\n");
		}		
		writer.close();
	}

	public static void main(String[] args) 
	{
		try {
			RegularUpdated_Get_value reg_update = new RegularUpdated_Get_value();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

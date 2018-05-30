package Data;

/*
 * 
 * Get TAIWAN stock list (TWSE)
 * Copyright (C) 2018 Phelps Ke, phelpske.dev at gmail dot com
 * 
 * Last revision: May 30, 2018 09:05 PM
 * 
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TWSE_info_collection 
{
	// Source
	private String stock_list = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	// Parameter
	private int year = 2018;
	// output
	private String output_folder = "C:\\Users\\Barry\\Desktop\\"; 
	private String output_file = "TWSE_" + String.valueOf(year) + "_file.txt";
	
	
	// output
	BufferedWriter writer;
	
	public TWSE_info_collection() throws Exception
	{
		Document doc = Jsoup.connect(stock_list).get();				
		Elements tr = doc.select("tr");
		Elements td;
		Elements list_name_ele;
		
		String code;
		String name;
		String temp[];
		
		// output parameter
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_folder + output_file), "utf-8"));
		
		for(int i=0; i<tr.size(); i++)
		{
			td = tr.get(i).select("td");			
			list_name_ele = td.get(0).select("td[bgcolor=#FAFAD2]");			
			
			// Parsing
			temp = list_name_ele.text().split("　");
			if(temp.length == 2) {
				code = temp[0];
				name = temp[1];
				
				// Filter
				if(code.length() == 4) {
					System.out.println(code+"	"+name);

					Output_text(Integer.parseInt(code), name);
				}
			}
		}
		
		writer.close();
	}
	
	private void Output_text(int code, String name) throws Exception
	{
		//writer.write("\""+code+"\t"+name+"\","+"\n");
		writer.write(code+"\t"+name+"\n");
	}
	
	public static void main(String args[])
	{
		try {
			TWSE_info_collection twse = new TWSE_info_collection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

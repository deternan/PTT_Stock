package ptt;

/*
 * 
 * Get TAIWAN stock list (TWSE)
 * Copyright (C) 2019 Phelps Ke, phelpske.dev at gmail dot com
 * 
 * Last revision: May 01, 2019 04:40 PM
 * 
 */

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class get_listedcompany_list {

	private String url = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	
	// output
	String output_folder = "C:\\Users\\barry.ke\\Desktop\\";
	String output_file = "TWSE.txt";
	
	
	public get_listedcompany_list() throws Exception
	{
		Document doc = Jsoup.connect(url).get();	
	
		Elements trs = doc.select("tr");
		
		//System.out.println(trs.size());
		//System.out.println(tr.size());
		
		/*
		 * <tr> <td bgcolor="#FAFAD2">034071 台苯國票88購01</td> <td
		 * bgcolor="#FAFAD2">TW19Z0340719</td> <td bgcolor="#FAFAD2">2019/01/04</td> <td
		 * bgcolor="#FAFAD2">上市</td> <td bgcolor="#FAFAD2"></td> <td
		 * bgcolor="#FAFAD2">RWSCCA</td> <td bgcolor="#FAFAD2"></td> </tr>
		 * 
		 */
		
		String temp[];
		for(int i=0; i<trs.size(); i++) {
			//System.out.println(trs.get(i));
			Elements tds = trs.get(i).select("td");
			if(tds.size() == 7) {
				System.out.println(tds.get(0).text());
				temp = tds.get(0).text().split("　");
				if(temp[0].length() == 4) {
					companyId.add(temp[0].trim());
					companyName.add(temp[1].trim());
					//System.out.println(temp[0]+"	"+temp[1]);
				}
			}						
		}
		
		// output
		
		output();
		
	}
	
	private void output()
	{
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_folder + output_file), "utf-8"));
			
			for(int i=0; i<companyId.size(); i++) {
				writer.write(companyId.get(i)+"	"+companyName.get(i)+"\n");
			}
			
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		try {
			get_listedcompany_list listed = new get_listedcompany_list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

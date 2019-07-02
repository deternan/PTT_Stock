package GUI.httpGet.company;

/*
 * 
 * Get TAIWAN stock list (TWSE)
 * Copyright (C) 2019 Chao-Hsuan Ke, phelpske.dev at gmail dot com
 * 
 * Last revision: June 30, 2019 12:20 PM
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

import GUI.Units;

public class get_Company_list {
	
	String todayStr;
	
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	
	public get_Company_list(String htmlStr, String todayStr, String tag) throws Exception
	{
		this.todayStr = todayStr;
		
		System.setProperty("file.encoding", "UTF-8");
		String UTF_8_str = new String(htmlStr.getBytes("UTF-8"), "UTF-8");
		
		Document doc = Jsoup.parse(UTF_8_str);
		Elements trs = doc.select("tr");
			
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
				//System.out.println(tds.get(0).text());
				temp = tds.get(0).text().split("　");
				//System.out.println(temp[0].trim());
				if(temp[0].trim().length() == 4) {
					companyId.add(temp[0].trim());
					companyName.add(temp[1].trim());
					//System.out.println(temp[0]+"	"+temp[1]);
				}
			}						
		}
		
		
		// output		
		if(tag.equals("twse")) {
			output(Units.TWSE_outputTag);
		}else if(tag.equals("tpex")) {
			output(Units.TPEX_outputTag);
		}
		
	}
	
	private void output(String tag)
	{
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Units.sourceFoder + tag +"_" + todayStr + Units.extension), "utf-8"));
			
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
	
}

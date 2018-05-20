/*
 * 
 * Get current stock value 
 * Copyright (C) 2018 Phelps Ke, phelpske.dev at gmail dot com
 * 
 * divided information source
 * https://goodinfo.tw/StockInfo
 * 
 * Last revision: May 21, 2018 00:45 AM
 * 
 * JAR
 * jsoup-1.10.2.jar
 * 
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Get_value 
{
	private String stock_url = "https://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=";
	private String stock_path = "";
	
	String year;
	double cash;
	double divided;
	double value;
	
	public Get_value(int code) throws Exception
	{
		stock_path = stock_url + String.valueOf(code);
		
		value(code);		
	}
	
	private void value(int code) throws Exception
	{
		Document doc = Jsoup.connect(stock_path).get();
		Elements tr = doc.select("td[style]");
		
		value = Double.parseDouble(tr.get(16).text());		
	}
	
	public double return_value()
	{
		return value;
	}

}

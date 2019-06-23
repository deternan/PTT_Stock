package GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;


public class GetValues 
{
	// source URL
		// http://www.twse.com.tw/exchangeReport/BFT41U?response=csv&date=20190621&selectType=ALL
	private String baseurl = "http://www.twse.com.tw/exchangeReport/BFT41U?response=csv&date=";
	private String endurl = "&selectType=ALL";
	
	// Date
	String yearStr = "2019";
	String monthStr = "06";
	String dayStr = "21";
	String dateStr = yearStr+monthStr+dayStr;
	
	private String completeURL = baseurl + dateStr + endurl;
	
	//
	private JSONObject get_json = null;
	private String jsonStrurl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190624&stockNo=2388";
	
	public GetValues()
	{
		ReadValueJson();
	}
	
	private void ReadValueJson()
	{
		InputStream is;
		String jsonText = "";
		
		try {
			is = new URL(jsonStrurl).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			//String jsonText = readAll(rd);
			StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    jsonText = sb.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		try {
			get_json = new JSONObject(jsonText);
			System.out.println(get_json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		GetValues gv = new GetValues();
	}
	
}

package GUI.httpGet.company;

/*
 * https GET company list
 * 
 * version: June 29, 2019 09:30 AM
 * Last revision: July 02, 2019 07:16 PM
 * 
 * Author : Chao-Hsuan Ke
 * Email: phelpske.dev at gmail dot com
 */


import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import GUI.httpGet.company.get_Company_list;
import GUI.Units;
import GUI.httpGet.HttpsReader;

public class GetCompany_Main {

	//private String basic_pattern = Units.basic_pattern;
	DateFormat df = new SimpleDateFormat(Units.basic_pattern, Locale.getDefault());
	private String todayStr = "";
	
	public GetCompany_Main() throws Exception
	{
		System.setProperty("file.encoding", "UTF-8");
		
		Today();
		
		// TWSE
		GerCompanyList(Units.twselisrUrl, "twse");
		System.out.println("TWSE finised");
		// TPEX
		GerCompanyList(Units.tpexlisrUrl, "tpex");
		System.out.println("TPEX finised");
    }
	
	private void GerCompanyList(String url, String tag) throws Exception
	{
		HttpsReader https = new HttpsReader();
		//https.charSet = "UTF-8";
		https.charSet = "BIG5";			// Taiwanese
        https.root = url;
        
        String cookie = "";				//cookie字串
        boolean isPost =  false;		//true表示用post送資料，false:get方式傳送

        BufferedReader buf = https.readyBuffer(cookie, isPost);
        
        String line = null;
        String allLine = "";
        while((line = buf.readLine()) != null){
        	allLine += line;
        }
        
        get_Company_list twse = new get_Company_list(allLine, todayStr, tag);        
	}
	
	private void Today()
	{
		Date today = Calendar.getInstance().getTime();
		todayStr = df.format(today.getTime());
	}
	
	public static void main(String[] args) {
		String defaultEncodingName = System.getProperty( "file.encoding" );
		System.setProperty("file.encoding", "UTF-8");
		
		try {
			GetCompany_Main hg = new GetCompany_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

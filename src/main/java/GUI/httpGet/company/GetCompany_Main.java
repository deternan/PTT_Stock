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
import GUI.httpGet.HttpsReader;
import GUI.httpGet.Units;

public class GetCompany_Main {

	private String basic_pattern = Units.basic_pattern;
	DateFormat df = new SimpleDateFormat(basic_pattern, Locale.getDefault());
	private String todayStr = "";
	
	public GetCompany_Main() throws Exception
	{
		System.setProperty("file.encoding", "UTF-8");
		
		Today();
		
		// TWSE
		GerCompanyList(Units.twselisrUrl, "twse");
		System.out.println("TWSE");
		// TPEX
		GerCompanyList(Units.tpexlisrUrl, "tpex");
		System.out.println("TPEX");
		
		
		// https
//		HttpsReader https = new HttpsReader();
//		https.charSet = "utf-8";
//        https.root = url;
//        
////        http.referer = "";//從哪一頁來
////        http.subUrl = "/ray00000test/blog/63561879";//網站domain後面的子網址
//        String cookie = "";				//cookie字串
//        boolean isPost =  false;		//true表示用post送資料，false:get方式傳送
//
//        /*
//        //若有需要用post方式傳送data，只要以key-value方式put即可，
//        //執行readyBuffer()會幫您送出，在url後面?是get方式，也可使用put方式，不需要串在網址後面很長
//        http.putQueryString("key1", "value1");
//        http.putQueryString("key2", "value2");
//        http.putQueryString("key3", "value3");
//         */
//
//        BufferedReader buf = https.readyBuffer(cookie, isPost);
//        
//        String line = null;
//        while((line = buf.readLine()) != null){//每次讀取一行
//            System.out.println(line);
//        }
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
		//System.out.println(todayStr);
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

package GUI.httpGet;

/*
 * https GET
 * 
 * version: June 29, 2019 09:30 AM
 * Last revision: June 30, 2019 12:23 PM
 * 
 * Author : Chao-Hsuan Ke
 * Email: phelpske.dev at gmail dot com
 */

/*
 * source from
 * https://blog.xuite.net/ray00000test/blog/63561879-http與https讀取網頁、下載檔案
 * 
 */

import java.io.BufferedReader;

import GUI.httpGet.company.get_TWSECompany_list;
import GUI.httpGet.Units;

public class HttpsGet {

	//private String twselisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	
	// value
	//private String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190628&stockNo=2388";
	
	private String url = Units.twselisrUrl;
	
	public HttpsGet() throws Exception
	{
		System.setProperty("file.encoding", "UTF-8");
		
		GerCompanyList(url);
		
		
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
	
	private void GerCompanyList(String url) throws Exception
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
        
        get_TWSECompany_list twse = new get_TWSECompany_list(allLine);
	}
	
	public static void main(String[] args) {
		String defaultEncodingName = System.getProperty( "file.encoding" );
		System.setProperty("file.encoding", "UTF-8");
		
		try {
			HttpsGet hg = new HttpsGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

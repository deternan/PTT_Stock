package GUI.httpGet;

/*
 * https get
 * 
 * version: June 29, 2019 09:30 AM
 * Last revision: June 29, 2019 10:03 AM
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

public class HttpsGet {

	public HttpsGet() throws Exception
	{
//		HttpReader http = new HttpReader();
//		http.charSet = "utf-8";//讀取的網頁格式
//      http.root = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190628&stockNo=2388";//domain
        
		HttpsReader https = new HttpsReader();
		https.charSet = "utf-8";//讀取的網頁格式
        https.root = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190628&stockNo=2388";//domain
        
//        http.referer = "";//從哪一頁來
//        http.subUrl = "/ray00000test/blog/63561879";//網站domain後面的子網址
        String cookie = "";//cookie字串
        boolean isPost =  false;//true表示用post送資料，false:get方式傳送

        /*
        //若有需要用post方式傳送data，只要以key-value方式put即可，
        //執行readyBuffer()會幫您送出，在url後面?是get方式，也可使用put方式，不需要串在網址後面很長
        http.putQueryString("key1", "value1");
        http.putQueryString("key2", "value2");
        http.putQueryString("key3", "value3");
         */

        //BufferedReader buf = http.readyBuffer(cookie, isPost);
        BufferedReader buf = https.readyBuffer(cookie, isPost);
        
        String line = null;
        while((line = buf.readLine()) != null){//每次讀取一行
            System.out.println(line);
        }
    }
	
	
	public static void main(String[] args) {
		try {
			HttpsGet hg = new HttpsGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

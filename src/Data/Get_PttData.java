package Data;

/*
 * PTT data Parser
 * 
 * https://github.com/jwlin/ptt-web-crawler
 * 
 * Copyright (C) 2018 Phelps Ke, phelpske.dev at gmail dot com
 * 
 * Last revision: April 22, 2018 11:19 AM
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.LogManager;

public class Get_PttData 
{
	// location
	String local = System.getProperty("user.dir");
	// parameters
	String PageName = "Stock";
	int start_page = 4201;
	int end_page = 4201;
	
	public Get_PttData() throws Exception
	{		
		System.out.println(local);
		
		// python crawler.py -b 看板名稱 -i 起始索引 結束索引
		for(int i=start_page; i<=end_page; i++)
		{
			Shell(i);
		}
		
	}
	
	private void Shell(int page) throws Exception
	{
		String all_parameters = " -b "+PageName +" -i "+String.valueOf(page)+" "+String.valueOf(page)+" &";		
		Process pl = Runtime.getRuntime().exec("python /home/rsa-key-20180304phelps.ke/ptt-web-crawler/PttWebCrawler/crawler.py"+ all_parameters);
		StreamGobbler errorGobbler = new StreamGobbler(pl.getErrorStream(), "Error");            
        StreamGobbler outputGobbler = new StreamGobbler(pl.getInputStream(), "Output");
        errorGobbler.start();
        outputGobbler.start();
		pl.waitFor();
        String line = "";
        BufferedReader p_in = new BufferedReader(new InputStreamReader(pl.getInputStream()));
        while((line = p_in.readLine()) != null){
            System.out.println(line);
        }
        p_in.close();
	}
	
	public static void main(String[] args) 
	{
		try {
			Get_PttData getptt = new Get_PttData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class StreamGobbler extends Thread {
	 InputStream is;

	 String type;

	 StreamGobbler(InputStream is, String type) {
	  this.is = is;
	  this.type = type;
	 }

	 public void run() {
		  try {
		   InputStreamReader isr = new InputStreamReader(is);
		   BufferedReader br = new BufferedReader(isr);
		   String line = null;
		   while ((line = br.readLine()) != null) {
		    if (type.equals("Error")) {
		    	//LogManager.logError(line);
		    }	
		    else {
		    	//LogManager.logDebug(line);
		    }		    	
		   }
		  } catch (IOException ioe) {
		   ioe.printStackTrace();
		  }
		 }
}
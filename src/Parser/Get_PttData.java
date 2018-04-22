package Parser;

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
import java.io.InputStreamReader;

public class Get_PttData 
{
	// location
	String local = System.getProperty("user.dir");
	// parameters
	String PageName = "Stock";
	int start_page = 3000;
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
		String[] args = new String[] { "python", "/crawler.py", "-b", PageName, "-i", String.valueOf(page), String.valueOf(page), "&" };
		Process pl = Runtime.getRuntime().exec(args);
		System.out.println(args);
		//Process pl = Runtime.getRuntime().exec("python " +local+"/crawler.py -b "+PageName +" -i "+page+" "+page +" &");
		//System.out.println("python crawler.py -b "+PageName +" -i "+page+" "+page +" &");
        String line = "";
        BufferedReader p_in = new BufferedReader(new InputStreamReader(pl.getInputStream()));
        while((line = p_in.readLine()) != null){
            System.out.println(page);
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

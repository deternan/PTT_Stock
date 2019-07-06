package GUI.Tagging;

import java.io.BufferedReader;

/*
 * Get values (Main)
 * version: July 06, 2019 15:03 PM
 * Last revision: July 06, 2019 09:02 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;

import GUI.Units;

public class Tagging_Main 
{
	String aa = "Stock_0002.json";
	String bb = "M.1441549887.A.0FC";
	
	FileOutputStream writer;
	PrintStream ps;
	
	// Parameters
		private String fileName_index;
		private String artileID_index;
	
	public Tagging_Main() throws Exception
	{
		// Read history
		ReadHistory();
		System.out.println(fileName_index+"	"+artileID_index);
		
		ReadAllArticles(fileName_index, artileID_index);
		
		// Save history
		//StoragedHistory(aa, bb);
	}
	
	// Raad History
	private void ReadHistory() throws Exception 
	{
		File file = new File(Units.historyFolder + Units.historyName);
		if(file.exists()) {
			fileName_index = "";
			artileID_index = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String Line;
			String temp[];
			while((Line = bfr.readLine())!=null)
			{
				temp = Line.split("\\t");
				//System.out.println(temp[0]+"	"+temp[1]);
				fileName_index = temp[0];
				artileID_index = temp[1];
			}
			
			bfr.close();
		}
	}
	
	private void ReadAllArticles(String fileName, String articleId)
	{
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		
		int listOfFilesSize = listOfFiles.length;
		int index = 1;
		String nextFile;
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        //System.out.println(file.getName());
		    	if(index != listOfFilesSize) {
		    		nextFile = listOfFiles[index].getName();
		    		if(file.getName().equalsIgnoreCase(fileName)) {
		    			articleParsing(fileName, nextFile);
		    			break;
		    		}
		    	}else {
		    		System.out.println("the final file");
		    	}
		    	index++;
		    }
		}
	}
	
	private void articleParsing(String fileName, String nextfileName)
	{
		//System.out.println(fileName+"	"+nextfileName);
		
	}
	
	private void StoragedHistory(String articleFileName, String articleId) throws Exception
	{
		writer = new FileOutputStream(Units.historyFolder + Units.historyName, true);
		
		Date date = new Date();
	    //System.out.println(date.toString());
	    
		ps = new PrintStream(writer); 
		ps.print(articleFileName+"	"+articleId+"	"+date.toString()+"\n");
		ps.close();
	}
	
	public static void main(String args[])
	{
		try {
			Tagging_Main tagging = new Tagging_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

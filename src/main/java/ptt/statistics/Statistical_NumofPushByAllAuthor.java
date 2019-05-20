package ptt.statistics;

/*
 * Authors Statistical
 * version: May 20, 2019 07:40 PM
 * Last revision: May 20, 2019 11:10 PM
 * 
 * Author : Chao-Hsuan Ke
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Statistical_NumofPushByAllAuthor 
{
	// Read source
	private String folder_source = "/Users/phelps/Documents/github/PTT_Stock/output/";
	private BufferedReader bfr;

	// Statistical
	private String outputAuthorList = "AuthorList";
	private String outputAuthorStatistical = "AuthorStatistical";
	// check latest file
	int checkNum;
	
	// Author List
	ArrayList<String> allAuthor_array = new ArrayList<String>();
	
	public Statistical_NumofPushByAllAuthor() throws Exception{
		
		AllAuthorList();
		
	}
	
	private void AllAuthorList() throws Exception {
		
		String finalFile;
		
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		checkNum = 0;
		String finalFileDate = "";
		
		// To get the latest file
		for (File file : listOfFiles) {

			//System.out.println(file.getName());
			if(file.getName().indexOf(outputAuthorList) == 0) {
				//System.out.println(file);
				finalFile = file.getName();
				// Check Latest file
				finalFileDate = LatestFileCheck(outputAuthorList, finalFile);
			}
		}
		
		finalFileDate = outputAuthorList + "_" + finalFileDate + ".txt";
		//System.out.println(finalFileDate);
		
		// Import
		ImportAuthorList(folder_source + finalFileDate);
		//System.out.println(allAuthor_array.size());
		// Read Push Number
		ReadPushRecord();
	}
	
	private void ImportAuthorList(String path) throws Exception {
		
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;
		
		while((Line = bfr.readLine())!=null)
		{								
			//System.out.println(Line);
			allAuthor_array.add(Line.trim());
		}
		fr.close();
		bfr.close();	
	}
	
	private void ReadPushRecord() {
		
	}
	
	private String LatestFileCheck(String TagStr, String fileName) {
		String fileNameDate = fileName.substring(TagStr.length() + 1, fileName.indexOf(".txt"));
		String finalfileNameDate = "";
		
		if(fileNameDate.length() > 0) {
			
			if(Integer.parseInt(fileNameDate) > checkNum) {
				finalfileNameDate = fileNameDate;
				checkNum = Integer.parseInt(finalfileNameDate);
			}
			
		}
		
		return finalfileNameDate;
	}
	
	public static void main(String args[])
	{
		try {
			Statistical_NumofPushByAllAuthor st_push = new Statistical_NumofPushByAllAuthor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

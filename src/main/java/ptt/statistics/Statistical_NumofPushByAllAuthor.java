package ptt.statistics;

/*
 * Authors Statistical
 * version: May 20, 2019 07:40 PM
 * Last revision: May 21, 2019 00:10 AM
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
	private String outputMessageStatistical = "MessageStatistical";
	// check latest file
	int checkNum;
	
	// Author List
	ArrayList<String> allAuthor_array = new ArrayList<String>();
	// Author pushed bumber
	ArrayList<Integer> allAuthorPushedNum_array = new ArrayList<Integer>();
	
	public Statistical_NumofPushByAllAuthor() throws Exception{
		
		AllAuthorList();
		// Read Push Number
		ReadPushRecord();
		
		//System.out.println(allAuthor_array.size()+"	"+allAuthorPushedNum_array.size());
		for(int i=0; i<allAuthorPushedNum_array.size(); i++) {
			System.out.println(allAuthor_array.get(i)+"	"+allAuthorPushedNum_array.get(i));
		}
	}
	
	private void AllAuthorList() throws Exception {
		
		String finalFile;
		
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		checkNum = 0;
		String finalFileName = "";
		
		// To get the latest file
		for (File file : listOfFiles) {

			//System.out.println(file.getName());
			if(file.getName().indexOf(outputAuthorList) == 0) {
				//System.out.println(file);
				finalFile = file.getName();
				// Check Latest file
				finalFileName = LatestFileCheck(outputAuthorList, finalFile);
			}
		}
		
		finalFileName = outputAuthorList + "_" + finalFileName + ".txt";
		//System.out.println(finalFileDate);
		
		// Import
		ImportAuthorList(folder_source + finalFileName);
		//System.out.println(allAuthor_array.size());
	}
	
	private void ImportAuthorList(String path) throws Exception {
		
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;
		
		while((Line = bfr.readLine())!=null)
		{								
			//System.out.println(Line);
			allAuthor_array.add(Line.trim());
			allAuthorPushedNum_array.add(0);
		}
		fr.close();
		bfr.close();	
	}
	
	private void ReadPushRecord() throws Exception {
		
		String finalFile;
		
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		checkNum = 0;
		String finalFileName = "";
		
		// To get the latest file
		for (File file : listOfFiles) {

			if(file.getName().indexOf(outputMessageStatistical) == 0) {
				finalFile = file.getName();
				// Check Latest file
				finalFileName = LatestFileCheck(outputMessageStatistical, finalFile);
			}
		}
		
		finalFileName = outputMessageStatistical + "_" + finalFileName + ".txt";
		//System.out.println(finalFileDate);
		
		ImportPushNumber(folder_source + finalFileName);
	}
	
	private void ImportPushNumber(String path) throws Exception {
		
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;
		String temp[];
		String authorTemp;
		int pushNumTemp;
		int tempNum;
		int Index = 0;
		while((Line = bfr.readLine())!=null)
		{								
			//System.out.println(Line);
			temp = Line.split("	");
			if(temp.length >=4) {
				authorTemp = temp[1];
				pushNumTemp = Integer.parseInt(temp[2]);
				
				if(pushNumTemp > 0) {
					//System.out.println(authorTemp+"	"+pushNumTemp);
					
					for(int i=0; i<allAuthor_array.size(); i++) {
						if(authorTemp.equalsIgnoreCase(allAuthor_array.get(i))) {
							tempNum = allAuthorPushedNum_array.get(i) + pushNumTemp;
							allAuthorPushedNum_array.set(i, tempNum);
							break;
						}
					}
				}
			}
			Index++;
		}
		fr.close();
		bfr.close();	
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

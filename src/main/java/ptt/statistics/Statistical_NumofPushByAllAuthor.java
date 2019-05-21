package ptt.statistics;

/*
 * Authors Statistical
 * version: May 20, 2019 07:40 PM
 * Last revision: May 21, 2019 11:04 PM
 * 
 * Author : Chao-Hsuan Ke
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Statistical_NumofPushByAllAuthor 
{
	// Read source
	private String folder_source = "/Users/phelps/Documents/github/PTT_Stock/output/";
	private BufferedReader bfr;
	private String folder_output = "/Users/phelps/Desktop/";
	private BufferedWriter writer;
	String finalFileName = "";
	// Statistical
	private String outputAuthorList = "AuthorList";
	private String outputMessageStatistical = "MessageStatistical";
	private String outputAuthorMessageStatistical = "AuthorPushNumber";
	// check latest file
	int checkNum;
	// Author List
	ArrayList<String> allAuthor_array = new ArrayList<String>();
	// Author pushed bumber
	ArrayList<Integer> allAuthorPushedNum_array = new ArrayList<Integer>();
	// output
	String outputBase = "";
	
	
	public Statistical_NumofPushByAllAuthor() throws Exception{
		
		AllAuthorList();
		// Read Push Number
		ReadPushRecord();
		// Sort
		BubbleSort();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder_output + outputAuthorMessageStatistical+"_"+outputBase+".txt"), "utf-8"));
		//System.out.println(allAuthor_array.size()+"	"+allAuthorPushedNum_array.size());
		for(int i=0; i<allAuthorPushedNum_array.size(); i++) {
			//System.out.println(allAuthor_array.get(i)+"	"+allAuthorPushedNum_array.get(i));
			writer.write(allAuthor_array.get(i)+"	"+allAuthorPushedNum_array.get(i)+"\n");
		}
		writer.close();
	}
	
	private void AllAuthorList() throws Exception {
		
		String finalFile;
		
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();		
		Arrays.sort(listOfFiles);
		
		checkNum = 0;
		finalFileName = "";
		
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
		finalFileName = "";
		
		// To get the latest file
		for (File file : listOfFiles) {
			
			if(file.getName().indexOf(outputMessageStatistical) == 0) {
				finalFile = file.getName();
				// Check Latest file
				finalFileName = LatestFileCheck(outputMessageStatistical, finalFile);
			}
		}
		
		outputBase = finalFileName;
		finalFileName = outputMessageStatistical + "_" + finalFileName + ".txt";
		
		//System.out.println(finalFileName);
		
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
	
	private void BubbleSort()
    {
    	int lenD = allAuthorPushedNum_array.size();
		int j = 0;
		int tmp = 0;
		String name = "";
		
		for(int i=0; i<lenD; i++)
		{
		    j = i;
		    for(int k=i; k<lenD; k++)
		    {
		      
		    	if(allAuthorPushedNum_array.get(j) < allAuthorPushedNum_array.get(k)){
		        j = k;		        
		      }
		    }
		    
		    tmp = allAuthorPushedNum_array.get(i);		   							 	name = allAuthor_array.get(i);
		    allAuthorPushedNum_array.set(i, allAuthorPushedNum_array.get(j));			allAuthor_array.set(i, allAuthor_array.get(j));
		    allAuthorPushedNum_array.set(j, tmp);										allAuthor_array.set(j, name);
		}
		
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

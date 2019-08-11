package GUI.Tagging;

/*
 * Get values (Main)
 * version: August 10, 2019 00:01 AM
 * Last revision: August 12, 2019 07:30 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;


public class TaggingMain_Sample {

	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String alllist = "articlelist.txt";
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	
	
	public TaggingMain_Sample() throws Exception
	{
		
		ReadAllArticles();
	}
	
	private void ReadAllArticles() throws Exception
	{
		File file = new File(sourceFolder + alllist);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		boolean startcheck = false;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				if(startcheck) {
					filenameVec.add(temp[0]);
					articleIdVec.add(temp[1]);
				}
				
			}
		}
		bfr.close();
	}
	
	public static void main(String args[])
	{
		TaggingMain_Sample sample = new TaggingMain_Sample();
		
	}
	
}

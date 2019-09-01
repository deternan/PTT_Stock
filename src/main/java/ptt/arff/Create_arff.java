package ptt.arff;

/*
 * create arff file
 * 
 * version: September 01, 2019 09:54 PM
 * Last revision: September 01, 2019 09:54 PM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;

public class Create_arff 
{
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String file = "tagging.txt";
	private BufferedReader bfr;
	
	public Create_arff() throws Exception
	{
		// Read tagging txt
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		bfr = new BufferedReader(fr);
				
		while((Line = bfr.readLine())!=null)
		{					
			System.out.println(Line);			
		}
		fr.close();
		bfr.close();
	}

	public static void main(String args[]) 
	{
		try {
			Create_arff ca = new Create_arff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

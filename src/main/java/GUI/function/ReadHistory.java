package GUI.function;

/*
 * Data tagging GUI
 * version: July 09, 2019 06:40 AM
 * Last revision: July 09, 2019 06:59 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadHistory {

	private String sourcefilepath;
	
	private String fileName_index = "";
	private String artileID_index = "";
	
	public ReadHistory(String sourcefilepath) throws Exception
	{
		this.sourcefilepath = sourcefilepath;
		
		File file = new File(this.sourcefilepath);
		if(file.exists()) {
			fileName_index = "";
			artileID_index = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String Line;
			String temp[];
			while((Line = bfr.readLine())!=null)
			{
				temp = Line.split("\\t");
				fileName_index = temp[0];
				artileID_index = temp[1];
			}
			
			bfr.close();
		}
	}
	
	public String returnfileName()
	{
		return fileName_index;
	}
	
	public String returnartileID()
	{
		return artileID_index;
	}
	
}

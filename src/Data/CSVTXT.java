package Data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 
 * version: September 22, 2017 01:07 PM
 * Last revision: September 22, 2017 01:07 PM
 * 
 * Author : Chao-Hsuan Ke
 * Institute: Delta Research Center
 * Company : Delta Electronics Inc. (Taiwan)
 * 
 */

public class CSVTXT 
{
	private String folder = "C:\\Users\\Barry\\Desktop\\";
	private String filename = "AAA.csv";
	private BufferedReader br = null;    
    String cvsSplitBy = ",";
	
	String ID;
    String Name;
	
    
	public CSVTXT()
	{
		String line = "";
		
		try {
//			br = new BufferedReader(new FileReader(folder + filename));			
//			while ((line = br.readLine()) != null) 
//	        {
//				// use comma as separator
//                String[] str_ = line.split(cvsSplitBy);
//                System.out.println(str_[0]+"	"+str_[1]);
//	        }
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(folder + filename), "UTF8"));
			while ((line = in.readLine()) != null) 
			{
				String[] str_ = line.split(cvsSplitBy);
				//System.out.println(str_[0]+"	"+str_[1]);
				
				ID = ID_Filter(str_[0]);
				System.out.println(ID);
			}			        
	        in.close();
		     
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	private String ID_Filter(String input)
	{
		String temp = "";
		temp = input.substring(1, input.length());
		
		return temp;
	}
	
	public static void main(String args[])
	{
		CSVTXT r_csv = new CSVTXT();
	}
	
}

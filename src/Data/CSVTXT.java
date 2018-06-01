package Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private String folder = "D:\\Phelps\\GitHub\\PTT_Stock\\";
	private String filename = "20180530_UTF8.csv";	    
    String cvsSplitBy = ",";
	
    // Parameters
	String ID;
    String Name;
	double value;
    
    // Regular expression
    String regex = "[0-9]+";
    
    // output
    private String output_folder = folder;
	private String output_file = "";
    
	public CSVTXT() throws Exception
	{
		// date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		output_file = dateFormat.format(date).toString()+"_values.txt";
		// output
		BufferedWriter writer;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_folder + output_file), "utf-8"));		
		
		String line = "";		
	
			
			int count = 0;
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(folder + filename), "UTF8"));
			while ((line = in.readLine()) != null) 
			{
				String[] str_ = line.split(cvsSplitBy);
				//System.out.println(str_[0]+"	"+str_[1]);
				
				ID = ID_Filter(str_[0]);
				Name = str_[1].replace("　", "");
				
				if((Number_check(ID) == true) && (ID.length() == 4)){
					value = Double.parseDouble(str_[5]);
					System.out.println(count+++"	"+ID+"	"+Name.trim()+"	"+value);
					writer.write(ID+"	"+Name.trim()+"	"+value+"\n");
				}
				
			}			        
	        in.close();
        
		writer.close();
	}
	
	private String ID_Filter(String input)
	{
		String temp = "";
		temp = input.substring(1, input.length());
		
		return temp;
	}
	
	private boolean Number_check(String input)
	{
		boolean num_check = false;
		//System.out.println(input.matches(regex));
		num_check = input.matches(regex);
		
		return num_check;
	}
	
	
	public static void main(String args[])
	{
		try {
			CSVTXT r_csv = new CSVTXT();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

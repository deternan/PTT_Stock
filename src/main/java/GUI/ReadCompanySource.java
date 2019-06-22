package GUI;

/*
 * Sotck GUI
 * a. Read Company Information
 * 
 * version: June 19, 2019 10:29 PM
 * Last revision: June 22, 2019 09:26 PM
 * 
 * Author : Chao-Hsuan Ke
 * 
 */

import java.io.BufferedReader;


import java.io.FileReader;
import java.util.Vector;

public class ReadCompanySource 
{
	private String tpexlist = "/Users/phelps/Documents/github/PTT_Stock/source/TPEX_2019.txt";
	private String twselist = "/Users/phelps/Documents/github/PTT_Stock/source/TWSE_2019.txt";
	
	private BufferedReader bfr;	
	
	// Data
	Vector tpexVec_id = new Vector();
	Vector tpexVec_name = new Vector();
	Vector twseVec_id = new Vector();
	Vector twseVec_name = new Vector();
	
	
	public ReadCompanySource() throws Exception
	{
		// Read tpex
		ReadCompanylist(tpexlist, tpexVec_id, tpexVec_name);
		// Read TWSE
		ReadCompanylist(twselist, twseVec_id, twseVec_name);
		
		//System.out.println(tpexVec_id.size()+"	"+tpexVec_name.size()+"	"+twseVec_id.size()+"	"+twseVec_name.size());
	}
	
	private void ReadCompanylist(String path, Vector idVec, Vector nameVec) throws Exception {
		String Line = "";
		Vector tmpVec;
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
				
		String tmpStr[];
		while((Line = bfr.readLine())!=null)
		{					
			//System.out.println(Line);	
			tmpStr = Line.split("\\t");
			idVec.add(tmpStr[0]);
			nameVec.add(tmpStr[1]);
		}
		fr.close();
		bfr.close();
	}
	
	public static void main(String args[])
	{
		try {
			ReadCompanySource readSource = new ReadCompanySource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

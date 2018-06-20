package Lottery;

/*
 * PTT Json parser & Lottery 
 * 
 * version: June 20, 2018 00:40 AM
 * Last revision: June 21, 2018 00:54 AM
 * 
 * Author: Chao-Hsuan Ke
 * E-mail: phelpske dot dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class Sticker_Lottery 
{
	// Get source
	private String read_file_path = "";
	private String read_file_name = "M.1529344339.A.0DB.json";
	private BufferedReader bfr;
	// output
	private String outputAll_folder = read_file_path;
	private String outputAll_file = "allpush.txt";	
	private BufferedWriter writer;
	
	private Vector All_date = new Vector();
	private Vector All_userid = new Vector();
	private Vector All_content = new Vector();
	
	// Lottery
	private int L_Number = 20;
	private int min = 1;
	private int max;
	private int randomNum;
		// Random
		int seed = 0616;	// lopm27
	
	public Sticker_Lottery() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + read_file_name);
		BufferedReader bfr = new BufferedReader(fr);
		
		JSONObject obj;
		while ((Line = bfr.readLine()) != null) {			
			obj = new JSONObject(Line);						
			ArrayParser(obj.get("messages").toString());
		}
		
		// Lottery
		Lottery(L_Number);
		
		fr.close();
		bfr.close();		
	}
	
	private void ArrayParser(String input) throws Exception
	{
		JSONArray jsonarray = new JSONArray(input);		
		JSONObject pusgobj;
		for(int i=0; i<jsonarray.length(); i++)
		{			
			pusgobj = new JSONObject(jsonarray.get(i).toString());
			//System.out.println(i+"	"+pusgobj.get("push_ipdatetime")+"	"+pusgobj.get("push_userid")+"	"+pusgobj.get("push_content"));
			All_date.add(pusgobj.get("push_ipdatetime"));
			All_userid.add(pusgobj.get("push_userid"));
			All_content.add(pusgobj.get("push_content"));						
		}
		
		// output
		output_all();
	}
	
	private void Lottery(int L_Num)
	{
		max = All_userid.size();
		Random rand = new Random(seed);
		
		for(int i=0; i<L_Num; i++)
		{
			randomNum = min + rand.nextInt(max);
			//System.out.println((i+1)+"	"+randomNum);
			System.out.println((i+1)+"	"+randomNum+"	"+All_date.get(randomNum)+"	"+All_userid.get(randomNum)+"	"+All_content.get(randomNum));
		}	
	}
	
	private void output_all() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputAll_folder + outputAll_file), "utf-8"));
		
		for(int i=0; i<All_userid.size(); i++)
		{
			writer.write((i+1)+"	"+All_date.get(i)+"	"+All_userid.get(i)+"	"+All_content.get(i)+"\n");
		}
		
		writer.close();
	}
	
	public static void main(String args[])
	{
		try {
			Sticker_Lottery SL = new Sticker_Lottery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

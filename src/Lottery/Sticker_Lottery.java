package Lottery;

/*
 * PTT Json parser & Lottery 
 * 
 * version: June 20, 2018 00:40 AM
 * Last revision: June 20, 2018 01:15 AM
 * 
 * Author: Chao-Hsuan Ke
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class Sticker_Lottery 
{
	private String read_file_path = "C:\\Users\\Barry\\Desktop\\";
	private String read_file_name = "M.1529344339.A.0DB.json";
	private BufferedReader bfr;

	private Vector All_date = new Vector();
	private Vector All_userid = new Vector();
	private Vector All_content = new Vector();
	
	// Lottery
	private int L_Number = 20;
	private int min = 0;
	private int max;
	private int randomNum;
		// Random
		int seed = 1128;
	
	public Sticker_Lottery() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(read_file_path + read_file_name);
		BufferedReader bfr = new BufferedReader(fr);
		
		JSONObject obj;
		while ((Line = bfr.readLine()) != null) {
			
			obj = new JSONObject(Line);			
			//System.out.println(obj.get("messages"));
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
			//System.out.println(i+"	"+pusgobj.get("push_ipdatetime")+"	"+pusgobj.get("push_userid"));
			All_date.add(pusgobj.get("push_ipdatetime"));
			All_userid.add(pusgobj.get("push_userid"));
			All_content.add(pusgobj.get("push_content"));
		}
		
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

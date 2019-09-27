package FeatureSelection;

/*
 * generate arff based on selected attributes
 * 
 * version: September 25, 2019 10:00 PM
 * Last revision: September 25, 2019 11:45 PM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Vector;

public class Arff_Translation 
{
	
	// attribute record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String fsFolder = "feature selection/";
	private String attribute_file = "InfoGainAttributeEval_tfidf.txt";
	private String arff_file = "tagging_tfidf.arff";
	
	
	// selected attribute num;
	Vector attributeVec = new Vector();	
	ArrayList<Integer> attributeList = new ArrayList<Integer>();
	ArrayList<Integer> attributeListSort = new ArrayList<Integer>();
	// old arff
	Vector arffvalue = new Vector();
	// new arff
	Vector arffvalueNew = new Vector();
	// output
		private BufferedWriter writer;
		private String arfffolder = sourceFolder;
		private String arfffilename = "tagging_tfidf_fs_ig.arff";
		private String allweValueStr = "";
	
	
	public Arff_Translation() throws Exception
	{
		read_attributeTag();
				
		attributeListSort = BubbleSort(attributeList);
		
		read_Arff();
		//System.out.println(arffvalue.size()+"	"+attributeListSort.size());
		generate_Arff();
	}
	
	private void read_attributeTag() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + fsFolder + attribute_file);
		BufferedReader bfr = new BufferedReader(fr);
		
		String tempStr;
		double scoreDouble;
		int lastIndex;
		String featureStr;
		int attributeInt;
		
		while ((Line = bfr.readLine()) != null) 
		{
			//temp = Line.split(" ");
			Line = Line.trim();
			//System.out.println(Line);
			tempStr = Line.substring(0, Line.indexOf(" "));
			scoreDouble = Double.valueOf(tempStr);
			
			if(scoreDouble > 0) {
				lastIndex = Line.lastIndexOf(" ");
				featureStr = Line.substring(lastIndex+1, Line.indexOf("_"));
				attributeInt = Integer.valueOf(featureStr);
				attributeVec.add(attributeInt - 1);
				attributeList.add(attributeInt - 1);
				//System.out.println(tempStr.trim()+"	"+scoreDouble+"	"+featureStr);
			}
			
		}
		
		fr.close();
		bfr.close();
	}
	
	private void read_Arff() throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + arff_file);
		BufferedReader bfr = new BufferedReader(fr);
		
		while ((Line = bfr.readLine()) != null) 
		{
			if(Line.contains("@") == false) {
				arffvalue.add(Line);
			}
		}
		
		fr.close();
		bfr.close();
	}
	
	private void generate_Arff() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
	
		// @
		writer.write("@RELATION arff_FeatureSelection"+"\n");
		for(int i=0; i<attributeListSort.size(); i++) {
			writer.write("@ATTRIBUTE "+ attributeListSort.get(i) +"_feature NUMERIC"+"\n");
		}
		
		writer.write("@ATTRIBUTE class	{positive,negative}"+"\n");
		writer.write("@DATA"+"\n");
		
		// 
		String temp[];
		String newLine = "";
		for(int i=0; i<arffvalue.size(); i++)
		{
			temp = arffvalue.get(i).toString().split(",");
			newLine = "";
			for(int j=0; j<attributeListSort.size(); j++)
			{
				newLine += temp[attributeListSort.get(j)]+",";
			}
			newLine += temp[temp.length-1];
			arffvalueNew.add(newLine);
			writer.write(newLine+"\n");
		}
		
		writer.close();
	}
	
	private ArrayList<Integer> BubbleSort(ArrayList<Integer> input)
    {
    	int lenD = input.size();
		int j = 0;
		int tmp = 0;
		
		for(int i=0; i<lenD; i++)
		{
		    j = i;
		    for(int k=i; k<lenD; k++)
		    {
		      //if(input[j] < input[k]){
		    	if(input.get(j) > input.get(k)){
		        j = k;		        
		      }
		    }
		    
		    tmp = input.get(i);		    		  
		    input.set(i, input.get(j));
		    input.set(j, tmp);
		}
		
		return input;
    }
	
	public static void main(String[] args) 
	{
		try {
			Arff_Translation at = new Arff_Translation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package ptt.other;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class create_tfidf_arff {
	
	// output
	private BufferedWriter writer;
	private String arfffolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String arfffilename = "tagging_tfidf.arff";
	private String allweValueStr = "";

	private int wordim = 3198;
	
	public create_tfidf_arff() throws Exception
	{
		output_Initialize();
		
		writer.close();
	}
	
	private void output_Initialize() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
		
		writer.write("@RELATION ptttagging_tfidf"+"\n");
		for(int i=0; i<wordim; i++) {
			writer.write("@ATTRIBUTE "+ String.valueOf(i+1)  +"_feature NUMERIC"+"\n");
		}
		
		writer.write("@ATTRIBUTE class	{positive,negative}"+"\n");
		writer.write("@DATA"+"\n");
	}
	
	public static void main(String args[]) {
		try {
			create_tfidf_arff create = new create_tfidf_arff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

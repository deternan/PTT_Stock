package ptt.tf.idf;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Vector;

public class TfIdfMain extends Parameters
{
	// All video id 
	private Vector all_video;
	// TF-IDF
	private static ArrayList<double[]> tfidf_list = new ArrayList<double[]>();
	private static ArrayList<String[]> allterms = new ArrayList<String[]>();
	private static ArrayList<String[]> termsDocs_terms = new ArrayList<String[]>();
	// Output
	private Vector tagCategoryVec;
	
	// output
	private String arfffolder = output_folder;
	private String arfffilename = "tagging_tfidf.txt";
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
		
	
	public TfIdfMain() throws Exception
	{
		 
		DocumentParser_Chinese dp_ch = new DocumentParser_Chinese();
		 dp_ch.tfIdfCalculator();
		 
		 // tfidf list
		 tfidf_list = (ArrayList<double[]>) dp_ch.Return_tfidfDocsVector();		
		 tagCategoryVec = dp_ch.Return_tagCategoryVec();
		 
		 String dimStr = "";
		 for(int i=0; i<tfidf_list.size(); i++)
		 {
			 dimStr = "";
			System.out.println(i+"	"+tfidf_list.get(i).length+"	"+tagCategoryVec.get(i));
			for(int j=0; j<tfidf_list.get(i).length; j++)
			{
				dimStr += tfidf_list.get(i)[j]+",";
				//System.out.print(tfidf_list.get(i)[j]+",");
			}
			dimStr += tagCategoryVec.get(i).toString();
			writer.write(dimStr + "\n");
		 }
		 	 
		 writer.close();
	}
	
	 public static void main(String args[]) throws Exception
	 {	
		 TfIdfMain tfidf = new TfIdfMain(); 
	 }
	 
}
package ptt.arff;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/*
 * create arff file based on fasttxt
 * tagging to arff 
 * 
 * version: September 23, 2019 00:02 AM
 * Last revision: September 23, 2019 00:12 AM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import com.spreada.utils.chinese.ZHConverter;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class TaggingData_to_Arff_Word2Vec 
{
	
	private String modelPath = "";						// path
	private String modelgzName = "GoogleNews-vectors-negative300.bin.gz";		// Google News
	private String modeltxtName = "zh_wiki_word2vec_300.txt";					// Wiki
	
	// tagging record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String file = "tagging.txt";
	// Articles
	private String articleFolder = "/data/git/DataSet/ptt/Stock data/";
	// article content
	private String articleFile;
	private String articleId;
	private String author;
	private String title;
	private String content;
	private String date;
	private String allTitleContent = "";
	// file index
	String fileNameStr;
	String articleIdStr;
	String tagCategoryStr;
	// BIG5 to GB
	ZHConverter simconverter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	// Segmentation
	private static final String basedir = "/Users/phelps/Documents/github/Light-tools/data/stanford-word-segmenter/data/"; // data
																															// path

	static List<String> segmented;
	CRFClassifier<CoreLabel> segmenter;
	// Segmented Terms
	Vector segTerms = new Vector();
	
	
	File gModel;
	WordVectors word2Vec;
	//Word2Vec
	
//	String inputStr = "西元前";
	String inputStr = "apple";
	double[] wordVectorDouble = {};	
	
	private int wordim = 300;
	private ArrayList averageValue = new ArrayList();
	double[] averageValueTmp = new double[wordim];
	// output
	private BufferedWriter writer;
	private String arfffolder = sourceFolder;
	private String arfffilename = "tagging_word2vec.arff";
	private String allweValueStr = "";
	

	public TaggingData_to_Arff_Word2Vec() throws Exception
	{
		//ReadBin();
		ReadTXT();
		
		// Chinese segmentation initialize
		Chinese_Seg_Initialize();
		
		// Read tagging txt
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		BufferedReader bfr = new BufferedReader(fr);
		// output arff
		output_Initialize();

		// int index = 0;
		String temp[];
		while ((Line = bfr.readLine()) != null) {

		}
		
		fr.close();
		bfr.close();
		
		writer.close();
	}
	
	private void ReadBin() {
		gModel = new File(modelPath + modelgzName);
	    word2Vec = WordVectorSerializer.readWord2VecModel(gModel);
	}
	
	private void ReadTXT() {
		gModel = new File(modelPath + modeltxtName);
		try {
			word2Vec = WordVectorSerializer.loadTxtVectors(gModel);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void Chinese_Seg_Initialize() throws Exception{
		
		System.setOut(new PrintStream(System.out, true, "utf-8"));

	    Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", basedir);
	    // props.setProperty("NormalizationTable", "data/norm.simp.utf8");
	    // props.setProperty("normTableEncoding", "UTF-8");
	    
	    props.setProperty("serDictionary", basedir + "dict-chris6.ser.gz");
	    props.setProperty("inputEncoding", "UTF-8");
	    props.setProperty("sighanPostProcessing", "true");

	    segmenter = new CRFClassifier<>(props);
	    segmenter.loadClassifierNoExceptions(basedir + "ctb.gz", props);
	}
	
	private void output_Initialize() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
		
		writer.write("@RELATION ptttagging"+"\n");
		writer.write("\n");
		for(int i=0; i<wordim; i++) {
			writer.write("@ATTRIBUTE "+ String.valueOf(i+1)  +"_feature NUMERIC"+"\n");
		}
		
		writer.write("@ATTRIBUTE class	{positive,negative}"+"\n");
		writer.write("@DATA"+"\n");
	}
	
	public static void main(String[] args) 
	{
		try {
			TaggingData_to_Arff_Word2Vec w2v = new TaggingData_to_Arff_Word2Vec();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

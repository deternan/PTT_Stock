package ptt.arff;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/*
 * create arff file based on fasttxt
 * tagging to arff 
 * 
 * version: September 23, 2019 00:02 AM
 * Last revision: September 23, 2019 08:40 PM
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
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.spreada.utils.chinese.ZHConverter;

import GUI.Units;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class TaggingData_to_Arff_Word2Vec 
{
	
	private String modelPath = "/Users/phelps/Downloads/";		// model folder
//	private String modelPath_Type = "wiki/cc.zh.Chinese.model/";
	private String modelgzName = "GoogleNews-vectors-negative300.bin.gz";				// Google News
	private String modeltxtName = "zh_wiki_word2vec_300.txt";							// Wiki
	
	// tagging record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	private String file = "tagging.txt";
	// Articles
	private String articleFolder = "/data/git/DataSet/ptt/Stock data/";
	// article content
	private String title;
	private String content;
	private String allTitleContent = "";
	// file index
	String fileNameStr;
	String articleIdStr;
	String tagCategoryStr;
	// BIG5 to GB
	ZHConverter simconverter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	// Segmentation
	private static final String basedir = "/Users/phelps/Documents/github/Light-tools/data/stanford-word-segmenter/data/"; // data
																															

	static List<String> segmented;
	CRFClassifier<CoreLabel> segmenter;
	// Segmented Terms
	Vector segTerms = new Vector();
	
	
	File gModel;
	WordVectors word2Vec;
	//Word2Vec
	double[] wordVectorDouble = {};	
	
	private int wordim = 300;
	private ArrayList averageValue = new ArrayList();
	double[] averageValueTmp = new double[wordim];
	double wordCount = 0;
	// output
	private BufferedWriter writer;
	private String arfffolder = sourceFolder;
	private String arfffilename = "tagging_word2vec_simple.arff";
	private String allweValueStr = "";
	

	public TaggingData_to_Arff_Word2Vec() throws Exception
	{
		//ReadBin();	// English
		ReadTXT();		// Chinese		
		
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
		while ((Line = bfr.readLine()) != null) 
		{
			temp = Line.split("	");
			fileNameStr = temp[0];
			articleIdStr = temp[1];
			tagCategoryStr = temp[3];
			System.out.println(fileNameStr+"	"+articleIdStr);
			// content
			ReadSourceFile(temp[0], temp[1]);
			
			allTitleContent += title + " " + content;

			String tmpStr = ChineseWordParser(allTitleContent);
			// BIG5 to GB
			String simStr = BIG5GB_converter(tmpStr);
			// Chinese words Segmentation
			// System.out.println(simStr);
			Chinese_Segmentation(simStr);
			
			//System.out.println(segTerms.size());
			for (int k = 0; k < segTerms.size(); k++) {
				word2vec_wordembedding(segTerms.get(k).toString());
			}
			// average
			average();
			// output
			if ((tagCategoryStr.trim().equalsIgnoreCase("positive"))
					|| (tagCategoryStr.trim().equalsIgnoreCase("negative"))) {
				writer.write(allweValueStr + "\n");
			}
			
			//System.out.println(fileNameStr+" "+articleIdStr+" "+tagCategoryStr);
			
			Clean();
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
	
	private void word2vec_wordembedding(String inputStr)
	{
		// list word dim value
		if (word2Vec.hasWord(inputStr)) {
			wordVectorDouble = word2Vec.getWordVector(inputStr);
			// System.out.println("dim length "+wordVectorDouble.length);
			for (int i = 0; i<wordVectorDouble.length; i++) {
				//System.out.println(wordVectorDouble[i]);
				averageValueTmp[i] += wordVectorDouble[i];
			}
			wordCount++;
		} else {
			//System.out.println("no term in the model");
		}
	}
	
	private void average()
	{		
//		System.out.println("----------- average -----------");
		for(int j=0; j<wordim; j++) {
			//averageValue.add(averageValueTmp[j]/segTerms.size());
			averageValue.add(averageValueTmp[j] / wordCount);
			allweValueStr += averageValue.get(j)+",";
		}
		allweValueStr += tagCategoryStr;
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
	
	private void ReadSourceFile(String filenameIndex, String articleIdIndex) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) 
		{
			strTmp += Line;
		}
		fr.close();
		bfr.close();

		String idTmp;
		if (isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if (obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if (articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");

						if (idTmp.equalsIgnoreCase(articleIdIndex)) {
							
							// title
							if (articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}
							// content
							if (articleobj.has("content")) {
								content = articleobj.getString("content");
							}
							
							break;
						}
					}
				}
			}
		}
	}
	
	private boolean isJSONValid(String jsonInString) {

		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}
	
	private String ChineseWordParser(String input_str) 
	{
		String sentence = "";
		for(int i=0; i<input_str.length();i++)
		{  
		    String test = input_str.substring(i, i+1);  
		    if(test.matches("[\\u4E00-\\u9FA5]+")){  
		    	sentence += test;
		    }  
		}
		
		return sentence;
	}
	
	private String BIG5GB_converter(String traStr)
	{
		// BIG5 to GB
		String simStrResult = simconverter.convert(traStr);
		
		return simStrResult;
	}
	
	private void Chinese_Segmentation(String inputStr)
	{
	    segmented = segmenter.segmentString(inputStr);
	    
	    Terms_Split(segmented);
	}
	
	private void Terms_Split(List<String> listStr)
	{
		for(int i=0; i<listStr.size(); i++) {
			// Filter (length>1)
			if(listStr.get(i).toString().trim().length() > 1) {
				segTerms.add(listStr.get(i));
			}			
		}		
	}
	
	private void Clean()
	{
		title = "";
		content = "";
		allTitleContent = "";
		fileNameStr = "";
		articleIdStr = "";
		tagCategoryStr = "";
		segTerms.clear();
		averageValue.clear();
		allweValueStr = "";
		averageValueTmp.clone();
		wordCount = 0;
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

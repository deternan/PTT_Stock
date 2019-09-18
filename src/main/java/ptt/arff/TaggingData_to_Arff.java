package ptt.arff;

/*
 * create arff file
 * tagging to arff
 * 
 * version: September 01, 2019 09:54 PM
 * Last revision: September 18, 2019 10:32 PM
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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.spreada.utils.chinese.ZHConverter;

import GUI.Units;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import com.mayabot.mynlp.fasttext.FastText;

public class TaggingData_to_Arff 
{
	// tagging record
	private String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	//private String file = "tagging_tmp.txt";
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
	private static final String basedir = "/Users/phelps/Documents/github/Light-tools/data/stanford-word-segmenter/data/";	// data path
	
	static List<String> segmented;
	CRFClassifier<CoreLabel> segmenter;
	// Segmented Terms
	Vector segTerms = new Vector();
	
	// fasttext (word embedding)
	private int wordim = 300;
	private String sourcebinPath = "/Users/phelps/Downloads/wiki/";		// should be revised
	private String modelFolder_zh = "wiki.simple.zh.Chinese.model";
	FastText fastText_zh;
	private ArrayList averageValue = new ArrayList();
	double[] averageValueTmp = new double[wordim];
	// output
	private BufferedWriter writer;
	private String arfffolder = sourceFolder;
	private String arfffilename = "tagging.arff";
	private String allweValueStr = "";
	
	public TaggingData_to_Arff() throws Exception
	{
		// Chinese segmentation initialize
		Chinese_Seg_Initialize();
		fastText_zh = FastText.loadModel(sourcebinPath + modelFolder_zh, true);
		
		// Read tagging txt
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		BufferedReader bfr = new BufferedReader(fr);
		// output arff
		output_Initialize();
		
		//int index = 0;
		String temp[];
		while((Line = bfr.readLine())!=null)
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

			for (int k = 0; k < segTerms.size(); k++) {
				fasttext(segTerms.get(k).toString());
			}
			// average
			average();
			// output
			if ((tagCategoryStr.trim().equalsIgnoreCase("positive"))
					|| (tagCategoryStr.trim().equalsIgnoreCase("negative"))) {
				writer.write(allweValueStr + "\n");
			}
			// System.out.println(fileNameStr+" "+articleIdStr+" "+tagCategoryStr);
			
			
			Clean();
		}
		
		fr.close();
		bfr.close();
		
		writer.close();
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
						articleId = idTmp;
						if (idTmp.equalsIgnoreCase(articleIdIndex)) {
							// author
							if (articleobj.has("author")) {
								if(articleobj.get("author") instanceof String) {
									author = articleobj.getString("author");
								}
							}
							// title
							if (articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}
							// content
							if (articleobj.has("content")) {
								content = articleobj.getString("content");
							}
							// date
							if (articleobj.has("date")) {
								date = articleobj.getString("date");
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
		        //System.out.printf("\t[Info] %s -> 中文!\n", test);
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
	
	private void Clean()
	{
		articleFile = "";
		articleId = "";
		author = "";
		title = "";
		content = "";
		date = "";
		allTitleContent = "";
		fileNameStr = "";
		articleIdStr = "";
		tagCategoryStr = "";
		segTerms.clear();
		averageValue.clear();
		allweValueStr = "";
	}
	
	private void Chinese_Seg_Initialize() throws Exception
	{
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
	
	private void fasttext(String inputStr)
	{
		//for(int i=0; i<segTerms.size(); i++)
		{
			com.mayabot.blas.Vector vecTmpzh = fastText_zh.getWordVector(inputStr);
//			System.out.println(inputStr+"	"+vecTmpzh);
			for(int j=0; j<wordim; j++) {
				averageValueTmp[j] += vecTmpzh.get(j);
			}
		}
	}
	
	private void average()
	{		
//		System.out.println("----------- average -----------");
		for(int j=0; j<wordim; j++) {
			averageValue.add(averageValueTmp[j]/segTerms.size());
			//System.out.println(j+"	"+averageValueTmp[j]+"	"+averageValue.get(j));
			//System.out.print(averageValue.get(j)+",");
			allweValueStr += averageValue.get(j)+",";
		}
		allweValueStr += tagCategoryStr;
	}
	
	private void output_Initialize() throws Exception
	{
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arfffolder + arfffilename), "utf-8"));
		
		writer.write("@RELATION ptttagging"+"\n");
		for(int i=0; i<wordim; i++) {
			writer.write("@ATTRIBUTE article  NUMERIC"+"\n");
		}
		
		writer.write("@ATTRIBUTE class        {positive,negative}"+"\n");
		writer.write("@DATA"+"\n");
	}
	
	public static void main(String args[]) 
	{
		try {
			TaggingData_to_Arff ca = new TaggingData_to_Arff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package ptt.arff;

/*
 * create arff file
 * 
 * version: September 01, 2019 09:54 PM
 * Last revision: September 02, 2019 10:14 PM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
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
//import com.mayabot.blas.Vector;

public class Create_arff 
{
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
		private int messagesCount;
		private String allTitleContent = "";
	
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
	
	public Create_arff() throws Exception
	{
		// Chinese segmentation initialize
		Chinese_Seg_Initialize();
		fastText_zh = FastText.loadModel(sourcebinPath + modelFolder_zh, true);
		
		// Read tagging txt
		String Line = "";
		FileReader fr = new FileReader(sourceFolder + file);
		BufferedReader bfr = new BufferedReader(fr);
				
		int index = 0;
		String temp[];
		while((Line = bfr.readLine())!=null)
		{					
			temp = Line.split("	");
			//System.out.println(temp[0]+"	"+temp[1]);
			
			// content
			ReadSourceFile(temp[0], temp[1]);
			
			if(index == 0) 
			{
				allTitleContent += title + " " + content;
				System.out.println(allTitleContent);
				//for(int i=0; i<titlecontentVec.size(); i++)
				{
					String tmpStr = ChineseWordParser(allTitleContent);
					// BIG5 to GB
					String simStr = BIG5GB_converter(tmpStr);
					// Chinese words Segmentation
					//System.out.println(simStr);
					Chinese_Segmentation(simStr);
					
					//System.out.println(segTerms.size());
				    for(int k=0; k<segTerms.size(); k++) {
				    	//System.out.println(segTerms.get(k));
				    	fasttext(segTerms.get(k).toString());
				    } 
				}
				average();
			}
			
			index++;
			Clean();
		}
		fr.close();
		bfr.close();
	}

	private void ReadSourceFile(String filenameIndex, String articleIdIndex) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) {
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
								author = articleobj.getString("author");
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
							// message
							if (articleobj.has("messages")) {
								JSONArray mesarray = new JSONArray(articleobj.getJSONArray("messages").toString());
								messagesCount = mesarray.length();
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
		
//		if(sentence.length() > 0){
//			System.out.println(sentence);
//		}
		
		return sentence;
	}
	
	private String BIG5GB_converter(String traStr)
	{
		// BIG5 to GB
		String simStrResult = simconverter.convert(traStr);
		//System.out.println(simStrResult);
		
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
		messagesCount = 0;
		allTitleContent = "";
		//titlecontentVec.clear();
		segTerms.clear();
		averageValue.clear();
		//saveWords.clear();
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
			System.out.println(inputStr+"	"+vecTmpzh);
			for(int j=0; j<wordim; j++) {
				averageValueTmp[j] += vecTmpzh.get(j);
			}
		}
	}
	
	private void average()
	{
		System.out.println("----------- average -----------");
		for(int j=0; j<wordim; j++) {
			averageValue.add(averageValueTmp[j]/segTerms.size());
			//System.out.println(j+"	"+averageValueTmp[j]+"	"+averageValue.get(j));
			System.out.print(averageValue.get(j)+",");
		}
	}
	
	public static void main(String args[]) 
	{
		try {
			Create_arff ca = new Create_arff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

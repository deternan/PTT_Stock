package ptt.arff;

/*
 * FastText sample
 * 
 * version: September 03, 2019 11:18 PM
 * Last revision: September 06, 2019 05:57 AM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.mayabot.mynlp.fasttext.FastText;
import com.spreada.utils.chinese.ZHConverter;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class FastText_Sample 
{
	// Segmentation
	private static final String basedir = "/Users/phelps/Documents/github/Light-tools/data/stanford-word-segmenter/data/";	// data path
	static List<String> segmented;
	CRFClassifier<CoreLabel> segmenter;
	// BIG5 to GB
	ZHConverter simconverter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	// Segmented Terms
	Vector segTerms = new Vector();
	
	// FastText
	private int wordim = 300;					
	private String sourcebinPath = "/Users/phelps/Downloads/wiki/";		// 放置 bin 
	private String sourcebinName = "wiki.simple.zh.Chinese.model";		// 讀取的 bin 
	// Model
	private String modelFolder_zh = "wiki.simple.zh.Chinese.model";	// 輸出的 model 檔
	FastText fastText_zh;
	// average value
	private ArrayList averageValue = new ArrayList();
	double[] averageValueTmp = new double[wordim];
	
	private String inputStr = "這檔今天有趣了，成交量4900多張，外資買超3050張，盤中一直有壓盤，但買盤似乎小贏 ，下禮拜5/15財報公布後，多空方向應該會明朗。 籌碼面，外資連續買超31天，一共76000張。 技術面，高檔震盪，加上買盤比較強，應該是要拉根長紅表態。 實際面，向右。 下禮拜繼續看怎麼演";
	
	public FastText_Sample() throws Exception
	{
		Chinese_Seg_Initialize();
		String tmpStr = ChineseWordParser(inputStr);
		String simStr = BIG5GB_converter(tmpStr);
		Chinese_Segmentation(simStr);
		
		// Save as model
//		SaveAsModel();
		// word to vector
		fastText_zh = FastText.loadModel(sourcebinPath + modelFolder_zh, true);
		fasttext();
		
		// Average
		average();
	}
	
	private void Chinese_Seg_Initialize() throws Exception
	{
		System.setOut(new PrintStream(System.out, true, "utf-8"));

	    Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", basedir);
	    
	    props.setProperty("serDictionary", basedir + "dict-chris6.ser.gz");
	    props.setProperty("inputEncoding", "UTF-8");
	    props.setProperty("sighanPostProcessing", "true");

	    segmenter = new CRFClassifier<>(props);
	    segmenter.loadClassifierNoExceptions(basedir + "ctb.gz", props);
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
				//System.out.println(listStr.get(i));
			}			
		}		
	}
	
	private void SaveAsModel() throws Exception
	{
		// Save as model		
		FastText fastText = FastText.loadFasttextBinModel(sourcebinPath + sourcebinName);
		fastText.saveModel(sourcebinPath + modelFolder_zh);
	}
	
	private void fasttext()
	{
		for(int i=0; i<segTerms.size(); i++)
		{
			com.mayabot.blas.Vector vecTmpzh = fastText_zh.getWordVector(segTerms.get(i).toString());
			System.out.println(segTerms.get(i)+"	"+vecTmpzh);
			
			// average
			for(int j=0; j<wordim; j++) {
				averageValueTmp[j] += vecTmpzh.get(j);
			}
		}
	}
	
	private void average()
	{
		for(int j=0; j<wordim; j++) {
			averageValue.add(averageValueTmp[j]/segTerms.size());
			System.out.print(averageValue.get(j)+",");
		}
	}
	
	public static void main(String args[])
	{
		try {
			FastText_Sample ft =  new FastText_Sample();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

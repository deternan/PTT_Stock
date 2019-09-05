package ptt.arff;

/*
 * FastText sample
 * 
 * version: September 03, 2019 11:18 PM
 * Last revision: September 05, 2019 06:18 AM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class FastText_Sample 
{

	// Segmentation
	private static final String basedir = "/Users/phelps/Documents/github/Light-tools/data/stanford-word-segmenter/data/";	// data path
	static List<String> segmented;
	CRFClassifier<CoreLabel> segmenter;
	
	private String inputStr = "内资仍是目前撑盘主力，主要靠政府表明政策作多台股，营造明年总统大选行情，寿险资金活水也开始进场卡位；中实户持续当冲中小型股；自营商因应季底作帐，乘势回补持股赶绩效。";
	// Segmented Terms
	Vector segTerms = new Vector();
	
	public FastText_Sample() throws Exception
	{
		Chinese_Seg_Initialize();
		Chinese_Segmentation(inputStr);
		
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
				System.out.println(listStr.get(i));
			}			
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

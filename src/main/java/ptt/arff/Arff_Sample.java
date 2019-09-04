package ptt.arff;

/*
 * Fast text
 * version: July 02, 2019 06:25 PM
 * Last revision: September 05, 2019 06:54 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import com.mayabot.mynlp.fasttext.FastText;

public class Arff_Sample 
{
	private int wordim = 300;
	private String sourcebinPath = "";								// 放置 bin 
	private String sourcebinName = "wiki.zh.bin";					// 讀取的 bin 
	// Model
	private String modelFolder_zh = "wiki.simple.zh.Chinese.model";	// 輸出的 model 檔
	
	
	public Arff_Sample () throws Exception
	{
		// Save as model
		SaveAsModel();
		
		
	}
	
	private void SaveAsModel() throws Exception
	{
		// Save as model		
		FastText fastText = FastText.loadFasttextBinModel(sourcebinPath + sourcebinName);
		fastText.saveModel(sourcebinPath + modelFolder_zh);
	}
	
	public static void main(String args[]) 
	{
		try {
			Arff_Sample as = new Arff_Sample();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

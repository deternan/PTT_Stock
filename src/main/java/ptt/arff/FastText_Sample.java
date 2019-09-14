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
	
	private String inputStr = "由公司年報中提供的數據可以知道公司的散熱模組出貨量持續在提升，至於毛利的問題 稍後在說明。另外超眾去年才在中國重慶擴建新廠，散熱模組的月產能至少可以提高 100萬組，且產量每年都是有在增加的。 https://i.imgur.com/73ZHQuA.png https://i.imgur.com/DtVR78w.png https://i.imgur.com/kfacIDr.png https://i.imgur.com/TkvrZdW.png 公司的散熱模組中的原料熱導管100%為自製，因此在這一方面不會受到供應商的售價 所影響；且微型熱導管的技術門檻較高其他業者不易跨入。其他的原料包括風扇、導熱墊 片、銅和鋁塊則需要外購，其中影響成本最大的就是銅和鋁的價格，由因此公司的毛利和 獲利和這兩者的價格有很大的關係，2017年獲利和毛利大副下降有很大一部分就是因為近 一兩年銅和鋁價上揚導致成本增加，但不同於石油的是金屬可以用完再回收，因此原料的 供應比較不成問題。 https://i.imgur.com/f1VfuqD.png 這些可能會遇到而影響公司獲利的因素在過去不斷的出現，但公司的營收和獲利還是 繼續成長，除了上面我所提到的散熱的應用會越來越廣泛之外，公司其實也一直在做研發 和創新；最新年報讓我驚豔的是，公司以往不會申請專利，但最新研發的成果卻已經申請 了專利。散熱元件其實生活中很常見，你如果用的是筆電、只要拆開來裡面一定有熱導管 ，只是不一定是超眾做的就是了；且可以發現若是沒有散熱元件，很多電子產品其實都無 法運作。從年報中超眾也說明了他們的在電腦市占率有14%，但方面不是重點，而是未來 的其他應用。 公司散熱產業的特性是接單後生產，因此庫存的管理很重要，由公司的財報中可以看 到存貨佔總資產的比例不高，這也說了散熱產業一定程度的客製化特性。 公司未來發展的策略將致力於產品性能更薄、更小、散熱功率更高的方向前進，且在 目前的產業趨勢上。發展的不利因素包括產業競爭、原物料成本、人力成本、匯率變動等 等，但這些因素過去公司都經歷過:公司成立40年沒有虧損過就說明了公司的競爭力。 說一下財報數字方面的基本面：最重要的毛利和獲利受原物料、匯率和產品價格影響，產 品價格在未來需求還是會成長之下應該會回升，短期內要看的就是原物料價格和匯率，但 長期來看產業還是在成長的。股利政策方面公司章程有提到每年的股利最少都有盈餘的 5成，現金股利政策穩定。現金流量方面就算是去年衰退及今年第一季獲利不好，但自由 現金流量都還是維持正值。 https://i.imgur.com/9eV29sF.png 另外我買股票比較沒在看技術和籌碼的，所以在此就不分析 4. 進退場機制：(非長期投資者，必須有停損機制) 長期投資，基本上持有3~5以上，除非公司基本面出問題才會賣出。";
	
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
		System.out.println();
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

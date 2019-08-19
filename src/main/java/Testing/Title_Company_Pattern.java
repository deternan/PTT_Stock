package Testing;

/*
 * Title pattern testing
 * version: August 19, 2019 11:30 PM
 * Last revision: August 19, 2019 11:30 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GUI.Units;

public class Title_Company_Pattern 
{
	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	// Regular expression
	Pattern pattern;
	Matcher matcher;
	// display
	Vector companyIdDisplay = new Vector();
	Vector companyNameDisplay = new Vector();
	Vector IdIndexVec = new Vector();
	private String contentTmp = "1. 標的：2325 矽品 2. 分類：空 3. 分析/正文： 大宇繼昨天大買1.5萬張矽品後，大宇資今天明教再度出手 15 萬張 http://tinyurl.com/jssx42s 不過看那成交金額6111，每股53.21元，很明顯不是在市場上買 從其它新聞來看，應該是跟特定法人盤後買的吧 今天已經取得30.44%的股權，粗略算一算，應該再買7萬張左右就33%了吧 今年矽品股東會應該很精彩 矽品今天收52.7，明教收購價55元，約4%的差距 收購案要一年後才能提，合併也還沒通過公平會審查 從明教說要收購開始，矽品最高大概就是53左右 而現在收購要拖一年，怎樣看這個價位都是高點，所以可以空空看 下週開盤後可以看看矽品沖多高，然後空個幾張來玩玩(沒券空期貨也行) 反正最高就漲到55元，風險也不會太高(除非阿伯突然有錢可以搶股)， 4. 進退場機制：(停損價位/出場條件/長期投資) 進場：下週一抓高點空，53元以上大概都可以吧 出場：我覺得跌回50元都是很有可能的，不然就是小賺就出場 停損：如果沒人提出高於55元的收購價，應該是沒必要停損，除非遇到強制回補 "; 
	
	public Title_Company_Pattern() throws Exception
	{
		ReadCompany();
		//System.out.println(companyId.size()+"	"+companyName.size());
		PatternCheck(contentTmp);
		
		System.out.println(companyNameDisplay.size()+"	"+companyIdDisplay.size());
		for(int i=0; i<companyNameDisplay.size(); i++) {
			System.out.println(companyNameDisplay.get(i));
		}
		for(int i=0;i<companyIdDisplay.size();i++) {
			System.out.println(companyIdDisplay.get(i));
		}
		System.out.println(IdIndexVec.size());
	}
	
	private void ReadCompany() throws Exception {
		// TWSE
		ReadCompany(Units.sourceFolder, Units.twsefile);
		// TPEX
		ReadCompany(Units.sourceFolder, Units.tpexfile);
	}

	private void ReadCompany(String pathName, String fileName) throws Exception {
		File file = new File(pathName + fileName);
		if (file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				companyId.add(temp[0]);
				companyName.add(temp[1]);
			}
			bfr.close();
		}
	}
	
	private void PatternCheck(String strTmp) 
	{
		// Company Name and ID
		String regexName = "";
		String regexId = "";
		String tmpName;
		String patternName;
		String patternId;
		
		boolean companyCheck;
		boolean idCheck;
		
		for (int i=0; i<companyId.size(); i++) {
			patternName = "";
			patternId = "";	
			companyCheck = false;
			idCheck = false;

			// Company Name
			tmpName = companyName.get(i).toString().replace("-KY", "");
			tmpName = tmpName.replace("-DR", "");
			regexName = "(" + tmpName + ")+";
			pattern = Pattern.compile(regexName, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
				companyCheck = true;
			}
			// Company Id
			regexId = companyId.get(i).toString();
			pattern = Pattern.compile(regexId, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				patternId = matcher.group();
				companyIdDisplay.add(patternId);
				idCheck = true;
			}
			
			// Method 1
			if(companyCheck && idCheck) {
				IdIndexVec.add(patternId);
			}
		}


	}
	
	public static void main(String args[]) {
		try {
			Title_Company_Pattern tp = new  Title_Company_Pattern();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

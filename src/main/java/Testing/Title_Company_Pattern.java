package Testing;

/*
 * Title pattern testing
 * version: August 19, 2019 11:30 PM
 * Last revision: August 20, 2019 08:22 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
//	Vector IdIndexVec = new Vector();
	private String contentTmp = "1. 標的：2325 矽品 2. 分類：空 3. 分析/正文： 大宇繼昨天大買1.5萬張矽品後，大宇資今天明教再度出手 15 萬張 http://tinyurl.com/jssx42s 不過看那成交金額6111，每股53.21元，很明顯不是在大宇資市場上買 從其它新聞來看，應該是跟特定法人盤後買的吧 今天已經取得30.44%的股權，粗略算一算，應該再買7萬張左右就33%了吧 今年矽品股東會應該很精彩 矽品今天收52.7，明教收購價55元，約4%的差距 收購案要一年後才能提，合併也還沒通過公平會審查 從明教說要收購開始，矽品最高大概就是53左右 而現在收購要拖一年，怎樣看這個價位都是高點，所以可以空空看 下週開盤後可以看看矽品沖多高，鴻海然後空個幾張來玩玩(沒券空期貨也行) 反正最高就漲到55元，風險也不會太高(除非阿伯突然有錢可以搶股)， 4. 進退場機制：(停損價位/出場條件/長期投資) 進場：下週一抓高點空，53元以上大概都可以吧 出場：我覺得跌回50元都是很有可能的，不然就是小賺就出場 停損：如果沒人提出高於55元的收購價，應該是沒必要停損，除非遇到強制回補 "; 
	
	public Title_Company_Pattern() throws Exception
	{
		ReadCompany();
		PatternCheck(contentTmp);
		
		System.out.println(companyNameDisplay.size()+"	"+companyIdDisplay.size());
		for(int i=0; i<companyNameDisplay.size(); i++) {
			System.out.println(companyNameDisplay.get(i));
		}
		for(int i=0;i<companyIdDisplay.size();i++) {
			System.out.println(companyIdDisplay.get(i));
		}
		// Method 1
//		System.out.println(IdIndexVec.size());
//		System.out.println("----------------------------------");
		// Metgod 2
		MapSort();
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
			
			while(matcher.find()) {
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
				companyCheck = true;
			}
			// Company Id
			regexId = companyId.get(i).toString();
			pattern = Pattern.compile(regexId, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			
			while(matcher.find()){
				patternId = matcher.group();
				companyIdDisplay.add(patternId);
				idCheck = true;
			}
			
			// Method 1
//			if(companyCheck && idCheck) {
//				IdIndexVec.add(patternId);
//			}
		}


	}
	
	private void MapSort()
	{
		// company name
		String indexnameStr = "";
		int indexnameValue = 0;
		List<String> companylist = new ArrayList<String>();
		for(int i=0; i<companyNameDisplay.size(); i++) {
			companylist.add(companyNameDisplay.get(i).toString());
		}

		Map<String, Integer> Nameduplicates = new HashMap<String, Integer>();
		for (String str : companylist) {
			if (Nameduplicates.containsKey(str)) {
				Nameduplicates.put(str, Nameduplicates.get(str) + 1);
			} else {
				Nameduplicates.put(str, 1);
			}
		}
		
		// Sort (company name)
		LinkedHashMap<String, Integer> sortednameMap = new LinkedHashMap<>();
		Nameduplicates.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEachOrdered(x -> sortednameMap.put(x.getKey(), x.getValue()));

		for (Map.Entry<String, Integer> nameentry : sortednameMap.entrySet()) {
			System.out.println(nameentry.getKey() + " = " + nameentry.getValue());
			indexnameStr = nameentry.getKey();
			indexnameValue = nameentry.getValue();
		}
		
		// company id
		String indexidStr = "";
		int indexidValue = 0;
		List<String> idlist = new ArrayList<String>();
		for(int i=0; i<companyIdDisplay.size(); i++) {
			idlist.add(companyIdDisplay.get(i).toString());
		}
		
		Map<String, Integer> idduplicates = new HashMap<String, Integer>();
		for (String str : idlist) {
			if (idduplicates.containsKey(str)) {
				idduplicates.put(str, idduplicates.get(str) + 1);
			} else {
				idduplicates.put(str, 1);
			}
		}
		// sort (company id)
		LinkedHashMap<String, Integer> sortedidMap = new LinkedHashMap<>();
		idduplicates.entrySet().stream().sorted(Map.Entry.comparingByValue())
		.forEachOrdered(x -> sortedidMap.put(x.getKey(), x.getValue()));
		
		for (Map.Entry<String, Integer> identry : sortedidMap.entrySet()) {
			System.out.println(identry.getKey() + " = " + identry.getValue());
			indexidStr = identry.getKey();
			indexidValue = identry.getValue();
		}
		
		System.out.println("-----------------------------------");
		// Compared size
		System.out.println(indexnameStr+"	"+indexnameValue);
		System.out.println(indexidStr+"	"+indexidValue);
		
		String ouputCompanyStr = "";
		String outputIdStr = "";
		if(indexnameValue > indexidValue) {
			for (int i=0; i<companyName.size(); i++)
			{
				if(indexnameStr.trim().equalsIgnoreCase(companyName.get(i).toString())) {
					ouputCompanyStr = companyName.get(i).toString();
					outputIdStr = companyId.get(i).toString();
					break;
				}
			}
		}else if(indexidValue > indexnameValue) {
			for (int i=0; i<companyId.size(); i++)
			{
				if(indexidStr.trim().equalsIgnoreCase(companyId.get(i).toString())) {
					ouputCompanyStr = companyName.get(i).toString();
					outputIdStr = companyId.get(i).toString();
					break;
				}
			}
		}else if((indexnameValue == indexidValue) && (indexnameValue>0) && (indexidValue>0)){
			ouputCompanyStr = companyName.get(0).toString();
			outputIdStr = companyId.get(0).toString();
		}
		
		System.out.println("-----------------------------------");
		System.out.println("ourput company	"+ouputCompanyStr);
		System.out.println("output id	"+outputIdStr);
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

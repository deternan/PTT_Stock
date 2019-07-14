package GUI.Tagging;

/*
 * Get values (Main)
 * version: July 06, 2019 15:03 PM
 * Last revision: July 13, 2019 11:40 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.Units;


public class Tagging_Main {
	// articlelist
	BufferedWriter writerarticlelist;
	// File Check
		String extension_Json = "json";
	// Storage
	FileOutputStream writer;
	PrintStream ps;

	// Parameters
	private String fileName_index = "";
	private String artileID_index = "";

	private boolean filestartPoint = false;
	private boolean startPoint = false;
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	// article content
	private String articleId;
	private String author;
	private String title;
	private String content;
	private String date;
	private int messagesCount;
	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	// Regular expression
	Pattern pattern;
	Matcher matcher;
	// display
	Vector companyIdDisplay = new Vector();
	Vector companyNameDisplay = new Vector();
	Vector valueDisplay = new Vector();
	// values
	Vector allvalueVec = new Vector();
	// Date
	DateFormat df = new SimpleDateFormat(Units.basic_pattern, Locale.getDefault());
	private double onemonthAverage;
	private double twomonthAverage;
	private double threemonthAverage;
	private String dateremindTag;
	// Testing
	//private String contentTmp = "1. 標的：6558興能高 2. 分類：短、中多 3. 分析/正文： 貿易戰暫告一段落，行動裝置鋰電池應會再度回到熱門市場中。貿易戰之前這支已經拉了 一波，前高75。隨著貿易戰進行，穿戴裝置市場保守，掉到50底，後轉強。 昨天貿易戰中場嘉年華，這支獲得跳空缺口（66.5跳69），60ma強勢上揚，搭配之前就已 擺好的5ma、10ma、20ma，均線皆已上揚且依序排列。 K值雖已達87.5，但高檔鈍化可能性高。 4. 進退場機制：(非長期投資者，必須有停損機制) 今早洗盤68已進 分段停利：73起 加碼區：66.568.5 停損：55";
	private String contentTmp = "1. 標的：2325 矽品 2. 分類：空 3. 分析/正文： 繼昨天大買1.5萬張矽品後，今天明教再度出手 15 萬張 http://tinyurl.com/jssx42s 不過看那成交金額，每股53.21元，很明顯不是在市場上買 從其它新聞來看，應該是跟特定法人盤後買的吧 今天已經取得30.44%的股權，粗略算一算，應該再買7萬張左右就33%了吧 今年矽品股東會應該很精彩 矽品今天收52.7，明教收購價55元，約4%的差距 收購案要一年後才能提，合併也還沒通過公平會審查 從明教說要收購開始，矽品最高大概就是53左右 而現在收購要拖一年，怎樣看這個價位都是高點，所以可以空空看 下週開盤後可以看看矽品沖多高，然後空個幾張來玩玩(沒券空期貨也行) 反正最高就漲到55元，風險也不會太高(除非阿伯突然有錢可以搶股)， 4. 進退場機制：(停損價位/出場條件/長期投資) 進場：下週一抓高點空，53元以上大概都可以吧 出場：我覺得跌回50元都是很有可能的，不然就是小賺就出場 停損：如果沒人提出高於55元的收購價，應該是沒必要停損，除非遇到強制回補 "; 
	
	public Tagging_Main() throws Exception {

// article list
//		writerarticlelist = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Units.sourceFolder + Units.alllist), "utf-8"));
//		ReadAllArticlesList();
//		writerarticlelist.close();
		
// automatic tagging
		// Company info.
		ReadCompany();
		// articles related
		// Read history
		ReadHistory();
		// Find start point by history record
//		ReadAllArticles(fileName_index, artileID_index);
		ReadAllArticles_v2(fileName_index, artileID_index);
		// All list
		
		String inputarticleId;
		String formatdate;
		String TWDate;
		String formatdateAdd;
		String TWDateAdd;
		String addtwpday;
		// Get article content and filtering
		for (int i = 0; i < articleIdVec.size(); i++) {
			articleId = "";
			author = "";
			title = "";
			content = "";
			date = "";
			messagesCount = 0;
			companyIdDisplay.clear();
			companyNameDisplay.clear();
			valueDisplay.clear();
			inputarticleId = "";
			formatdate = "";
			TWDate = "";
			onemonthAverage = 0;
			twomonthAverage = 0;
			threemonthAverage = 0;
			dateremindTag = "";
			formatdateAdd = "";
			TWDateAdd = "";
			addtwpday = "";

			// Get article content
			GetContentByArticleId(filenameVec.get(i).toString(), articleIdVec.get(i).toString());
			// Filter
			pattern = Pattern.compile(Units.regexTitle, Pattern.MULTILINE);
			matcher = pattern.matcher(title);
			if (matcher.find()) {
				// Pattern Recognition
				PatternCheck(content);
//				System.out.println(filenameVec.get(i) + "	" + articleId + "	" + date + "	" + title + "	" + author + "	"
//						 + companyIdDisplay.size() + "	" + companyNameDisplay.size() + "	"+ valueDisplay.size());
				
				if(companyIdDisplay.size() > 0) {
					inputarticleId = companyIdDisplay.get(0).toString();
					// date 
					date = replaceSpace(date);
					formatdate = ISODateParser(date);
					TWDate = convertTWDate(formatdate);
					// add day
					addtwpday = addTwoDate(formatdate);
					formatdateAdd = ISODateParserZone(addtwpday);
					TWDateAdd = convertTWDate(formatdateAdd);
					
					//System.out.println(TWDate+"	"+TWDateAdd);
					// values average
					getValueAverageByarticleId(inputarticleId, TWDate, TWDateAdd);
				}
			}
			System.out.println(TWDate+"	"+filenameVec.get(i)+"	"+articleIdVec.get(i)+"	"+onemonthAverage+"	"+twomonthAverage+"	"+threemonthAverage);
		}

		// Save history
		// StoragedHistory(aa, bb);
	}
// Read History

	private void ReadHistory() throws Exception 
	{
		File file = new File(Units.historyFolder + Units.historyName);
		if (file.exists()) {
			fileName_index = "";
			artileID_index = "";
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				fileName_index = temp[0];
				artileID_index = temp[1];
			}
			bfr.close();
		}
	}

	
	private void ReadAllArticles(String historyfileName, String historyarticleId) throws Exception 
	{
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);

		for (File file : listOfFiles) {
			if (file.isFile()) {

				if ((filestartPoint == true) && (startPoint == true)) {
					// System.out.println(file.getName());
					StartCoolection(file.getName());
				}

				if (file.getName().equalsIgnoreCase(historyfileName)) {
					// System.out.println(file.getName());
					filestartPoint = true;
					articleIndex(historyfileName, historyarticleId, file.getName());
				}
			}
		}
	}
	

	private void ReadAllArticles_v2(String historyfileName, String historyarticleId) throws Exception
	{
		File file = new File(Units.sourceFolder + Units.alllist);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		boolean startcheck = false;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				if(startcheck) {
					filenameVec.add(temp[0]);
					articleIdVec.add(temp[1]);
				}
				
				if(historyarticleId.equalsIgnoreCase(temp[1])) {
					startcheck = true;
					//System.out.println(temp[0]+"	"+temp[1]);
				}
			}
		}
		bfr.close();
	}

	private void articleIndex(String historyfileName, String historyarticleId, String currentFileName)
			throws Exception {
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + historyfileName);
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

						if ((filestartPoint == true) && (startPoint == true)) {
							// ==== Collection
							// System.out.println(currentFileName+" "+idTmp+" "+startPoint);
							filenameVec.add(currentFileName);
							articleIdVec.add(idTmp);
						}

						if ((filestartPoint == true) && (idTmp.equalsIgnoreCase(historyarticleId))) {
							startPoint = true;
						}
					}
				}
			}
		}
	}

	
	
	private void StartCoolection(String currentFileName) throws Exception 
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + currentFileName);
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
						// ==== Collection
						System.out.println(currentFileName+" "+idTmp);
						filenameVec.add(currentFileName);
						articleIdVec.add(idTmp);
					}
				}
			}
		}
	}

	private void GetContentByArticleId(String filenameIndex, String articleIdIndex) throws Exception {
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
								// System.out.println(articleobj.getJSONArray("messages"));
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

	
	private void StoragedHistory(String articleFileName, String articleId) throws Exception {
		writer = new FileOutputStream(Units.historyFolder + Units.historyName, true);

		Date date = new Date();
		// System.out.println(date.toString());

		ps = new PrintStream(writer);
		ps.print(articleFileName + "	" + articleId + "	" + date.toString() + "\n");
		ps.close();
	}

	private boolean isJSONValid(String jsonInString) {
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}

	// Read company name & id
	
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

	// Regular Expression
	
	private void PatternCheck(String strTmp) {
		// Company Name and ID
		String regexName = "";
		String regexId = "";
		String tmpName;
		String patternName;
		String patternId;
		boolean namecheck = false;
		boolean idcheck = false;

		for (int i = 0; i < companyId.size(); i++) {
			patternName = "";
			patternId = "";

			// Name
			tmpName = companyName.get(i).toString().replace("-KY", "");
			tmpName = tmpName.replace("-DR", "");
			regexName = "(" + tmpName + ")+";
			pattern = Pattern.compile(regexName, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				// System.out.println(companyId.get(i)+" "+companyName.get(i)+"
				// "+matcher.group());
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
				namecheck = true;
			}
			// Id
			regexId = companyId.get(i).toString();
			pattern = Pattern.compile(regexId, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				patternId = matcher.group();
				idcheck = true;
				companyIdDisplay.add(patternId);
			}

		}

		// Values
		String regexValue = "([0-9]+\\.?[0-9]+)";
		pattern = Pattern.compile(regexValue, Pattern.MULTILINE);
		matcher = pattern.matcher(strTmp);
		while (matcher.find()) {
			// System.out.println(matcher.group(0));
			valueDisplay.add(matcher.group(0));
		}
	}

	private void ReadAllArticlesList() throws Exception {
		boolean checkResponse;
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);

		String articlenameTmp;
		String idTmp = "";
		String strTmp = "";
		String Line = "";
		for (File file : listOfFiles) {
			strTmp = "";
			
			if (file.isFile()) {
				articlenameTmp = file.getName();
				checkResponse = ExtensionCheck(Units.articleFolder + articlenameTmp);
				if(checkResponse) {
					FileReader fr = new FileReader(Units.articleFolder + articlenameTmp);
					BufferedReader bfr = new BufferedReader(fr);
					while ((Line = bfr.readLine()) != null) {
						strTmp += Line;
					}
					fr.close();
					bfr.close();
					
					if (isJSONValid(strTmp)) {
						JSONObject obj = new JSONObject(strTmp);
						if (obj.has("articles")) {
							JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
							for (int i = 0; i < jsonarray.length(); i++) {
								JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
								if (articleobj.has("article_id")) {
									idTmp = articleobj.getString("article_id");							
								}
								//System.out.println(articlenameTmp+"	"+idTmp);
								writerarticlelist.write(articlenameTmp+"	"+idTmp+"\n");
							}
						}
					}
					System.out.println(articlenameTmp);
				}				
			}
		}
	}
	
	private boolean ExtensionCheck(String path) {
		boolean checkResponse = false;

		String Getextension = getFileExtension(new File(path));
		String extension = Getextension.substring(1, Getextension.length());
		if (extension.equalsIgnoreCase(extension_Json)) {
			checkResponse = true;
		}
		return checkResponse;
	}
	
	private static String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
    }
	
	public static void main(String args[]) {
		try {
			Tagging_Main tagging = new Tagging_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getValueAverageByarticleId(String articleId, String dateStr, String addtwoday) throws Exception 
	{
		File file = new File(Units.value_folder + articleId + Units.extension);
		if (file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			int dateindexTag = 0;
			int dateindextwoTag = 0;
			int index = 0;
			allvalueVec.clear();
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("	");
				if(dateStr.equalsIgnoreCase(temp[0])) {
					dateindexTag = index;
					//break;
				}
				if(addtwoday.equalsIgnoreCase(temp[0])) {
					dateindextwoTag = index;
				}
				allvalueVec.add(temp[1]);
				index++;
			}
			bfr.close();
			
			int nextIndex;
			if(dateindexTag > dateindextwoTag) {
				nextIndex = dateindexTag;
			}else {
				nextIndex = dateindextwoTag;
			}
			
			//System.out.println(nextIndex+"	"+dateStr+"	"+addtwoday);
			// values average
			onemonthAverage = monthAverage(1, nextIndex, allvalueVec);
			twomonthAverage = monthAverage(2, nextIndex, allvalueVec);
			threemonthAverage = monthAverage(3, nextIndex, allvalueVec);
		}
	}
	
	private double monthAverage(int monthInt, int dateIndex, Vector allvalueVec)
	{
		int remnants = allvalueVec.size() - dateIndex;
		double value1 = 0;
		double value2 = 0;
		double value3 = 0;
		double value = 0; 
		if(monthInt == 1) {
			if(remnants < 22) {
				dateremindTag = "less than 30 days";
			}else {
				for(int i=0; i<5; i++) {
					value1 += Double.parseDouble(allvalueVec.get(22+i+dateIndex).toString());
				}
				value = value1 / 5;
			}
		}else if(monthInt == 2) {
			if(remnants < 46) {
				dateremindTag = "less than 60 days";
			}else {
				for(int i=0; i<5; i++) {
					value2 += Double.parseDouble(allvalueVec.get(46+i+dateIndex).toString());
				}
				value = value2/ 5;
			}
		}else if(monthInt == 3) {
			if(remnants < 66) {
				dateremindTag = "less than 90 days";
			}else {
				for(int i=0; i<5; i++) {
					value3 += Double.parseDouble(allvalueVec.get(66+i+dateIndex).toString());
				}
				value = value3 / 5;
			}
		}
		
		return value;
	}
	
 	private String ISODateParser(String dateStr) throws Exception {
		boolean chinesecheck;
		chinesecheck = isChinese(dateStr);
		DateTimeFormatter formatter;
		if (chinesecheck == true) {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_pattern, Locale.TAIWAN);
		} else {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_pattern, Locale.ENGLISH);
		}

		LocalDate dateTime = LocalDate.parse(dateStr, formatter);
		
		return dateTime.toString().replaceAll("-", "");
	}	

 	private String ISODateParserZone(String dateStr) throws Exception {
		boolean chinesecheck;
		chinesecheck = isChinese(dateStr);
		DateTimeFormatter formatter;
		if (chinesecheck == true) {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_patternZone, Locale.TAIWAN);
		} else {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_patternZone, Locale.ENGLISH);
		}

		LocalDate dateTime = LocalDate.parse(dateStr, formatter);
		
		return dateTime.toString().replaceAll("-", "");
	}
 	
	private boolean isChinese(String con) {
		for (int i = 0; i < con.substring(0, 3).length(); i++) {
			if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(String.valueOf(con.charAt(i))).find()) {
				return false;
			}
		}

		return true;
	}
	
	
	private String replaceSpace(String dateStr) {
		String dategap = "0";
		String front = dateStr.substring(0, 8);
		String last = dateStr.substring(9, dateStr.length());
		String newdateStr = "";
		if(dateStr.substring(8, 9).equalsIgnoreCase(" ")) {
			newdateStr = front + dategap + last;
		}else {
			newdateStr = dateStr;
		}
		
		return newdateStr;
	}
	
	
	private String convertTWDate(String AD) {
	    SimpleDateFormat df4 = new SimpleDateFormat("yyyyMMdd");
	    SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
	    Calendar cal = Calendar.getInstance();
	    String TWDate = "";
	    try {
	        cal.setTime(df4.parse(AD));
	        cal.add(Calendar.YEAR, -1911);
	        TWDate = Integer.toString(cal.get(Calendar.YEAR)) + df2.format(cal.getTime());
	    } catch (Exception e) {
	        e.printStackTrace();
	        //return null;
	    }
	    
	    return TWDate;
	}
	
	private String addTwoDate(String dateStr) throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date inputDate = dateFormat.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + 2);
		
		return cal.getTime().toString();
		//return cal.getTime();
	}
	
}

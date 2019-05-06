package ptt.other;

/*
 * ouput the last date
 * version: May 02, 2019 07:00 PM
 * Last revision: May 06, 2019 09:12 PM
 * 
 * Author : Chao-Hsuan Ke
 * Institute: Delta Research Center
 * Company : Delta Electronics Inc. (Taiwan)
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Data_Output {

	private String folder_source = "/Users/phelps/temp/";
	private BufferedReader bfr;
	// Parsing
	JSONParser parser = new JSONParser();
	// DateTime
	private String isotime_pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
	DateTimeFormatter formatter;
	// File Check
	String extension_Json = "json";
	// Date 
	String year;
	String month;
	String day;
	
	// output
	private String outputBase = "";
	
	public Data_Output() throws Exception
	{
		boolean checkResponse;
		File folder = new File(folder_source);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        		    
		        // Check extension file name
		        checkResponse = ExtensionCheck(folder_source + file.getName());
		        if(checkResponse) {
		        	 // Read files
			        ReadFile(folder_source + file.getName());
		        }
		       
		    }
		}
		
		System.out.println(outputBase+".txt");
	}
	
	private boolean ExtensionCheck(String path)
	{
		boolean checkResponse = false;
		        		        
		        String Getextension = getFileExtension(new File(path));
		        String extension = Getextension.substring(1, Getextension.length());
		        if(extension.equalsIgnoreCase(extension_Json)) {
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
		
	private void ReadFile(String path) throws Exception
	{
		FileReader fr = new FileReader(path);
		bfr = new BufferedReader(fr);
		String Line;
		String allText = "";
		while((Line = bfr.readLine())!=null)
		{								
			allText += Line;
		}
		fr.close();
		bfr.close();	
		
		year = "";
		month = "";
		day = "";
		
		// Parsing
		Parsing(allText);
	}
	
	private void Parsing(String lineStr) throws Exception
	{
		JSONObject json = (JSONObject) parser.parse(lineStr);
		JSONArray msg = (JSONArray) json.get("articles");
			
		String author;
		
		for(int i=0; i<msg.size(); i++) {
			JSONObject articlejson = (JSONObject) parser.parse(msg.get(i).toString());
			
			if(articlejson.containsKey("date")) {
				//DatePasring(articlejson.get("date").toString());
				Date_Split(articlejson.get("date").toString());
			}
		}
		
	}
	
	private void Date_Split(String dateStr)
	{
		//"date":"Tue Aug 30 13:38:20 2016",
		String temp[];
		temp = dateStr.split(" ");
		if(temp.length == 6) {
			month = temp[1];
			day = temp[3];
			year = temp[5];
//			System.out.println(year+"_"+month+"_"+day);
			outputBase = String.valueOf(year)+"_"+String.valueOf(month)+"_"+String.valueOf(day);
		}
	}

	
	private void DatePasring(String dateStr)
	{
		//"date":"Tue Aug 30 13:38:20 2016",
		boolean chinesecheck;
		chinesecheck = isChinese(dateStr);
		
		if(chinesecheck == true) {
			formatter = DateTimeFormatter.ofPattern(isotime_pattern, Locale.TAIWAN);
		}else {
			formatter = DateTimeFormatter.ofPattern(isotime_pattern, Locale.ENGLISH);
		}
		
		LocalDate dateTime = LocalDate.parse(dateStr, formatter);
		System.out.println(dateTime.getYear()+"	"+dateTime.getMonth()+"	"+dateTime.getDayOfMonth());
	}
	
	private boolean isChinese(String con)
	{
		for (int i = 0; i < con.substring(0, 3).length(); i++) {
			if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(
					String.valueOf(con.charAt(i))).find()) {
				return false;
			}
		}

		return true;
	}
	
	public static void main(String args[])
	{
		try {
			Data_Output dd = new Data_Output();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

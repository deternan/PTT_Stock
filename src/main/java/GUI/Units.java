package GUI;

public class Units {

	// Exercise parameter
	public final static int sleepTime = 5200;	// 5.2 secs
				// 20 --> 15 --> 13 --> 11 --> 10.5 --> 9.9 --> 8.8 --> 7.8 --> 5.8 --> 5.2
	
	// Company source
	public static final String twselisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=2";	// TWSE
	public static final String tpexlisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=4";	// TPEX
		// file name
		public static final String twsefile = "TWSE.txt";
		public static final String tpexfile = "TPEX.txt";
	
	// Values source
	public static final String TWSEvalueUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=";
	public static final String TPEXvalueUrl = "https://www.tpex.org.tw/web/stock/aftertrading/daily_trading_info/st43_result.php?d=";
		public static final String startYear = "2018";
		public static final String startMonth = "01";
		public static final String startDay = "01";
		
	// Storage
	public static final String sourceFolder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	public static final String TWSE_outputTag = "TWSE";
	public static final String TPEX_outputTag = "TPEX";
	public static final String extension = ".txt";
	public static final String value_folder = "/Users/phelps/Documents/github/PTT_Stock/output/Values/";
	
	// Date
	public static final String basic_pattern = "yyyyMMdd";
	
	// Articles
	public static final String articleFolder = "/data/git/DataSet/ptt/Stock data/";
	// History
	public static final String historyFolder = sourceFolder;
	public static final String historyName = "history.txt";
	// All list
	public static final String alllist = "articlelist.txt";
	// Date
	public static String isotime_pattern = "EEE MMM dd HH:mm:ss yyyy"; 
	public static String isotime_patternZone = "EEE MMM dd HH:mm:ss zzz yyyy"; 
	// Frame
	public static final String regexTitle = "(標的)|(心得)|(閒聊)|(其他)";	
	
}

package GUI;

public class Units {

	// Exercise parameter
	public final static int sleepTime = 2000;
	
	// Company source
	public static final String twselisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	public static final String tpexlisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=4";
	
//	public static final String twselist = "";
	// Values source
	public static final String valueUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=";
		public static final String startYear = "2018";
		public static final String startMonth = "01";
		public static final String startDay = "01";
		
	// Storage
	public static final String sourceFoder = "/Users/phelps/Documents/github/PTT_Stock/source/";
	public static final String output_folder = "/Users/phelps/Desktop/";
	public static final String TWSE_outputTag = "TWSE";
	public static final String TPEX_outputTag = "TPEX";
	public static final String extension = ".txt";
	public static final String value_folder = "/Users/phelps/Documents/github/PTT_Stock/output/Values/";
	
	// Date
	public static final String basic_pattern = "yyyyMMdd";
	
}

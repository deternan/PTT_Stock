package GUI.httpGet;

public class Units {

	// Exercies parameter
	public final static int sleepTime = 1000;
	
	// Company source
	public static final String twselisrUrl = "https://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	// Value source
	public static final String valueUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=";
		public static final String startYear = "2019";
		public static final String startMonth = "01";
		public static final String startDay = "01";
		
	// Storage
	public static final String output_folder = "/Users/phelps/Desktop/";
	public static final String TWSE_outputTag = "TWSE";
	public static final String extension = ".txt";
	public static final String value_folder = "/Users/phelps/Documents/github/PTT_Stock/output/Values/";
	
	// Date
	public static final String basic_pattern = "yyyyMMdd";
	
}

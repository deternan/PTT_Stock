package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parameters 
{
	protected String aa;
	
	// Company info.
	protected String path = "D:\\GitHub\\PTT_Stock\\";	
	protected String twse = "TWSE_2018.txt";
	protected String tpex = "TPEX_2018.txt";	
	// PTT source
	protected String ppt_path = path;
	protected String ppt_filename = "ptt_text.txt";
	// Current values
	protected String value_path = path;
	protected String value_filename = "2018-06-01_values.txt";
	// Text matching
	final List<String> pageTypes_filter = new ArrayList<>(Arrays.asList("標的", "心得", "請益", "投顧"));
	final List<String> TagsTypes_filter = new ArrayList<>(Arrays.asList("多", "空"));
	
}

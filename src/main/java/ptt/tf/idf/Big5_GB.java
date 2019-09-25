package ptt.tf.idf;

import java.util.HashMap;
import java.util.Map;

/*
 * Jar
 * ZHConverter.jar
 * 
 */

import com.spreada.utils.chinese.ZHConverter;

public class Big5_GB 
{
	private String str_;
	
	public Big5_GB(String input_str)
	{				
		//GB_to_Big5(GB_input);
		Big5_to_GB(input_str);
	}
		
	private void GB_to_Big5(String input)
	{
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);    
		//String big5fiedStr = converter.convert(input);
		str_ = converter.convert(input);
		//System.out.println(str_);        
	}
	
	private void Big5_to_GB(String input)
	{        
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);    
		str_ = converter.convert(input);
        //System.out.println(simplifiedStr);
	}
	
	public String Return_str()
	{
		return str_;
	}
	
	/*
	public static void main(String args[])
	{
		Big5_GB BG = new Big5_GB();		
	}
	*/
}

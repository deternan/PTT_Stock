package ptt.arff;

/*
 * create arff file
 * 
 * version: September 04, 2019 10:24 PM
 * Last revision: September 05, 2019 05:22 AM
 * 
 * Author : Chao-Hsuan Ke 
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import com.spreada.utils.chinese.ZHConverter;

public class BIG5GB_converter_Sample 
{

	private String BIG5Str = "內資仍是目前撐盤主力，主要靠政府表明政策作多台股，營造明年總統大選行情，壽險資金活水也開始進場卡位；中實戶持續當沖中小型股；自營商因應季底作帳，乘勢回補持股趕績效。";
	
	public BIG5GB_converter_Sample()
	{
		// BIG5 to GB
		ZHConverter simconverter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String simStrResult = simconverter.convert(BIG5Str);
		System.out.println(simStrResult);
	}
	
	public static void main(String args[]) 
	{
		BIG5GB_converter_Sample sample = new BIG5GB_converter_Sample();
	}
	
}

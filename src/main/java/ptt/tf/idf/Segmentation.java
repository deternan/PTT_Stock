package ptt.tf.idf;


/*
 * Reference
 * http://nlpchina.github.io/ansj_seg/main.html
 * 
 * Jar
 * ansj_seg-5.1.1.jar
 * nlp-lang-1.7.2.jar
 */

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.splitWord.analysis.ToAnalysis;

/*
 * Jar
 * ansj_seg-5.1.0.jar
 * nlp-lang-1.7.7.jar
 * 
 */

public class Segmentation extends Parameters
{
	// Regression
		String regEx="[\u4e00-\u9fa5]+\\/?";
		Pattern pattern;
		Matcher match;
		
		public Vector return_str_vec;
	
	public Segmentation(String input) throws Exception
	{		
		return_str_vec = new Vector();
		
		//String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
		String input_str = ToAnalysis.parse(input).toString();
		//System.out.println(ToAnalysis.parse(input));
		
		Split(input_str);
		//System.out.println(return_str_vec.size()+"	"+input);
	}
		
	private void Split(String input_str)
	{
		String temp_all[];
		temp_all = input_str.split(",");		
		String temp;
		
		for(int i=0;i<temp_all.length;i++)
		{
			if((temp_all[i].length() >0) && (temp_all[i].contains("/"))){				
				temp = temp_all[i].substring(0, temp_all[i].indexOf("/"));
				//System.out.println(temp_all[i]+"	"+temp);
				
				Chinese_word_check(temp);
			}
			
		}
	}	
	
	private void Chinese_word_check(String input_str)
	{
		pattern = Pattern.compile(regEx);		
		match = pattern.matcher(input_str);
				
		if(match.matches()){
			//System.out.println(match.group());
			return_str_vec.add(match.group().toString());
		}
	}	
	
	public Vector Return_vec()
	{
		return return_str_vec;
	}
	
}

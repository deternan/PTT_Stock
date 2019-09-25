package ptt.tf.idf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class StopWords extends Parameters
{
	private FileReader fr;
	private BufferedReader br = null;		
	
	private Vector stopwords_vec;
	
	public StopWords() throws Exception
	{
		stopwords_vec = new Vector();
		
		Stopwords();	// GB
		//System.out.println(stopwords_vec.size());
	}
	
	private void Stopwords() throws Exception
	{
		fr = new FileReader(stopword_path + stopword_file);
		br = new BufferedReader(fr);
		String Line;
		
		while((Line = br.readLine())!=null)
		{
			stopwords_vec.add(Line.trim());			
		}				
		fr.close();
		br.close();		
	}
	
	public Vector Return_vec()
	{
		return stopwords_vec;
	}
	
	/*
	public static void main (String args[])
	{
		try {
			StopWords RS = new StopWords();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
}

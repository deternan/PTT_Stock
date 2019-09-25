package ptt.tf.idf;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DocumentParser_Chinese extends Parameters
{  
    private List termsDocsArray = new ArrayList<>();		// terms in each doc
    private List allTerms = new ArrayList<>(); 				// all terms in all docs
    private List tfidfDocsVector = new ArrayList<>();
 
    // -------------------------------------------------- 
 	private FileReader fr;
 	private BufferedReader br = null;
    // Video info
 	private Vector all_video_id;
    // Translation
 	private Big5_GB BG;
 	private String translation_str;	
 	// Stop Words
 	private Vector stopwords_vec;
 	// Segment Words
 	private Segmentation seg;
 	private Vector words_str_vec;
 	// doc words
 	private Vector doc_words_total;
 	
 	private Vector tagCategoryVec;
 	
    public DocumentParser_Chinese() throws Exception
    {
    	all_video_id = new Vector();
    	stopwords_vec = new Vector();
    	words_str_vec = new Vector();
    	tagCategoryVec = new Vector();
    	
    	Read_Stopwords();
    	//Read_all_video();
    	Read_text();
    }
    
    private void Read_text() throws Exception
    {
    	fr = new FileReader(file_folder + file_name);
		br = new BufferedReader(fr);
		String line;
		String[] AfterSplit;		
		String tagCategoryStr;
		
		while (br.ready())	
		{
			line = br.readLine().toString();
			AfterSplit = line.split("\t");
			tagCategoryStr = AfterSplit[2];
			
			if ((tagCategoryStr.trim().equalsIgnoreCase("positive"))
					|| (tagCategoryStr.trim().equalsIgnoreCase("negative"))) {
				
				all_video_id.add(AfterSplit[1]);
				
				// Big5 GB Translation		
				translation_str = Big5_GB(AfterSplit[3]);					
				
				words_str_vec = Segmentation(translation_str);
				doc_words_total = Remove_StopWords(words_str_vec);
				
				parseFiles(doc_words_total);
				tagCategoryVec.add(tagCategoryStr);
			}
		}
		fr.close();
		br.close();
    }
    
	private void parseFiles(Vector input) throws FileNotFoundException, IOException 
	{
		String[] tokenizedTerms = new String[input.size()];
		input.toArray(tokenizedTerms);
		
		for (String term : tokenizedTerms) {
			if (!allTerms.contains(term)) { // avoid duplicate entry
				allTerms.add(term);
			}
		}
		termsDocsArray.add(tokenizedTerms);
	}
    
    public void tfIdfCalculator()
    {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency          
        //for (String[] docTermsArray : termsDocsArray) 
        for(int i=0; i<termsDocsArray.size(); i++)
        {
        	String[] docTermsArray = (String[]) termsDocsArray.get(i); 
        	double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            //for (String terms : allTerms)
            for(int j=0; j<allTerms.size(); j++)
            {
                String terms = allTerms.get(j).toString();
            	tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = new TfIdf().idfCalculator(termsDocsArray, terms);
                if((tf == 0) || (idf ==0)){
                	
                }
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);  		//storing document vectors;
        }
    }
    
    // Return tf_idf vector
    public List Return_tfidfDocsVector()
    {
    	return tfidfDocsVector;
    }
    
    public Vector Return_tagCategoryVec()
    {
    	return tagCategoryVec;
    }
    
    // Return terms in each doc
    public List Return_termsDocsArray()
    {
    	return termsDocsArray;
    }
    
    // Return all terms in all docs
    public List Return_allTerms()
    {
    	return allTerms;
    }
    
    // Rrturn all video id
    public Vector Return_all_video_id()
    {
    	return all_video_id;
    }
    
    // Read Stopwords
    private void Read_Stopwords() throws Exception
	{
		StopWords st = new StopWords();
		stopwords_vec = st.Return_vec();		
	}
    
    // Big5 GB Translation
 	private String Big5_GB(String input_str)
 	{
 		BG = new Big5_GB(input_str);		
 		String translation_str = BG.Return_str();
 		//System.out.println(translation_str);
 		
 		return translation_str;
 	}
    
 	// Segment words 
 	private Vector Segmentation(String input_str) throws Exception
 	{
 		Vector words_str_vec_temp = new Vector(); 
 		seg = new Segmentation(input_str);
 		
 		words_str_vec_temp = seg.Return_vec(); 		
 		
 		return words_str_vec_temp;
 	}
 	
 	// Remove Stop words
 	private Vector Remove_StopWords(Vector input)
	{
 		Vector All_words_temp = new Vector();
 		boolean check;
		
		for(int i=0; i<input.size(); i++)
		{
			check = true;
			for(int j=0;j<stopwords_vec.size();j++)
			{
				if(input.get(i).toString().equalsIgnoreCase(stopwords_vec.get(j).toString())){
					check = false;
					break;
				}				
			}
			
			if(check == true)
			{
				// Term length
				if(input.get(i).toString().length() > 1)
				{
					All_words_temp.add(input.get(i));	
				}
				
			}
		}
		
		return All_words_temp;
	}
 	
}
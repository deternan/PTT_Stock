package ptt.tf.idf;

import java.util.ArrayList;
import java.util.List;

public class TfIdf 
{
	private static ArrayList<String[]> docterms = new ArrayList<String[]>();
	    
    public double tfCalculator(String[] totalterms, String termToCheck) 
    {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        
        if(count == 0){
        	//System.out.println(count);
        	return 0;        	
        }else{
        	//System.out.println(count / totalterms.length);
        	return count / totalterms.length;        	
        }
        
    }

    public double idfCalculator(List allTerms, String termToCheck) 
    {
        double count = 0;
        double idf;
        
        //for (String[] ss : allTerms)        
        for(int i=0; i<allTerms.size(); i++)
        {
        	//docterms = (ArrayList<String[]>) allTerms.get(i);
        	docterms = (ArrayList<String[]>)allTerms;        	
        	//System.out.println(docterms.get(i).length);
        	
        	//for (String s : ss)
        	for(int j=0; j<docterms.get(i).length; j++)
        	{
                String s = docterms.get(i)[j].toString();
        		//System.out.println(s);
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        if(count == 0){
        	idf = 1;
        	//System.out.println(1);
        }else{
        	idf = 1 + Math.log(allTerms.size() / count);
        	//System.out.println("idf	"+idf);
        }
        
        //return 1 + Math.log(allTerms.size() / count);
        return idf;       
    }
}

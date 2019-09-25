package ptt.tf.idf;


import java.util.ArrayList;
import java.util.List;

public class CosineSimilarity 
{
	private List tfidfDocsVector = new ArrayList<>();
	private double cosine_sim_value;
	
	private double[] vector_list_1;	
	private double[] vector_list_2;
	//private ArrayList<double[]> vector_list_2 = new ArrayList<double[]>();
	
	public CosineSimilarity(List tfidfDocsVector)
	{
		for (int i = 0; i < tfidfDocsVector.size(); i++) 
		{
			//vector_list_1 = (ArrayList<double[]>) tfidfDocsVector.get(i);
			vector_list_1 = (double[]) tfidfDocsVector.get(i);
			
			for (int j = 0; j < tfidfDocsVector.size(); j++) 
            {
				//vector_list_2 = (ArrayList<double[]>) tfidfDocsVector.get(j);
				vector_list_2 = (double[]) tfidfDocsVector.get(j);
            	System.out.println("between " + i + " and " + j + "  =  "+ CosineSimilarity(vector_list_1, vector_list_2));
            }
        }
    }    
	
	private double CosineSimilarity(double[] docVector1, double[] docVector2)
	{
		double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        
        for (int i = 0; i < docVector1.length; i++)
        {
            dotProduct += docVector1[i] * docVector2[i];
            magnitude1 += Math.pow(docVector1[i], 2);
            magnitude2 += Math.pow(docVector2[i], 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
	}

}

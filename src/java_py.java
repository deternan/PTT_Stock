import java.io.BufferedReader;
import java.io.InputStreamReader;

public class java_py 
{

	public java_py()
	{
		try {
            
            String a = "car", b = "D3455054", c = "LJ12GKS28D4418248", d = "qingdao";
                        
            String[] args = new String[] { "python", "/api_test.py", a, b, c, d };
            Process pr = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {                
                System.out.println(line);
            }
            in.close();
            pr.waitFor();         
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args)
	{
		java_py jy = new java_py();
	}
	
}

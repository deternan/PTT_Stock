package GUI.function;

/*
 * Data tagging GUI (article list)
 * version: July 13, 2019 02:10 PM
 * Last revision: July 13, 2019 02:20 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import GUI.Units;

public class ReadArticleList
{
	private Vector fileNameVec = new Vector();
	private Vector articleIdVec = new Vector();

	public ReadArticleList(String historyarticleId) throws Exception
	{
		File file = new File(Units.sourceFolder + Units.alllist);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		boolean startcheck = false;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				if(startcheck) {
					fileNameVec.add(temp[0]);
					articleIdVec.add(temp[1]);
				}
				
				if(historyarticleId.equalsIgnoreCase(temp[1])) {
					startcheck = true;
					//System.out.println(temp[0]+"	"+temp[1]);
				}
			}
		}
		bfr.close();
	}
	
	public Vector returnfilename() {
		return fileNameVec;
	}
	
	public Vector returnarticleId(){
		return articleIdVec;
	}
	
}

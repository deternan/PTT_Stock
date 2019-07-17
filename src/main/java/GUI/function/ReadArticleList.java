package GUI.function;

/*
 * Data tagging GUI (article list)
 * version: July 13, 2019 02:10 PM
 * Last revision: July 18, 2019 00:40 AM
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
	private Vector articleAuthorVec = new Vector();
	private int articleIndex;
	
	public ReadArticleList(String historyarticleId, boolean nullTag) throws Exception
	{
		File file = new File(Units.sourceFolder + Units.alllist);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		boolean startcheck;
		if(nullTag == true) {
			startcheck = false;
		}else {
			startcheck = true;
			articleIndex = 0;
		}
		int aa = 1;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				//System.out.println(Line+"	"+temp.length);
				if(temp.length == 3) {
					if(startcheck) {
						fileNameVec.add(temp[0]);
						articleIdVec.add(temp[1]);
						articleAuthorVec.add(temp[2]);
					}
					
					if(historyarticleId.equalsIgnoreCase(temp[1])) {
						startcheck = true;
						articleIndex = aa;
					}
					aa++;
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
	
	public Vector returnarticleAuthorVec() {
		return articleAuthorVec;
	}
	
	public int returnarticleIndex() {
		return articleIndex;
	}
	
}

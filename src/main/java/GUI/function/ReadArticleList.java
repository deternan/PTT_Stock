package GUI.function;

/*
 * Data tagging GUI (article list)
 * version: July 13, 2019 02:10 PM
 * Last revision: September 18, 2019 00:56 AM
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
	
	Vector TmpStr = new Vector();
	private int allarticleNum = 0;
	
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
		
		String temp[];
		int aa = 0;
		
		if (file.exists()) {
			String Line;
			
			while ((Line = bfr.readLine()) != null) 
			{
				TmpStr.add(Line);
				temp = Line.split("\\t");
//				//System.out.println(Line+"	"+temp.length);
				if(temp.length == 3) {
//					if(startcheck) {
//						fileNameVec.add(temp[0]);
//						articleIdVec.add(temp[1]);
//						articleAuthorVec.add(temp[2]);
//					}
//					
					if(historyarticleId.equalsIgnoreCase(temp[1])) {
						startcheck = true;
						articleIndex = aa;
					}
					
					aa++;
					allarticleNum++;
				}
				
			}
		}
		bfr.close();
		
		boolean stratTag = false;
		for(int i=0; i<TmpStr.size(); i++) {
			if(i == articleIndex) {
				stratTag = true;
			}
			
			if(stratTag){
				temp = TmpStr.get(i).toString().trim().split("\\t");
				if(temp.length == 3) {
					fileNameVec.add(temp[0]);
					articleIdVec.add(temp[1]);
					articleAuthorVec.add(temp[2]);	
				}
			}
		}
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
	
	public int returnallarticleNum() {
		return allarticleNum;
	}
	
}

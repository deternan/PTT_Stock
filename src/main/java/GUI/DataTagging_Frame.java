package GUI;

/*
 * PTT Data tagging GUI
 * version: July 08, 2019 07:40 PM
 * Last revision: July 12, 2019 00:16 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import GUI.function.ReadHistory;

import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class DataTagging_Frame {

	private JFrame frame;
	// public parameter
	JButton btnNewButton;
	JLabel lblNewLabel_2;
	JLabel lblNewLabel_3;
	JLabel lblNewLabel_6;
	JTextPane textPane;
	JTextPane textPane_1;
	JTextPane textPane_2;
	
	
	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	
	private boolean filestartPoint = false;
	private boolean startPoint = false;
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	// display
		Vector companyIdDisplay = new Vector();
		Vector companyNameDisplay = new Vector();
		Vector valueDisplay = new Vector();
	// Parameters
	 	// read history index
		private String fileName_index = "";
		private String artileID_index = "";
	
	// Regular expression
	Pattern pattern;
	Matcher matcher;
	String regexTitle = Units.regexTitle;	
		
	// ActionListener (count)
	int articleIndex = 0;
	// article content
		private String articleId;
		private String author;
		private String title;
		private String content;
		private String date;
		private int messagesCount;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataTagging_Frame window = new DataTagging_Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DataTagging_Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 944, 22);
		frame.getContentPane().add(menuBar);
		
		// MenuBar (File)
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);
		
		// FileChoice
		JMenuItem mnload = new JMenuItem("Load");
		mnload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					
					// Company info.
					try {
						ReadCompany();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					btnNewButton.setEnabled(true);
				    // file dialog
					File selectedFile = fileChooser.getSelectedFile();
				    // function
					try {
						ReadHistory rh = new ReadHistory(selectedFile.getAbsolutePath());
						//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
						fileName_index = rh.returnfileName();
						artileID_index = rh.returnartileID();
						//System.out.println(fileName_index+"	"+artileID_index);
						
						ReadAllArticles(fileName_index, artileID_index);
						// articleIndex = 0
						GetContentByArticleId(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
						pattern = Pattern.compile(regexTitle, Pattern.MULTILINE);
						matcher = pattern.matcher(title);
						if (matcher.find()) {
							// Pattern check
							PatternCheck(content);
							
							lblNewLabel_2.setText(date);
							lblNewLabel_3.setText(author);
							lblNewLabel_6.setEnabled(true);
							lblNewLabel_6.setText(title);
							textPane_2.setText(content);
							
							String companynameStr = "";
							for(int i=0; i<companyNameDisplay.size(); i++) {
								companynameStr += companyNameDisplay.get(i).toString() + "\n";
							}
							textPane.setText(companynameStr);
							String companyidStr = "";
							for(int i=0; i<companyIdDisplay.size(); i++) {
								companyidStr += companyIdDisplay.get(i).toString() + "\n";
							}
							textPane_1.setText(companyidStr);
						}else {
							lblNewLabel_2.setText(date);
							lblNewLabel_3.setText(author);
							lblNewLabel_6.setText(title);
							textPane_1.setText("");
							textPane_2.setText("");
							textPane.setText("");
							lblNewLabel_6.setEnabled(false);
							
							messagesCount = 0;
							companyIdDisplay.clear();
							companyNameDisplay.clear();
							valueDisplay.clear();
						}
						
						articleIndex++;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    
				}
				
			}
		});
		
		mnFile.add(mnload);
		
		JMenuItem mnexit = new JMenuItem("Exit");
		mnexit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if("Exit".equals(e.getActionCommand())){
					int result = JOptionPane.showConfirmDialog(frame, "確定要結束程式嗎?", "確認訊息",
				               JOptionPane.YES_NO_OPTION,
				               JOptionPane.WARNING_MESSAGE);
				    if (result==JOptionPane.YES_OPTION) {System.exit(0);}
				    
				}
			}
		});
		mnFile.add(mnexit);
		
		
		// MenuBar (Help0
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JLabel lblNewLabel = new JLabel("文章發表時間");
		lblNewLabel.setBounds(35, 51, 130, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("作者");
		lblNewLabel_1.setBounds(35, 86, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		// article published time
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(139, 51, 202, 16);
		//lblNewLabel_2.setVisible(false);
		frame.getContentPane().add(lblNewLabel_2);
		
		// article author
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(139, 86, 202, 16);
		//lblNewLabel_3.setVisible(false);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("公司名稱");
		lblNewLabel_4.setBounds(35, 132, 61, 16);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("公司編號");
		lblNewLabel_5.setBounds(176, 132, 61, 16);
		frame.getContentPane().add(lblNewLabel_5);
		
		// Company name
		textPane = new JTextPane();
		//textPane.setBounds(35, 171, 102, 152);
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(35, 171, 102, 152);
		//frame.getContentPane().add(textPane);
        frame.getContentPane().add(scrollPane);
        
        // Company ID
		textPane_1 = new JTextPane();
		JScrollPane scrollPane1 = new JScrollPane(textPane_1);
		//textPane_1.setBounds(172, 171, 102, 152);
		scrollPane1.setBounds(172, 171, 102, 152);
		//frame.getContentPane().add(textPane_1);
		frame.getContentPane().add(scrollPane1);
		
		// content panel
		textPane_2 = new JTextPane();
		textPane_2.setBounds(389, 97, 536, 226);
		frame.getContentPane().add(textPane_2);
		
		// article title
		lblNewLabel_6 = new JLabel("Title");
		lblNewLabel_6.setBounds(389, 51, 536, 16);
		frame.getContentPane().add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Values");
		lblNewLabel_7.setBounds(35, 370, 61, 16);
		frame.getContentPane().add(lblNewLabel_7);
		
		JTextPane textPane_3 = new JTextPane();
		textPane_3.setBounds(35, 398, 78, 117);
		frame.getContentPane().add(textPane_3);
		
		JLabel lblNewLabel_8 = new JLabel("Real Values");
		lblNewLabel_8.setBounds(176, 370, 78, 16);
		frame.getContentPane().add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("1 month");
		lblNewLabel_9.setBounds(176, 398, 78, 16);
		frame.getContentPane().add(lblNewLabel_9);
		
		JLabel lblMonth = new JLabel("2 month");
		lblMonth.setBounds(176, 442, 78, 16);
		frame.getContentPane().add(lblMonth);
		
		JLabel lblMonth_1 = new JLabel("3 month");
		lblMonth_1.setBounds(176, 491, 78, 16);
		frame.getContentPane().add(lblMonth_1);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		rdbtnNewRadioButton.setBounds(403, 398, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("New radio button");
		rdbtnNewRadioButton_1.setBounds(403, 438, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButton_1);
		
		// Next article button
		btnNewButton = new JButton("Next");
		btnNewButton.setBounds(403, 486, 117, 29);
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					GetContentByArticleId(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				pattern = Pattern.compile(regexTitle, Pattern.MULTILINE);
				matcher = pattern.matcher(title);
				if (matcher.find()) {
					// Pattern check
					PatternCheck(content);
					
					lblNewLabel_2.setText(date);
					lblNewLabel_3.setText(author);
					lblNewLabel_6.setEnabled(true);
					lblNewLabel_6.setText(title);
					textPane_2.setText(content);
					
					String companynameStr = "";
					for(int i=0; i<companyNameDisplay.size(); i++) {
						companynameStr += companyNameDisplay.get(i).toString() + "\n";
					}
					textPane.setText(companynameStr);
					String companyidStr = "";
					for(int i=0; i<companyIdDisplay.size(); i++) {
						companyidStr += companyIdDisplay.get(i).toString() + "\n";
					}
					textPane_1.setText(companyidStr);
				}else {
					lblNewLabel_2.setText(date);
					lblNewLabel_3.setText(author);
					lblNewLabel_6.setText(title);
					textPane_1.setText("");
					textPane_2.setText("");
					textPane.setText("");
					lblNewLabel_6.setEnabled(false);
					
					messagesCount = 0;
					companyIdDisplay.clear();
					companyNameDisplay.clear();
					valueDisplay.clear();
				}
				
				articleIndex++;
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		JButton btnSaveExit = new JButton("Save");
		btnSaveExit.setBounds(540, 486, 117, 29);
		frame.getContentPane().add(btnSaveExit);
	}
	
	private void ReadAllArticles(String historyfileName, String historyarticleId) throws Exception
	{
		File folder = new File(Units.articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	
		    	if((filestartPoint == true) && (startPoint == true)) {
		    		StartCoolection(file.getName());
		    	}
		    	
		    	if(file.getName().equalsIgnoreCase(historyfileName)) {
	    			filestartPoint = true;
	    			articleIndex(historyfileName, historyarticleId, file.getName());
	    		}
		    }
		}
	}
	
	private void StartCoolection(String currentFileName) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + currentFileName);
		BufferedReader bfr = new BufferedReader(fr);
		
		String strTmp = "";
		while((Line = bfr.readLine())!=null)
		{	
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		if(isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if(obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for(int i=0; i<jsonarray.length(); i++)
				{
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					
					if(articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						// ==== Collection
						//System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
						filenameVec.add(currentFileName);
						articleIdVec.add(idTmp);
					}
				}
			}
		}
	}
	
	private void articleIndex(String historyfileName, String historyarticleId, String currentFileName) throws Exception
	{
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + historyfileName);
		BufferedReader bfr = new BufferedReader(fr);
		
		String strTmp = "";
		while((Line = bfr.readLine())!=null)
		{	
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		if(isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if(obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for(int i=0; i<jsonarray.length(); i++)
				{
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if(articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						
						if((filestartPoint == true) && (startPoint == true)) {
							// ==== Collection
							//System.out.println(currentFileName+"	"+idTmp+"	"+startPoint);
							filenameVec.add(currentFileName);
							articleIdVec.add(idTmp);
						}
						
						if((filestartPoint == true) && (idTmp.equalsIgnoreCase(historyarticleId))) {
							startPoint = true;
						}
					}
				}
			}
		}
	}
	
	private boolean isJSONValid(String jsonInString) {
		
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check; 
		check = jsonele.isJsonObject();
		return check;
	}

	private void GetContentByArticleId(String filenameIndex, String articleIdIndex) throws Exception {
		String Line = "";
		FileReader fr = new FileReader(Units.articleFolder + filenameIndex);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) {
			strTmp += Line;
		}
		fr.close();
		bfr.close();

		String idTmp;
		if (isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if (obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if (articleobj.has("article_id")) {
						idTmp = articleobj.getString("article_id");
						articleId = idTmp;
						if (idTmp.equalsIgnoreCase(articleIdIndex)) {

							// author
							if (articleobj.has("author")) {
								author = articleobj.getString("author");
							}
							// title
							if (articleobj.has("article_title")) {
								title = articleobj.getString("article_title");
							}
							// content
							if (articleobj.has("content")) {
								content = articleobj.getString("content");
							}
							// date
							if (articleobj.has("date")) {
								date = articleobj.getString("date");
							}
							// message
							if (articleobj.has("messages")) {
								// System.out.println(articleobj.getJSONArray("messages"));
								JSONArray mesarray = new JSONArray(articleobj.getJSONArray("messages").toString());
								messagesCount = mesarray.length();
							}
							break;
						}
					}
				}
			}
		}
	}
	
	// Regular Expression
	private void PatternCheck(String strTmp) 
	{
		// Company Name and ID
		String regexName = "";
		String regexId = "";
		String tmpName;
		String patternName;
		String patternId;

		for (int i = 0; i < companyId.size(); i++) {
			patternName = "";
			patternId = "";

			// Name
			tmpName = companyName.get(i).toString().replace("-KY", "");
			tmpName = tmpName.replace("-DR", "");
			regexName = "(" + tmpName + ")+";
			pattern = Pattern.compile(regexName, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
			}
			// Id
			regexId = companyId.get(i).toString();
			pattern = Pattern.compile(regexId, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			if (matcher.find()) {
				patternId = matcher.group();
				companyIdDisplay.add(patternId);
			}
		}

		// Values
		String regexValue = "([0-9]+\\.?[0-9]+)";
		pattern = Pattern.compile(regexValue, Pattern.MULTILINE);
		matcher = pattern.matcher(strTmp);
		while (matcher.find()) {
			valueDisplay.add(matcher.group(0));
		}
	}
	
	private void ReadCompany() throws Exception {
		// TWSE
		ReadCompany(Units.sourceFolder, Units.twsefile);
		// TPEX
		ReadCompany(Units.sourceFolder, Units.tpexfile);
	}
	
	private void ReadCompany(String pathName, String fileName) throws Exception 
	{
		File file = new File(pathName + fileName);
		if (file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				companyId.add(temp[0]);
				companyName.add(temp[1]);
			}

			bfr.close();
		}
	}
	
}

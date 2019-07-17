package GUI;

/*
 * PTT Data tagging GUI
 * version: July 08, 2019 07:40 PM
 * Last revision: July 16, 2019 09:02 PM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

import GUI.function.ReadArticleList;
import GUI.function.ReadHistory;

import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
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
	JTextPane textPane_3;
	JLabel lblMonth1;
	JLabel lblMonth2;
	JLabel lblMonth3;
	JLabel mclabel;
	JLabel labArticleIdStr;
	JButton btnSaveExit;
	ButtonGroup radiogroup;
	JRadioButton rdbtnNewRadioButtonPositive;
	JRadioButton rdbtnNewRadioButtonNegative;
	JRadioButton rdbtnUndefined;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	
	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	private Vector articleAuthorVec = new Vector();
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
	
	String inputarticleId;
	String formatdate;
	String TWDate;
	String formatdateAdd;
	String TWDateAdd;
	String addtwpday;
	// values
	Vector allvalueVec = new Vector();
	// Date
	DateFormat df = new SimpleDateFormat(Units.basic_pattern, Locale.getDefault());
	private double onemonthAverage;
	private double twomonthAverage;
	private double threemonthAverage;
	private String dateremindTag;
	
	// Storage
		// history
		FileOutputStream writer;
		PrintStream ps;
		// manual tagging
		FileOutputStream writerTagging;
		PrintStream psTagging;
	
	// Running Tag
	int indexNum = 1;
		
	
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
					
					// Loading Company info.
					try {
						ReadCompany();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					btnSaveExit.setEnabled(true);
					
				    // file dialog
					File selectedFile = fileChooser.getSelectedFile();
				    // function
					try {
						// Loading history
						ReadHistory rh = new ReadHistory(selectedFile.getAbsolutePath());
						fileName_index = rh.returnfileName();
						artileID_index = rh.returnartileID();
						
						// Loading article list
						ReadArticleList ra = new ReadArticleList(artileID_index);
						filenameVec = ra.returnfilename();
						articleIdVec = ra.returnarticleId();
						articleAuthorVec = ra.returnarticleAuthorVec();
						indexNum = ra.returnarticleIndex();
						
						
						// start to load article content by articleIndex 
						GetContentByArticleId(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
						pattern = Pattern.compile(regexTitle, Pattern.MULTILINE);
						matcher = pattern.matcher(title);
						if (matcher.find()) {
							// Pattern check
							PatternCheck(content);
							
							// Display on the Frame
							lblNewLabel_2.setText(date);
							lblNewLabel_3.setText(author);
							lblNewLabel_6.setEnabled(true);
							lblNewLabel_6.setText(title);
							textPane_2.setText(content);
							mclabel.setText(String.valueOf(messagesCount));
							labArticleIdStr.setText(articleId);
							label_3.setText(String.valueOf(articleIdVec.size()));
							label_4.setText(String.valueOf(indexNum));
							label_5.setText(String.valueOf(articleIdVec.size() - indexNum));
							
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
							String companyvalueStr = "";
							for(int i=0; i<valueDisplay.size(); i++) {
								companyvalueStr += valueDisplay.get(i).toString() + "\n";
							}
							textPane_3.setText(companyvalueStr);
						}else {
							DisplayAndClean();
						}
						indexNum++;
						articleIndex++;
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		mnFile.add(mnload);
		
		// Exit button
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
		lblNewLabel.setBounds(34, 34, 130, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("作者");
		lblNewLabel_1.setBounds(34, 62, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		// article published time
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(139, 34, 202, 16);
		//lblNewLabel_2.setVisible(false);
		frame.getContentPane().add(lblNewLabel_2);
		
		// article author
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(139, 62, 202, 16);
		//lblNewLabel_3.setVisible(false);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("公司名稱");
		lblNewLabel_4.setBounds(35, 143, 61, 16);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("公司編號");
		lblNewLabel_5.setBounds(176, 143, 61, 16);
		frame.getContentPane().add(lblNewLabel_5);
		
		// Company name
		textPane = new JTextPane();
		//textPane.setBounds(35, 171, 102, 152);
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(35, 171, 102, 152);
        frame.getContentPane().add(scrollPane);
        
        // Company ID
		textPane_1 = new JTextPane();
		JScrollPane scrollPane1 = new JScrollPane(textPane_1);
		//textPane_1.setBounds(172, 171, 102, 152);
		scrollPane1.setBounds(172, 171, 102, 152);
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
		
		// article values
		textPane_3 = new JTextPane();
		JScrollPane scrollPane2 = new JScrollPane(textPane_1);
		//textPane_3.setBounds(35, 398, 78, 117);
		scrollPane2.setBounds(35, 398, 78, 117);
		frame.getContentPane().add(scrollPane2);
		
		JLabel lblNewLabel_8 = new JLabel("Real Values");
		lblNewLabel_8.setBounds(176, 370, 78, 16);
		frame.getContentPane().add(lblNewLabel_8);
		
		lblMonth1 = new JLabel("1 month");
		lblMonth1.setBounds(176, 398, 150, 16);
		lblMonth1.setText("");
		frame.getContentPane().add(lblMonth1);
		
		lblMonth2 = new JLabel("2 month");
		lblMonth2.setBounds(176, 442, 150, 16);
		lblMonth2.setText("");
		frame.getContentPane().add(lblMonth2);
		
		lblMonth3 = new JLabel("3 month");
		lblMonth3.setBounds(176, 491, 150, 16);
		lblMonth3.setText("");
		frame.getContentPane().add(lblMonth3);
		
		rdbtnNewRadioButtonPositive = new JRadioButton("Positive");
		rdbtnNewRadioButtonPositive.setText("positive");
		//rdbtnNewRadioButtonPositive.setActionCommand("positive");
		rdbtnNewRadioButtonPositive.setBounds(403, 398, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButtonPositive);
		rdbtnNewRadioButtonPositive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(true);
			}
		});
		
		rdbtnNewRadioButtonNegative = new JRadioButton("Negative");
		rdbtnNewRadioButtonNegative.setText("negative");
		//rdbtnNewRadioButtonNegative.setActionCommand("negative");
		rdbtnNewRadioButtonNegative.setBounds(403, 435, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButtonNegative);
		rdbtnNewRadioButtonNegative.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(true);
			}
		});
		
		rdbtnUndefined = new JRadioButton("undefined");
		rdbtnUndefined.setBounds(403, 473, 141, 23);
		rdbtnUndefined.setText("undefined");
		frame.getContentPane().add(rdbtnUndefined);
		rdbtnUndefined.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(true);
			}
		});
		
		radiogroup = new ButtonGroup();
		radiogroup.add(rdbtnNewRadioButtonPositive);
		radiogroup.add(rdbtnNewRadioButtonNegative);
		radiogroup.add(rdbtnUndefined);
		
		
		// Next article button
		btnNewButton = new JButton("Next");
		btnNewButton.setBounds(403, 530, 117, 29);
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
					
					// display on board
					lblNewLabel_2.setText(date);
					lblNewLabel_3.setText(author);
					lblNewLabel_6.setEnabled(true);
					lblNewLabel_6.setText(title);
					textPane_2.setText(content);
					mclabel.setText(String.valueOf(messagesCount));
					labArticleIdStr.setText(articleId);
					
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
					String companyvalueStr = "";
					for(int i=0; i<valueDisplay.size(); i++) {
						companyvalueStr += valueDisplay.get(i).toString() + "\n";
					}
					textPane_3.setText(companyvalueStr);
					
					// functions
					if(companyIdDisplay.size() > 0) {
						inputarticleId = companyIdDisplay.get(0).toString();
						// date 
						date = replaceSpace(date);
						try {
							formatdate = ISODateParser(date);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						TWDate = convertTWDate(formatdate);
						
						try {
							// add day
							addtwpday = addTwoDate(formatdate);
							formatdateAdd = ISODateParserZone(addtwpday);
							TWDateAdd = convertTWDate(formatdateAdd);
							
							// values average
							getValueAverageByarticleId(inputarticleId, TWDate, TWDateAdd);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						lblMonth1.setText(String.valueOf(onemonthAverage));
						lblMonth2.setText(String.valueOf(twomonthAverage));
						lblMonth3.setText(String.valueOf(threemonthAverage));
					}else {
						lblMonth1.setText("");
						lblMonth2.setText("");
						lblMonth3.setText("");
					}
					
				}else {
					DisplayAndClean();
				}
				
				label_4.setText(String.valueOf(indexNum+1));
				label_5.setText(String.valueOf(articleIdVec.size() - indexNum));
				indexNum++;
				articleIndex++;
				
				// radio group
				String radiochoice = "";
				if (rdbtnNewRadioButtonPositive.isSelected()) {
					radiochoice = rdbtnNewRadioButtonPositive.getText();
				} else if (rdbtnNewRadioButtonNegative.isSelected()) {
					radiochoice = rdbtnNewRadioButtonNegative.getText();
				} else if(rdbtnUndefined.isSelected()) {
					radiochoice = rdbtnUndefined.getText();
				}
				
				System.out.println(radiochoice);
				if(radiochoice.trim().length() == 0) {
					radiochoice = "null";
				}
				String companyIdTag = "";
				if(companyIdDisplay.size() == 0) {
					companyIdTag = "null";
				}else if(companyIdDisplay.size() == 1) {
					companyIdTag = "single";
				}else {
					companyIdTag = "multiple";
				}
				
				// Save tagging result
				try {
					manualTagging(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString(), articleAuthorVec.get(articleIndex).toString(), radiochoice, companyIdTag);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Remove radioButton
				radiogroup.clearSelection();
				btnNewButton.setEnabled(false);
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		// save button
		btnSaveExit = new JButton("Save");
		btnSaveExit.setBounds(532, 530, 117, 29);
		btnSaveExit.setEnabled(false);
		btnSaveExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					StoragedHistory(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
				
			}
		});
		frame.getContentPane().add(btnSaveExit);
		
		JLabel lblMessageCount = new JLabel("Message Count");
		lblMessageCount.setBounds(35, 563, 102, 16);
		frame.getContentPane().add(lblMessageCount);
		
		mclabel = new JLabel("Message Count");
		mclabel.setBounds(35, 591, 102, 16);
		mclabel.setText("");
		frame.getContentPane().add(mclabel);
		
		JLabel lblArticleId = new JLabel("article id");
		lblArticleId.setBounds(34, 90, 61, 16);
		frame.getContentPane().add(lblArticleId);
		
		labArticleIdStr = new JLabel("");
		labArticleIdStr.setBounds(139, 90, 202, 16);
		frame.getContentPane().add(labArticleIdStr);
		
		label = new JLabel("總數");
		label.setBounds(704, 398, 78, 16);
		frame.getContentPane().add(label);
		
		label_1 = new JLabel("已完成");
		label_1.setBounds(704, 435, 78, 16);
		frame.getContentPane().add(label_1);
		
		label_2 = new JLabel("剩餘");
		label_2.setBounds(704, 473, 78, 16);
		frame.getContentPane().add(label_2);
		
		// article total number
		label_3 = new JLabel("");
		label_3.setBounds(794, 398, 78, 16);
		frame.getContentPane().add(label_3);
		
		// finished number
		label_4 = new JLabel("");
		label_4.setBounds(794, 435, 78, 16);
		frame.getContentPane().add(label_4);
		
		// remind number
		label_5 = new JLabel("");
		label_5.setBounds(794, 477, 78, 16);
		frame.getContentPane().add(label_5);
		
		
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
	
	private void getValueAverageByarticleId(String articleId, String dateStr, String addtwoday) throws Exception 
	{
		File file = new File(Units.value_folder + articleId + Units.extension);
		if (file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			int dateindexTag = 0;
			int dateindextwoTag = 0;
			int index = 0;
			allvalueVec.clear();
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("	");
				if(dateStr.equalsIgnoreCase(temp[0])) {
					dateindexTag = index;
					//break;
				}
				if(addtwoday.equalsIgnoreCase(temp[0])) {
					dateindextwoTag = index;
				}
				allvalueVec.add(temp[1]);
				index++;
			}
			bfr.close();
			
			int nextIndex;
			if(dateindexTag > dateindextwoTag) {
				nextIndex = dateindexTag;
			}else {
				nextIndex = dateindextwoTag;
			}
			
			// values average
			onemonthAverage = monthAverage(1, nextIndex, allvalueVec);
			twomonthAverage = monthAverage(2, nextIndex, allvalueVec);
			threemonthAverage = monthAverage(3, nextIndex, allvalueVec);
		}
	}
	
	private double monthAverage(int monthInt, int dateIndex, Vector allvalueVec)
	{
		int remnants = allvalueVec.size() - dateIndex;
		double value1 = 0;
		double value2 = 0;
		double value3 = 0;
		double value = 0; 
		if(monthInt == 1) {
			if(remnants < 22) {
				dateremindTag = "less than 30 days";
			}else {
				for(int i=0; i<5; i++) {
					value1 += Double.parseDouble(allvalueVec.get(22+i+dateIndex).toString());
				}
				value = value1 / 5;
			}
		}else if(monthInt == 2) {
			if(remnants < 46) {
				dateremindTag = "less than 60 days";
			}else {
				for(int i=0; i<5; i++) {
					value2 += Double.parseDouble(allvalueVec.get(46+i+dateIndex).toString());
				}
				value = value2/ 5;
			}
		}else if(monthInt == 3) {
			if(remnants < 66) {
				dateremindTag = "less than 90 days";
			}else {
				for(int i=0; i<5; i++) {
					value3 += Double.parseDouble(allvalueVec.get(66+i+dateIndex).toString());
				}
				value = value3 / 5;
			}
		}
		
		return value;
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
	
	private String ISODateParser(String dateStr) throws Exception {
		boolean chinesecheck;
		chinesecheck = isChinese(dateStr);
		DateTimeFormatter formatter;
		if (chinesecheck == true) {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_pattern, Locale.TAIWAN);
		} else {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_pattern, Locale.ENGLISH);
		}

		LocalDate dateTime = LocalDate.parse(dateStr, formatter);
		
		return dateTime.toString().replaceAll("-", "");
	}	

 	private String ISODateParserZone(String dateStr) throws Exception {
		boolean chinesecheck;
		chinesecheck = isChinese(dateStr);
		DateTimeFormatter formatter;
		if (chinesecheck == true) {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_patternZone, Locale.TAIWAN);
		} else {
			formatter = DateTimeFormatter.ofPattern(Units.isotime_patternZone, Locale.ENGLISH);
		}

		LocalDate dateTime = LocalDate.parse(dateStr, formatter);
		
		return dateTime.toString().replaceAll("-", "");
	}
 	
 	private boolean isChinese(String con) {
		for (int i = 0; i < con.substring(0, 3).length(); i++) {
			if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(String.valueOf(con.charAt(i))).find()) {
				return false;
			}
		}

		return true;
	}
	
	private String replaceSpace(String dateStr) {
		String dategap = "0";
		String front = dateStr.substring(0, 8);
		String last = dateStr.substring(9, dateStr.length());
		String newdateStr = "";
		if(dateStr.substring(8, 9).equalsIgnoreCase(" ")) {
			newdateStr = front + dategap + last;
		}else {
			newdateStr = dateStr;
		}
		
		return newdateStr;
	}
	
	private String convertTWDate(String AD) {
	    SimpleDateFormat df4 = new SimpleDateFormat("yyyyMMdd");
	    SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
	    Calendar cal = Calendar.getInstance();
	    String TWDate = "";
	    try {
	        cal.setTime(df4.parse(AD));
	        cal.add(Calendar.YEAR, -1911);
	        TWDate = Integer.toString(cal.get(Calendar.YEAR)) + df2.format(cal.getTime());
	    } catch (Exception e) {
	        e.printStackTrace();
	        //return null;
	    }
	    
	    return TWDate;
	}
	
	private String addTwoDate(String dateStr) throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date inputDate = dateFormat.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + 2);
		
		return cal.getTime().toString();
		//return cal.getTime();
	}
	
	private void DisplayAndClean()
	{
		lblNewLabel_2.setText(date);
		lblNewLabel_3.setText(author);
		lblNewLabel_6.setText(title);
		mclabel.setText(String.valueOf(messagesCount));
		labArticleIdStr.setText(articleId);
		textPane_1.setText("");
		textPane_2.setText("");
		textPane_3.setText("");
		textPane.setText("");
		lblNewLabel_6.setEnabled(false);
		
		messagesCount = 0;
		companyIdDisplay.clear();
		companyNameDisplay.clear();
		valueDisplay.clear();
		articleId = "";
		author = "";
		title = "";
		content = "";
		date = "";
		inputarticleId = "";
		formatdate = "";
		TWDate = "";
		onemonthAverage = 0;
		twomonthAverage = 0;
		threemonthAverage = 0;
		dateremindTag = "";
		formatdateAdd = "";
		TWDateAdd = "";
		addtwpday = "";
		lblMonth1.setText("");
		lblMonth2.setText("");
		lblMonth3.setText("");
	}
	
	// Storage
	private void StoragedHistory(String articleFileName, String articleId) throws Exception {
		writer = new FileOutputStream(Units.historyFolder + Units.historyName, true);

		Date date = new Date();

		ps = new PrintStream(writer);
		ps.print(articleFileName + "	" + articleId + "	" + date.toString() + "\n");
		ps.close();
	}

	// Tagging
	private void manualTagging(String articleFileName, String articleId, String articleauthor, String tagging, String category) throws Exception
	{
		writerTagging = new FileOutputStream(Units.taggingFolder + Units.taggingName, true);
		
		psTagging = new PrintStream(writerTagging);
		psTagging.print(articleFileName + "	" + articleId + "	" + articleauthor +"	" +tagging + "	"+ category+"\n");
		psTagging.close();
	}
	
	
}

package GUI;

/*
 * PTT Data manually tagging GUI
 * version: July 08, 2019 07:40 PM
 * Last revision: September 22, 2019 03:16 PM
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

	JFrame frame;
	JButton nextButton;
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
	JLabel labArticleFileStr;
	JButton btnSaveExit;
	ButtonGroup radiogroup;
	JRadioButton rdbtnNewRadioButtonPositive;
	JRadioButton rdbtnNewRadioButtonNegative;
	JRadioButton rdbtnUndefined;
	JLabel label;
	JLabel label_1;
	JLabel label_2;
	JLabel label_3;
	JLabel label_4;
	JLabel label_5;
	JLabel label_6;
	JLabel thisValuelabel;
	JLabel thisCompanylabel;

	// Company info.
	Vector companyId = new Vector();
	Vector companyName = new Vector();

	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	private Vector articleAuthorVec = new Vector();
	private int allarticleNum;
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

	// ActionListener (count)
	int articleIndex = 0;
//	String articleNameStr = "";
//	String articleIdStr = "";
	String authorNameStr = "";
	// article content
	private String articleFile;
	private String articleId;
	private String author;
	private String title;
	private String content;
	private String date;
	private int messagesCount;

	String inputcompanyId;
	
	String formatdate;
	String TWDate;
	String formatdateAdd;
	String TWDateAdd;
	String addtwpday;
	String radiochoice = "";
	String companyIdTag = "";
	// values
	Vector allvalueVec = new Vector();
	// Tagging record;
	private int validCount = 0;
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
	int indexNum;
	// Content pattern check
	boolean contentcheck;
	String titleCompany = "";
	String titleCompanyId = "";	boolean startArticle = true;
	
	// Temp (debug)
	private String currentValue;
	
	// final company name & id
	// final output
	private String ouputCompanyStr = "";
	private String outputIdStr = "";
	
	// whether title includes the companyName 
	boolean titleContentcheck;
	
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
					
					// valid tagging count
					try {
						validTaggingCount();
						label_6.setText(String.valueOf(validCount));
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					btnSaveExit.setEnabled(true);
					contentcheck = false;

					// file dialog
					File selectedFile = fileChooser.getSelectedFile();
					// function
					try {
						// Loading history
						ReadHistory rh = new ReadHistory(selectedFile.getAbsolutePath());
						fileName_index = rh.returnfileName();
						artileID_index = rh.returnartileID();
						articleFile = fileName_index.substring(0, fileName_index.indexOf(".json"));
						
						// Loading article list
						ReadArticleList ra;
						if (artileID_index.trim().length() == 0) {
							ra = new ReadArticleList(artileID_index, false);
						} else {
							ra = new ReadArticleList(artileID_index, true);
						}

						// read all articles list
						filenameVec = ra.returnfilename();
						articleIdVec = ra.returnarticleId();
						articleAuthorVec = ra.returnarticleAuthorVec();
						indexNum = ra.returnarticleIndex();
						allarticleNum = ra.returnallarticleNum();
						// start to load article content by articleIndex
						GetContentByArticleId(filenameVec.get(articleIndex).toString(),	articleIdVec.get(articleIndex).toString());
						
						// Title detection
						titleContentcheck = TitleContentDetection(title);
//						if (titleContentcheck) {
//							companyNameDisplay.add(titleCompany);
//							companyIdDisplay.add(titleCompanyId);	
//						} 
						
						pattern = Pattern.compile(Units.regexTitle, Pattern.MULTILINE);
						matcher = pattern.matcher(title);
						if (matcher.find()) {
							// Pattern check
							companyPatternCheck(title+" "+content);
							contentcheck = true;
							// Display on the Frame
							lblNewLabel_2.setText(date);
							lblNewLabel_3.setText(author);
							lblNewLabel_6.setEnabled(true);
							lblNewLabel_6.setText(title);
							textPane_2.setText(content);
							mclabel.setText(String.valueOf(messagesCount));
							labArticleIdStr.setText(articleId);
							labArticleFileStr.setText(articleFile);
							label_3.setText(String.valueOf(allarticleNum));	
							label_4.setText(String.valueOf(indexNum));
							label_5.setText(String.valueOf(allarticleNum - indexNum));
							
							String companynameStr = "";
							String companyidStr = "";
							String companyvalueStr = "";
							
							
							// functions
							if ((companyIdDisplay.size() > 0) || (companyNameDisplay.size() > 0)) {

								for (int i = 0; i < companyNameDisplay.size(); i++) {
									companynameStr += companyNameDisplay.get(i).toString() + "\n";
								}
								for (int i = 0; i < companyIdDisplay.size(); i++) {
									companyidStr += companyIdDisplay.get(i).toString() + "\n";
								}
								for (int i = 0; i < valueDisplay.size(); i++) {
									companyvalueStr += valueDisplay.get(i).toString() + "\n";
								}

								
								MapSort();
								inputcompanyId = outputIdStr;
								// Title detection
								if (titleContentcheck) {
									inputcompanyId = titleCompanyId;
								}
								
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
									
									// values average by company id
									getValueAverageBycompanyId(inputcompanyId, TWDate, TWDateAdd);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								lblMonth1.setText(String.valueOf(onemonthAverage));
								lblMonth2.setText(String.valueOf(twomonthAverage));
								lblMonth3.setText(String.valueOf(threemonthAverage));
								thisValuelabel.setText(currentValue);
								thisCompanylabel.setText(ouputCompanyStr);
							} else {
								lblMonth1.setText("");
								lblMonth2.setText("");
								lblMonth3.setText("");
								thisValuelabel.setText("");
								thisCompanylabel.setText("");
							}

							textPane.setText(companynameStr);
							textPane_1.setText(companyidStr);
							textPane_3.setText(companyvalueStr);
							
							// title filter
							contentFilter();
							if(!contentcheck) {
								nextButton.setEnabled(true);
							}
							
							if(content.trim().length() == 0) {
								nextButton.setEnabled(true);
							}

						} else {
							// title is not meet the pattern
							labArticleIdStr.setText(articleId);
							labArticleFileStr.setText(articleFile);
							textPane_2.setText(content);
							label_3.setText(String.valueOf(allarticleNum));
							label_4.setText(String.valueOf(indexNum));
							label_5.setText(String.valueOf(allarticleNum - indexNum));
							nextButton.setEnabled(true);

							lblMonth1.setText("");
							lblMonth2.setText("");
							lblMonth3.setText("");
							thisValuelabel.setText("");
							thisCompanylabel.setText("");
							
							TitleisNull();
						}
						indexNum++;

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
				if ("Exit".equals(e.getActionCommand())) {
					int result = JOptionPane.showConfirmDialog(frame, "確定要結束程式嗎?", "確認訊息", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
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
		// lblNewLabel_2.setVisible(false);
		frame.getContentPane().add(lblNewLabel_2);

		// article author
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(159, 62, 202, 16);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("公司名稱");
		lblNewLabel_4.setBounds(34, 167, 61, 16);
		frame.getContentPane().add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("公司編號");
		lblNewLabel_5.setBounds(172, 167, 61, 16);
		frame.getContentPane().add(lblNewLabel_5);

		// Company name
		textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(35, 195, 102, 152);
		frame.getContentPane().add(scrollPane);

		// Company ID
		textPane_1 = new JTextPane();
		JScrollPane scrollPane1 = new JScrollPane(textPane_1);
		scrollPane1.setBounds(172, 195, 102, 152);
		frame.getContentPane().add(scrollPane1);

		// article values
		textPane_3 = new JTextPane();
		JScrollPane scrollPane2 = new JScrollPane(textPane_3);
		scrollPane2.setBounds(35, 398, 102, 117);
		frame.getContentPane().add(scrollPane2);

		// Content panel
		textPane_2 = new JTextPane();
		JScrollPane scrollPaneContent = new JScrollPane(textPane_2);
		scrollPaneContent.setBounds(389, 97, 536, 330);
		frame.getContentPane().add(scrollPaneContent);

		// article title
		lblNewLabel_6 = new JLabel("Title");
		lblNewLabel_6.setBounds(389, 51, 536, 16);
		frame.getContentPane().add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel("Values");
		lblNewLabel_7.setBounds(35, 370, 61, 16);
		frame.getContentPane().add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("Real Values");
		lblNewLabel_8.setBounds(176, 370, 78, 16);
		frame.getContentPane().add(lblNewLabel_8);

		lblMonth1 = new JLabel("1 month");
		lblMonth1.setBounds(172, 458, 150, 16);
		lblMonth1.setText("");
		frame.getContentPane().add(lblMonth1);

		lblMonth2 = new JLabel("2 month");
		lblMonth2.setBounds(172, 491, 150, 16);
		lblMonth2.setText("");
		frame.getContentPane().add(lblMonth2);

		lblMonth3 = new JLabel("3 month");
		lblMonth3.setBounds(172, 519, 150, 16);
		lblMonth3.setText("");
		frame.getContentPane().add(lblMonth3);

		rdbtnNewRadioButtonPositive = new JRadioButton("Positive");
		rdbtnNewRadioButtonPositive.setText("positive");
		rdbtnNewRadioButtonPositive.setBounds(403, 469, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButtonPositive);
		rdbtnNewRadioButtonPositive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButton.setEnabled(true);
			}
		});

		rdbtnNewRadioButtonNegative = new JRadioButton("Negative");
		rdbtnNewRadioButtonNegative.setText("negative");
		rdbtnNewRadioButtonNegative.setBounds(403, 505, 141, 23);
		frame.getContentPane().add(rdbtnNewRadioButtonNegative);
		rdbtnNewRadioButtonNegative.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButton.setEnabled(true);
			}
		});

		rdbtnUndefined = new JRadioButton("undefined");
		rdbtnUndefined.setBounds(403, 540, 141, 23);
		rdbtnUndefined.setText("undefined");
		frame.getContentPane().add(rdbtnUndefined);
		rdbtnUndefined.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButton.setEnabled(true);
			}
		});

		radiogroup = new ButtonGroup();
		radiogroup.add(rdbtnNewRadioButtonPositive);
		radiogroup.add(rdbtnNewRadioButtonNegative);
		radiogroup.add(rdbtnUndefined);

		// Next article button
		nextButton = new JButton("Next");
		nextButton.setBounds(410, 608, 117, 29);
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (startArticle) {
					radiochoice = "";
					if (rdbtnNewRadioButtonPositive.isSelected()) {
						radiochoice = rdbtnNewRadioButtonPositive.getText();
						validCount++;
						label_6.setText(String.valueOf(validCount));
					} else if (rdbtnNewRadioButtonNegative.isSelected()) {
						radiochoice = rdbtnNewRadioButtonNegative.getText();
						validCount++;
						label_6.setText(String.valueOf(validCount));
					} else if (rdbtnUndefined.isSelected()) {
						radiochoice = rdbtnUndefined.getText();
					}

					if (radiochoice.trim().length() == 0) {
						radiochoice = "ignore";
					}
					
					
					companyIdTag = "";
					if (companyIdDisplay.size() == 0) {
						companyIdTag = "null";
					} else if (companyIdDisplay.size() == 1) {
						companyIdTag = "single";
					} else {
						companyIdTag = "multiple";
					}

					if (contentcheck == false) {
						radiochoice = "ignore";
					}
					
					try {
						//System.out.println((articleIndex)+"	load	"+filenameVec.get(0).toString()+"	"+articleIdVec.get(0).toString()+"	"+inputarticleId+"	"+articleAuthorVec.get(0).toString());
						manualTagging(filenameVec.get(0).toString(), articleIdVec.get(0).toString(), articleAuthorVec.get(0).toString(), radiochoice, companyIdTag);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					articleIndex++;
					startArticle = false;
					
				}else {
					Updated();
					articleIndex++;
				}
				
				Display();
			}
		});
		frame.getContentPane().add(nextButton);

		// save button
		btnSaveExit = new JButton("Save");
		btnSaveExit.setBounds(532, 608, 117, 29);
		btnSaveExit.setEnabled(false);
		btnSaveExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if ("Save".equals(e.getActionCommand())) {
						int result = JOptionPane.showConfirmDialog(frame, "確定要儲存?", "確認訊息", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (result == JOptionPane.YES_OPTION) {
							StoragedHistory(filenameVec.get(articleIndex - 1).toString(), articleIdVec.get(articleIndex - 1).toString());
						}
					}

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
		lblArticleId.setBounds(34, 117, 91, 16);
		frame.getContentPane().add(lblArticleId);

		labArticleFileStr = new JLabel("");
		labArticleFileStr.setBounds(159, 90, 202, 16);
		frame.getContentPane().add(labArticleFileStr);
		
		labArticleIdStr = new JLabel("");
		labArticleIdStr.setBounds(159, 117, 202, 16);
		frame.getContentPane().add(labArticleIdStr);

		label = new JLabel("總數");
		label.setBounds(704, 491, 78, 16);
		frame.getContentPane().add(label);

		label_1 = new JLabel("已完成");
		label_1.setBounds(704, 535, 78, 16);
		frame.getContentPane().add(label_1);

		label_2 = new JLabel("剩餘");
		label_2.setBounds(704, 575, 78, 16);
		frame.getContentPane().add(label_2);

		// article total number
		label_3 = new JLabel("");
		label_3.setBounds(794, 491, 78, 16);
		frame.getContentPane().add(label_3);

		// finished number
		label_4 = new JLabel("");
		label_4.setBounds(794, 535, 78, 16);
		frame.getContentPane().add(label_4);

		// remind number
		label_5 = new JLabel("");
		label_5.setBounds(794, 575, 78, 16);
		frame.getContentPane().add(label_5);
		
		JLabel lblArticleFile = new JLabel("article file");
		lblArticleFile.setBounds(34, 90, 91, 16);
		frame.getContentPane().add(lblArticleFile);
		
		JLabel lblValideCount = new JLabel("Valid Tagging Count");
		lblValideCount.setBounds(188, 563, 138, 16);
		frame.getContentPane().add(lblValideCount);
		
		label_6 = new JLabel("");
		label_6.setBounds(198, 591, 102, 16);
		frame.getContentPane().add(label_6);
		
		thisValuelabel = new JLabel("");
		thisValuelabel.setBounds(261, 411, 61, 16);
		frame.getContentPane().add(thisValuelabel);
		
		thisCompanylabel = new JLabel("");
		thisCompanylabel.setBounds(172, 411, 61, 16);
		frame.getContentPane().add(thisCompanylabel);
		
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
	private void companyPatternCheck(String strTmp) {
		
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
			while (matcher.find()) {
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
			}
			// Id
			regexId = companyId.get(i).toString();
			pattern = Pattern.compile(regexId, Pattern.MULTILINE);
			matcher = pattern.matcher(strTmp);
			while (matcher.find()) {
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

	private void getValueAverageBycompanyId(String companyIdStr, String dateStr, String addtwoday) throws Exception {
		
		System.out.println(indexNum+"	"+companyIdStr+"	"+dateStr+"	"+ouputCompanyStr+"	"+outputIdStr+"	"+titleCompanyId);
		
		File file = new File(Units.value_folder + companyIdStr + Units.extension);
		if (file.exists()) {
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String Line;
			String temp[];
			int dateindexTag = 0;
			int dateindextwoTag = 0;
			int index = 0;
			String valueTemp = "";
			allvalueVec.clear();
			boolean dateCheck = false;
			
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("	");
				if (dateStr.equalsIgnoreCase(temp[0])) {
					dateindexTag = index;
					valueTemp = temp[1];
					dateCheck = true;
				}
				
				if(dateCheck == false) {
					if(addtwoday.equalsIgnoreCase(temp[0])) {
						dateindextwoTag = index;
						valueTemp = temp[1];
					}
				}
				
				allvalueVec.add(temp[1]);
				index++;
			}
			bfr.close();
			
			int nextIndex;
			currentValue = valueTemp;
			//System.out.println(indexNum+"	"+companyIdStr+"	"+dateStr+"	"+currentValue);
			if(currentValue.trim().length() >0) {
				if (dateindexTag > dateindextwoTag) {
					nextIndex = dateindexTag;
					//currentDate = dateStr;
				} else {
					nextIndex = dateindextwoTag;
					//currentDate = addtwoday;
				}
				
				// values average
				onemonthAverage = monthAverage(1, nextIndex, allvalueVec);
				twomonthAverage = monthAverage(2, nextIndex, allvalueVec);
				threemonthAverage = monthAverage(3, nextIndex, allvalueVec);
			}else {
				// No data
				onemonthAverage = 0;
				twomonthAverage = 0;
				threemonthAverage = 0;
			}
		}
	}

	private double monthAverage(int monthInt, int dateIndex, Vector allvalueVec) {
		int remnants = allvalueVec.size() - dateIndex;
		double value1 = 0;
		double value2 = 0;
		double value3 = 0;
		double value = 0;
		if (monthInt == 1) {
			if (remnants < 22) {
				dateremindTag = "less than 30 days";
			} else {
				for (int i = 0; i < 5; i++) {
					value1 += Double.parseDouble(allvalueVec.get(22 + i + dateIndex).toString());
				}
				value = value1 / 5;
			}
		} else if (monthInt == 2) {
			if (remnants < 46) {
				dateremindTag = "less than 60 days";
			} else {
				for (int i = 0; i < 5; i++) {
					value2 += Double.parseDouble(allvalueVec.get(46 + i + dateIndex).toString());
				}
				value = value2 / 5;
			}
		} else if (monthInt == 3) {
			if (remnants < 66) {
				dateremindTag = "less than 90 days";
			} else {
				for (int i = 0; i < 5; i++) {
					value3 += Double.parseDouble(allvalueVec.get(66 + i + dateIndex).toString());
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

	private void ReadCompany(String pathName, String fileName) throws Exception {
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
		if (dateStr.substring(8, 9).equalsIgnoreCase(" ")) {
			newdateStr = front + dategap + last;
		} else {
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
			// return null;
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
	}

	private void TitleisNull() 
	{
		labArticleIdStr.setText(articleId);
		labArticleFileStr.setText(articleFile);
		textPane_2.setText(content);
		lblNewLabel_2.setText(date);
		lblNewLabel_3.setText(author);
		lblNewLabel_6.setText(title);
		mclabel.setText(String.valueOf(messagesCount));
	}

	private void CleanData()
	{
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
		inputcompanyId = "";
		formatdate = "";
		TWDate = "";
		onemonthAverage = 0;
		twomonthAverage = 0;
		threemonthAverage = 0;
		dateremindTag = "";
		formatdateAdd = "";
		TWDateAdd = "";
		addtwpday = "";
		titleCompany = "";
		titleCompanyId = "";
		lblMonth1.setText("");
		lblMonth2.setText("");
		lblMonth3.setText("");
		mclabel.setText("");
		thisValuelabel.setText("");
		
		outputIdStr = "";
		currentValue = "";
		ouputCompanyStr = "";
		
		titleContentcheck = false;
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
	private void manualTagging(String articleFileName, String articleId, String articleauthor, String tagging, String category) throws Exception {
		writerTagging = new FileOutputStream(Units.taggingFolder + Units.taggingName, true);

		psTagging = new PrintStream(writerTagging);
		psTagging.print(
				articleFileName + "	" + articleId + "	" + articleauthor + "	" + tagging + "	" + category + "\n");
		psTagging.close();
	}

	private boolean TitleContentDetection(String inputTitle) {
		
		boolean titleContentcheck = false;
		// titleCompany
		String regexName = "";
		String tmpName;
		String patternName;

		int indexTag;
		for (int i=0; i<companyId.size(); i++) 
		{
			patternName = "";

			// Name
			tmpName = companyName.get(i).toString().replace("-KY", "");
			tmpName = tmpName.replace("-DR", "");
			regexName = "(" + tmpName + ")+";
			pattern = Pattern.compile(regexName, Pattern.MULTILINE);
			matcher = pattern.matcher(inputTitle);
			indexTag = i;
			while(matcher.find()) {
				
				patternName = matcher.group();
				companyNameDisplay.add(patternName);
				companyIdDisplay.add(companyId.get(indexTag));
				//System.out.println(patternName+"	"+companyId.get(indexTag));
				titleContentcheck = true;
			}
			
		}
		
		double score;
		double nameScoreTmp = -1000;
		if(companyNameDisplay.size() > 1) {
			for (int i=0; i<companyNameDisplay.size(); i++) {
				score = sim(companyNameDisplay.get(i).toString(), inputTitle);
				if(score > nameScoreTmp) {
					titleCompany = companyNameDisplay.get(i).toString();
					titleCompanyId = companyIdDisplay.get(i).toString();
					nameScoreTmp = score;
				}
			}
		}else if(companyNameDisplay.size() == 1) {
			titleCompany = companyNameDisplay.get(0).toString();
			titleCompanyId = companyIdDisplay.get(0).toString();
		}
		

		return titleContentcheck;
	}

	private void validTaggingCount() throws Exception {
		
		File file = new File(Units.taggingFolder + Units.taggingName);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		//
		String tmpStr;
		if (file.exists()) {
			String Line;
			String temp[];
			while ((Line = bfr.readLine()) != null) {
				temp = Line.split("\\t");
				tmpStr = temp[3].trim();
				if(tmpStr.equalsIgnoreCase("positive")) {
					validCount++;
				}else if(tmpStr.equalsIgnoreCase("negative")) {
					validCount++;
				}
			}
		}
		bfr.close();
	}
	
	// updated
	private void Updated()
	{
		CleanData();
		
		contentcheck = false;

		try {
			GetContentByArticleId(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
			articleFile = filenameVec.get(articleIndex).toString();
			articleId = articleIdVec.get(articleIndex).toString();
			authorNameStr = articleAuthorVec.get(articleIndex).toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// Title detection
		boolean titleContentcheck;
		titleContentcheck = TitleContentDetection(title);
//		if (titleContentcheck) {
//			companyNameDisplay.add(titleCompany);
//			companyIdDisplay.add(titleCompanyId);	
//		}

		pattern = Pattern.compile(Units.regexTitle, Pattern.MULTILINE);
		matcher = pattern.matcher(title);
		if (matcher.find()) {
			// company namd & id Pattern check
			companyPatternCheck(title+" "+content);
			contentcheck = true;
			// display on board
			lblNewLabel_2.setText(date);
			lblNewLabel_3.setText(author);
			lblNewLabel_6.setEnabled(true);
			lblNewLabel_6.setText(title);
			textPane_2.setText(content);
			mclabel.setText(String.valueOf(messagesCount));
			labArticleIdStr.setText(articleId);
			labArticleFileStr.setText(articleFile);
			 
			String companynameStr = "";
			String companyidStr = "";
			String companyvalueStr = "";
			
			

			// functions
			if ((companyIdDisplay.size() > 0) || (companyNameDisplay.size() > 0)) {

				for (int i = 0; i < companyNameDisplay.size(); i++) {
					companynameStr += companyNameDisplay.get(i).toString() + "\n";
				}
				for (int i = 0; i < companyIdDisplay.size(); i++) {
					companyidStr += companyIdDisplay.get(i).toString() + "\n";
				}
				for (int i = 0; i < valueDisplay.size(); i++) {
					companyvalueStr += valueDisplay.get(i).toString() + "\n";
				}

				// Title detection
//				if (titleContentcheck) {
//					inputarticleId = titleCompanyId;
//				} else {
//					inputarticleId = companyIdDisplay.get(0).toString();
//				}
				
				MapSort();
				inputcompanyId = outputIdStr;
				if (titleContentcheck) {
					//companynameStr = titleCompany + "\n";
					//companyidStr = titleCompanyId + "\n";
					inputcompanyId = titleCompanyId;
				}
				

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

					// values average by company id
					getValueAverageBycompanyId(inputcompanyId, TWDate, TWDateAdd);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				lblMonth1.setText(String.valueOf(onemonthAverage));
				lblMonth2.setText(String.valueOf(twomonthAverage));
				lblMonth3.setText(String.valueOf(threemonthAverage));
				thisValuelabel.setText(currentValue);
				thisCompanylabel.setText(ouputCompanyStr);
			} else {
				lblMonth1.setText("");
				lblMonth2.setText("");
				lblMonth3.setText("");
				thisValuelabel.setText("");
				thisCompanylabel.setText("");
			}

			textPane.setText(companynameStr);
			textPane_1.setText(companyidStr);
			textPane_3.setText(companyvalueStr);

		} else {
			// title is not meet the pattern
			label_3.setText(String.valueOf(allarticleNum));
			label_4.setText(String.valueOf(indexNum));
			label_5.setText(String.valueOf(allarticleNum - indexNum));
			labArticleIdStr.setText(articleId);
			labArticleFileStr.setText(articleFile);
			textPane_2.setText(content);
			
			lblMonth1.setText("");
			lblMonth2.setText("");
			lblMonth3.setText("");
			thisValuelabel.setText("");
			thisCompanylabel.setText("");
			
			TitleisNull();
		}

		if(content.trim().length() == 0) {
			contentcheck = false;
		}
		
		label_4.setText(String.valueOf(indexNum));
		label_5.setText(String.valueOf(allarticleNum - indexNum));
		
		// radio group
		radiochoice = "";
		if (rdbtnNewRadioButtonPositive.isSelected()) {
			radiochoice = rdbtnNewRadioButtonPositive.getText();
			validCount++;
			label_6.setText(String.valueOf(validCount));
		} else if (rdbtnNewRadioButtonNegative.isSelected()) {
			radiochoice = rdbtnNewRadioButtonNegative.getText();
			validCount++;
			label_6.setText(String.valueOf(validCount));
		} else if (rdbtnUndefined.isSelected()) {
			radiochoice = rdbtnUndefined.getText();
		}

		if (radiochoice.trim().length() == 0) {
			radiochoice = "ignore";
		}
		companyIdTag = "";
		if (companyIdDisplay.size() == 0) {
			companyIdTag = "null";
		} else if (companyIdDisplay.size() == 1) {
			companyIdTag = "single";
		} else {
			companyIdTag = "multiple";
		}

		if (contentcheck == false) {
			radiochoice = "ignore";
		}

		// Save tagging result
//		System.out.println(articleIndex+"	"+filenameVec.get(articleIndex).toString()+"	"+articleIdVec.get(articleIndex).toString()+"	"+inputcompanyId+"	"+articleAuthorVec.get(articleIndex).toString());
		try {
			manualTagging(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString(), articleAuthorVec.get(articleIndex).toString(), radiochoice, companyIdTag);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// Remove radioButton
		radiogroup.clearSelection();
		
		if (contentcheck == false) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}

		indexNum++;
	}
	
	private void Display()
	{
		CleanData();
		
		contentcheck = false;

		try {
			GetContentByArticleId(filenameVec.get(articleIndex).toString(), articleIdVec.get(articleIndex).toString());
			articleFile = filenameVec.get(articleIndex).toString();
			articleId = articleIdVec.get(articleIndex).toString();
			authorNameStr = articleAuthorVec.get(articleIndex).toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		pattern = Pattern.compile(Units.regexTitle, Pattern.MULTILINE);
		matcher = pattern.matcher(title);
		if (matcher.find()) {
			// company name & id Pattern check
			companyPatternCheck(content);
			contentcheck = true;
			// display on board
			lblNewLabel_2.setText(date);
			lblNewLabel_3.setText(author);
			lblNewLabel_6.setEnabled(true);
			lblNewLabel_6.setText(title);
			textPane_2.setText(content);
			mclabel.setText(String.valueOf(messagesCount));
			labArticleIdStr.setText(articleId);
			labArticleFileStr.setText(articleFile);
			textPane_2.setText(content);

			String companynameStr = "";
			String companyidStr = "";
			String companyvalueStr = "";
			// Title detection
			boolean titleContentcheck;
			titleContentcheck = TitleContentDetection(title);
//			if (titleContentcheck) {
//				companynameStr = titleCompany +"\n";
//				companyidStr = titleCompanyId +"\n";
//			}

			// functions
			if ((companyIdDisplay.size() > 0) || (companyNameDisplay.size() > 0)) {

				for (int i = 0; i < companyNameDisplay.size(); i++) {
					companynameStr += companyNameDisplay.get(i).toString() + "\n";
				}
				for (int i = 0; i < companyIdDisplay.size(); i++) {
					companyidStr += companyIdDisplay.get(i).toString() + "\n";
				}
				for (int i = 0; i < valueDisplay.size(); i++) {
					companyvalueStr += valueDisplay.get(i).toString() + "\n";
				}

				
				MapSort();
				inputcompanyId = outputIdStr;
				if (titleContentcheck) {
//					companynameStr = titleCompany +"\n";
//					companyidStr = titleCompanyId +"\n";
					inputcompanyId = titleCompanyId;
				}

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
					getValueAverageBycompanyId(inputcompanyId, TWDate, TWDateAdd);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				lblMonth1.setText(String.valueOf(onemonthAverage));
				lblMonth2.setText(String.valueOf(twomonthAverage));
				lblMonth3.setText(String.valueOf(threemonthAverage));
				thisValuelabel.setText(currentValue);
				thisCompanylabel.setText(ouputCompanyStr);
			} else {
				lblMonth1.setText("");
				lblMonth2.setText("");
				lblMonth3.setText("");
				thisValuelabel.setText("");
			}

			textPane.setText(companynameStr);
			textPane_1.setText(companyidStr);
			textPane_3.setText(companyvalueStr);

		} else {
			// title is not meet the pattern
			label_3.setText(String.valueOf(allarticleNum));
			label_4.setText(String.valueOf(indexNum));
			label_5.setText(String.valueOf(allarticleNum - indexNum));
			labArticleIdStr.setText(articleId);
			labArticleFileStr.setText(articleFile);
			textPane_2.setText(content);
			
			lblMonth1.setText("");
			lblMonth2.setText("");
			lblMonth3.setText("");
			thisValuelabel.setText("");
			thisCompanylabel.setText("");
			
			TitleisNull();
		}

		// title filter
		contentFilter();
		
		if(content.trim().length() == 0) {
			contentcheck = false;
		}
		
		label_4.setText(String.valueOf(indexNum));
		label_5.setText(String.valueOf(allarticleNum - indexNum));

		// radio group
		radiochoice = "";
		if (rdbtnNewRadioButtonPositive.isSelected()) {
			radiochoice = rdbtnNewRadioButtonPositive.getText();
		} else if (rdbtnNewRadioButtonNegative.isSelected()) {
			radiochoice = rdbtnNewRadioButtonNegative.getText();
		} else if (rdbtnUndefined.isSelected()) {
			radiochoice = rdbtnUndefined.getText();
		}

		if (radiochoice.trim().length() == 0) {
			radiochoice = "ignore";
		}
		companyIdTag = "";
		if (companyIdDisplay.size() == 0) {
			companyIdTag = "null";
		} else if (companyIdDisplay.size() == 1) {
			companyIdTag = "single";
		} else {
			companyIdTag = "multiple";
		}

		if (contentcheck == false) {
			radiochoice = "ignore";
		}
	
		
		// Remove radioButton
		radiogroup.clearSelection();
		
		if (contentcheck == false) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}

	}

	private void contentFilter()
	{
		if(title.contains("盤中")) {
			contentcheck = false;
		}else if(title.contains("盤後")) {
			contentcheck = false;
		}else if(title.contains("交易統計")) {
			contentcheck = false;
		}else if(title.contains("統計表")) {
			contentcheck = false;
		}else if(title.contains("閒聊")) {
			contentcheck = false;
		}else if(title.contains("閒聊")) {
			contentcheck = false;
		}else if(title.contains("大盤")) {
			contentcheck = false;
		}else if(title.contains("加權")) {
			contentcheck = false;
		}else if(title.contains("市場")) {
			contentcheck = false;
		}else if(title.contains("警告")) {
			contentcheck = false;
		}
	}
	
	private void MapSort()
	{		
		// company name
		String indexnameStr = "";
		int indexnameValue = 0;
		ouputCompanyStr = "";
		outputIdStr = "";
		List<String> companylist = new ArrayList<String>();
		for(int i=0; i<companyNameDisplay.size(); i++) {
			companylist.add(companyNameDisplay.get(i).toString().trim());
		}

		Map<String, Integer> Nameduplicates = new HashMap<String, Integer>();
		for (String str : companylist) {
			if (Nameduplicates.containsKey(str)) {
				Nameduplicates.put(str, Nameduplicates.get(str) + 1);
			} else {
				Nameduplicates.put(str, 1);
			}
		}
		
		// Sort (company name)
		LinkedHashMap<String, Integer> sortednameMap = new LinkedHashMap<>();
		Nameduplicates.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEachOrdered(x -> sortednameMap.put(x.getKey(), x.getValue()));

		for (Map.Entry<String, Integer> nameentry : sortednameMap.entrySet()) {
			indexnameStr = nameentry.getKey();
			indexnameValue = nameentry.getValue();
		}

		// company id
		String indexidStr = "";
		int indexidValue = 0;
		List<String> idlist = new ArrayList<String>();
		for(int i=0; i<companyIdDisplay.size(); i++) {
			idlist.add(companyIdDisplay.get(i).toString().trim());
		}
		
		Map<String, Integer> idduplicates = new HashMap<String, Integer>();
		for (String str : idlist) {
			if (idduplicates.containsKey(str)) {
				idduplicates.put(str, idduplicates.get(str) + 1);
			} else {
				idduplicates.put(str, 1);
			}
		}
		// sort (company id)
		LinkedHashMap<String, Integer> sortedidMap = new LinkedHashMap<>();
		idduplicates.entrySet().stream().sorted(Map.Entry.comparingByValue())
		.forEachOrdered(x -> sortedidMap.put(x.getKey(), x.getValue()));
		
		for (Map.Entry<String, Integer> identry : sortedidMap.entrySet()) {
			indexidStr = identry.getKey();
			indexidValue = identry.getValue();
		}
		
		// final output
		if(indexnameValue > indexidValue) {
			for (int i=0; i<companyName.size(); i++)
			{
				if(indexnameStr.trim().equalsIgnoreCase(companyName.get(i).toString())) {
					ouputCompanyStr = companyName.get(i).toString();
					outputIdStr = companyId.get(i).toString();
					break;
				}
			}
		}else if(indexidValue > indexnameValue) {
			for (int i=0; i<companyId.size(); i++)
			{
				if(indexidStr.trim().equalsIgnoreCase(companyId.get(i).toString())) {
					ouputCompanyStr = companyName.get(i).toString();
					outputIdStr = companyId.get(i).toString();
					break;
				}
			}
		}else if((indexnameValue == indexidValue) && (indexnameValue>0) && (indexidValue>0)){
			ouputCompanyStr = indexnameStr;
			outputIdStr = indexidStr;
		}
		
		companyIdDisplay.clear();
		companyNameDisplay.clear();
//		System.out.println("-----------------------------------");
//		System.out.println(indexnameStr+"	"+indexidStr);
//		System.out.println(indexidValue+"	"+indexnameValue);
//		System.out.println("output company	"+ouputCompanyStr);
//		System.out.println("output id	"+outputIdStr);
	}

	private static double sim(String str1, String str2) 
	{
		try {
			double ld = (double)ld(str1, str2);
			return (1-ld/(double)Math.max(str1.length(), str2.length()));
		} catch (Exception e) {
			return 0.1;
		}
	}

	public static int ld(String str1, String str2) 
	{
		int d[][]; 
		int n = str1.length();
		int m = str2.length();
		int i; 
		int j; 
		char ch1; 
		char ch2; 
		int temp; 
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { 
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) { 
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) { 
			ch1 = str1.charAt(i - 1);
			
			for (j = 1; j <= m; j++) 
			{
				ch2 = str2.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]+ temp);
			}
		}
		
		return d[n][m];
	}

	private static int min(int one, int two, int three) 
	{
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		
		return min;
	}
	
}

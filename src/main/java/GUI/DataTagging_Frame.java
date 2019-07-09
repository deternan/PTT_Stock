package GUI;

/*
 * Data tagging GUI
 * version: July 08, 2019 07:40 PM
 * Last revision: July 10, 2019 00:19 AM
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

public class DataTagging_Frame {

	private JFrame frame;

	private boolean filestartPoint = false;
	private boolean startPoint = false;
	private Vector filenameVec = new Vector();
	private Vector articleIdVec = new Vector();
	// Parameters
	 	// read history index
		private String fileName_index = "";
		private String artileID_index = "";
		// Article
		private String date;
		private String author;
		
	
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
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 594, 22);
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
				    // file dialog
					File selectedFile = fileChooser.getSelectedFile();
				    // function
					try {
						ReadHistory rh = new ReadHistory(selectedFile.getAbsolutePath());
						//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
						fileName_index = rh.returnfileName();
						artileID_index = rh.returnartileID();
						System.out.println(fileName_index+"	"+artileID_index);
						
						ReadAllArticles(fileName_index, artileID_index);
						
						System.out.println(filenameVec.size()+"	"+articleIdVec.size());
						
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
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(139, 51, 120, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		lblNewLabel_3.setBounds(139, 86, 61, 16);
		frame.getContentPane().add(lblNewLabel_3);
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
	
}

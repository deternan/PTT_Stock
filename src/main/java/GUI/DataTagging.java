package GUI;

/*
 * Data tagging GUI
 * version: July 08, 2019 07:40 PM
 * Last revision: July 09, 2019 06:59 AM
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
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import GUI.function.ReadHistory;

import javax.swing.JMenu;

public class DataTagging {

	private JFrame frame;

	// Parameters
	 	// read history index
		private String fileName_index = "";
		private String artileID_index = "";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataTagging window = new DataTagging();
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
	public DataTagging() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
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
						//System.out.println(fileName_index+"	"+artileID_index);
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
	}
	
	
}

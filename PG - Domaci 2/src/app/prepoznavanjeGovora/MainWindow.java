package app.prepoznavanjeGovora;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabelStyle;
import com.alee.laf.panel.WebPanelStyle;
import com.alee.laf.tabbedpane.WebTabbedPaneStyle;
import com.alee.laf.toolbar.WebToolBarStyle;
import com.alee.managers.CoreManagers;

public class MainWindow {

	public JFrame frame;
	private JComboBox<String> ExistingWordsBox;
	private JDesktopPane desktopPane;
	private LoadModels loadModels;
	
	/**
	 * Create the application.
	 */
	public MainWindow(LoadModels loadModels) {
		//this.loadModels = loadModels;
		installTheme();
		//initialize();
	}

	private void installTheme() {
		WebLookAndFeel.install ();
		CoreManagers.initialize ();
		
		WebPanelStyle.backgroundColor = new Color(0,196,255);	
		WebTabbedPaneStyle.bottomBg = new Color(190,242,255);
		WebTabbedPaneStyle.selectedTopBg = new Color(0,196,255);
		WebTabbedPaneStyle.selectedBottomBg = new Color(0,196,255);
		WebTabbedPaneStyle.contentBorderColor = new Color(0,196,255);
		WebLabelStyle.backgroundColor = new Color(0,0,0);
		WebToolBarStyle.topBgColor = new Color(190,242,255);
		WebToolBarStyle.topBgColor = new Color(0,196,255);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Prepoznavanje govora");
		frame.setResizable(false);
		frame.setBounds(100, 100, 440, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(312, 227, 112, 25);
		frame.getContentPane().add(btnPlay);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.WHITE);
		desktopPane.setBounds(10, 10, 415, 200);
		frame.getContentPane().add(desktopPane);
		
		JButton btnAddNewWord = new JButton("Add New Word");
		btnAddNewWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAddNewWord.setBounds(10, 227, 112, 25);
		frame.getContentPane().add(btnAddNewWord);
		
		ExistingWordsBox = new JComboBox<String>();
		ExistingWordsBox.setToolTipText("Existing words");
		ExistingWordsBox.setMaximumRowCount(10);
		ExistingWordsBox.setBounds(146, 227, 142, 25);
		ExistingWordsBox.addItem("Papak");
		ExistingWordsBox.addItem("Pipak");
		frame.getContentPane().add(ExistingWordsBox);
	}

	public JComboBox<String> getExistingWordsBox() {
		return ExistingWordsBox;
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}
	
}

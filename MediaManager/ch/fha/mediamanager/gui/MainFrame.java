//$Id: MainFrame.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.prefs.*;

import ch.fha.mediamanager.gui.components.*;
import ch.fha.mediamanager.gui.components.menu.*;

public class MainFrame extends JFrame implements
	ActionOccurredListener, 
	WindowListener,
	PrefsOwnerListener
{
	private StandartToolBar standartToolBar;
	private ActionListener mainActionListener = new ActionHandler(this);
	private JButton jb1, jb2;	
	
	// default settings
	public static final int defaultWindowWidth = 400;
	public static final int defaultWindowHeight = 300;
	public static final int defaultScreenMode = JFrame.NORMAL;
	
	// information loaded from the package/manifest
	private static final String apptitle = "MediaManager";

	// keywords that name the settings
	private static final String WINDOW_WIDTH = "windowWidth";
	private static final String WINDOW_HEIGHT = "windowHeight";
	private static final String SCREEN_MODE = "screenMode";
	
	MainFrame() {
		Mediamanager.addPrefsOwner(this);
		addWindowListener(this);
		Preferences prefs = Mediamanager.getPrefs();
		
		// Load window preferences
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(apptitle);
		setSize(
				prefs.getInt(WINDOW_WIDTH, defaultWindowWidth),
				prefs.getInt(WINDOW_HEIGHT, defaultWindowHeight));

		// Menu-Bar
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new FileMenu(mainActionListener);
		menuBar.add(fileMenu);

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		standartToolBar = new StandartToolBar(this, mainActionListener);
		cp.add(standartToolBar, BorderLayout.NORTH);
		
		JPanel modPanelHolder = new JPanel(new BorderLayout());
		JPanel modPanel = new JPanel(new GridLayout(2, 1));
		
		jb1 = new JButton("Test1");
		jb2 = new JButton("Exit");
		jb2.setActionCommand("exit");
		jb1.addActionListener(mainActionListener);
		jb2.addActionListener(mainActionListener);
		modPanel.add(jb1);
		modPanel.add(jb2);
		modPanelHolder.add(modPanel, BorderLayout.NORTH);
		cp.add(modPanelHolder, BorderLayout.WEST);
		
		cp.add(new MainView(), BorderLayout.CENTER);
		show();
		setExtendedState(
				prefs.getInt(SCREEN_MODE, defaultScreenMode));
	}
	
	public void display(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			this,
			e.getMessage(),
			"Fehler",
			JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Saves the current settings.
	 */
	public void savePrefs() {
		System.out.println("Saving MainFrame settings!");
		Preferences prefs = Mediamanager.getPrefs();
		Dimension windowSize = getSize();
		int windowWidth = windowSize.width;
		int windowHeight = windowSize.height;
		int screenMode = getExtendedState(); 
		
		try {
			prefs.putInt(WINDOW_WIDTH, windowWidth);
			prefs.putInt(WINDOW_HEIGHT, windowHeight);
			prefs.putInt(SCREEN_MODE, screenMode);
			prefs.flush();
		} catch(BackingStoreException e) {
			display(e);
		}
	}

	public void runAction() {
		System.out.println("runAction performed in MainFrame");
	}
	
	public void windowClosing(WindowEvent arg0) {
		try {
			Mediamanager.fireSave();
		} catch(Exception e) {
			display(e);
		}
	}

	public void windowOpened(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
}

//$Id: MainFrame.java,v 1.2 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.prefs.*;

import ch.fha.mediamanager.gui.components.*;
import ch.fha.mediamanager.gui.components.menu.*;
import ch.fha.mediamanager.gui.framework.*;

/**
 * The main view of the <em>SpellCheck</em> application.
 *
 * @author Roman Rietmann
 */
public class MainFrame extends JFrame implements
	KeyPointListener, 
	Savable
{
	private StandartToolBar standartToolBar;
	private JPanel mainTabPanel;
	private JPanel mainConfigPanel;
	private JPanel mainPanelHolder;
	private StatePanel statePanel;
	private ActionHandler mainActionListener = new ActionHandler();
	private JMenuBar menuBar;
	
	// Singleton reference
	private static MainFrame instance = null;
	
	// Default settings
	private static final int defaultWindowWidth = 400;
	private static final int defaultWindowHeight = 300;
	private static final int defaultScreenMode = JFrame.NORMAL;
	
	// Default graphic objects
	public static final Border b = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	public static final JPanel ep = new JPanel(new BorderLayout());
	public static final Font titleFont = new Font("arial", Font.BOLD, 20);
	
	// Information loaded from the package/manifest
	private static final String apptitle = "MediaManager";

	// Keywords that name the settings
	private static final String WINDOW_WIDTH = "windowWidth";
	private static final String WINDOW_HEIGHT = "windowHeight";
	private static final String SCREEN_MODE = "screenMode";
	
	// Private to prevent instantiation.
	private MainFrame() {}
	
	/**
	 * Initialisation of the basic compomponents and the window structure
	 */
	private void init() {
		Mediamanager.addSavable(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myWindowClosing(e);
			}
		});
		Preferences prefs = Mediamanager.getPrefs();

		// ---> test
		mainActionListener.addActionListener(this, new KeyPointEvent(KeyPointEvent.WINDOW_EXIT, "test successfull at MainFrame::init"));
		
		// Main Panel
		mainTabPanel = new MainTabPanel();
		
		// Config Panel
		mainConfigPanel = new MainConfigPanel();
		
		// Load window preferences
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(apptitle);
		setSize(
				prefs.getInt(WINDOW_WIDTH, defaultWindowWidth),
				prefs.getInt(WINDOW_HEIGHT, defaultWindowHeight));
		Image icon = Toolkit.getDefaultToolkit().getImage("images/Icon.jpg");
		setIconImage(icon);
		
		// Menu-Bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new FileMenu(mainActionListener);
		menuBar.add(fileMenu);

		// Panels
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		mainPanelHolder = new JPanel(new BorderLayout());
		JPanel windowHolder = new JPanel(new BorderLayout());

		standartToolBar = new StandartToolBar();
		cp.add(standartToolBar, BorderLayout.NORTH);
		
		JPanel modificationPanel = new ModificationPanel(this);
		windowHolder.add(modificationPanel, BorderLayout.WEST);
		
		mainPanelHolder.add(mainTabPanel, BorderLayout.CENTER);
		mainPanelHolder.setBorder(MainFrame.b);
		windowHolder.add(mainPanelHolder, BorderLayout.CENTER);

		statePanel = new StatePanel();
		windowHolder.add(statePanel, BorderLayout.SOUTH);
		
		cp.add(windowHolder, BorderLayout.CENTER);
		
		setVisible(true);
		setExtendedState(prefs.getInt(SCREEN_MODE, defaultScreenMode));
	}
	
	/** 
	 * Returns a reference to the global <code>MainFrame</code> object.
	 *
	 * All methods must use this method instead of directly
	 * accessing the instance reference!
	 *
	 * @return the application's top-level window
	 */
	public static MainFrame getInstance() {
		if(instance == null) {
			instance = new MainFrame();
			instance.init();
		}
		return instance;
	}
	
	/** 
	 * Returns the <code>mainActionListener</code> object, which handles
	 * the basic operations. (e.g. Exit)
	 *
	 * @return mainActionListener
	 */
	public ActionListener getMainActionListener() {
		return mainActionListener;
	}
	
	/**
	 * Loads the <code>panel</code> object into the main panel.
	 *
	 * @param panel which should be loaded
	 */
	public void loadMainPanel(JPanel panel) {
		mainPanelHolder.removeAll();
		mainPanelHolder.add(panel, BorderLayout.CENTER);
		repaint();
		setVisible(true);
	}
	
	/** Loads the standart <code>mainTabPanel</code> */
	public void loadStdPanel() { loadMainPanel(mainTabPanel); }
	/** Loads the standart <code>mainConfigPanel</code> */
	public void loadConfigPanel() { loadMainPanel(mainConfigPanel); }
	
	/**
	 * Sets the status-text in the <code>statePanel</code> to <code>s</code>.
	 * The <code>timeOut</code> defines whether the status-text disapears
	 * after a certain time (true) or never (false).
	 *
	 * @param s is the string which should be shown in the status-bar
	 * @param timeOut defines whether the status-text disapears
	 *        automaticly or not.
	 */
	public void setStatus(String s, boolean timeOut) {
		statePanel.setStatus(s, timeOut);
	}
	
	/**
	 * Removes the status-text from the <code>statePanel</code>.
	 * Used to remove permanent showed text.
	 */
	public void removeStatus() {
		setStatus("", false);
	}

	/**
	 * TODO Description 
	 */
	public void display(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			this,
			e.getMessage(),
			"Fehler",
			JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * TODO Description 
	 */
	public int askUser(String message) {
		return JOptionPane.showConfirmDialog(
				this,
				message,
				"Nachticht",
				JOptionPane.OK_OPTION);
	}
	
	/**
	 * Saves the current settings.
	 * 
	 * @param prefs the object where the local settings should be stored
	 */
	public void savePrefs(Preferences prefs) {
		System.out.println("Saving MainFrame settings!");
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

	/**
	 * TODO Description 
	 */
	public void runAction(KeyPointEvent e) {
		System.out.println("runAction performed in MainFrame:");
		System.out.println("  --> Parameter: " + e.getParameter());
	}
	
	/**
	 * Triggers the method <code>fireSave</code> which calls all registred
	 * <code>Savable</code> classes to store those local settings.
	 * 
	 * @param arg0 WindowEvent
	 */
	public void myWindowClosing(WindowEvent arg0) {
		if(askUser("Wollen Sie wirklich beenden?") == JOptionPane.YES_OPTION) {
			mainActionListener.fireAction(KeyPointEvent.WINDOW_EXIT);
			try {
				Mediamanager.fireSave();
			} catch(Exception e) {
				display(e);
			}
			System.exit(0);
		}
	}
}

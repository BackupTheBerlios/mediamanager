//$Id: MainFrame.java,v 1.18 2004/06/29 13:15:21 radisli Exp $
package ch.fha.mediamanager.gui;

import java.awt.*;
import java.awt.Container;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.prefs.*;

import ch.fha.pluginstruct.*;
import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.gui.components.*;
import ch.fha.mediamanager.gui.framework.*;

/**
 * The main view of the <em>SpellCheck</em> application.
 *
 * @author Roman Rietmann
 */
public class MainFrame extends JFrame implements
	KeyPointListener,
	Savable,
	InOut
{
	private StandartToolBar standartToolBar;
	private MainTabPanel mainTabPanel;
	private JPanel mainConfigPanel;
	private JPanel aboutPanel = null;
	private JPanel mainPanelHolder;
	private StatePanel statePanel;
	private ActionHandler mainActionListener = new ActionHandler();
	private MainMenuBar menuBar;
	private JPanel coverPanel = null;
	
	// Singleton reference
	private static MainFrame instance = null;
	
	// Windows specifications
	private static int windowWidth;
	private static int windowHeight;
	private static int windowXPos;
	private static int windowYPos;
	
	// Default settings
	private static final int defaultWindowWidth = 400;
	private static final int defaultWindowHeight = 300;
	private static final int defaultWindowXPos = 0;
	private static final int defaultWindowYPos = 0;
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
	private static final String WINDOW_X_POS = "windowXPos";
	private static final String WINDOW_Y_POS = "windowYPos";
	private static final String SCREEN_MODE = "screenMode";
	
	// Private to prevent instantiation.
	private MainFrame() {}
	
	/**
	 * Initialisation of the basic compomponents and the window structure
	 */
	private void init() {
		MediaManager.addSavable(this);
		Preferences prefs = MediaManager.getPrefs();

		// Main Panel
		mainTabPanel = new MainTabPanel();
		
		// Config Panel
		mainConfigPanel = new MainConfigPanel();
		
		// Load window preferences
		windowWidth = prefs.getInt(WINDOW_WIDTH, defaultWindowWidth);
		windowHeight = prefs.getInt(WINDOW_HEIGHT, defaultWindowHeight);
		windowXPos = prefs.getInt(WINDOW_X_POS, defaultWindowXPos);
		windowYPos = prefs.getInt(WINDOW_Y_POS, defaultWindowYPos);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(apptitle);
		Image icon = Toolkit.getDefaultToolkit().getImage("images/icon.gif");
		setIconImage(icon);
		
		// Menu-Bar
		menuBar = new MainMenuBar();
		setJMenuBar(menuBar);

		// Panels
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		mainPanelHolder = new JPanel(new BorderLayout());
		JPanel windowHolder = new JPanel(new BorderLayout());

		// Tool-Bar
		standartToolBar = new StandartToolBar();
		cp.add(standartToolBar, BorderLayout.NORTH);
		
		JPanel modificationPanel = new ModificationPanel();
		windowHolder.add(modificationPanel, BorderLayout.WEST);
		
		mainPanelHolder.add(mainTabPanel, BorderLayout.CENTER);
		mainPanelHolder.setBorder(MainFrame.b);
		windowHolder.add(mainPanelHolder, BorderLayout.CENTER);

		statePanel = new StatePanel();
		windowHolder.add(statePanel, BorderLayout.SOUTH);
		
		cp.add(windowHolder, BorderLayout.CENTER);

		pack();
		setSize(windowWidth, windowHeight);
		setLocation(windowXPos, windowYPos);
		setExtendedState(prefs.getInt(SCREEN_MODE, defaultScreenMode));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myWindowClosing(e);
			}
		});
		mainActionListener.addActionListener(this);
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
	public ActionHandler getMainActionListener() {
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
	
	/**
	 * Covers the showed panel by the <code>panel</code>. The old one will be saved
	 * and can be restored by the <code>removeCoverPanel</code> method.
	 *
	 * @param panel which should cover the active panel
	 */
	public void loadCoverPanel(JPanel panel) {
		coverPanel = (JPanel)mainPanelHolder.getComponent(0);
		loadMainPanel(panel);
	}
	
	/**
	 * Restores the panel which was covered by the <code>loadCoverPanel</code> method
	 */
	public void removeCoverPanel() {
		if(coverPanel != null) {
			loadMainPanel(coverPanel);
			coverPanel = null;
		}
	}
	
	/** Loads the standart <code>mainTabPanel</code> */
	public void loadStdPanel() {
		mainTabPanel.showMainTab();
		loadMainPanel(mainTabPanel);
	}
	/** Loads the standart <code>mainConfigPanel</code> */
	public void loadConfigPanel() { loadMainPanel(mainConfigPanel); }
	/** Loads the standart <code>aboutConfigPanel</code> */
	public void loadAboutPanel() {
		if(aboutPanel == null) {
			aboutPanel = new AboutPanel();
		}
		loadMainPanel(aboutPanel);
	}
	
	/**
	 * Sets the status-text in the <code>statePanel</code> to <code>s</code>.
	 * The <code>timeOut</code> defines whether the status-text disapears
	 * after a certain time (true) or never (false).
	 *
	 * @param s is the string which should be shown in the status-bar
	 * @param timeOut defines whether the status-text disapears
	 *        automaticly or not.
	 */
	public void setStatusText(String s, boolean timeOut) {
		statePanel.setStatusText(s, timeOut);
	}
	
	/**
	 * Removes the status-text from the <code>statePanel</code>.
	 * Used to remove permanent showed text.
	 */
	public void removeStatusText() {
		setStatusText("", false);
	}
	
	/**
	 * 
	 */
	public void connect() {
		setStatusText("Wird verbunden ...", false);
		mainActionListener.fireAction(KeyPointEvent.PRE_CONNECT);
		new Thread(new Runnable() {
			public void run() {
				mainActionListener.fireAction(KeyPointEvent.CONNECT);
			}
		}).start();
	}

	/**
     * 
	 */
	public void disconnect() {
		setStatusText("Wird getrennt ...", false);
		mainActionListener.fireAction(KeyPointEvent.PRE_DISCONNECT);
		new Thread(new Runnable() {
			public void run() {
				mainActionListener.fireAction(KeyPointEvent.DISCONNECT);
			}
		}).start();
	}
	
	/**
	 * Gets the connection status
	 * 
	 * @return Connection status 
	 */
	public boolean getConnectionStatus() {
		return statePanel.getConnectionStatus();
	}

	/**
	 * Saves the current settings.
	 * 
	 * @param prefs the object where the local settings should be stored
	 */
	public void savePrefs(Preferences prefs) {
		int screenMode = getExtendedState();
		setExtendedState(JFrame.NORMAL);
		Dimension windowSize = getSize();
		windowWidth = windowSize.width;
		windowHeight = windowSize.height;
		windowXPos = this.getX();
		windowYPos = this.getY();
		setVisible(false);
 		
		try {
			prefs.putInt(WINDOW_WIDTH, windowWidth);
			prefs.putInt(WINDOW_HEIGHT, windowHeight);
			prefs.putInt(WINDOW_X_POS, windowXPos);
			prefs.putInt(WINDOW_Y_POS, windowYPos);
			prefs.putInt(SCREEN_MODE, screenMode);
			prefs.flush();
		} catch(BackingStoreException e) {
			exception(e);
		}
	}
	
	/**
	 * Triggers the method <code>fireSave</code> which calls all registred
	 * <code>Savable</code> classes to store those local settings.
	 * 
	 * @param arg0 WindowEvent
	 */
	public void myWindowClosing(WindowEvent arg0) {
		String[] options = {"Ja", "Nein"};
		if(option("Wollen Sie wirklich beenden?", options) == 0) {
			mainActionListener.fireAction(KeyPointEvent.WINDOW_EXIT);
			try {
				MediaManager.fireSave();
			} catch(Exception e) {
				exception(e);
			}
			System.exit(0);
		}
	}

	/**
	 * Shows the <code>message</code> in a message dialog
	 * 
	 * @param message the message wich will be shown
	 */	
	public void message(String message) {
		JOptionPane.showMessageDialog(
			this,
			message, 
			"Hinweis",
			JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Shows a <code>showOptionDialog</code> which displays a
	 * <code>message</code> and must be answered by one of the
	 * <code>options</code> choices.
	 * 
	 * @param message the message which will be displayed
	 * @param options the choices which will be given
	 */
	public int option(String message, String[] options) {
		return JOptionPane.showOptionDialog(
			this,
			message,
			"Nachricht",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null, 
			options,
			options[0]);
	}

	/**
	 * Used due compatibility with <code>InOut</code> interface
	 * 
	 * @param e <code>PluginLogicException</code>
	 */
	public void exception(PluginLogicException e) {
		exception((Exception)e);
	}

	/**
	 * Shows the <code>e</code> exception in a message dialog
	 * 
	 * @param e is the exeption which should be displayed
	 */
	public void exception(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			this,
			e.getMessage(),
			"Fehler",
			JOptionPane.ERROR_MESSAGE);
	}

	public void runAction(KeyPointEvent e) {
		int kpe = e.getKeyPointEvent();
		if(kpe == KeyPointEvent.POST_CONNECT) {
			statePanel.setConnectionStatus(true);
			setStatusText("Verbunden!", true);
		} else if(kpe == KeyPointEvent.CONNECT) {
			DataBus.connect();
			mainTabPanel.connect();
		} else if(kpe == KeyPointEvent.DISCONNECT_ERROR ||
				kpe == KeyPointEvent.CONNECT_ERROR) {
			removeStatusText();
		} else if(kpe == KeyPointEvent.POST_DISCONNECT) {
			statePanel.setConnectionStatus(false);
			setStatusText("Getrennt!", true);
		} else if(kpe == KeyPointEvent.DISCONNECT) {
			DataBus.disconnect();
			mainTabPanel.disconnect();
		} else if(kpe == KeyPointEvent.PRE_DISCONNECT) {
			loadStdPanel();
		}
	}
}

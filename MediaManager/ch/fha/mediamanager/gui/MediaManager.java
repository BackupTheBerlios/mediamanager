package ch.fha.mediamanager.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.gui.framework.Savable;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Version;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.theme.SkyBluer;

/**
 * Mediamanager application
 *
 * @author Roman Rietmann
 * @version $Id: MediaManager.java,v 1.4 2004/07/04 15:19:04 crac Exp $
 */
public class MediaManager {
	/* Preferences */
	private static Preferences prefs = Preferences.userNodeForPackage(MediaManager.class);
	/* Preferences owner */
	private static LinkedList prefsOwner = new LinkedList();
	/* Declaration of all possible plugin events in the application */
	private final static String[] PLUGIN_EVENTS = {
			"prenew",
			"postnew",
			"preedit",
			"postedit",
			"predelete",
			"postdelete",
			"preinsert",
			"postinsert",
			"preupdate",
			"postupdate",
			"norow",
			"singlerow",
			"multirow"
	};
	/* current application version, used for plugin manager */
	private final static Version APPL_VERSION = new Version("1_0");
	
	

	/**
	 * Creates a <code>MainFrame</code> and lunches 
     * the application.
	 */
	public static void main(String[] args) {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		Splash splash = new Splash();
		
		// Repository
		splash.setProcess(0, "lade Repository ...");
		DataBus.initialize();
		
		// TESTING
		//DataBus.connect();
		
		// PluginManager
		splash.setProcess(0.15, "lade Plugin Manager ...");
		configurePluginManager();
		
		// Look & Feel
		splash.setProcess(0.45, "lade Look & Feel ...");
		configureUI();
		
		// Gui
		splash.setProcess(0.7, "lade graphische Umgebung ...");
		MainFrame mainFrame = MainFrame.getInstance();
		PluginManager.getInstance().setInOut(mainFrame);
		
		// waiting ...
		splash.setProcess(1, "");
		try { Thread.sleep(50); } catch (Exception e) {}
		splash.dispose();
		
		mainFrame.setVisible(true);
		mainFrame.requestFocus();
	}

	/**
	 * Adds a <code>Savable</code> components to the <code>prefsOwner</code>
	 * list which will be iterated on Program shutsown to save all
	 * preferences
	 * 
	 * @param listener is the class which will be added
	 */
	public static void addSavable(Savable listener){
		prefsOwner.add(listener);
	}

	/**
	 * Removes a <code>Savable</code> components from the <code>prefsOwner</code>
	 * list which will be iterated on Program shutsown to save all preferences
	 * 
	 * @param listener is the class which will be removed
	 */
	public static void removeSavable(Savable listener){
		int figureIndex = prefsOwner.indexOf((Object)listener);
		prefsOwner.remove(figureIndex);
	}
	
	/**
	 * Calls the <code>savePrefs</code> method in all registred
	 * <code>Savable</code> components in the <code>prefsOwner</code>
	 */
	public static void fireSave() {
		System.out.println("fireSave performed in Mediamanager");
		Iterator it = prefsOwner.iterator();
		while(it.hasNext()) {
			((Savable)it.next()).savePrefs(prefs);
		}
	}
	
	/**
	 * Returns the <code>prefs</code> object
	 * 
	 * @return prefs which contains all settings
	 */
	public static Preferences getPrefs() {
		return prefs;
	}
	
    /**
     * Configures the UI.
     * Tries to set the system look on Mac,
     * <code>ExtWindowsLookAndFeel</code> on general Windows, and
     * <code>Plastic3DLookAndFeel</code> on Windows XP and all other OS.

     * The JGoodies Swing Suite's <code>ApplicationStarter</code>,
     * <code>ExtUIManager</code>, and <code>LookChoiceStrategies</code>
     * classes provide a much more fine grained algorithm to choose and
     * restore a look and theme.
     */
    private static void configureUI() {
        UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        Options.setGlobalFontSizeHints(FontSizeHints.MIXED);
        Options.setDefaultIconSize(new Dimension(18, 18));

        /*String lafName =
            LookUtils.isWindowsXP()
                ? Options.getCrossPlatformLookAndFeelClassName()
                : Options.getSystemLookAndFeelClassName();*/
                
        LookAndFeel plastic = new Plastic3DLookAndFeel();
        PlasticLookAndFeel.setMyCurrentTheme(new SkyBluer());

        try {
            UIManager.setLookAndFeel(plastic);
        } catch (Exception e) {
            System.err.println("Can't set look & feel:" + e);
        }
    }
    
    /**
     * Initializes and configures the Plugin Manager.
     */
    private static void configurePluginManager() {
    	PluginManager manager = PluginManager.getInstance(
				PLUGIN_EVENTS, APPL_VERSION);
		manager.initialize();
    }
		
}

// $Id: Mediamanager.java,v 1.5 2004/06/16 09:03:51 ia02vond Exp $
package ch.fha.mediamanager.gui;

import javax.swing.*;
import java.awt.Dimension;
import java.util.*;
import java.util.prefs.*;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.theme.SkyBluer;

import ch.fha.mediamanager.gui.framework.*;

/**
 * Mediamanager application
 *
 * @author Roman Rietmann
 */
public class Mediamanager {
	/** Preferences */
	private static Preferences prefs = Preferences.userNodeForPackage(Mediamanager.class);
	/** Preferences owner */
	private static LinkedList prefsOwner = new LinkedList();

	/**
	 * Main method creates a <code>MainFrame</code>
	 */
	public static void main(String[] args) {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		configureUI();
		MainFrame.getInstance();
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
}

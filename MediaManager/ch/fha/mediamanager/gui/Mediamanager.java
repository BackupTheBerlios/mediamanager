// $Id: Mediamanager.java,v 1.3 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui;

import javax.swing.*;
import java.util.*;
import java.util.prefs.*;

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
		JFrame.setDefaultLookAndFeelDecorated(true);
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
}

// $Id: Mediamanager.java,v 1.2 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui;

// import java.io.*;
import javax.swing.*;
import java.util.*;
import java.util.prefs.*;

/**
 * Mediamanager application
 *
 * @author Roman Rietmann
 */
public class Mediamanager {
	private static Preferences prefs = Preferences.userNodeForPackage(Mediamanager.class);
	private static LinkedList prefsOwner = new LinkedList();
	
	private static final String NOTSAVED = "notSaved";

	/**
	 *  Private to prevent instantiation.
	 */
	private Mediamanager() {
		Package p = getClass().getPackage();
	}

	public static void main(String[] args) {
		JFrame mainFrame = new MainFrame(); 
	}

	public static void addPrefsOwner(PrefsOwnerListener listener){
		prefsOwner.add(listener);
	}

	public static void removePrefsOwnerListener(PrefsOwnerListener listener){
		int figureIndex = prefsOwner.indexOf((Object)listener);
		prefsOwner.remove(figureIndex);
	}
	
	public static void fireSave() {
		System.out.println("fireSave performed in Mediamanager");
		Iterator it = prefsOwner.iterator();
		while(it.hasNext()) {
			((PrefsOwnerListener)it.next()).savePrefs();
		}
	}
	
	public static Preferences getPrefs() {
		return prefs;
	}
}

//$Id: Savable.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.framework;

import java.util.prefs.*;

/**
 * This interface is used by components which have some properties to
 * save.
 * 
 * @author Roman Rietmann
 */
public interface Savable {
	/**
	 * The method <code>savePrefs</code> is called automaticly as soon
	 * as the window is closing.
	 * 
	 * @param prefs is used to store the local settings
	 */
	void savePrefs(Preferences prefs);
}

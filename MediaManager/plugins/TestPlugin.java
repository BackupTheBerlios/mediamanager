package plugins;


import ch.fha.mediamanager.plugin.MMPlugin;
import ch.fha.mediamanager.plugin.MMPluginEvent;


/**
 * @author ia02vond
 * @version $Id: TestPlugin.java,v 1.4 2004/06/28 12:00:31 ia02vond Exp $
 */
public class TestPlugin extends MMPlugin {

	public boolean run(MMPluginEvent event) {
		System.out.println ("Hallo, ich bin ein Plugin.");
		return true;
	}

	public void addPropertie(String key, String[] values) {
		/*
		System.out.println ("new prop:");
		for (int i=0; i<values.length; i++) {
			System.out.println ("  " + key + ": " + values[i]);
		}
		*/
	}
}

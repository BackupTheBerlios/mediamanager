package plugins;


import ch.fha.mediamanager.plugin.MMPlugin;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;


/**
 * @author ia02vond
 * @version $Id: TestPlugin.java,v 1.3 2004/06/23 11:32:14 ia02vond Exp $
 */
public class TestPlugin extends MMPlugin {

	public void run(MMPluginEvent event) throws OperationCancelException {
		System.out.println ("Hallo, ich bin ein Plugin.");
		finish();
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

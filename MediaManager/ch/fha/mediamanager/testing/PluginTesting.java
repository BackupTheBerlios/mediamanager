package ch.fha.mediamanager.testing;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Version;


/**
 * @author ia02vond
 * @version $Id: PluginTesting.java,v 1.1 2004/05/13 12:18:26 ia02vond Exp $
 */
public class PluginTesting {
	
	public static void main(String[] args) {
		System.out.println("start");
		String[] events = {
				"applicationstart",
				"applicationexit",
				"preinitialize",
				"postinitialize",
				"prevalidate",
				"postvalidate",
				"presave",
				"postsave",
				"predelete",
				"postdelete",
				"independent",
				"singlerow",
				"multirow"};
		PluginManager.setSystemVersion(new Version("1_0"));
		PluginManager.setEventList(events);
		PluginManager.initialize();
		
		try {
			PluginManager.fireEvent(new MMPluginEvent(), "postvalidate", "movies");
		} catch (OperationCancelException e) {
			System.out.println("operation canceled");
		}
	}
}

package ch.fha.mediamanager.testing;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Version;


/**
 * @author ia02vond
 * @version $Id: PluginManagerTesting.java,v 1.1 2004/05/18 07:42:25 ia02vond Exp $
 */
public class PluginManagerTesting {
	
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
		PluginManager manager = PluginManager.getInstance(
				events, new Version("1_0"));
		manager.initialize();
		
		try {
			manager.fireEvent(new MMPluginEvent(), "postvalidate", "movies");
		} catch (OperationCancelException e) {
			System.out.println("operation canceled");
		}
	}
}

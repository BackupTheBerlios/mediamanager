package ch.fha.mediamanager.plugin;

import ch.fha.pluginstruct.*;

/**
 * @author ia02vond
 * @version $Id: MMPlugin.java,v 1.1 2004/05/13 12:10:08 ia02vond Exp $
 */
public abstract class MMPlugin extends AbstractPlugin {
	
	public abstract void run(MMPluginEvent event)
		throws OperationCancelException;
	
	public void run(PluginEvent event) throws OperationCancelException {
		run( (MMPluginEvent)event );
	}
	
}

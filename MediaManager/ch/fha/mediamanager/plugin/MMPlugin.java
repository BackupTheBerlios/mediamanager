package ch.fha.mediamanager.plugin;

import ch.fha.pluginstruct.*;

/**
 * @author ia02vond
 * @version $Id: MMPlugin.java,v 1.2 2004/06/28 11:24:45 ia02vond Exp $
 */
public abstract class MMPlugin extends AbstractPlugin {
	
	public abstract boolean run(MMPluginEvent event);
	
	public boolean run(PluginEvent event) {
		return run( (MMPluginEvent)event );
	}
	
}

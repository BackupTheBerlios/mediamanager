package ch.fha.mediamanager.plugin;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.pluginstruct.*;

/**
 * @author ia02vond
 * @version $Id: MMPluginEvent.java,v 1.2 2004/06/17 14:29:35 ia02vond Exp $
 */
public class MMPluginEvent extends PluginEvent {
	
	private DataElement dataElement;
	
	public MMPluginEvent() {
	}
	
	public MMPluginEvent(DataElement dataElement) {
		this.dataElement = dataElement;
	}
	
	public DataElement getDataElement() {
		return dataElement;
	}
}

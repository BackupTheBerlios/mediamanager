package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: PluginEvent.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public class PluginEvent {

	private String eventName;
	
	public String getEventName() {
		return eventName;
	}
	
	protected void setEventName(String eventName) {
		this.eventName = eventName;
	}
}

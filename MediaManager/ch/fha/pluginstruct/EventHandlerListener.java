package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: EventHandlerListener.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public interface EventHandlerListener {
	
	public void addedEventListener(
			String identifier, String event, String condition);
	
	public void removedEventListener(
			String identifier, String event, String condition);
	
}

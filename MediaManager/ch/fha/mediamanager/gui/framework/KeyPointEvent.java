//$Id: KeyPointEvent.java,v 1.3 2004/06/21 07:29:09 radisli Exp $
package ch.fha.mediamanager.gui.framework;

/**
 * @author Roman Rietmann
 *
 * Event used to define key-points
 */
public class KeyPointEvent {
	/** Key Points */
	public static final int WINDOW_EXIT = 1;
	public static final int CONFIG_PANEL_LOAD = 2;
	public static final int CONNECTING = 3;
	public static final int DISCONNECTING = 4;
	
	/** Event responds only on this action */
	public final int alertOnKey;
	
	/**
	 * Event constructor
	 * 
	 * @param alertOnKey defines on what kind of event it should respond
	 */
	public KeyPointEvent(int alertOnKey) {
		this.alertOnKey = alertOnKey;
	}
	
	/**
	 * Gets the <code>alertOnKey</code> which specifies the action 
	 * 
	 * @return action number
	 */
	public int getKeyPointEvent() {
		return alertOnKey;
	}	
}

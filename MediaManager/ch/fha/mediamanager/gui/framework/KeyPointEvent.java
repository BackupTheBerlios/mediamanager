//$Id: KeyPointEvent.java,v 1.5 2004/06/29 13:35:03 radisli Exp $
package ch.fha.mediamanager.gui.framework;

/**
 * @author Roman Rietmann
 *
 * Event used to define key-points
 */
public class KeyPointEvent {
	/** Key Points */
	public static final int WINDOW_EXIT = 1;
	public static final int WINDOW_FINAL_EXIT = 2;
	public static final int CONFIG_PANEL_LOAD = 3;
	public static final int PRE_CONNECT = 4;
	public static final int CONNECT = 5;
	public static final int POST_CONNECT = 6;
	public static final int PRE_DISCONNECT = 7;
	public static final int DISCONNECT = 8;
	public static final int POST_DISCONNECT = 9;
	public static final int CONNECT_ERROR = 10;
	public static final int DISCONNECT_ERROR = 11;
	
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

//$Id: KeyPointEvent.java,v 1.2 2004/06/18 11:07:38 radisli Exp $
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
	
	/** Event responds only on this action */
	public final int alertOnKey;
	
	/** Additional Parameters */
	private String parameter;
	
	/**
	 * Event constructor
	 * 
	 * @param alertOnKey defines on what kind of event it should respond
	 */
	public KeyPointEvent(int alertOnKey, String parameter) {
		this.alertOnKey = alertOnKey;
		this.parameter = parameter;
	}
	
	/**
	 * Gets the parameter
	 * 
	 * @return parameter which gives some additional informations
	 */
	public String getParameter() {
		return parameter;
	}
	
	public int getKeyPointEvent() {
		return alertOnKey;
	}	
}

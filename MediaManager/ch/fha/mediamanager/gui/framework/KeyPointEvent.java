//$Id: KeyPointEvent.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.framework;

/**
 * @author Roman Rietmann
 *
 * TODO 
 */
public class KeyPointEvent {
	/** Key Points */
	public static final int WINDOW_EXIT = 1;
	
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
}

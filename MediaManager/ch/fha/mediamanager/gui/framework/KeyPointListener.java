//$Id: KeyPointListener.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.framework;

/**
 * @author Roman Rietmann
 *
 * Listener which informs <code>KeyPointListener</code> about predefined
 * events <code>e</code>.
 */
public interface KeyPointListener {
	/**
	 * Will be called if a special event <code>e</code> happend.
	 * 
	 * @param e is the event which contains:
	 *        - specific KeyPoint on which <code>runAction</code>
	 *          should be called.
	 *        - Additional Parameter
	 */
	void runAction(KeyPointEvent e);
}

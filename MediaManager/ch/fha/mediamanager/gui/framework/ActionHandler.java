//$Id: ActionHandler.java,v 1.2 2004/06/16 08:10:36 radisli Exp $
package ch.fha.mediamanager.gui.framework;

import java.util.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.MainFrame;

/**
 * The main action-handler which treats the basic actions (e.g. Exit).
 * It also informs registered components about predefined crossings
 * of keypoints.
 *
 * @author Roman Rietmann
 */
public class ActionHandler implements 
	ActionListener
{
	HashMap map = new HashMap();
	
	/**
	 * Adds a <code>KeyPointListener</code> to the <code>map</code> list.
	 * This list contains listeners which will be informed about predefined
	 * crossings of keypoints. 
	 * 
	 * @param l is the <code>KeyPointListener</code> to add.
	 * @param e is the <code>KeyPointEvent</code> which belongs to
	 *        the <code>KeyPointListener</code>.
	 */
	public void addActionListener(KeyPointListener l, KeyPointEvent e){
		map.put(l, e);
	}

	/**
	 * Removes a <code>KeyPointListener</code> from the <code>map</code> list.
	 * This list contains listeners which will be informed about predefined
	 * crossings of keypoints. 
	 * 
	 * @param l is the <code>KeyPointListener</code> to remove.
	 */
	public void removeModelChangeListener(KeyPointListener l){
		map.remove((Object)l);
	}
	
	/**
	 * Runs the <code>runAction</code> methods from all the components in
	 * <code>map</code> list which are intresstet in the action
	 * <code>fireKey</code>.
	 * 
	 * @param fireKey is a predefined keypoint-action.
	 */
	public void fireAction(int fireKey) {
		Set s = map.keySet();
		Iterator it = s.iterator();
		Object obj;
		KeyPointEvent e;
		while(it.hasNext()) {
			obj = it.next();
			e = (KeyPointEvent)map.get(obj);
			if(e.alertOnKey == fireKey) {
				((KeyPointListener)obj).runAction(e);
			}
		}
	}

	/**
	 * ActionHandler which treats all the default actions (e.g. Exit).
	 */
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		MainFrame mainWindow = MainFrame.getInstance();
		
		if(s.equals("exit")) {			// Closes the windows
			mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
		} else if(s.equals("tabs")) {	// Loads the default TabPanel
			mainWindow.loadStdPanel();
		} else if(s.equals("config")) {	// Loads the default ConfigPanel
			mainWindow.loadConfigPanel();
		} else if(s.equals("about")) {	// Loads the AboutWindow
			mainWindow.loadAboutPanel();
		}
	}
}

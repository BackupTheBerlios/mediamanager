//$Id: ActionHandler.java,v 1.4 2004/06/21 08:39:02 radisli Exp $
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
	LinkedList list = new LinkedList();
	
	/**
	 * Adds a <code>KeyPointListener</code> to the <code>map</code> list.
	 * This list contains listeners which will be informed about predefined
	 * crossings of keypoints. 
	 * 
	 * @param l is the <code>KeyPointListener</code> to add.
	 */
	public void addActionListener(KeyPointListener l) {
		list.add(l);
	}

	/**
	 * Removes a <code>KeyPointListener</code> from the <code>map</code> list.
	 * This list contains listeners which will be informed about predefined
	 * crossings of keypoints. 
	 * 
	 * @param l is the <code>KeyPointListener</code> to remove.
	 */
	public void removeModelChangeListener(KeyPointListener l) {
		list.remove(l);
	}
	
	/**
	 * Runs the <code>runAction</code> methods from all the components in
	 * <code>map</code> list which are intresstet in the action
	 * <code>fireKey</code>.
	 * 
	 * @param fireKey is a predefined keypoint-action.
	 */
	public void fireAction(int fireKey) {
		Iterator it = list.iterator();
		KeyPointEvent e = new KeyPointEvent(fireKey);
		while(it.hasNext()) {
			((KeyPointListener)it.next()).runAction(e);
		}
	}

	/**
	 * ActionHandler which treats all the default actions (e.g. Exit).
	 */
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		MainFrame mainWindow = MainFrame.getInstance();
		mainWindow.removeStatusText();

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

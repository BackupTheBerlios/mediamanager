//$Id: ActionHandler.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class ActionHandler implements 
	ActionListener
{
	private JFrame mainView;
	LinkedList al = new LinkedList();
	
	public ActionHandler(JFrame mainView) {
		this.mainView = mainView;
	}

	public void addActionListener(ActionOccurredListener listener){
		al.add(listener);
	}

	public void removeModelChangeListener(ActionOccurredListener listener){
		int figureIndex = al.indexOf((Object)listener);
		al.remove(figureIndex);
	}
	
	public void fireAction() {
		Iterator it = al.iterator();
		System.out.println("fireAction performed in Action Listener:");
		while(it.hasNext()) {
			((ActionOccurredListener)it.next()).runAction();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if(s.equals("self")) {
			
		} else if(s.equals("exit")) {
			mainView.dispatchEvent(new WindowEvent(mainView, WindowEvent.WINDOW_CLOSING));
			return;
		}
	}
}

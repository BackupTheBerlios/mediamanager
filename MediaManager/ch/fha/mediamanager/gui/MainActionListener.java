package ch.fha.mediamanager.gui;
import java.awt.Color;
import java.util.*;

class MainActionListener {
	private ArrayList actionListeners = new ArrayList();
	private Color color;
	private boolean colorNotSet = true;

	public void setColor(Color newColor) {
        if(colorNotSet || !color.equals(newColor)) {
        	color = newColor;
        	colorNotSet = false;
        	fireAction();
        }
	}

	public void setColor(int red, int green, int blue) {
       	fireAction();
	}
	
	Color getColor() {
		return color;
	}
	
	public void addActionListener(Mediamanager cc) {
		actionListeners.add(cc);
	}

	public void removeActionListener(Mediamanager cc) {
		actionListeners.remove(cc);
	}

	public void fireAction() {
		Iterator it = actionListeners.iterator();
		while(it.hasNext()) {
			((ActionOccurredListener)it.next()).colorChanged(this);
		}
	}
}
package ch.fha.mediamanager.plugin;

import javax.swing.JPanel;

import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.components.BarBorder;
import ch.fha.pluginstruct.*;

/**
 * @author ia02vond
 * @version $Id: MMPlugin.java,v 1.3 2004/06/28 13:07:43 ia02vond Exp $
 */
public abstract class MMPlugin extends AbstractPlugin {
	
	public abstract boolean run(MMPluginEvent event);
	
	public boolean run(PluginEvent event) {
		return run( (MMPluginEvent)event );
	}
	
	public void addPanel(JPanel panel) {
		panel.setBorder(new BarBorder(getName()));
		MainFrame.getInstance().loadCoverPanel(panel);
	}
	
	public void removePanel() {
		MainFrame.getInstance().removeCoverPanel();
	}
	
}

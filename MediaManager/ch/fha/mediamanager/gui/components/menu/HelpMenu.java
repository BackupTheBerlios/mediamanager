package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

/**
 * 
 * @author radisli
 * @version $Id: HelpMenu.java,v 1.3 2004/07/04 15:19:04 crac Exp $
 */
public class HelpMenu extends JMenu {
    
    /**
     * 
     *
     */
	public HelpMenu() {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		
		setText("Help");
		JMenuItem about = new JMenuItem("About");
		about.setActionCommand("about");
		about.addActionListener(mainActionListener);
		add(about);
	}
}

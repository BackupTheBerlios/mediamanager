//$Id: HelpMenu.java,v 1.2 2004/06/21 07:43:58 radisli Exp $
package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class HelpMenu extends JMenu {
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

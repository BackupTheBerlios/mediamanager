//$Id: HelpMenu.java,v 1.1 2004/06/16 08:09:49 radisli Exp $
package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public class HelpMenu extends JMenu {
	public HelpMenu() {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		setText("Help");
		JMenuItem about = new JMenuItem("About");
		about.setActionCommand("about");
		about.addActionListener(mainActionListener);
		add(about);
	}
}

package ch.fha.mediamanager.gui.components;

import javax.swing.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.components.menu.*;

/**
 * The main-menu bar
 * 
 * @author Roman Rietmann
 * @version $Id: MainMenuBar.java,v 1.3 2004/06/30 18:54:56 crac Exp $
 */
public class MainMenuBar extends JMenuBar {
    
    /**
     * 
     *
     */
	public MainMenuBar() {
		JMenu fileMenu = new FileMenu();
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);
		
		add(Box.createHorizontalGlue());

		JMenu helpMenu = new HelpMenu();
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(helpMenu);
	}
}

package ch.fha.mediamanager.gui.components;

import javax.swing.*;

import java.awt.event.*;

import ch.fha.mediamanager.gui.components.menu.*;

/**
 * The main-menu bar
 * 
 * @author Roman Rietmann
 * @version $Id: MainMenuBar.java,v 1.4 2004/07/04 15:19:04 crac Exp $
 */
public class MainMenuBar extends JMenuBar {

    private static final int HEIGHT = 25;
    
    /**
     * 
     *
     */
	public MainMenuBar() {
		JMenu fileMenu = new FileMenu();
		fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setPreferredSize(
            new java.awt.Dimension(40,HEIGHT));
		add(fileMenu);
		
		add(Box.createHorizontalGlue());

		JMenu helpMenu = new HelpMenu();
		helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.setPreferredSize(
            new java.awt.Dimension(40,HEIGHT));
		add(helpMenu);
	}
}

//$id$
package ch.fha.mediamanager.gui.components;

import javax.swing.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.components.menu.*;

/**
 * @author Roman Rietmann
 *
 * The main-menu bar
 */
public class MainMenuBar extends JMenuBar {
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

//$Id: FileMenu.java,v 1.2 2004/06/16 08:10:36 radisli Exp $
package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public class FileMenu extends JMenu {
	public FileMenu() {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		setText("File");
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		exit.addActionListener(mainActionListener);
		add(exit);

/*		JMenuItem add = new JMenuItem("Add");
		add.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					standartToolBar.addToolbarElement("images/LINE.gif", "Select");
				}
			}
		);
		add(add);
*/
	}
}

//$Id: FileMenu.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;
import java.awt.event.*;

public class FileMenu extends JMenu {
	private ActionListener mainActionHandler;
	
	public FileMenu(ActionListener mainActionHandler) {
		this.mainActionHandler = mainActionHandler;
		setText("File");
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(mainActionHandler);
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

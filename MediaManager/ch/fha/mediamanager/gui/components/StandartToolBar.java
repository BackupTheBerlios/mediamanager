//$Id: StandartToolBar.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui.components;

import javax.swing.*;
import java.awt.event.*;

public class StandartToolBar extends AbstractToolBar {

	public StandartToolBar(JFrame mainWindow, ActionListener mainActionHandler) {
		super(mainWindow, mainActionHandler);
		addToolbarElement("images/SELECTION.gif", "Select");
	}

	public void runAction() {
		
	}
}

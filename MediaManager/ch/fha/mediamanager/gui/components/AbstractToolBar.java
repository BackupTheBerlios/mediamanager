//$Id: AbstractToolBar.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public abstract class AbstractToolBar extends JToolBar implements
	ActionOccurredListener
{
	private JFrame mainWindow;
	private ActionListener mainActionHandler;
	
	public AbstractToolBar(JFrame mainWindow, ActionListener mainActionHandler) {
		this.mainWindow = mainWindow;
		this.mainActionHandler = mainActionHandler;
	}

	public void addToolbarElement(String image, String toolTipText) {
		Icon icon = new ImageIcon(image);
		JButton newButton = new JButton(icon);
		newButton.setMargin(new Insets(0, 0, 0, 0));
		newButton.setToolTipText(toolTipText);
		newButton.addActionListener(mainActionHandler);
		this.add(newButton);
		mainWindow.show();
	}
}

//$Id: AbstractToolBar.java,v 1.3 2004/06/18 11:07:38 radisli Exp $
package ch.fha.mediamanager.gui.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public abstract class AbstractToolBar extends JToolBar {
	public void addToolbarElement(String image, String toolTipText) {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		add(new ToolBarButton(image, mainActionListener, "Select test"));
	}
	
	public class ToolBarButton extends JButton {
		final String toolTip;
		
		ToolBarButton(String image, ActionHandler mainActionListener, String newToolTip) {
			this.toolTip = newToolTip;
			this.setIcon(new ImageIcon(image));
			setMargin(new Insets(0, 0, 0, 0));
			setToolTipText(toolTip);
			addActionListener(mainActionListener);
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent arg0) {
					MainFrame.getInstance().setStatusText(toolTip, true);
				}
			});
		}
	}
}

//$Id: AbstractToolBar.java,v 1.2 2004/06/16 08:10:36 radisli Exp $
package ch.fha.mediamanager.gui.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public abstract class AbstractToolBar extends JToolBar {
	public void addToolbarElement(String image, String toolTipText) {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		add(new ToolBarButton(image, mainActionListener, "Select test"));
	}
	
	public class ToolBarButton extends JButton implements 
		MouseListener
	{
		ToolBarButton(String image, ActionListener mainActionListener, String toolTip) {
			this.setIcon(new ImageIcon(image));
			setMargin(new Insets(0, 0, 0, 0));
			setToolTipText(toolTip);
			addActionListener(mainActionListener);
			addMouseListener(this);
		}

		public void mouseEntered(MouseEvent arg0) {
			MainFrame.getInstance().setStatusText(this.getToolTipText(), true);
		}

		public void mouseExited(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
}

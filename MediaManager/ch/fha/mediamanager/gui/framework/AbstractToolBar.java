//$Id: AbstractToolBar.java,v 1.4 2004/06/27 20:32:16 radisli Exp $
package ch.fha.mediamanager.gui.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

public abstract class AbstractToolBar extends JToolBar {
	public ToolBarButton addToolbarElement(String image, ActionListener al,
			String ac, String toolTipText) {
		ToolBarButton jb = new ToolBarButton(image, al, ac, toolTipText);
		add(jb);
		return jb ;
	}
	
	public class ToolBarButton extends JButton {
		String toolTip;
		
		ToolBarButton(String image, ActionListener al, String ac, String toolTip) {
			this.setIcon(new ImageIcon(image));
			setMargin(new Insets(0, 0, 0, 0));
			setToolTip(toolTip);
			addActionListener(al);
			if(ac != null) {
				this.setActionCommand(ac);
			}
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent arg0) {
					setStatusText();
				}
			});
		}
		
		private void setStatusText() {
			MainFrame.getInstance().setStatusText(toolTip, true);
		}
		
		public void setToolTip(String toolTip) {
			this.setToolTipText(toolTip);
			this.toolTip = toolTip;
		}
	}
}

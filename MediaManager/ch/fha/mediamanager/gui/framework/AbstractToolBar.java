package ch.fha.mediamanager.gui.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;

/**
 * 
 * @author radisli
 * @version $Id: AbstractToolBar.java,v 1.5 2004/07/04 15:19:04 crac Exp $
 */
public abstract class AbstractToolBar extends JToolBar {
	
    /**
     * 
     * @param image
     * @param al
     * @param ac
     * @param toolTipText
     * @return
     */
    public ToolBarButton addToolbarElement(
            String image, ActionListener al,
			String ac, String toolTipText) {
        
		ToolBarButton jb = new ToolBarButton(image, al, ac, toolTipText);
		add(jb);
		return jb ;
	}
	
    /**
     * 
     * @author radisli
     */
	public class ToolBarButton extends JButton {
		String toolTip;
		
        /**
         * 
         * @param image
         * @param al
         * @param ac
         * @param toolTip
         */
		ToolBarButton(
                String image, ActionListener al, 
                String ac, String toolTip) {
            
            super(new ImageIcon(image));
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
		
        /**
         * 
         *
         */
		private void setStatusText() {
			MainFrame.getInstance().setStatusText(toolTip, true);
		}
		
        /**
         * 
         * @param toolTip
         */
		public void setToolTip(String toolTip) {
			this.setToolTipText(toolTip);
			this.toolTip = toolTip;
		}
	}
}

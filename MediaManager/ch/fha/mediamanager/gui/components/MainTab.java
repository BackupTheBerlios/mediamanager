//$Id: MainTab.java,v 1.2 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ch.fha.mediamanager.gui.*;

public class MainTab extends JPanel {
	public MainTab() {
		MainFrame mainWindow = MainFrame.getInstance();
		setLayout(new BorderLayout());
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		JPanel basePanel = new JPanel(new GridLayout(3, 1));
		JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middleButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		ImageIcon connectImage = new ImageIcon("images/connect.gif");
		NavButton topButton = new NavButton(connectImage,
				mainActionListener, "Zum Server verbinden");
		topButtonPanel.add(topButton);
		JLabel topLabel = new JLabel("Verbinden");
		topLabel.setFont(MainFrame.titleFont);
		topButtonPanel.add(topLabel);
		basePanel.add(topButtonPanel);
		
		ImageIcon prefsImage = new ImageIcon("images/prefs.gif");
		NavButton middleButton = new NavButton(prefsImage,
				mainActionListener, "Haupteinstellungen verändern");
		middleButton.setActionCommand("config");
		middleButtonPanel.add(middleButton);
		JLabel middleLabel = new JLabel("Einstellungen");
		middleLabel.setFont(MainFrame.titleFont);
		middleButtonPanel.add(middleLabel);
		basePanel.add(middleButtonPanel);

		ImageIcon exitImage = new ImageIcon("images/exit.gif");
		NavButton bottomButton = new NavButton(exitImage,
				mainActionListener, "Mediamanager beenden");
		bottomButton.setActionCommand("exit");
		bottomButtonPanel.add(bottomButton);
		JLabel bottomLabel = new JLabel("Beenden");
		bottomLabel.setFont(MainFrame.titleFont);
		bottomButtonPanel.add(bottomLabel);
		basePanel.add(bottomButtonPanel);

		add(basePanel, BorderLayout.WEST);
	}
	
	private class NavButton extends JButton implements
		MouseListener
	{
		private final Dimension d = new Dimension(52, 52);
		
		NavButton(ImageIcon img, ActionListener al, String toolTip) {
			setIcon(img);
			setToolTipText(toolTip);
			setMargin(new Insets(0, 0, 0, 0));
			setPreferredSize(d);
			addActionListener(al);
			addMouseListener(this);
		}

		public void mouseEntered(MouseEvent arg0) {
			MainFrame.getInstance().setStatus(this.getToolTipText(), false);
		}

		public void mouseExited(MouseEvent arg0) {
			MainFrame.getInstance().removeStatus();
		}
		
		public void mouseClicked(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
}
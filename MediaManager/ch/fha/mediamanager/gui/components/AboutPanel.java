//$Id: AboutPanel.java,v 1.1 2004/06/16 08:09:49 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;

/**
 * About panel which shows the details about the MediaManager project
 *
 * @author Roman Rietmann
 */
public class AboutPanel extends JPanel {
	/**
	 * Constructor creates the about panel
	 */
	public AboutPanel() {
		MainFrame mainWindow = MainFrame.getInstance();
		setLayout(new BorderLayout());
		ActionListener mainActionListener = mainWindow.getMainActionListener();

		Color bg = new Color(194, 242, 242);
		JPanel aboutHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel aboutLabel = new JLabel();
		ImageIcon aboutImage = new ImageIcon("images/about.jpg");
		Dimension d = new Dimension(aboutImage.getIconWidth(), aboutImage.getIconHeight());
		aboutHolder.setPreferredSize(d);
		aboutLabel.setIcon(aboutImage);
		aboutHolder.add(aboutLabel);
		aboutHolder.setBackground(bg);
		add(aboutHolder, BorderLayout.CENTER);
		
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		defaultButtonPanel.setBackground(bg);
		add(defaultButtonPanel, BorderLayout.SOUTH);
	}
}
//$Id: AboutPanel.java,v 1.2 2004/06/28 08:08:32 radisli Exp $
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
		setBorder(new BarBorder("About"));

		JPanel aboutHolder = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		aboutHolder.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;

		JLabel aboutLabel = new JLabel();
		ImageIcon aboutImage = new ImageIcon("images/about.jpg");
		Dimension d = new Dimension(aboutImage.getIconWidth(), aboutImage.getIconHeight());
		aboutHolder.setPreferredSize(d);
		aboutLabel.setIcon(aboutImage);
		aboutHolder.add(aboutLabel);
		gridbag.setConstraints(aboutLabel, c);
		aboutHolder.add(aboutLabel);
		JScrollPane sp = new JScrollPane(aboutHolder);
		sp.setBorder(null);
		add(sp, BorderLayout.CENTER);
	
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
	}
}
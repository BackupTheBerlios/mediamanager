//$Id: RepositoryTab.java,v 1.1 2004/06/17 12:22:51 radisli Exp $
package ch.fha.mediamanager.gui.config.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;

/**
 * Repository Configuration panel
 *
 * @author Roman Rietmann
 */
public class RepositoryTab extends JPanel {
	/**
	 * Constructor creates the main-config panel
	 */
	public RepositoryTab() {
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
	
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
	}
}

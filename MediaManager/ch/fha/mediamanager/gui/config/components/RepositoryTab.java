//$Id: RepositoryTab.java,v 1.3 2004/06/18 12:31:48 ia02vond Exp $
package ch.fha.mediamanager.gui.config.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;
import ch.fha.mediamanager.gui.util.RepositoryConf;

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
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
	
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
		
		RepositoryConf rconf = new RepositoryConf(DataBus.getRepositories());
		add(rconf, BorderLayout.CENTER);
	}
}

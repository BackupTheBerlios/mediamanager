//$Id: MainConfigPanel.java,v 1.3 2004/06/17 12:35:04 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.config.components.*;

/**
 * Configuration panel
 *
 * @author Roman Rietmann
 */
public class MainConfigPanel extends JPanel {
	/**
	 * Constructor creates the main-config panel
	 */
	public MainConfigPanel() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel repositoryTab = new RepositoryTab();
		JPanel pluginsTab = new PluginsTab();
		tabbedPane.addTab("Repository", repositoryTab);
		tabbedPane.addTab("Plugins", pluginsTab);
		
		add(tabbedPane, BorderLayout.CENTER);
	}
}

//$Id: MainConfigPanel.java,v 1.5 2004/06/30 18:54:56 crac Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;
import ch.fha.mediamanager.gui.config.components.*;

/**
 * Configuration panel
 *
 * @author Roman Rietmann
 * @version $Id: MainConfigPanel.java,v 1.5 2004/06/30 18:54:56 crac Exp $
 */
public class MainConfigPanel extends JPanel {
    
	/**
	 * Constructor creates the main-config panel
	 */
	public MainConfigPanel() {
		MainFrame mainWindow = MainFrame.getInstance();
		final ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setLayout(new BorderLayout());		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		final JPanel repositoryTab = new RepositoryTab();
		final JPanel pluginsTab = new PluginsTab();
		tabbedPane.addTab("Repository", repositoryTab);
		tabbedPane.addTab("Plugins", pluginsTab);
		tabbedPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTabbedPane jtp = (JTabbedPane)e.getSource();
				if(jtp.getComponentAt(jtp.getSelectedIndex()).equals(pluginsTab)) {
					mainActionListener.fireAction(KeyPointEvent.CONFIG_PANEL_LOAD);	
				}
			}
		});
		
		add(tabbedPane, BorderLayout.CENTER);
	}
}

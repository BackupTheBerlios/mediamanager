//$Id: PluginsTab.java,v 1.3 2004/06/18 11:07:38 radisli Exp $
package ch.fha.mediamanager.gui.config.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;
import ch.fha.mediamanager.gui.util.PluginConf;

/**
 * Plugins Configuration panel
 *
 * @author Roman Rietmann
 */
public class PluginsTab extends JPanel {
	/**
	 * Constructor creates the main-config panel
	 */
	public PluginsTab() {
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
	
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
		
		PluginConf pluginConf = new PluginConf();
		add(pluginConf, BorderLayout.CENTER);
	}
}

//$Id: PluginsTab.java,v 1.2 2004/06/18 07:42:39 ia02vond Exp $
package ch.fha.mediamanager.gui.config.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;
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
		ActionListener mainActionListener = mainWindow.getMainActionListener();
	
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

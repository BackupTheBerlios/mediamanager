//$Id: ModificationPanel.java,v 1.2 2004/06/16 08:10:59 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import ch.fha.mediamanager.gui.*;

public class ModificationPanel extends JPanel {
	private JButton exit, loadTabs, loadConfig;
	
	public ModificationPanel() {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		setLayout(new BorderLayout());
		setBorder(MainFrame.b);
		
		JPanel modPanel = new JPanel(new GridLayout(3, 1));
		loadTabs = new JButton("Load Tabs");
		loadTabs.setActionCommand("tabs");
		loadTabs.addActionListener(mainActionListener);
		loadConfig = new JButton("Load Config");
		loadConfig.setActionCommand("config");
		loadConfig.addActionListener(mainActionListener);
		exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(mainActionListener);

		modPanel.add(loadTabs);
		modPanel.add(loadConfig);
		modPanel.add(exit);

		add(modPanel, BorderLayout.NORTH);
	}
}

//$Id: ModificationPanel.java,v 1.5 2004/06/28 10:37:23 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class ModificationPanel extends JPanel {
	public ModificationPanel() {
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setLayout(new BorderLayout());
		setBorder(MainFrame.b);
		
		JPanel modPanel = new JPanel();
		modPanel.setLayout(new BoxLayout(modPanel, BoxLayout.Y_AXIS));
		JButton loadMain = new JButton("Hauptfenster");
		loadMain.setActionCommand("tabs");
		loadMain.addActionListener(mainActionListener);
		loadMain.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton loadConfig = new JButton("Einstellungen");
		loadConfig.setActionCommand("config");
		loadConfig.addActionListener(mainActionListener);
		loadConfig.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton exit = new JButton("Beenden");
		exit.setActionCommand("exit");
		exit.addActionListener(mainActionListener);
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		modPanel.add(loadMain);
		modPanel.add(loadConfig);
		modPanel.add(Box.createVerticalGlue());
		modPanel.add(exit);
		modPanel.setBorder(new BarBorder("Tools"));
	
		add(modPanel, BorderLayout.CENTER);
	}
}

//$Id: MainConfigPanel.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.prefs.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class MainConfigPanel extends JPanel implements
	Savable
{
	public MainConfigPanel() {
		setLayout(new BorderLayout());
		Mediamanager.addSavable(this);
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Abbrechen");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
		this.repaint();
	}

	public void savePrefs(Preferences prefs) {
		System.out.println("Saving ConfigPanel settings!");
		try {
//			prefs.putInt("test", 8);
			prefs.flush();
		} catch(BackingStoreException e) {
			MainFrame.getInstance().display(e);
		}
	}
}

//$Id: StdTabPanel.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.prefs.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class StdTabPanel extends JPanel implements
	Savable
{
	public StdTabPanel() {
		Mediamanager.addSavable(this);
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(mainActionListener);
		defaultButtonPanel.add(exit);
		add(defaultButtonPanel, BorderLayout.SOUTH);
	}

	public void savePrefs(Preferences prefs) {
		try {
//			prefs.putInt("test", 8);
			prefs.flush();
		} catch(BackingStoreException e) {
			MainFrame.getInstance().display(e);
		}
	}
}

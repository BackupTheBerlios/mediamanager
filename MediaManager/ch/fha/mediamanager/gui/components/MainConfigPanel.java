//$Id: MainConfigPanel.java,v 1.2 2004/06/16 08:10:36 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.prefs.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

/**
 * Configuration panel
 *
 * @author Roman Rietmann
 */
public class MainConfigPanel extends JPanel implements
	Savable
{
	/**
	 * Constructor creates the main-config panel
	 */
	public MainConfigPanel() {
		setLayout(new BorderLayout());
		Mediamanager.addSavable(this);
		MainFrame mainWindow = MainFrame.getInstance();
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton back = new JButton("Zur\u00fcck");
		back.setActionCommand("tabs");
		back.addActionListener(mainActionListener);
		defaultButtonPanel.add(back);
		add(defaultButtonPanel, BorderLayout.SOUTH);
		this.repaint();
	}

	/**
	 * Method which saves the preferences
	 * 
	 * @param prefs contains the preferences  
	 */
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

// $Id: MainTabPanel.java,v 1.2 2004/06/17 12:35:04 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

/**
 * The main view of the <em>Mediamanager</em> application.
 *
 * @author Roman Rietmann
 */
public class MainTabPanel extends JPanel {	
	public MainTabPanel() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel mainTab = new MainTab();
		JPanel moviesTab = new StdTabPanel();
		tabbedPane.addTab("Hauptfenster", mainTab);
		tabbedPane.addTab("Movies", moviesTab);
	
		add(tabbedPane, BorderLayout.CENTER);
	}
}

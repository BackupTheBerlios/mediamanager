// $Id: MainTabPanel.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

/**
 * The main view of the <em>Mediamanager</em> application.
 *
 * @author Roman Rietmann
 */
public class MainTabPanel extends JPanel {
	private JTabbedPane tabbedPane;
	private JPanel mainTab, moviesTab;
	
	public MainTabPanel() {
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainTab = new MainTab();
		moviesTab = new StdTabPanel();
		tabbedPane.addTab("Hauptfenster", mainTab);
		tabbedPane.addTab("Movies", moviesTab);
	
		add(tabbedPane, BorderLayout.CENTER);
	}
}

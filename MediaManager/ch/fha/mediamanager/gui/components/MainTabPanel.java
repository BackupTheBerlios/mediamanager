// $Id: MainTabPanel.java,v 1.5 2004/06/29 13:15:21 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.util.Iterator;

import javax.swing.*;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.MetaEntity;

/**
 * The main view of the <em>Mediamanager</em> application.
 *
 * @author Roman Rietmann
 */
public class MainTabPanel extends JPanel {
	
	private JTabbedPane tabbedPane;
	private JPanel mainTab;
	
	public MainTabPanel() {
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainTab = new MainTab();
		tabbedPane.addTab("Hauptfenster", mainTab);
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void showMainTab() {
		tabbedPane.setSelectedIndex(0);
	}
	
	public void connect() {
		if (DataBus.getMetaEntities() != null) {
			Iterator it = DataBus.getMetaEntities().iterator();
			MetaEntity metaEntity;
			while (it.hasNext()) {
				metaEntity = (MetaEntity)it.next();
				tabbedPane.add(metaEntity.getName(), new StdTabPanel(metaEntity));
			}
		}
	}
	
	public void disconnect() {
		Component[] c = tabbedPane.getComponents();
		for (int i=0; i<c.length; i++) {
			if (c[i] != mainTab) {
				tabbedPane.remove(c[i]);
			}
		}
	}
}

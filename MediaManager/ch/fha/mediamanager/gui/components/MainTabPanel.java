// $Id: MainTabPanel.java,v 1.3 2004/06/23 10:45:35 ia02vond Exp $
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
	
	public MainTabPanel() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel mainTab = new MainTab();
		tabbedPane.addTab("Hauptfenster", mainTab);
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void createEntityTabs() {	
		Iterator it = DataBus.getMetaEntities().iterator();
		MetaEntity metaEntity;
		while (it.hasNext()) {
			metaEntity = (MetaEntity)it.next();
			tabbedPane.add(metaEntity.getName(), new StdTabPanel(metaEntity));
		}
	}
}

//$Id: StdTabPanel.java,v 1.6 2004/06/23 11:45:57 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;

import javax.swing.*;
import java.util.prefs.*;

import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class StdTabPanel extends JPanel implements
	Savable
{
	
	private JTable table;
	
	public StdTabPanel(MetaEntity metaEntity) {
		Mediamanager.addSavable(this);
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setBorder(new BarBorder("TEST-Panel"));
		
		//table = new JTable(new DataTableModel(metaEntity));
		//add(table, BorderLayout.CENTER);
		
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
			MainFrame.getInstance().exception(e);
		}
	}
}

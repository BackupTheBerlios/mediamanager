//$Id: StdTabPanel.java,v 1.8 2004/06/23 13:34:42 ia02vond Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;
import ch.fha.mediamanager.gui.util.DataTableModel;
import ch.fha.mediamanager.gui.util.DataTablePopupMenu;

public class StdTabPanel extends JPanel {
	
	private JTable table;
	
	public StdTabPanel(MetaEntity metaEntity) {
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setBorder(new BarBorder("TEST-Panel"));
		
		table = new JTable(new DataTableModel(metaEntity));
		add(new JScrollPane(table), BorderLayout.CENTER);
		table.addMouseListener(new DataTablePopupMenu(table, metaEntity));
		
		JPanel defaultButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(mainActionListener);
		defaultButtonPanel.add(exit);
		add(defaultButtonPanel, BorderLayout.SOUTH);
	}
}

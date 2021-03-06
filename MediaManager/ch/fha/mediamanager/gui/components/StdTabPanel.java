package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;

import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;
import ch.fha.mediamanager.gui.util.DataTableModel;
import ch.fha.mediamanager.gui.util.DataTablePopupMenu;
import ch.fha.mediamanager.gui.util.SortDecorator;


/**
 * 
 * 
 * @author radisli
 * @version $Id: StdTabPanel.java,v 1.18 2004/06/30 18:54:56 crac Exp $
 */
public class StdTabPanel extends JPanel {
	
	private JTable table;
	
    /**
     * 
     * @param metaEntity
     */
	public StdTabPanel(MetaEntity metaEntity) {
		setLayout(new BorderLayout());
		MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = 
            mainWindow.getMainActionListener();
		setBorder(new BarBorder(
            metaEntity.getName() + " > \u00dcbersicht")
        );
		
		DataTableModel model = new DataTableModel(metaEntity);
		table = new JTable(model);
		SortDecorator decorator = 
            new SortDecorator(table, table.getModel());
		add(new JScrollPane(table), BorderLayout.CENTER);
		table.addMouseListener(new DataTablePopupMenu(
            table, model, decorator, metaEntity)
        );
	}
}

package ch.fha.mediamanager.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.workflow.DeleteWorkflow;
import ch.fha.mediamanager.workflow.EditWorkflow;
import ch.fha.mediamanager.workflow.NewWorkflow;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: DataTablePopupMenu.java,v 1.4 2004/06/23 20:45:44 ia02vond Exp $
 */
public class DataTablePopupMenu extends JPopupMenu
	implements MouseListener, ActionListener {

	private JTable table;
	private DataTableModel model;
	private SortDecorator sortDecorator;
	private MetaEntity metaEntity;
	private PluginManager manager;
	
	private JMenuItem newMI, editMI, deleteMI, emptyMI;
	private JMenu pluginM;
	
	public DataTablePopupMenu(
			JTable table,
			DataTableModel model,
			SortDecorator sortDecorator,
			MetaEntity metaEntity) {
		
		this.table = table;
		this.model = model;
		this.sortDecorator = sortDecorator;
		this.metaEntity = metaEntity;
		
		initMenu();
	}

	private void initMenu() {
		newMI = new JMenuItem("Neu");
		newMI.addActionListener(this);
		add(newMI);
		
		editMI = new JMenuItem("Bearbeiten");
		editMI.addActionListener(this);
		add(editMI);
		
		deleteMI = new JMenuItem("Löschen");
		deleteMI.addActionListener(this);
		add(deleteMI);
		
		addSeparator();
		
		emptyMI = new JMenuItem("<leer>");
		emptyMI.setEnabled(false);
		
		
		pluginM = new JMenu("Plugins");
		pluginM.add(emptyMI);
		add(pluginM);
	}
	
	private void checkPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			editMI.setEnabled(table.getSelectedRowCount() == 1);
			deleteMI.setEnabled(table.getSelectedRowCount() > 0);
			
			show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == newMI) {

			new NewWorkflow(metaEntity).start();

		} else if (source == editMI) {
			
			int index = sortDecorator.getRowSortIndex(table.getSelectedRow());
			DataElement element = model.getDataElement(index);
			
			new EditWorkflow(element).start();
		
		} else if (source == deleteMI) {
			
			DataSet set = new DataSet();
			int selected[] = table.getSelectedRows();
			
			for (int i=0; i<selected.length; i++) {
				int index = sortDecorator.getRowSortIndex(selected[i]);
				set.add(model.getDataElement(index));
			}
				
			new DeleteWorkflow(set).start();
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		checkPopupMenu(e);
	}
	public void mousePressed(MouseEvent e) {
		checkPopupMenu(e);
	}
	public void mouseReleased(MouseEvent e) {
		checkPopupMenu(e);
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	
}
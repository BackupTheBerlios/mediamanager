package ch.fha.mediamanager.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.mckoi.util.HashMapList;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.workflow.DeleteWorkflow;
import ch.fha.mediamanager.workflow.EditWorkflow;
import ch.fha.mediamanager.workflow.NewWorkflow;
import ch.fha.pluginstruct.Plugin;
import ch.fha.pluginstruct.PluginEventObserver;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: DataTablePopupMenu.java,v 1.7 2004/06/25 06:44:09 radisli Exp $
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
	private JMenuItem[] pluginMI;
	
	private PluginMenuItemData[] pmid;
	
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
		
		deleteMI = new JMenuItem("L\u00f6schen");
		deleteMI.addActionListener(this);
		add(deleteMI);
		
		addSeparator();
		
		pluginM = new JMenu("Plugins");
		
		// get plugin list
		HashMap map = new HashMap();
		iterateEvent(map, "norow");
		iterateEvent(map, "singlerow");
		iterateEvent(map, "multirow");
		
		pmid = new PluginMenuItemData[map.size()];
		Iterator it = map.keySet().iterator();
		for (int i=0; i<pmid.length && it.hasNext(); i++) {
			String key = it.next().toString();
			pmid[i] = (PluginMenuItemData)map.get(key);
		}
		
		pluginMI = new JMenuItem[pmid.length];
		
		for (int i=0; i<pmid.length; i++) {
			pluginMI[i] = new JMenuItem(pmid[i].name);
			pluginMI[i].addActionListener(this);
			pluginM.add(pluginMI[i]);
		}
		
		if (pluginM.getMenuComponentCount() == 0) {		
			emptyMI = new JMenuItem("<leer>");
			emptyMI.setEnabled(false);
			pluginM.add(emptyMI);
		}
		
		add(pluginM);
	}
	
	private void iterateEvent(HashMap map, String event) {
		Iterator it = PluginManager.getInstance().getPluginIterator(event);
		while (it.hasNext()) {
			PluginEventObserver peo = (PluginEventObserver)it.next();
			if (peo.condition == null ||
				peo.condition.equals("") || peo.condition.equals(metaEntity.getIdentifier())) {
				
				PluginMenuItemData pmid;
				
				if (map.containsKey(peo.plugin.getIdentifier())) {
					pmid = (PluginMenuItemData)map.get(peo.plugin.getIdentifier());
				} else {
					pmid = new PluginMenuItemData();
					pmid.plugin     = peo.plugin;
					pmid.name       = peo.plugin.getName();
					pmid.identifier = peo.plugin.getIdentifier();
					map.put(peo.plugin.getIdentifier(), pmid);
				}
						
				if (event.equals("norow")) {
					pmid.norow = true;
				} else if (event.equals("singlerow")) {
					pmid.singlerow = true;
				} else if (event.equals("multirow")) {
					pmid.multirow = true;
				}
			}
		}
	}
					
	
	private void checkPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (table.getSelectedRowCount() == 0) {
				for (int i=0; i<pluginMI.length; i++) {
					pluginMI[i].setEnabled(pmid[i].norow);
				}
			} else if (table.getSelectedRowCount() == 1) {
				for (int i=0; i<pluginMI.length; i++) {
					pluginMI[i].setEnabled(pmid[i].singlerow);
				}
			} else {
				for (int i=0; i<pluginMI.length; i++) {
					pluginMI[i].setEnabled(pmid[i].multirow);
				}
			}
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
	
	private class PluginMenuItemData {
		public Plugin plugin;
		public String name;
		public String identifier;
		public boolean norow;
		public boolean singlerow;
		public boolean multirow;
	}
	
}
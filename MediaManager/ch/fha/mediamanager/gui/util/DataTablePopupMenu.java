package ch.fha.mediamanager.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.mediamanager.workflow.DeleteWorkflow;
import ch.fha.mediamanager.workflow.EditWorkflow;
import ch.fha.mediamanager.workflow.NewWorkflow;
import ch.fha.pluginstruct.Plugin;
import ch.fha.pluginstruct.PluginEventObserver;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Returnable;

/**
 * @author ia02vond
 * @version $Id: DataTablePopupMenu.java,v 1.11 2004/06/29 12:12:18 crac Exp $
 */
public class DataTablePopupMenu extends JPopupMenu
	implements MouseListener, ActionListener, Returnable {

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
				peo.condition.equals("") || peo.condition.equals(metaEntity.getName())) {
				
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
		PluginManager manager = PluginManager.getInstance();
		if (e.isPopupTrigger()) {
			if (table.getSelectedRowCount() == 0) {
				for (int i=0; i<pluginMI.length; i++) {
					if (manager.isPluginActivated(pmid[i].identifier)) {
						pluginMI[i].setEnabled(pmid[i].norow);
					} else {
						pluginMI[i].setEnabled(false);
					}
				}
			} else if (table.getSelectedRowCount() == 1) {
				for (int i=0; i<pluginMI.length; i++) {
					if (manager.isPluginActivated(pmid[i].identifier)) {
						pluginMI[i].setEnabled(pmid[i].singlerow);
					} else {
						pluginMI[i].setEnabled(false);
					}
				}
			} else {
				for (int i=0; i<pluginMI.length; i++) {
					if (manager.isPluginActivated(pmid[i].identifier)) {
						pluginMI[i].setEnabled(pmid[i].multirow);
					} else {
						pluginMI[i].setEnabled(false);
					}
				}
			}
            if (table.getSelectedRowCount() == 1) {
                DataElement el = getSelectedDataElement();
                Field pk = el.getPKField();
                if ((pk.getValue() == null) || (pk.getValue().equals(""))) {
                    editMI.setEnabled(false);
                } else {
                    editMI.setEnabled(true);
                }
            }
			deleteMI.setEnabled(table.getSelectedRowCount() > 0);
			
			show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == newMI) {

			new NewWorkflow(metaEntity).start();

		} else if (source == editMI) {
			
			DataElement element = getSelectedDataElement();
			
			new EditWorkflow(element).start();
		
		} else if (source == deleteMI) {
			
			DataSet set = getSelectedDataSet();
				
			new DeleteWorkflow(set).start();
			
		} else {
			
			for (int i=0; i<pluginMI.length; i++) {
				if (source == pluginMI[i]) {
					
					switch (table.getSelectedRowCount()) {
						case 0:
							PluginManager.getInstance().fireEvent(
									this,
									new MMPluginEvent(),
									"norow",
									metaEntity.getName(),
									pmid[i].plugin);
							break;
						case 1:
							PluginManager.getInstance().fireEvent(
									this,
									new MMPluginEvent(getSelectedDataElement()),
									"singlerow",
									metaEntity.getName(),
									pmid[i].plugin);
							break;
						default:
							PluginManager.getInstance().fireEvent(
									this,
									new MMPluginEvent(getSelectedDataSet()),
									"multirow",
									metaEntity.getName(),
									pmid[i].plugin);
						break;
					}
				}
			}
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


	public void fireReturn(boolean operationCanceled) {}
	
	private DataSet getSelectedDataSet() {
		DataSet set = new DataSet();
		int selected[] = table.getSelectedRows();
		
		for (int i=0; i<selected.length; i++) {
			int index = sortDecorator.getRowSortIndex(selected[i]);
			set.add(model.getDataElement(index));
		}
		return set;
	}
	
	private DataElement getSelectedDataElement() {
		int index = sortDecorator.getRowSortIndex(table.getSelectedRow());
		if (index != -1) {
			return model.getDataElement(index);
		}
		return null;
	}
		
}
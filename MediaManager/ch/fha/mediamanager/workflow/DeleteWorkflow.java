package ch.fha.mediamanager.workflow;

import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Returnable;

/**
 * @author ia02vond
 * @version $Id: DeleteWorkflow.java,v 1.7 2004/06/28 11:23:25 ia02vond Exp $
 */
public class DeleteWorkflow implements Workflow, Returnable {

	private DataSet dataSet;
	private DataElement dataElement;
	private MetaEntity  metaEntity;
	private PluginManager pluginManager;
	
	public DeleteWorkflow(DataSet dataSet) {
		this.dataSet = dataSet;
		this.metaEntity  = dataSet.getMetaEntity();
		this.pluginManager = PluginManager.getInstance();
	}
	
	private int state;
	
	private final static int START = 0;
	private final static int PREDELETE = 1;
	private final static int POSTDELETE = 2;
	private final static int DONE = 3;
		
	
	public void start() {
		
		if (dataSet.size() == 1 && JOptionPane.showConfirmDialog(
					MainFrame.getInstance(),
					"Soll der Eintrag wirklich gelöscht werden?",
					"???",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			Iterator it = dataSet.iterator();
			it.hasNext();
			dataElement = (DataElement)it.next();
			delete();
			
		} else if (dataSet.size() > 1 && JOptionPane.showConfirmDialog(
				MainFrame.getInstance(),
				"Sollen die " + dataSet.size() + " Einträge wirklich gelöscht werden?",
				"???",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			Iterator it = dataSet.iterator();
			while (it.hasNext()) {
				dataElement = (DataElement)it.next();
				delete();
			}	
		}
	}
	
	private void delete() {
		state = START;
		fireReturn(false);
	}
	
	public void fireReturn(boolean operationCanceled) {
		
		if (!operationCanceled) {
			
			switch (state) {
				case START:
					
					state = PREDELETE;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"predelete",
						metaEntity.getName()); 
					break;
					
				case PREDELETE:
					
					// delete
					DataSet set = new DataSet();
					set.add(dataElement);
					AbstractQuery req = 
		                DataBus.getQueryInstance(set, AbstractQuery.DELETE);
							
					state = POSTDELETE;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"postdelete",
						metaEntity.getName());
					break;

				case POSTDELETE:
					
					state = DONE;
					break;
			}
		}
	}

	public void go(JPanel form, boolean continuee) {
	}
}
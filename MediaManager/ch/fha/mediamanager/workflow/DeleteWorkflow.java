package ch.fha.mediamanager.workflow;

import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.QueryRequest;
import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: DeleteWorkflow.java,v 1.4 2004/06/23 17:03:41 ia02vond Exp $
 */
public class DeleteWorkflow implements Workflow {

	private DataSet dataSet;
	private MetaEntity  metaEntity;
	private PluginManager pluginManager;
	
	public DeleteWorkflow(DataSet dataSet) {
		this.dataSet = dataSet;
		this.metaEntity  = dataSet.getMetaEntity();
		this.pluginManager = PluginManager.getInstance();
	}
	
	public void start() {
		if (dataSet.size() == 1 && JOptionPane.showConfirmDialog(
					MainFrame.getInstance(),
					"Soll der Eintrag wirklich gelöscht werden?",
					"???",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			Iterator it = dataSet.iterator();
			it.hasNext();
			delete((DataElement)it.next());
			
		} else if (dataSet.size() > 1 && JOptionPane.showConfirmDialog(
				MainFrame.getInstance(),
				"Sollen die " + dataSet.size() + " Einträge wirklich gelöscht werden?",
				"???",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			
			Iterator it = dataSet.iterator();
			while (it.hasNext()) {
				delete((DataElement)it.next());
			}	
		}
	}
		
	private void delete(DataElement dataElement) {
		try {
			pluginManager.fireEvent(
					new MMPluginEvent(dataElement),
					"predelete",
					metaEntity.getIdentifier());
			
			
			// delete
			DataSet set = new DataSet();
			set.add(dataElement);
			QueryRequest req = new QueryRequest(set, QueryRequest.DELETE);
			
			pluginManager.fireEvent(
					new MMPluginEvent(dataElement),
					"postdelete",
					metaEntity.getIdentifier());
		
		} catch (OperationCancelException e) {
			String message =
				"Operation 'delete " + metaEntity.getIdentifier() +
				" ' was canceled by a plugin.";
			DataBus.logger.info(message);
		}
	}
	
	public void go(JPanel form, boolean continuee) {}
}
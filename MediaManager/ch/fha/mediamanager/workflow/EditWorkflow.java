package ch.fha.mediamanager.workflow;

import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.QueryRequest;
import ch.fha.mediamanager.gui.util.InputFormular;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: EditWorkflow.java,v 1.5 2004/06/23 19:51:27 ia02vond Exp $
 */
public class EditWorkflow implements Workflow {
	
	private DataElement dataElement;
	private MetaEntity  metaEntity;
	private PluginManager pluginManager;
	
	public EditWorkflow(DataElement dataElement) {
		this.dataElement = dataElement;
		this.metaEntity  = dataElement.getMetaEntity();
		this.pluginManager = PluginManager.getInstance();
	}
	
	public void start() {
		
		try {
			pluginManager.fireEvent(
					new MMPluginEvent(dataElement),
					"preedit",
					metaEntity.getIdentifier());
			
			
			new InputFormular(dataElement, this, "Bearbeiten");
			
			
		} catch (OperationCancelException e) {
			String message =
				"Operation 'edit " + metaEntity.getIdentifier() +
				" ' was canceled by a plugin.";
			DataBus.logger.info(message);
		}
	}
	
	public void go(JPanel form, boolean continuee) {
		if (continuee) {
			try {
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"preupdate",
						metaEntity.getIdentifier());
				
				
				// update
				DataSet set = new DataSet();
				set.add(dataElement);
				QueryRequest req = new QueryRequest(set, QueryRequest.UPDATE);
				
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"postupdate",
						metaEntity.getIdentifier());
				
				
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"postedit",
						metaEntity.getIdentifier());
				
				
			} catch (OperationCancelException e) {
				String message =
					"Operation 'edit " + metaEntity.getIdentifier() +
					" ' was canceled by a plugin.";
				DataBus.logger.info(message);
			}
		}
		
		// TODO remove tab
	}
}

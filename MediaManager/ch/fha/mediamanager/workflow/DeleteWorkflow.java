package ch.fha.mediamanager.workflow;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.QueryRequest;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: DeleteWorkflow.java,v 1.3 2004/06/23 13:50:57 ia02vond Exp $
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
		/*
		try {	
			// TODO OptionPane confirm (yes, no);
			// TODO foreeach dataelement {
			/*
			if (true) {    // if yes
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
		*/
	}
}
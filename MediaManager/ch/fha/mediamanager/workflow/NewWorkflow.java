package ch.fha.mediamanager.workflow;

import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.gui.util.InputFormular;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.OperationCancelException;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $Id: NewWorkflow.java,v 1.4 2004/06/25 16:06:18 crac Exp $
 */
public class NewWorkflow implements Workflow {

	private MetaEntity metaEntity;
	private DataElement dataElement;
	private PluginManager pluginManager;
	
	public NewWorkflow(MetaEntity metaEntity) {
		this.metaEntity = metaEntity;
		this.dataElement = DataBus.getDefaultElement(metaEntity);
		this.pluginManager = PluginManager.getInstance();
	}
	
	public void start() {
		
		try {
			pluginManager.fireEvent(
					new MMPluginEvent(dataElement),
					"prenew",
					metaEntity.getName());
			
			
			new InputFormular(dataElement, this, "Neu");
			
			
		} catch (OperationCancelException e) {
			String message =
				"Operation 'new " + metaEntity.getName() +
				" ' was canceled by a plugin.";
			DataBus.logger.info(message);
		}
	}
	
	public void go(JPanel form, boolean continuee) {
		if (continuee) {
			try {
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"preinsert",
						metaEntity.getName());
				
				
				// insert
				DataSet set = new DataSet();
				set.add(dataElement);
				AbstractQuery req = 
                    DataBus.getQueryInstance(set, AbstractQuery.INSERT);
				
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"postinsert",
						metaEntity.getName());
				
				
				pluginManager.fireEvent(
						new MMPluginEvent(dataElement),
						"postnew",
						metaEntity.getName());
				
				
			} catch (OperationCancelException e) {
				String message =
					"Operation 'new " + metaEntity.getName() +
					" ' was canceled by a plugin.";
				DataBus.logger.info(message);
			}
		}
		
		// TODO remove tab
	}
}
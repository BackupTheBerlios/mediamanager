package ch.fha.mediamanager.workflow;

import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.gui.util.InputFormular;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Returnable;

/**
 * @author ia02vond
 * @version $Id: EditWorkflow.java,v 1.8 2004/06/28 11:23:25 ia02vond Exp $
 */
public class EditWorkflow implements Workflow, Returnable {
	
	private DataElement dataElement;
	private MetaEntity  metaEntity;
	private PluginManager pluginManager;
	
	public EditWorkflow(DataElement dataElement) {
		this.dataElement = dataElement;
		this.metaEntity  = dataElement.getMetaEntity();
		this.pluginManager = PluginManager.getInstance();
	}
	
	private int state;
	
	private final static int START = 0;
	private final static int PREEDIT = 1;
	private final static int INPUTFORMULAR = 2;
	private final static int PREUPDATE = 3;
	private final static int POSTUPDATE = 4;
	private final static int POSTEDIT = 5;
	private final static int DONE = 6;
		
	
	public void start() {
		
		state = START;
		
		fireReturn(false);
		
		// TODO remove tab
	}
	
	public void fireReturn(boolean operationCanceled) {
		
		if (!operationCanceled) {
			
			switch (state) {
				case START:
					
					state = PREEDIT;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"preedit",
						metaEntity.getName()); 
					break;
					
				case PREEDIT:
					
					state = INPUTFORMULAR;
					new InputFormular(dataElement, this, "Neu");
					break;
					
				case INPUTFORMULAR:
				
					state = PREUPDATE;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"preupdate",
						metaEntity.getName());
					break;
					
				case PREUPDATE:
					
					// update
					DataSet set = new DataSet();
					set.add(dataElement);
					AbstractQuery req = 
	                    DataBus.getQueryInstance(set, AbstractQuery.UPDATE);
					req.run();
					
					state = POSTUPDATE;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"postupdate",
						metaEntity.getName());
					break;
					
				case POSTUPDATE:
					
					state = POSTEDIT;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"postedit",
						metaEntity.getName());
					break;
					
				case POSTEDIT:
					
					state = DONE;
					break;
			}
		}
	}

	public void go(JPanel form, boolean continuee) {
		fireReturn(!continuee);
	}
}

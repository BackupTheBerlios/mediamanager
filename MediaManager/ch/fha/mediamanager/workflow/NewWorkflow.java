package ch.fha.mediamanager.workflow;

import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.gui.util.InputFormular;
import ch.fha.mediamanager.plugin.MMPluginEvent;
import ch.fha.pluginstruct.PluginManager;
import ch.fha.pluginstruct.Returnable;

/**
 * @author ia02vond
 * @version $Id: NewWorkflow.java,v 1.7 2004/06/28 13:45:59 crac Exp $
 */
public class NewWorkflow implements Workflow, Returnable {

	private MetaEntity metaEntity;
	private DataElement dataElement;
	private PluginManager pluginManager;
	
	public NewWorkflow(MetaEntity metaEntity) {
		this.metaEntity = metaEntity;
		this.dataElement = DataBus.getDefaultElement(metaEntity);
		this.pluginManager = PluginManager.getInstance();
	}
	
	private int state;
	
	private final static int START = 0;
	private final static int PRENEW = 1;
	private final static int INPUTFORMULAR = 2;
	private final static int PREINSERT = 3;
	private final static int POSTINSERT = 4;
	private final static int POSTNEW = 5;
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
					
					state = PRENEW;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"prenew",
						metaEntity.getName()); 
					break;
					
				case PRENEW:
					
					state = INPUTFORMULAR;
					new InputFormular(dataElement, this, "Neu");
					break;
					
				case INPUTFORMULAR:
				
					state = PREINSERT;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"preinsert",
						metaEntity.getName());
					break;
					
				case PREINSERT:
					
					// insert
					DataSet set = new DataSet();
					set.add(dataElement);
					AbstractQuery req = 
	                    DataBus.getQueryInstance(set, AbstractQuery.INSERT);
					req.run();
                    
					state = POSTINSERT;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"postinsert",
						metaEntity.getName());
					break;
					
				case POSTINSERT:
					
					state = POSTNEW;
					pluginManager.fireEvent(
						this,
						new MMPluginEvent(dataElement),
						"postnew",
						metaEntity.getName());
					break;
					
				case POSTNEW:
					
					state = DONE;
					break;
			}
		}
	}

	public void go(JPanel form, boolean continuee) {
		fireReturn(!continuee);
	}
}
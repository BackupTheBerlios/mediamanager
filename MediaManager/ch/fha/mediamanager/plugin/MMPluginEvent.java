package ch.fha.mediamanager.plugin;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.pluginstruct.*;

/**
 * @author ia02vond
 * @version $Id: MMPluginEvent.java,v 1.4 2004/06/28 21:10:21 ia02vond Exp $
 */
public class MMPluginEvent extends PluginEvent {
	
	private DataElement dataElement;
	private DataSet     dataSet;
	
	public MMPluginEvent() {
	}
	
	public MMPluginEvent(DataElement dataElement) {
		this.dataElement = dataElement;
	}
	
	public MMPluginEvent(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	public DataElement getDataElement() {
		return dataElement;		
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}
	
	public MetaEntity getMetaEntity() {
		if (dataElement != null) {
			return dataElement.getMetaEntity();
		} else if (dataSet != null) {
			return dataSet.getMetaEntity();
		} else {
			return null;
		}
	}

}

package ch.fha.mediamanager.gui.util;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.MetaField;
import ch.fha.mediamanager.data.QueryCondition;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.RepositoryListener;

/**
 * 
 * 
 * @author ia02vond
 * @version $Id: DataTableModel.java,v 1.17 2004/07/01 11:30:40 crac Exp $
 */
public class DataTableModel extends AbstractTableModel
	implements RepositoryListener {

	private MetaEntity metaEntity;
	private MetaField[] metaFields;
	private DataElement[] elements;
	private Object[][] data;
	
	private boolean empty = true;
	
	public DataTableModel(MetaEntity metaEntity) {
		this.metaEntity = metaEntity;
		DataBus.getRepository().addRepositoryListener(this);
		refresh();
	}
	
	public void refresh() {
        DataElement tmp = DataBus.getDefaultElement(metaEntity);
        Field field = tmp.getPKField();
		QueryCondition qc = 
		    new QueryCondition(
		        field, 
		        QueryCondition.GREATER, 
		        new Integer(0)
		    );
		Vector vec = new Vector();
		vec.add(qc);
		AbstractQuery qr = DataBus.getQueryInstance(
            vec,
            AbstractQuery.LOAD
        );
		DataSet ds = qr.run();
        
        if ((ds != null) && (ds.size() > 0)) {
            metaFields = ds.getNotHiddenMetaFields();
            
    		Iterator it = ds.iterator();
    		elements = new DataElement[ds.size()];
    		data = new Object[elements.length][metaFields.length];
    		
    		for (int i=0; i<elements.length && it.hasNext(); i++) {
    			elements[i] = (DataElement)it.next();
    			for (int k=0; k<metaFields.length; k++) {
    				data[i][k] = elements[i].getField(metaFields[k]).getValue();
    			}
    		}
    		
    		empty = false;
    		
        } else {
            // Create one empty element
            metaFields = tmp.getNotHiddenMetaFields();
            
            elements = new DataElement[1];
            elements[0] = tmp;
            data = new Object[1][metaFields.length];
            for (int j = 0; j < metaFields.length; j++) {
                data[0][j] = new String();
            }
            
            empty = true;
        }
	}
	
	public int getColumnCount() {
		return data[0].length;
	}

	public int getRowCount() {
		return data.length;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Class getColumnClass(int columnIndex) {
		if (!empty && metaFields[columnIndex].getType() == MetaField.BOOLEAN) {
			return Boolean.class;
		} else {
			return String.class;
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException();
	}

	public String getColumnName(int columnIndex) {
		return metaFields[columnIndex].getName();
	}
	
	public DataElement getDataElement(int index) {
		return elements[index];
	}

	public void dataChanged(MetaEntity metaEntity) {
		if (metaEntity.equals(this.metaEntity)) {
			refresh();
			fireTableChanged(new TableModelEvent(this));
		}
	}
	
	public boolean isEmpty() {
		return empty;
	}
}

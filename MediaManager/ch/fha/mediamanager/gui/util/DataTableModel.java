package ch.fha.mediamanager.gui.util;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.MetaField;
import ch.fha.mediamanager.data.QueryCondition;
import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.data.DataBus;

/**
 * @author ia02vond
 * @version $Id: DataTableModel.java,v 1.5 2004/06/27 16:19:16 crac Exp $
 */
public class DataTableModel extends AbstractTableModel {

	private MetaEntity metaEntity;
	private MetaField[] metaFields;
	private DataElement[] elements;
	private Object[][] data;
	
	public DataTableModel(MetaEntity metaEntity) {
		this.metaEntity = metaEntity;
		refresh();
	}
	
	public void refresh() {
        DataElement tmp = DataBus.getDefaultElement(metaEntity);
        Field field = tmp.getPKField();
		QueryCondition qc = 
		    new QueryCondition(
		        field, 
		        QueryCondition.GREATER_EQUALS, 
		        new Integer(0)
		    );
		Vector vec = new Vector();
		vec.add(qc);
		AbstractQuery qr = DataBus.getQueryInstance(
            vec,
            AbstractQuery.LOAD
        );
		DataSet ds = qr.run();
		metaFields = ds.getMetaFields();
		
		Iterator it = ds.iterator();
		elements = new DataElement[ds.size()];
		data = new Object[elements.length][metaFields.length];
		
		for (int i=0; i<elements.length && it.hasNext(); i++) {
			elements[i] = (DataElement)it.next();
			for (int k=0; k<metaFields.length; k++) {
				data[i][k] = elements[i].getField(metaFields[k]).getValue();
			}
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
		return String.class;
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
}

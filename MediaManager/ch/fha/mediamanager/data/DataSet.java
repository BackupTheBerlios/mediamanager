package ch.fha.mediamanager.data;

import java.util.Set;
import java.util.HashSet;

/**
 * Contains a set of data elements.
 * 
 * <p>Note: A <code>DataSet</code> contains only 
 * <code>MetaField</code>s of one <code>MetaEntity</code>.
 * 
 * @see DataElement
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.8 2004/06/21 21:42:45 crac Exp $
 */
public class DataSet {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    private Set dSet = new HashSet();
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @param e
     */
    public void add(DataElement e) {
        dSet.add(e);
    }
    
    /**
     * 
     * @return
     */
    public java.util.Iterator iterator() {
        return dSet.iterator();
    }
    
    /**
     * 
     * @return
     */
    public boolean isEmpty() {
        return dSet.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    public int size() {
        return dSet.size();
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public String getPKField() {
        MetaField[] mf = getMetaFields();
        for (int i = 0; i < mf.length; i++) {
            if (mf[i].getType() == MetaField.PK) {
                return mf[i].getName();
            }
        }
        return null;
    }
    
    /**
     * Returns the meta data about all fields of the 
     * <code>DataSet</code>. 
     * 
     * <p>Note: Since all its <code>DataElement</code>s 
     * have the same fields only the fields of the first 
     * elements are returned. 
     * 
     * @return
     */
    public MetaField[] getMetaFields() {
        if (isEmpty()) return null;
        
        java.util.Iterator it = iterator();
        DataElement element = (DataElement) it.next();
        
        return element.getMetaFields();
    }
    
    /**
     * Returns an array of all <code>Field</code>s of one 
     *  <code>DataElement</code>.
     * 
     * @return
     */
    public Field[] getFields() {
        if (isEmpty()) return null;
        
        java.util.Iterator it = iterator();
        DataElement element = (DataElement) it.next();
        
        it = element.iterator();
        Field[] arr = new Field[element.size()];
        int i = 0;
        while (it.hasNext()) {
            Field tmp = (Field) it.next();
            arr[i] = tmp; i++;
        }
        return arr;
    }
    
    /**
     * Returns the entity.
     * 
     * <p>Note: Since all <code>DataElement</code>s 
     * of a <code>DataSet</code> have fields of the same
     * entity, only one entity is returned.
     * 
     * @return
     */
    public MetaEntity getMetaEntity() {
        if (isEmpty()) return null;
        
        MetaField[] fields = getMetaFields();
        return fields[0].getEntity();
    }
}

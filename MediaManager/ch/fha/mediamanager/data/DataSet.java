package ch.fha.mediamanager.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains a set of data elements.
 * 
 * <p>Note: A <code>DataSet</code> contains only 
 * <code>MetaField</code>s of one <code>MetaEntity</code>.
 * 
 * @see DataElement
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.14 2004/07/01 11:30:40 crac Exp $
 */
public class DataSet {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    private Set dSet = new LinkedHashSet();
    
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
     * @param e
     */
    public void remove(DataElement e) {
        dSet.remove(e);
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
    public String getPKFieldname() {
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
     * @see DataElement#getMetaFields()
     * 
     * @return Returns array of all <code>MetaField</code>s 
     *      of this <code>DataSet</code>
     */
    public MetaField[] getMetaFields() {
        if (isEmpty()) return null;
        
        java.util.Iterator it = iterator();
        DataElement element = (DataElement) it.next();
        return element.getMetaFields();
    }
    
    /**
     * 
     * @see DataElement#getNotHiddenMetaFields()
     * 
     * @return Returns array of all non hidden 
     *      <code>MetaField</code>s of this 
     *      <code>DataSet</code>
     */
    public MetaField[] getNotHiddenMetaFields() {
        if (isEmpty()) return null;
        
        java.util.Iterator it = iterator();
        DataElement element = (DataElement) it.next();
        return element.getNotHiddenMetaFields();
    }
    
    /**
     * Returns an array of all <code>Field</code>s of one 
     *  <code>DataElement</code>.
     * 
     * @return
     */
    public Field[] getFields() {
        if (dSet.isEmpty()) 
            throw new RuntimeException("No Elements available.");
        
        java.util.Iterator it = dSet.iterator();
        DataElement element = (DataElement) it.next();
        
        if (element.isEmpty()) 
            throw new RuntimeException("No Fields available.");
        
        return element.getFields();
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
        return fields[0].getMetaEntity();
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /*protected void setElement(DataElement e) {
        java.util.Iterator it = dSet.iterator();
        
        while(it.hasNext()) {
            DataElement tmp = (DataElement) it.next();
            if (tmp.getEntryId() == e.getEntryId()) {
                dSet.remove(tmp);
                dSet.add(e);
                return;
            }
        }
        
        dSet.add(e);
    }*/
}

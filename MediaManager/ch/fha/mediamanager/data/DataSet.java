package ch.fha.mediamanager.data;

import java.util.Set;
import java.util.HashSet;

/**
 * Contains a set of data elements.
 * 
 * @see DataElement
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.6 2004/06/15 12:12:16 crac Exp $
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
     * Returns the meta data about all fields of the 
     * <code>DataSet</code>. 
     * 
     * <p>Note: Since all its <code>DataElement</code>s 
     * have the same fields only the field of the first 
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
}

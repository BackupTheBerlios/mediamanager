package ch.fha.mediamanager.data;

import java.util.Set;
import java.util.HashSet;

/**
 * Contains a set of data elements.
 * 
 * @see DataElement
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.5 2004/06/10 20:07:02 crac Exp $
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
}

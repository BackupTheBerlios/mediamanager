package ch.fha.mediamanager.data;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.4 2004/06/05 14:13:06 crac Exp $
 */
public class DataSet {
	
    // --------------------------------
    // ATTRIBUTES
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
    public Iterator iterator() {
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

package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.4 2004/06/05 23:23:15 crac Exp $
 */
public class DataElement {
	
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
	
    private Set fields = new HashSet();
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @param field
     */
    public void add(Field field) {
        fields.add(field);
    }
    
    /**
     * 
     * @return
     */
    public Iterator iterator() {
    	return fields.iterator();
    }
    
    /**
     * 
     * @return
     */
    public boolean isEmpty() {
    	return fields.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    public int size() {
    	return fields.size();
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @param f
     * @return
     */
    public Field getField(MetaField f) {
    	Iterator it = iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
        	if (tmp.getMetaField().equals(f)) {
                return tmp;
            }
        }
        
        return null;
    }
}

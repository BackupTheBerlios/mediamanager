package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.5 2004/06/11 12:35:27 crac Exp $
 */
public class DataElement {
	
    // --------------------------------
    // FIELDS
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
    public java.util.Iterator iterator() {
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
    	java.util.Iterator it = iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
        	if (tmp.getMetaField().equals(f)) {
                return tmp;
            }
        }
        
        return null;
    }
}

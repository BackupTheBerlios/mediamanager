package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.6 2004/06/15 12:12:16 crac Exp $
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
     * @return
     */
    public MetaField[] getMetaFields() {
        if (isEmpty()) return null;
        
        MetaField[] meta = new MetaField[size()];
        java.util.Iterator it = iterator();
        
        int i = 0;
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            meta[i] = tmp.getMetaField();
        }
        
        return meta;
    }
    
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

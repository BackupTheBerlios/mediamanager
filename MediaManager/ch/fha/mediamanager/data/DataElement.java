package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.9 2004/06/21 13:55:59 crac Exp $
 */
public class DataElement {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    private Set fields = new HashSet();
    private Entry entry = null;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param entity
     * @param entryId
     */
    public DataElement(MetaEntity entity, Entry entry) {
        this.entry = (Entry) entry.clone();   
    }
    
    /**
     *
     * @param entity
     */
    public DataElement(MetaEntity entity) {}
    
    /**
     * 
     *
     */
    public DataElement() {}
    
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
    
    /**
     * 
     * @return
     */
    public String toString() {
        String str = "DataElement:\n";
        java.util.Iterator it = iterator();
        while (it.hasNext()) {
            str += ((Field) it.next()).toString() + "\n";
        }
        return str;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return Returns the owner
     */
    public User getOwner() {
        return entry.getUser();   
    }
    
    /**
     * 
     * @return Returns the <code>Entry</code>
     */
    public Entry getEntry() {
        return (Entry) entry.clone();   
    }
    
    /**
     * 
     * @return Returns value of entryId
     */
    public int getEntryId() {
        return entry.getId();   
    }
    
    /**
     * 
     * @return Returns array of <code>MetaField</code>s
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
     * @return Returns array of all <code>Field</code>s
     */
    public Field[] getFields() {
        if (isEmpty()) return null;
        
        Field[] fields = new Field[size()];
        java.util.Iterator it = iterator();
        
        int i = 0;
        while(it.hasNext()) {
            fields[i] = (Field) it.next();
        }
        
        return fields;
    }
    
    /**
     * 
     * @param f
     * @return Returns a choosen field
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
    
    /**
     * 
     * @return Returns the <code>MetaEntity</code>
     */
    public MetaEntity getMetaEntity() {
        java.util.Iterator it = iterator();
        Field tmp = (Field) it.next();
        return tmp.getEntity();
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param entry
     */
    public void setEntry(Entry entry) {
        entry = (Entry) entry.clone();
    }
}
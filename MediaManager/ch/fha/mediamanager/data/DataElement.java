package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.14 2004/06/22 17:31:11 crac Exp $
 */
public class DataElement {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    private Set fields = new HashSet();
    private Entry entry = null;
    private MetaEntity entity = null;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param entity
     * @param entryId
     */
    public DataElement(MetaEntity entity, Entry entry) {
        this.entity = entity;
        this.entry = (Entry) entry.clone();
        initFields(entity);
    }
    
    /**
     *
     * @param entity
     * @param owner
     */
    public DataElement(MetaEntity entity, User owner) {
        this.entity = entity;
        this.entry = new Entry();
        this.entry.setOwner(owner);
        initFields(entity);
    }
    
    /**
    *
    * @param entity
    */
    public DataElement(MetaEntity entity) {
        this.entity = entity;
        this.entry = new Entry();
        initFields(entity);
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
   
    /**
     * 
     * @param entity
     */
    private void initFields(MetaEntity entity) {
        Set metaFields = 
            DataBus.getMetaData().getMetaFields(entity);
        java.util.Iterator it = metaFields.iterator();
        while (it.hasNext()) {
            Field field = new Field((MetaField) it.next());
            if (field != null)
                fields.add(field);
        }
    }
    
    /**
     * 
     * @see #setField(Field)
     * 
     * @param field
     */
    public void add(Field field) {
        setField(field);
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
     * @return Returns Timestamp of last modification
     */
    public java.sql.Timestamp getLastModified() {
        return entry.getEdit();
    }
    
    /**
     * 
     * @return Returns Timestamp of creation
     */
    public java.sql.Timestamp getCreation() {
        return entry.getCreation();
    }
    
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
        if (fields.isEmpty()) 
            throw new RuntimeException("No MetaFields");
        
        MetaField[] meta = new MetaField[fields.size()];
        java.util.Iterator it = fields.iterator();
        
        int i = 0;
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            meta[i] = tmp.getMetaField();
            i++;
        }
        
        return meta;
    }
    
    /**
     * 
     * @return Returns array of all <code>Field</code>s
     */
    public Field[] getFields() {
        if (fields.isEmpty()) return null;
        
        Field[] flds = new Field[fields.size()];
        java.util.Iterator it = fields.iterator();
        
        int i = 0;
        while(it.hasNext()) {
            flds[i] = (Field) it.next();
            i++;
        }
        
        return flds;
    }
    
    /**
     * 
     * @param f
     * @return Returns a choosen field
     */
    public Field getField(MetaField f) {
    	java.util.Iterator it = fields.iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
        	if ((tmp != null) && (tmp.getMetaField().equals(f))) {
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
        return entity;
    }
    
    /**
     * 
     * @return
     */
    public Field getPKField() {
        Field[] fields = getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getMetaField().getType() == MetaField.PK)
                return fields[i];
        }
        return null;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param entry
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
    }
    
    /**
     * 
     * @param field
     * @param value
     */
    public void setField(String field, Object value) {
        MetaField mf = new MetaField(field, entity);
        java.util.Iterator it = fields.iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            if ((tmp != null) && (tmp.getMetaField().equals(mf))) {
                fields.remove(tmp);
                tmp.setValue(value);
                fields.add(tmp);
                return;
            }
        }
        
        fields.add(new Field(mf, value));
    }
    
    /**
     * Changes a field of the set to the new value. 
     * If it is not already in the set, it gets added.
     * 
     * @param field
     */
    public void setField(Field field) {
        MetaField mf = field.getMetaField();
        java.util.Iterator it = fields.iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            
            // Compare MetaField data
            if ((tmp != null) && (tmp.getMetaField().equals(mf))) {
                fields.remove(tmp);
                fields.add(field);
                return;
            }
        }
        
        fields.add(field);
    }
    
    /**
     * 
     * @param owner
     */
    public void setOwner(User owner) {
        entry.setOwner(owner);
    }
}
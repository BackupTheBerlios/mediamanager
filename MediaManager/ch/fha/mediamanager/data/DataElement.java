package ch.fha.mediamanager.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.23 2004/07/01 11:30:40 crac Exp $
 */
public final class DataElement {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    private Set fields = new LinkedHashSet();
    private Entry entry = null;
    private MetaEntity entity = null;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param entity
     * @param entry
     */
    public DataElement(MetaEntity entity, Entry entry) {
        this.entity = (MetaEntity) entity.clone();
        this.entry = (Entry) entry.clone();
        initFields(entity);
    }
    
    /**
    * Used for creating a new <code>DataElement</code> 
    * which is not already in the repository.
    * 
    * @param entity
    */
    public DataElement(MetaEntity entity) {
        this(entity, new Entry());
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
            MetaField mf = (MetaField) it.next();
            Field field;
            if (mf.getType() == MetaField.LIST) {
                field = new Field(
                    mf,
                    ((String[]) mf.getDefaultValue())[0]
                );
            } else {
                field = new Field(
                    mf,
                    mf.getDefaultValue()
                );
            }
            if (field != null) {
                fields.add(field);
            }
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
    
    /**
     * Checks if a requested Field is available.
     * 
     * @param name
     * @return Returns true, if the field exists
     */
    public boolean hasField(String name) {
        MetaField mf = new MetaField(name, entity);
        java.util.Iterator it = fields.iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            if ((tmp != null) && (tmp.getMetaField().equals(mf))) {
                return true;
            }
        }
        
        return false;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return Returns Timestamp of last modification
     */
    public java.sql.Timestamp getLastModified() {
        return entry.getLastModified();
    }
    
    /**
     * 
     * @return Returns Timestamp of creation
     */
    public java.sql.Timestamp getCreation() {
        return entry.getCreation();
    }
    
    /**
     * Returns copy of the <code>Entry</code> to 
     * prevent malicious mutations.
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
     * @return Returns array of all <code>MetaField</code>s
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
     * @return Returns array of all non hidden 
     *      <code>MetaField</code>s
     */
    public MetaField[] getNotHiddenMetaFields() {
        if (fields.isEmpty()) 
            throw new RuntimeException("No MetaFields");
        
        java.util.Vector vec = new java.util.Vector();
        java.util.Iterator it = fields.iterator();
        
        while(it.hasNext()) {
            Field tmp = (Field) it.next();
            MetaField mf = tmp.getMetaField();
            if ((! mf.getHidden()) && (mf.getType() != MetaField.PK)) {
            	vec.add((MetaField) tmp.getMetaField());
            }
        }
        
        if (vec.isEmpty()) return null;
        
        MetaField[] meta = new MetaField[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            meta[i] = (MetaField) vec.get(i);
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
     * @see #getField(MetaField)
     * 
     * @param field
     * @return
     */
    public Field getField(String field) {
        return getField(new MetaField(field, getMetaEntity()));
    }
    
    /**
     * 
     * @return Returns the <code>MetaEntity</code>
     */
    public MetaEntity getMetaEntity() {
        return (MetaEntity) entity.clone();
    }
    
    /**
     * 
     * @return Returns the Primary Key field 
     *      of the element
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
    protected void setEntry(Entry entry) {
        this.entry = (Entry) entry.clone();
    }
    
    /**
     * Changes a field of the set to the new value. 
     * If it is not already in the set, it gets added.
     * 
     * @see #setField(Field)
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
}
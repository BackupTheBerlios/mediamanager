package ch.fha.mediamanager.data;

/**
 * 
 * 
 * @author crac
 * @version $Id: Field.java,v 1.11 2004/06/28 14:12:20 crac Exp $
 */
public class Field {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
	private MetaField meta;
    private Object value;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    
    /**
     * 
     * @see MetaEntity
     * 
     * @param name
     * @param entity
     * @param value
     */
    public Field(String name, MetaEntity entity, Object value) {
    	meta = new MetaField(name, entity);
    	this.value = value;
    }
    
    /**
     * 
     * @see MetaEntity
     * 
     * @param name
     * @param entity
     */
    public Field(String name, MetaEntity entity) {
    	this(name, entity, null);
    }
    
    /**
     * 
     * @param field
     * @param value
     */
    public Field(MetaField field, Object value) {
         this.meta = field;
         this.value = value;
    }
    
    /**
     * 
     * @param field
     */
    public Field(MetaField field) {
         this(field, null);
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public String toString() {
        String str = meta.getName() + "::" + value;
        return str;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------

    /**
     * Returns a copy of <code>MetaField</code> instead 
     * of the reference to avoid that it gets mutated 
     * from outside.
     * 
     * @see MetaField
     * 
     * @return
     */
    public MetaField getMetaField() {
        return (MetaField) meta.clone();
    }
    
    /**
     * Returns the value of the field.
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Returns the entity the field belongs to.
     * 
     * @return
     */
    public MetaEntity getMetaEntity() {
    	return meta.getMetaEntity();
    }
    
    /**
     * Returns the name of the field.
     * 
     * @return
     */
    public String getName() {
    	return meta.getName();
    }
    
    /**
     * Returns the identifier of the field.
     * 
     * @return
     */
    public String getIdentifier() {
        return meta.getIdentifier();   
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Sets the meta information of the field.
     * 
     * TODO: Do we need this or should it not be possible
     * to change the meta information?
     * 
     * @see MetaField
     * 
     * @param value
     */
    public void setMetaField(MetaField value) {
        meta = value;
    }
    
    /**
     * Sets the value of the field.
     * 
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}

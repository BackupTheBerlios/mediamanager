package ch.fha.mediamanager.data;

/**
 * 
 * 
 * @author crac
 * @version $Id: Field.java,v 1.4 2004/06/19 14:41:30 crac Exp $
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
     * @param id
     * @param entity
     */
    public Field(String id, MetaEntity entity) {
    	this(id, entity, null);
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
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
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
    public MetaEntity getEntity() {
    	return meta.getEntity();
    }
    
    /**
     * Returns the field's id.
     * 
     * @return
     */
    public String getName() {
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
    public void setMetaData(MetaField value) {
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

package ch.fha.mediamanager.data;

/**
 * 
 * 
 * @author crac
 * @version $Id: Field.java,v 1.13 2004/06/29 11:53:01 crac Exp $
 */
public class Field {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
	private MetaField meta;
    private Object value = null;
    private Object tmpValue = null; 
    
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
        str += " MetaId=" + meta.getId();
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
     * @return Returns its meta data
     */
    public MetaField getMetaField() {
        return (MetaField) meta.clone();
    }
    
    /**
     * 
     * 
     * @return Returns the value of the field
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * 
     * @return Returns temporary value of 
     *      the field
     */
    public Object getTmpValue() {
        return tmpValue;
    }
    
    /**
     * 
     * 
     * @return Returns the entity the field 
     *      belongs to
     */
    public MetaEntity getMetaEntity() {
    	return meta.getMetaEntity();
    }
    
    /**
     * 
     * 
     * @return Returns the name of the field
     */
    public String getName() {
    	return meta.getName();
    }
    
    /**
     * 
     * 
     * @return Returns the identifier of the 
     *      field
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
    
    /**
     * Sets the temporary value.
     * 
     * @param value
     */
    public void setTmpValue(Object value) {
        this.tmpValue = value;
    }
}

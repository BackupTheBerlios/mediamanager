package ch.fha.mediamanager.data;

/**
 * @author crac
 * @version $Id: Field.java,v 1.2 2004/06/05 23:23:35 crac Exp $
 */
public class Field {
	
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
	
	private MetaField meta;
    private Object value;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
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
     * @param id
     * @param entity
     */
    public Field(String id, MetaEntity entity) {
    	this(id, entity, null);
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------

    /**
     * Get the value of meta
     * 
     * @return the value of meta
     */
    public MetaField getMetaField() {
        return meta;
    }
    
    /**
     * Get the value of value
     * 
     * @return the value of value
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * 
     * 
     * @return
     */
    public MetaEntity getEntity() {
    	return meta.getEntity();
    }
    
    /**
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
     * Set the value of meta
     * 
     * @param value
     */
    public void setMetaData(MetaField value) {
        meta = value;
    }
    
    /**
     * Set the value of value
     * 
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
}

package ch.fha.mediamanager.data;

/**
 * Holds meta information about the field of an entity.
 * 
 * @see MetaEntity
 * @see MetaData
 * 
 * @author crac
 * @version $Id: MetaField.java,v 1.8 2004/06/19 14:42:08 crac Exp $
 */
public final class MetaField implements Cloneable {
	
	public final static int PK = 1;
    public final static int USERID = 2;
    public final static int INT = 3;
    public final static int VARCHAR = 4;
    public final static int TEXT = 5;
    public final static int BOOLEAN = 6;
    public final static int LIST = 7;
    
    /* Used for creation of hashCode */
    private final static int PRIME = 13;
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private int type;
    private int length;
    private String name;
    private String identifier;
    private boolean hidden;
    private boolean mandatory;
    private MetaEntity entity;
    private Object defaultValue;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param id
     * @param entity
     */
    public MetaField(String id, MetaEntity entity) {
        this.identifier = id;
        this.name = id;
        this.entity = entity;
    }
    
    /**
     * 
     * @param id
     * @param name
     * @param entity
     */
    public MetaField(String id, String name, MetaEntity entity) {
        this.identifier = id;
        this.name = name;
        this.entity = entity;
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @param f
     * @return Returns true if both MetaFields are equal.
     */
    public boolean equals(MetaField f) {
        return (identifier.equals(f.identifier) 
                && entity.equals(f.getEntity()));   
    }
    
    /**
     * 
     * @return Returns the hashCode.
     */
    public int hashCode() {
        return PRIME*entity.hashCode() + identifier.hashCode();
    }
    
    /**
     * 
     * @return
     */
    public Object clone() {
        MetaField f;
        try {
            f = (MetaField) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();   
        }
        return f;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * Gets the value of type.
     * 
     * @return the value of type
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the value of length.
     * 
     * @return the value of length
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Gets the value of name.
     * 
     * @return the value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the value of identifier.
     * 
     * @return the value of identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Gets the value of hidden.
     * 
     * @return the value of hidden
     */
    public boolean getHidden() {
        return hidden;
    }
    
    /**
     * Gets the value of mandatory.
     * 
     * @return the value of mandatory
     */
    public boolean getMandatory() {
        return mandatory;
    }
    
    /**
     * Gets the value of entity.
     * 
     * @return the value of entity
     */
    public MetaEntity getEntity() {
        return entity;
    }
    
    /**
     * 
     * @return
     */
    public Object getDefaultValue() {
        return defaultValue;   
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Sets the value of type.
     * 
     * @param value
     */
    public void setType(int value) {
        type = value;
    }

    /**
     * Sets the value of length.
     * 
     * @param value
     */
    public void setLength(int value) {
        length = value;
    }
    
    /**
     * Sets the value of name.
     * 
     * @param value
     */
    public void setName(String value) {
        name = value;
    }
    
    /**
     * Sets the value of identifier.
     * 
     * @param value
     */
    public void setIdentifier(String value) {
        identifier = value;
    }
    
    /**
     * Sets the value of hidden.
     * 
     * @param value
     */
    public void setHidden(boolean value) {
        hidden = value;
    }
    
    /**
     * Sets the value of mandatory.
     * 
     * @param value
     */
    public void setMandatory(boolean value) {
        mandatory = value;
    }
    
    /**
     * Sets the value of entity.
     * 
     * @param value
     */
    public void setEntity(MetaEntity value) {
        entity = value;
    }
    
    /**
     * Sets the default value.
     * 
     * @param value
     */
    public void setDefaultValue(Object value) {
        defaultValue = value;   
    }
}
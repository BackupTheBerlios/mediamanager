package ch.fha.mediamanager.data;

/**
 * Holds meta information about the field of an entity.
 * 
 * @see MetaEntity
 * @see MetaData
 * 
 * @author crac
 * @version $Id: MetaField.java,v 1.17 2004/06/28 10:07:24 crac Exp $
 */
public final class MetaField implements Cloneable {
	
    /* Field types */
    public final static int INVALID_TYPE = 0;
	public final static int PK = 1;
    public final static int ENTRYID = 2;
    public final static int USERID = 3;
    public final static int INT = 4;
    public final static int VARCHAR = 5;
    public final static int TEXT = 6;
    public final static int BOOLEAN = 7;
    public final static int LIST = 8;
    public final static int DATE = 9;
    public final static int TIMESTAMP = 10;
    
    /* Used for creation of hashCode */
    //private final static int PRIME = 13;
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private int id = 0;
    private int type = INVALID_TYPE;
    private int length = 0;
    private String name;
    private String identifier; // Entity.Field
    private boolean hidden = false;
    private boolean mandatory = false;
    private MetaEntity entity;
    private Object defaultValue;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param name
     * @param entity
     */
    public MetaField(String name, MetaEntity entity) {
        this.name = name;
        this.entity = entity;
        this.identifier = entity.getName() + "." + name;
    }
    
    /**
     * 
     * @param name
     * @param entity
     * @param id
     */
    public MetaField(String name, MetaEntity entity, int id) {
        this(name, entity);
        this.id = id;
    }
    
    /**
     * 
     * @param type
     * @param name
     * @param entity
     */
    public MetaField(int type, String name, MetaEntity entity) {
        this(name, entity, 0);
        this.type = type;
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
        if ((id == 0) || (f.id == 0)) {
            return identifier.equals(f.identifier);
        } else {
            return (id == f.id);
        }
    }
    
    /**
     * 
     * @return Returns the hashCode.
     */
    public int hashCode() {
        if (id == 0) {
            return identifier.hashCode();
        } else {
            return id;
        }
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
    
    /**
     * 
     * @return
     */
    public String toString() {
        return identifier + ":\n" + 
           "Type: " + type + "\nLength: " + length + "\n" +
           "Default: " + defaultValue;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public int getId() {
        return id;
    }
    
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
    
    /**
     * 
     * @param id
     */
    protected void setId(int id) {
        this.id = id;
    }
}
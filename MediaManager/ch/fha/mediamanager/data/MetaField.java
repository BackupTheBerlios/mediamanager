package ch.fha.mediamanager.data;

/**
 * Holds meta information about the field of an entity.
 * 
 * @see MetaEntity
 * @see MetaData
 * 
 * @author crac
 * @version $Id: MetaField.java,v 1.19 2004/06/30 22:11:59 crac Exp $
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
        this.entity = (MetaEntity) entity.clone();
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
     * If the <code>MetaField</code> was loaded 
     * from the repository it has an unique id. In 
     * this case it only compares the two ids. If
     * none of the two have an id then it compares
     * its names plus its entity names.
     * 
     * @param f
     * @return Returns true if both 
     *      <code>MetaField</code>s are equal
     */
    public boolean equals(MetaField f) {
        if ((id == 0) || (f.id == 0)) {
            return identifier.equals(f.identifier);
        } else {
            return (id == f.id);
        }
    }
    
    /**
     * If it was loaded from the repository its unique
     * id is taken to compute the hashcode. Else it was 
     * created newly by the application and returns 
     * the hashcode of its name and its entity name.
     * 
     * @return Returns the hashCode
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
     * @return Returns a clone of the object
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
     * @return Its internal Id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the value of type.
     * 
     * @return The value of type
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
     * @return The value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the value of identifier.
     * 
     * @return The value of identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Gets the value of hidden.
     * 
     * @return The value of hidden
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
     * Gets the value of entity. It returns only 
     * a copy of the <code>MetaEntity</code> to 
     * prevent malicious mutations.
     * 
     * @return The value of entity
     */
    public MetaEntity getMetaEntity() {
        return (MetaEntity) entity.clone();
    }
    
    /**
     * 
     * @return The default value
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
    protected void setMetaEntity(MetaEntity value) {
        entity = (MetaEntity) value.clone();
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
package ch.fha.mediamanager.data;

/**
 * @author crac
 * @version $Id: MetaField.java,v 1.1 2004/06/05 14:12:48 crac Exp $
 */
public class MetaField {
	
	public static int PK = 0;
    public static int USERID = 1;
    public static int INT = 2;
    public static int VARCHAR = 3;
    public static int BOOLEAN = 4;
	
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
	private int type;
    private int length;
    private String name;
    private String identifier;
    private boolean hidden;
    private boolean mandatory;
    private MetaEntity entity;
    
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
    // ACCESSORS
    // --------------------------------
    
    /**
     * Get the value of type
     * 
     * @return the value of type
     */
    public int getType() {
        return type;
    }
    
    /**
     * Get the value of length
     * 
     * @return the value of length
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Get the value of name
     * 
     * @return the value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the value of identifier
     * 
     * @return the value of identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Get the value of hidden
     * 
     * @return the value of hidden
     */
    public boolean getHidden() {
        return hidden;
    }
    
    /**
     * Get the value of mandatory
     * 
     * @return the value of mandatory
     */
    public boolean getMandatory() {
        return mandatory;
    }
    
    /**
     * Get the value of entity
     * 
     * @return the value of entity
     */
    public MetaEntity getEntity() {
        return entity;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Set the value of type
     * 
     * @param value
     */
    public void setType(int value) {
        type = value;
    }

    /**
     * Set the value of length
     * 
     * @param value
     */
    public void setLength(int value) {
        length = value;
    }
    
    /**
     * Set the value of name
     * 
     * @param value
     */
    public void setName(String value) {
        name = value;
    }
    
    /**
     * Set the value of identifier
     * 
     * @param value
     */
    public void setIdentifier(String value) {
        identifier = value;
    }
    
    /**
     * Set the value of hidden
     * 
     * @param value
     */
    public void setHidden(boolean value) {
        hidden = value;
    }
    
    /**
     * Set the value of mandatory
     * 
     * @param value
     */
    public void setMandatory(boolean value) {
        mandatory = value;
    }
    
    /**
     * Set the value of entity
     * 
     * @param value
     */
    public void setEntity(MetaEntity value) {
        entity = value;
    }
}
package ch.fha.mediamanager.data;

/**
 * @author crac
 * @version $Id: MetaEntity.java,v 1.2 2004/06/11 11:29:57 crac Exp $
 */
public class MetaEntity implements Cloneable {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
	private String name;
    private String identifier;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param id
     */
    public MetaEntity(String id) {
    	this.identifier = id;
    	this.name = id;
    }
    
    /**
     * 
     * @param id
     * @param name
     */
    public MetaEntity(String id, String name) {
    	this.identifier = id;
    	this.name = name;
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public Object clone() {
        MetaEntity e;
        try {
            e = (MetaEntity) super.clone();
        } catch (CloneNotSupportedException c) {
            throw new InternalError();
        }
        
        return e;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
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
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
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
}

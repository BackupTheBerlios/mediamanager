package ch.fha.mediamanager.data;

/**
 * @author crac
 * @version $Id: MetaEntity.java,v 1.1 2004/06/05 14:12:48 crac Exp $
 */
public class MetaEntity {
	
    // --------------------------------
    // ATTRIBUTES
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

package ch.fha.mediamanager.data;

/**
 * Holds meta information about an entity.
 * 
 * <p>It is immutable.
 * 
 * @author crac
 * @version $Id: MetaEntity.java,v 1.4 2004/06/19 11:04:41 crac Exp $
 */
public final class MetaEntity {
	
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
     * @param e
     * @return
     */
    public boolean equals(MetaEntity e) {
        return (identifier.equals(e.identifier));   
    }
    
    /**
     * 
     * @return
     */
    public int hashCode() {
        return identifier.hashCode();   
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
}

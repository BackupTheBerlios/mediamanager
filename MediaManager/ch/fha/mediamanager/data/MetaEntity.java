package ch.fha.mediamanager.data;

/**
 * Holds meta information about an entity.
 * 
 * <p>It is immutable.
 * 
 * @author crac
 * @version $Id: MetaEntity.java,v 1.5 2004/06/20 22:42:16 crac Exp $
 */
public final class MetaEntity {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
	private String name;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param name
     */
    public MetaEntity(String name) {
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
        return (name.equals(e.name));   
    }
    
    /**
     * 
     * @return
     */
    public int hashCode() {
        return name.hashCode();   
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * Get the value of name.
     * 
     * @return the value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the value of identifier.
     * 
     * @deprecated Use getName() instead.
     * @return
     */
    public String getIdentifier() {
        return name;   
    }
}

package ch.fha.mediamanager.data;

/**
 * Holds meta information about an entity.
 * 
 * <p>It is immutable.
 * 
 * @author crac
 * @version $Id: MetaEntity.java,v 1.6 2004/06/23 13:54:10 crac Exp $
 */
public final class MetaEntity {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
	private String name;
    private int id = 0;
    
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
    
    /**
     * 
     * @param name
     * @param id
     */
    public MetaEntity(String name, int id) {
        this.name = name;
        this.id = id;
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
     * Gets the value of name.
     * 
     * @return the value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return Returns id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the value of identifier.
     * 
     * @deprecated Use getName() instead.
     * @return
     */
    public String getIdentifier() {
        return name;   
    }
}

package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: User.java,v 1.2 2004/06/11 12:47:36 crac Exp $
 */
public final class User {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private String uuId; // immutable
    private String name;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public String getUUID() {
        return uuId;
    }

    /**
     * 
     * @return
     * @return
     */
    public String getName() {
        return name;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}

package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: User.java,v 1.4 2004/06/21 12:56:41 crac Exp $
 */
public final class User {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private String uuId; // immutable
    private String name;
    private String username;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param uuId
     * @param name
     * @param username
     */
    public User(String uuId, String name, String username) {
        this.uuId = uuId;
        this.name = name;
        this.username = username;
    }
    
    /**
     * 
     * @param uuId
     */
    public User(String uuId) {
        this.uuId = uuId;
    }
    
    /**
     * 
     *
     */
    public User() {}
    
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
     */
    public String getUsername() {
        return username;   
    }

    /**
     * 
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

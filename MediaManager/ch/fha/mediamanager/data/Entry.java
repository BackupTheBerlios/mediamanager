package ch.fha.mediamanager.data;

import java.sql.Timestamp;

/**
 * 
 * 
 * @author luca
 * @version $Id: Entry.java,v 1.5 2004/06/27 10:05:11 crac Exp $
 */
public final class Entry implements Cloneable {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private int id = 0;
    private Timestamp creation = 
        new Timestamp(System.currentTimeMillis());
    private Timestamp modified = 
        new Timestamp(System.currentTimeMillis());
    //private User owner = null;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * 
     */
    public Entry() {}
    
    /**
     * 
     * @param id
     * @param creation
     * @param edit
     * @param owner
     */
    public Entry(int id, Timestamp creation, Timestamp edit) {
        this.id = id;
        this.creation = creation;
        this.modified = edit;
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public Object clone() {
       Entry entry;
       try {
           entry = (Entry) super.clone();
       } catch(CloneNotSupportedException e) {
           throw new InternalError();
       }
       return entry;
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
     * 
     * @return
     */
    public Timestamp getCreation() {
        return creation;   
    }
    
    /**
     * 
     * @return
     */
    public Timestamp getLastModified() {
        return modified;   
    }
    
    /*
     * 
     * @return
     */
    /*public User getUser() {
        return owner;   
    }*/
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param stamp
     */
    protected void setLastModified(Timestamp stamp) {
        modified = stamp;
    }
    
    /**
     * 
     * @param id
     */
    protected void setId(int id) {
        this.id = id;
    }
    
    /*
     * 
     * @param user
     */
    /*public void setOwner(User user) {
        owner = user;
    }*/
}

package ch.fha.mediamanager.data;

import java.sql.Timestamp;

/**
 * @author luca
 * @version $Id: Entry.java,v 1.1 2004/06/21 12:56:26 crac Exp $
 */
public final class Entry implements Cloneable {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private int entryId = 0;
    private Timestamp creation = new Timestamp(System.currentTimeMillis());
    private Timestamp edit = new Timestamp(System.currentTimeMillis());
    private User owner = null;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param id
     * @param creation
     * @param edit
     * @param owner
     */
    public Entry(int id, Timestamp creation, Timestamp edit, User owner) {
        entryId = id;
        this.creation = creation;
        this.edit = edit;
        this.owner = owner;
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
        return entryId;   
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
    public Timestamp getEdit() {
        return edit;   
    }
    
    /**
     * 
     * @return
     */
    public User getUser() {
        return owner;   
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param id
     */
    protected void setId(int id) {
        entryId = id;
    }
}

package ch.fha.mediamanager.data;

/**
 * Holds meta information about an entity.
 * 
 * <p>It is almost immutable.
 * 
 * @see MetaData
 * @see MetaField
 * 
 * @author crac
 * @version $Id: MetaEntity.java,v 1.9 2004/06/30 22:11:59 crac Exp $
 */
public final class MetaEntity implements Cloneable {
	
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
     * Compares its name to the name of an other 
     * <code>MetaEntity</code>.
     * 
     * @param e
     * @return Returns true if both 
     *      <code>MetaEntity</code>s are equal
     */
    public boolean equals(MetaEntity e) {
        return (name.equals(e.name));   
    }
    
    /**
     * Returns the hashcode of its name.
     * 
     * @return Returns its hashCode
     */
    public int hashCode() {
        return name.hashCode();   
    }
    
    /**
     * 
     * @return Returns a clone of the object
     */
    public Object clone() {
        MetaEntity entity;
        try {
            entity = (MetaEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return entity;
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
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param id
     */
    protected void setId(int id) {
        this.id = id;
    }
}

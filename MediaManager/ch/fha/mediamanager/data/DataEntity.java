package ch.fha.mediamanager.data;

/**
 * 
 * 
 * 
 * @author crac
 * @version $Id: DataEntity.java,v 1.3 2004/05/22 10:50:26 crac Exp $
 */
public class DataEntity {

    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
    private String name;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param name
     */
    public DataEntity(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @param o
     * @return
     */
    public boolean equals(DataEntity o) {
        return name.equals(o.getName());
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

/**
 *
 *
 * @author crac
 * @version $Id: DataEntity.java,v 1.3 2004/05/22 10:50:26 crac Exp $
 */ 
class DataField {
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------

    private Object data;
    private String name;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------

    /**
     * 
     * 
     * @param name
     * @param data
     */
    public DataField(String name, Object data) {
       this.name = name;
       this.data = data;
    }
    
    /**
     * 
     * @param o
     * @return
     */
    public boolean equals(DataField o) {
        return name.equals(o.getName());
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
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return
     */
    public Object getData() {
        return data;
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

    /**
     * 
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }
}

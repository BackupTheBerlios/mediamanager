package ch.fha.mediamanager.data;


/**
 * 
 * 
 * @author luca
 * @version $Id: QueryCondition.java,v 1.2 2004/05/21 17:45:28 crac Exp $
 */
public class QueryCondition {
    
    public static final int EQUALS = 0;
    public static final int GREATER = 1;
    public static final int LESSER = 2;
    public static final int GREATER_EQUALS = 3;
    public static final int LESSER_EQUALS = 4;
    public static final int DONT_EQUALS = 5;
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
    private DataEntity entity;
    private DataField field;
    private int connector;
    private Object value;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param de    Entity
     * @param df    Field to compare
     * @param comp  Comparator
     * @param value Compare to this value
     */
    public QueryCondition(DataEntity de, DataField df, int comp, Object value) {
        this.entity = de;
        this.field = df;
        this.comparator = comp;
        this.value = value;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------

    /**
     * 
     * @return
     */
    public DataEntity getEntity() {
        return entity;
    }
    
    /**
     * 
     * @return
     */
    public DataField getField() {
        return field;
    }
    
    /**
     * 
     * @return
     */
    public int getComparator() {
        return comparator;
    }
    
    /**
     * Returns the object value to compare to.
     * 
     * @return 
     */
    public Object getValue() {
        return value;
    }
}

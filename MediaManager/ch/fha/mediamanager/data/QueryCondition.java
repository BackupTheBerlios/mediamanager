package ch.fha.mediamanager.data;

/**
 * 
 * 
 * @author crac
 * @version $Id: QueryCondition.java,v 1.8 2004/06/27 09:22:26 crac Exp $
 */
public class QueryCondition {
    
    // Comparator types
    public static final int INVALID_TYPE = 0;
    public static final int EQUALS = 1;
    public static final int GREATER = 2;
    public static final int LESSER = 3;
    public static final int GREATER_EQUALS = 4;
    public static final int LESSER_EQUALS = 5;
    public static final int DONT_EQUALS = 6;
    public static final int LIKE = 7;
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private MetaEntity entity;
    private Field field;
    private int comparator = INVALID_TYPE;
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
    public QueryCondition(Field df, int comp, Object value) {
        this.entity = df.getMetaEntity();
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
    protected MetaEntity getMetaEntity() {
        return entity;
    }
    
    /**
     * 
     * @return
     */
    protected Field getField() {
        return field;
    }
    
    /**
     * 
     * @return
     */
    protected int getComparator() {
        return comparator;
    }
    
    /**
     * Returns the object value to compare to.
     * 
     * @return 
     */
    protected Object getValue() {
        return value;
    }
}

package ch.fha.mediamanager.data;

/**
 * 
 * 
 * @author crac
 * @version $Id: QueryCondition.java,v 1.4 2004/06/05 14:13:06 crac Exp $
 */
public class QueryCondition {
    
    public static final int EQUALS = 0;
    public static final int GREATER = 1;
    public static final int LESSER = 2;
    public static final int GREATER_EQUALS = 3;
    public static final int LESSER_EQUALS = 4;
    public static final int DONT_EQUALS = 5;
    public static final int LIKE = 6;
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
    private MetaEntity entity;
    private Field field;
    private int comparator;
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
        this. entity = df.getEntity();
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
    public MetaEntity getEntity() {
        return entity;
    }
    
    /**
     * 
     * @return
     */
    public Field getField() {
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

package ch.fha.mediamanager.data;


/**
 * 
 * 
 * @author luca
 * @version $Id: QueryCondition.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */
public class QueryCondition {
    
    public static final int EQUALS = 0;
    public static final int GREATER = 1;
    public static final int LESSER = 2;
    public static final int GREATER_EQUALS = 3;
    public static final int LESSER_EQUALS = 4;
    public static final int DONT_EQUALS = 5;
    
    private DataEntity entity;
    private DataField field;
    private int connector;
    private Object value;
    
    /**
     * 
     * @param de    Entity
     * @param df    Field to compare
     * @param conn  Connector
     * @param value Compare to this value
     */
    public QueryCondition(DataEntity de, DataField df, int conn, Object value) {
        this.entity = de;
        this.field = df;
        this.connector = conn;
        this.value = value;
    }

}

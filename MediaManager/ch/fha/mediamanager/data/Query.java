package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * @author luca
 * @version $Id: Query.java,v 1.2 2004/06/26 09:58:07 crac Exp $
 */
public interface Query {
    
    // 
    public static final int OR = 1;           // +
    public static final int AND = 2;          // *
    public static final int BRACE_OPEN = 3;   // (
    public static final int BRACE_CLOSE = 4;  // )
    
    // query types
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int LOAD = 3;
    public static final int DELETE = 4;
    public static final int ENTITY_CREATE = 5;
    public static final int ENTITY_DELETE = 6;
    public static final int ENTITY_FIELD_CREATE = 7;
    public static final int FIELD_CREATE = 8;
    public static final int FIELD_DELETE = 9;
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    public DataSet run();
    public boolean admin(MetaEntity entity);
    public boolean admin(MetaField field);
    public boolean admin(MetaEntity entity, MetaField[] fields);

    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * Returns type of the Query
     * 
     * @return Returns type of the Query
     */
    public int getType();
    
    /**
     * 
     * @return
     */
    public Vector getQueryVector();
    
    /**
     * 
     * @return
     */
    public MetaEntity getEntity();
    
    /**
     * 
     * @return
     */
    public Vector getFields();
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Sets the type of the Query.
     *
     * @param type
     */
    public void setType(int type);
}

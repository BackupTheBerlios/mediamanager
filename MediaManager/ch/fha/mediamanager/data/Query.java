package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * @author luca
 * @version $Id: Query.java,v 1.1 2004/06/25 16:04:33 crac Exp $
 */
public interface Query {
    
    //   
    public static final int OR = 0;           // +
    public static final int AND = 1;          // *
    public static final int BRACE_OPEN = 3;   // (
    public static final int BRACE_CLOSE = 4;  // )
    
    // query types
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int LOAD = 2;
    public static final int DELETE = 3;
    public static final int ENTITY_CREATE = 4;
    public static final int ENTITY_DELETE = 5;
    public static final int FIELD_ADD = 6;
    public static final int FIELD_DELETE = 7;
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    public DataSet run();

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
    public Vector getVector();
    
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

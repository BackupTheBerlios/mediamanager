package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author crac
 * @version $Id: QueryRequest.java,v 1.8 2004/06/05 14:13:06 crac Exp $
 */
public class QueryRequest {
    
    // TODO: has to be auto-loaded from config
    private Repository repository = new DatabaseRepository();
    
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
    public static int ENTITY_CREATE = 4;
    public static int ENTITY_DELETE = 5;
    public static int FIELD_ADD = 6;
    public static int FIELD_DELETE = 7;
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------

    private DataSet dSet;
    private int type;
    private Vector request;
    private Set entitySet = new HashSet(5);
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param vec
     * @param type
     */
    public QueryRequest(Vector vec, int type) {
        parse(vec);
        this.type = type;
        this.request = vec;
    }
    
    /**
     * 
     * @param dSet
     * @param type
     */
    public QueryRequest(DataSet dSet, int type) {
        this.dSet = dSet;
        this.type = type;
    }
    
    /**
     * Parses the request vector and checks the
     * syntax for correctness.
     * 
     * @param vec
     */
    private void parse(Vector vec) {
        
        // create entitySet
        for(int i = 0; i < vec.size(); i++) {
            if (vec.elementAt(i) instanceof QueryCondition) {
                MetaEntity tmp = 
                    ((QueryCondition) vec.elementAt(i)).getEntity();
                
                if (! entitySet.contains(tmp))  entitySet.add(tmp);
            }
        }
        
        // TODO
    }
    
    /**
     * 
     * @return  DataSet
     */
    public DataSet run() {
        switch (this.type) {
            case(LOAD):
                return repository.load(this);
            case(UPDATE):
                return repository.update(dSet);
            case(DELETE):
                return repository.delete(dSet);
            
        }
        
        return null;
    }

    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * Returns type of the QueryRequest.
     * 
     * @return  int Type of request
     */
    public int getType() {
        return type;
    }
    
    /**
     * 
     * @return
     */
    public Vector getVector() {
        return request;
    }
    
    /**
     * 
     * @return
     */
    public Set getEntities() {
        return entitySet;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Sets the type of the QueryRequest.
     *
     * @param   int Type of request
     */
    public void setType(int type) {
        this.type = type;
    }
}
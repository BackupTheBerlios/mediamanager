package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author crac
 * @version $Id: QueryRequest.java,v 1.5 2004/05/22 13:01:27 crac Exp $
 */
public class QueryRequest {
    
    // TODO: has to be auto-loaded from config
    private Repository repository = new DatabaseRepository();
    
    public static final int OR = 0;          // +
    public static final int AND = 1;         // *
    public static final int BRACE_OPEN = 3;   // (
    public static final int BRACE_CLOSE = 4;  // )
    
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
                DataEntity tmp = 
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
            case(Repository.LOAD):
                return repository.load(this);
            case(Repository.UPDATE):
                return repository.update(dSet);
            case(Repository.DELETE):
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
package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 *
 * @author crac
 * @version $Id: QueryRequest.java,v 1.2 2004/05/21 17:45:28 crac Exp $
 */
public class QueryRequest {
    
    // TODO: has to be auto-loaded from config
    private Repository repository = new DatabaseRepository();
    
    public static final int OR = 0;          // +
    public static final int AND = 1;         // *
    public static final int BACE_OPEN = 3;   // (
    public static final int BACE_CLOSE = 4;  // )
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------

    private DataSet dSet;
    private int type;
    private Vector request;
    
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
        // TODO
    }
    
    /**
     * 
     * @return  DataSet
     */
    public DataSet run() {
        switch (this.type) {
            case(Repository.DISPLAY):
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
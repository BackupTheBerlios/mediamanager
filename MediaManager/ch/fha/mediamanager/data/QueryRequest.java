package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 *
 * @author crac
 * @version $Id: QueryRequest.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */
public class QueryRequest {
    
    // TODO: has to be auto-loaded from config
    private Repository repository = new DatabaseRepository();
    
    public static final int OR = 0;     // +
    public static final int AND = 1;    // *
    public static final int NOR = 2;    // -
    public static final int NAND = 3;   // /
    public static final int OPEN = 4;   // (
    public static final int CLOSE = 5;  // )

    private DataSet dSet;
    private int type;
    private Vector request;
    
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

    /**
     * Returns type of the QueryRequest.
     * 
     * @return  int Type of request
     */
    public int getType() {
        return type;
    }
    
    /**
     * Sets the type of the QueryRequest.
     *
     * @param   int Type of request
     */
    public void setType(int type) {
        this.type = type;
    }
}
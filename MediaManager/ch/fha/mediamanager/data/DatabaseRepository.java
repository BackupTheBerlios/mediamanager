package ch.fha.mediamanager.data;

/**
 *
 *
 * @author crac
 * @version $Id: DatabaseRepository.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */
public class DatabaseRepository implements Repository {
    
    private DatabaseConnection lnkDatabaseConnection;

    /**
     * 
     * @param ds
     * @return
     */
    public DataSet insert(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet update(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet delete(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return 
     */
    public DataSet load(QueryRequest qr) {
        return null;
    }
}

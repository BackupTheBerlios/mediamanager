package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: Repository.java,v 1.2 2004/05/22 13:01:27 crac Exp $
 */
public interface Repository {
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int LOAD = 2;
    public static final int DELETE = 3;
    
    public DataSet update(DataSet ds);
    public DataSet insert(DataSet ds);
    public DataSet delete(DataSet ds);
    public DataSet load(QueryRequest qr);
}

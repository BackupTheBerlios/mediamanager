package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: Repository.java,v 1.3 2004/05/29 18:10:41 crac Exp $
 */
public interface Repository {
    public DataSet update(DataSet ds);
    public DataSet insert(DataSet ds);
    public DataSet delete(DataSet ds);
    public DataSet load(QueryRequest qr);
}

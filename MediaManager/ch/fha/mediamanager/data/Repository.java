package ch.fha.mediamanager.data;

import javax.swing.JPanel;

/**
 * A Repository holds the data.
 *
 * @author crac
 * @version $Id: Repository.java,v 1.8 2004/06/19 09:42:31 crac Exp $
 */
public interface Repository {
    public DataSet update(DataSet ds);
    public DataSet insert(DataSet ds);
    public DataSet delete(DataSet ds);
    public DataSet load(QueryRequest qr);
    
    public boolean create(MetaEntity entity);
    public boolean create(MetaField field);
    public boolean delete(MetaEntity entity);
    public boolean delete(MetaField field);
    
    public MetaData loadMetaData();
    
    public JPanel getConfPanel();
    public String getName();
    
    public void initialize();
    public void connect();
    public void disconnect();
}

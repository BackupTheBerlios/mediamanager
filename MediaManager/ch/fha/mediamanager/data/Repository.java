package ch.fha.mediamanager.data;

import javax.swing.JPanel;

/**
 * A Repository holds the data.
 *
 * @author crac
 * @version $Id: Repository.java,v 1.6 2004/06/15 12:01:07 ia02vond Exp $
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
}

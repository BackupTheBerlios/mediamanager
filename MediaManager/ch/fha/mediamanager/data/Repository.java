package ch.fha.mediamanager.data;

import javax.swing.JPanel;

/**
 * A Repository holds the data.
 *
 * @author crac
 * @version $Id: Repository.java,v 1.11 2004/06/25 16:06:18 crac Exp $
 */
public interface Repository {
    
    public DataSet update(DataSet ds);
    public DataSet insert(DataSet ds);
    public DataSet delete(DataSet ds);
    public DataSet load(AbstractQuery qr);
    
    public boolean create(MetaEntity entity, MetaField[] fields);
    public boolean create(MetaEntity entity);
    public boolean create(MetaField field);
    public boolean delete(MetaEntity entity);
    public boolean delete(MetaField field);
    
    public MetaData loadMetaData();
    
    public void insertUser(User user);
    public void deleteUser(User user);
    public void updateUser(User user);
    
    public JPanel getConfPanel();
    public String getName();
    public Query getQuery();
    
    public MetaData initialize();
    public void connect();
    public void disconnect();
}

package ch.fha.mediamanager.data;

/**
 * A Repository holds all the necessary data.
 * 
 * @see AbstractQuery
 * @see DataSet
 * @see MetaData
 *
 * @author crac
 * @version $Id: Repository.java,v 1.13 2004/06/26 09:57:00 crac Exp $
 */
public abstract class Repository {
    
    abstract protected DataSet update(DataSet ds);
    abstract protected DataSet insert(DataSet ds);
    abstract protected DataSet delete(DataSet ds);
    abstract protected DataSet load(AbstractQuery qr);
    
    abstract protected boolean create(MetaEntity entity, MetaField[] fields);
    abstract protected boolean create(MetaEntity entity);
    abstract protected boolean create(MetaField field);
    abstract protected boolean delete(MetaEntity entity);
    abstract protected boolean delete(MetaField field);
    
    abstract protected MetaData loadMetaData();
    
    abstract protected void insertUser(User user);
    abstract protected void deleteUser(User user);
    abstract protected void updateUser(User user);
    
    abstract public javax.swing.JPanel getConfPanel();
    abstract public String getName();
    abstract protected Class getQueryClass();
    
    abstract protected MetaData initialize();
    abstract protected void connect();
    abstract protected void disconnect();
}

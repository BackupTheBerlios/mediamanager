package ch.fha.mediamanager.data;

/**
 * A Repository holds all the necessary data.
 * 
 * @see AbstractQuery
 * @see DataSet
 * @see MetaData
 *
 * @author crac
 * @version $Id: Repository.java,v 1.15 2004/06/26 13:16:03 crac Exp $
 */
public abstract class Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    protected boolean connected = false;
    protected static String name = "Repository";
    protected static Class query = AbstractQuery.class;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    abstract protected DataSet update(DataSet ds);
    abstract protected DataSet insert(DataSet ds);
    abstract protected DataSet delete(DataSet ds);
    abstract protected DataSet load(AbstractQuery qr);
    
    abstract protected boolean create(MetaEntity entity, MetaField[] fields);
    abstract protected boolean create(MetaEntity entity);
    abstract protected boolean create(MetaField field);
    abstract protected boolean delete(MetaEntity entity);
    abstract protected boolean delete(MetaField field);
    
    abstract protected void insertUser(User user);
    abstract protected void deleteUser(User user);
    abstract protected void updateUser(User user);
    
    abstract public javax.swing.JPanel getConfPanel();
    abstract protected MetaData initialize();
    abstract protected void connect();
    abstract protected void disconnect();
    
    /**
     * 
     * @return Returns true if connection to 
     *      repository is established
     */
    protected boolean isConnected() {
        return connected;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    protected Class getQueryClass() {
        return query;
    }
    
    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }
}

package ch.fha.mediamanager.data;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A Repository holds all the necessary data.
 * 
 * @see AbstractQuery
 * @see DataSet
 * @see MetaData
 *
 * @author crac
 * @version $Id: Repository.java,v 1.17 2004/06/28 14:12:20 crac Exp $
 */
public abstract class Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    protected boolean connected = false;
    protected static String name = "Repository";
    protected static Class query = AbstractQuery.class;
    
    private LinkedList repositoryListeners = new LinkedList();
    
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
    
    /**
     * 
     * @param l
     */
    public void addRepositoryListener(RepositoryListener l) {
        repositoryListeners.add(l);
    }
    
    /**
     * 
     * @param l
     */
    public void removeRepositoryListener(RepositoryListener l) {
        repositoryListeners.remove(l);
    }
    
    /**
     * 
     * @param metaEntity
     */
    public void fireDataChanged(MetaEntity metaEntity) {
        Iterator it = repositoryListeners.iterator();
        while (it.hasNext()) {
            ((RepositoryListener)it.next()).dataChanged(metaEntity);
        }
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
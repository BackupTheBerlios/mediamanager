package ch.fha.mediamanager.data;

import java.io.FileNotFoundException;

import java.lang.reflect.InvocationTargetException;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.KeyPointEvent;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.38 2004/07/01 19:42:55 crac Exp $
 */
public final class DataBus {
	
    // --------------------------------
    // FIELDS
    // --------------------------------
	
    /**
     * Logger. The configuration file is 
     * <code>conf/logging.conf</conf>.
     */
    public static Logger logger = 
        Logger.getLogger("ch.fha.mediamanager");
    
    static {
        String file = "conf" + java.io.File.separator + "logging.conf";
        PropertyConfigurator.configure(file);
    }
    
    //private static User user;
    
    private static MetaData metaData;
	private static AbstractRepository[] repositories;
	private static AbstractRepository   currentRepository;
    private static Query query;
	
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Attaches the application to a data repository.
     */
    public static void initialize() {
    	try {
    	    repositories = RepositoryLoader.loadRepositories();
    	} catch (FileNotFoundException e) {
    		throw new InternalError("No repositories found.");
    	}
        if (repositories != null) {
        	currentRepository = repositories[0];
        } else {
        	throw new InternalError("No repositories found.");
        }
    }
    
    /**
     * Connects to the repository and loads its 
     * <code>MetaData</code>.
     * 
     * @see MetaData
     * @see AbstractRepository#initialize()
     */
    public static void connect() {
        if (currentRepository != null) {
            metaData = currentRepository.initialize();
            //user = new User();
            
            DataBus.logger.info("Repository connected.");
            MainFrame.getInstance().getMainActionListener().fireAction(KeyPointEvent.POST_CONNECT);
        } else {
            DataBus.logger.info("No repository available.");
            MainFrame.getInstance().getMainActionListener().fireAction(KeyPointEvent.CONNECT_ERROR);
        }
    }
    
    /**
     * Used for testing the data repository from 
     * outside the application.
     * 
     * <p>Test the data package by invoking</p>
     * 
     * <p><code>
     * DataBus.initialize();
     * DataBus.logger.info("App started.");
     * DataBus.connectStandalone();
     * // ... some code ...
     * DataBus.disconnectStandalone();
     * DataBus.logger.info("App stoped.");
     * </code>
     * 
     * @see disconnectStandalone()
     */
    public static void connectStandalone() {
        if (currentRepository != null) {
            metaData = currentRepository.initialize();
            //user = new User();
            
            DataBus.logger.info("Repository connected.");
        } else {
            DataBus.logger.info("No repository available.");
        }
    }
    
    /**
     * Disconnects from the repository.
     * 
     * @see AbstractRepository#disconnect()
     */
    public static void disconnect() {
    	MainFrame mf = MainFrame.getInstance();
    	if (currentRepository != null) {
            currentRepository.disconnect();
            metaData = null;
            DataBus.logger.info("Repository disconnected.");
            if(mf.exiting) {
            	mf.getMainActionListener().fireAction(
                   KeyPointEvent.WINDOW_FINAL_EXIT
                );
            } else {
            	mf.getMainActionListener().fireAction(
                   KeyPointEvent.POST_DISCONNECT
                );
            }
        } else {
            DataBus.logger.info("No repository available.");
            MainFrame.getInstance().getMainActionListener().fireAction(KeyPointEvent.DISCONNECT_ERROR);
        }
    }
    
    /**
     * 
     * @see #connectStandalone()
     */
    public static void disconnectStandalone() {
        if (currentRepository != null) {
            currentRepository.disconnect();
            metaData = null;
            DataBus.logger.info("Repository disconnected.");
        } else {
            DataBus.logger.info("No repository available.");
        }
    }
	
    // --------------------------------
    // ACCESSORS
    // --------------------------------
	
    /**
     * 
     * @param vec
     * @param type
     * 
     * @return Returns query handler of the 
     *      active repository
     */
    public static AbstractQuery getQueryInstance(
        java.util.Vector vec,
        int type) {
        
        try {
            Object[] args = {vec, new Integer(type)};
            Class[] argTypes = {vec.getClass(), int.class};
            Class qrClass = 
                currentRepository.getQueryClass();

        	AbstractQuery qr = (AbstractQuery)
                qrClass.getConstructor(argTypes).newInstance(args);
            return qr;
            
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e ){
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * @param ds
     * @param type
     * 
     * @return Returns query handler of the 
     *      active repository
     */
    public static AbstractQuery getQueryInstance(
        DataSet ds, 
        int type) {
        
        try {
            Object[] args = {ds, new Integer(type)};
            Class[] argTypes = {ds.getClass(), int.class};
            Class qrClass = 
                currentRepository.getQueryClass();

            AbstractQuery qr = (AbstractQuery)
                qrClass.getConstructor(argTypes).newInstance(args);
            return qr;
            
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e ){
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * @param type
     * 
     * @return Returns query handler of the 
     *      active repository
     */
    public static AbstractQuery getQueryInstance(
        int type) {
        
        try {
            Object[] args = {new Integer(type)};
            Class[] argTypes = {int.class};
            Class qrClass = 
                currentRepository.getQueryClass();

            AbstractQuery qr = (AbstractQuery)
                qrClass.getConstructor(argTypes).newInstance(args);
            return qr;
            
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e ){
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
	/**
	 * 
	 * @return
	 */
	public static AbstractRepository getRepository() {
	    return currentRepository;
	}
	
    /**
     * 
     * @return
     */
	public static AbstractRepository[] getRepositories() {
		return repositories;
	}
	
	/**
	 * 
     * @see MetaData
     * 
	 * @return Returns the MetaData
	 */
	protected static MetaData getMetaData() {
	    return metaData;
	}
    
    /**
     * 
     * @see MetaField
     * @see MetaData
     * 
     * @return Returns Set of all MetaFields
     */
    public static LinkedHashSet getMetaFields() {
        return metaData.getMetaFields();
    }
    
    /**
     * 
     * @see MetaEntity
     * @see MetaData
     * 
     * @return Returns Set of all MetaEntities
     */
    public static LinkedHashSet getMetaEntities() {
        return metaData.getMetaEntities();
    }
    
    /**
     * 
     * @param entity
     * @return Returns Primary Key <code>Field</code> of 
     *      the <code>MetaEntity</code>
     */
    public static Field getPKField(MetaEntity entity) {
        DataElement el = getDefaultElement(entity);
        return el.getPKField();
    }
    
    /**
     * 
     * @see #getPKField(MetaEntity)
     * 
     * @param entity
     * @return Returns Primary Key <code>Field</code> of 
     *      the <code>MetaEntity</code>
     */
    public static Field getPKField(String entity) {
        return getPKField(new MetaEntity(entity));
    }
    
    /**
     * 
     * @return
     */
    public static Logger getLogger(String pkg) {
        return Logger.getLogger(pkg);
    }
    
    /**
     * Creates a <code>DataElement</code> based on the 
     * definition of a <code>MetaEntitiy</code>. The 
     * fields are filled with their default values.
     * 
     * @see DataElement
     * @see MetaData
     * 
     * @param e
     * @return Returns a <code>DataElement</code> 
     *      of <code>Field</code>s with default value 
     *      of the requested of the 
     *      <code>MetaEntity</code>
     */
    public static DataElement getDefaultElement(MetaEntity e) {
        Set set = metaData.getMetaFields(e);
        
        if (set.size() == 0) return null;
        
        DataElement el = new DataElement(e);
        java.util.Iterator it = set.iterator();
        
        while (it.hasNext()) {
            MetaField mf = (MetaField) it.next();
            Field f;
            if (mf.getType() == MetaField.LIST) {
                f = new Field(
                    mf,
                    ((String[]) mf.getDefaultValue())[0]
                );
            } else {
                f = new Field(
                    mf,
                    mf.getDefaultValue()
                );
            }
            el.add(f);
        }
        
        return el;
    }
    
    /**
     * 
     * @see #getDefaultElement(MetaEntity)
     * 
     * @param e
     * @return Returns a <code>DataElement</code> 
     *      of <code>Field</code>s with default value 
     *      of the requested name of the 
     *      <code>MetaEntity</code>
     */
    public static DataElement getDefaultElement(String e) {
        return getDefaultElement(new MetaEntity(e));
    }
    
    /*
     * 
     * @return Returns user data of the user 
     *      running the application
     */
    /*public static User getUser() {
        return user;
    }*/
	
    // --------------------------------
    // MUTATORS
    // --------------------------------
	
	/**
	 * Changes the current Repository to a new one, 
     * but only if current one has been disconnected. 
     * Do not forget to call in advance
     * 
     * <p><code>DataBus.disconnect();</code></p>
     * 
	 * @param rep
     * @throws CurrentRepositoryConnectedException if 
     *      repository was not disconnected in 
     *      advance
	 */
	public static void setRepository(AbstractRepository rep) {
        if (! currentRepository.isConnected()) {
            currentRepository = rep;
        } else {
            throw new CurrentRepositoryConnectedException(
                "Current repository " + 
                currentRepository.getName() + 
                "still connected!"
            );
        }
	}
}

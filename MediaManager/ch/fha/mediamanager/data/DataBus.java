package ch.fha.mediamanager.data;

import java.io.FileNotFoundException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.18 2004/06/24 15:58:51 crac Exp $
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
    
    private static User user;
    
    private static MetaData metaData;
	private static Repository[] repositories;
	private static Repository   currentRepository;
	
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Attaches the application to a data repository and
     * loads all its meta-data.
     * 
     * @see Repository
     * @see MetaField
     * @see MetaEntity
     * @see MetaData
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
     * Connects to the repository.
     */
    public static void connect() {
        if (currentRepository != null) {
            metaData = currentRepository.initialize();
            user = new User();
            
            DataBus.logger.info("Repository connected.");
        } else {
            DataBus.logger.info("No repository available.");   
        }
    }
    
    /**
     * Disconnects from the repository.
     */
    public static void disconnect() {
        if (currentRepository != null) {
            currentRepository.disconnect();
            DataBus.logger.info("Repository disconnected.");
        } else {
            DataBus.logger.info("No repository available.");   
        }
    }
    
    /**
     * 
     * @deprecated Use #connect() instead.
     * 
     * @see #connect()
     * @see #disconnect()
     */
    public static void loadRepository() {
        if (currentRepository != null) {
            metaData = currentRepository.initialize();
        } else {
            DataBus.logger.info("No repository available.");   
        }
    }
	
    // --------------------------------
    // ACCESSORS
    // --------------------------------
	
	/**
	 * 
	 * @return
	 */
	public static Repository getRepository() {
	    return currentRepository;
	}
	
    /**
     * 
     * @return
     */
	public static Repository[] getRepositories() {
		return repositories;
	}
	
	/**
	 * 
     * @see MetaData
     * 
	 * @return Returns the MetaData
	 */
	public static MetaData getMetaData() {
	    return metaData;
	}
    
    /**
     * 
     * @see MetaField
     * @see MetaData
     * 
     * @return Returns Set of all MetaFields
     */
    public static Set getMetaFields() {
        return metaData.getFields();
    }
    
    /**
     * 
     * @see MetaEntity
     * @see MetaData
     * 
     * @return Returns Set of all MetaEntities
     */
    public static Set getMetaEntities() {
        return metaData.getEntities();
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
        DataElement el = new DataElement(e);
        java.util.Iterator it = set.iterator();
        
        while (it.hasNext()) {
            MetaField mf = (MetaField) it.next();
            Field f = new Field(mf, mf.getDefaultValue());
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
    
    /**
     * 
     * @return Returns user data of the user 
     *      running the application
     */
    public static User getUser() {
        return user;
    }
	
    // --------------------------------
    // MUTATORS
    // --------------------------------
	
	/**
	 * 
	 * @param repository
	 */
	public void setRepository(Repository rep) {
	    currentRepository = rep;
	}
}

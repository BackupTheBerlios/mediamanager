package ch.fha.mediamanager.data;

import java.io.FileNotFoundException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.8 2004/06/19 09:43:51 crac Exp $
 */
public class DataBus {
	
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
     * 
     *
     */
    public static void loadRepository() {
        if (currentRepository != null) {
            currentRepository.initialize();
            metaData = currentRepository.loadMetaData();
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
	
	public static Repository[] getRepositories() {
		return repositories;
	}
	
	/**
	 * 
	 * @return
	 */
	public MetaData getMetaData() {
	    return metaData;
	}
    
    /**
     * 
     * @return
     */
    public Set getMetaFields() {
        return metaData.getFields();
    }
    
    /**
     * 
     * @return
     */
    public Set getMetaEntities() {
        return metaData.getEntities();
    }
    
    /**
     * 
     * @return
     */
    public static Logger getLogger(String pkg) {
        return Logger.getLogger(pkg);
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

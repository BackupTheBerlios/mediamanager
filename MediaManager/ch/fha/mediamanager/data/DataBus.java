package ch.fha.mediamanager.data;

import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.4 2004/06/10 14:02:20 crac Exp $
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
	private static Repository repository;
	
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Initializes the databus with a repository and
     * its metadata.
     * 
     * @see Repository
     * @see MetaField
     * @see MetaEntity
     */
    public static void initialize() {
    	repository = new DatabaseRepository();
        metaData = repository.loadMetaData();
    }
	
    // --------------------------------
    // ACCESSORS
    // --------------------------------
	
	/**
	 * 
	 * @return
	 */
	public static Repository getRepository() {
		return repository;
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
		repository = rep;
	}
}

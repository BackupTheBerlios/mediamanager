package ch.fha.mediamanager.data;

import java.util.Set;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.2 2004/06/05 18:24:42 crac Exp $
 */
public class DataBus {
	
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
	
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
	public Repository getRepository() {
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

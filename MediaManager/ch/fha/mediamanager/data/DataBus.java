package ch.fha.mediamanager.data;

import java.util.Set;

/**
 * 
 * 
 * @author crac
 * @version $Id: DataBus.java,v 1.1 2004/06/05 16:26:56 crac Exp $
 */
public class DataBus {
	
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
	
	private static Set metaFields;
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
        metaFields = repository.loadMetaData();
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
	public Set getMetaFields() {
		return metaFields;
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

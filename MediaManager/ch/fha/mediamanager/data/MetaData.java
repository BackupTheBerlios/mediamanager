package ch.fha.mediamanager.data;

import java.util.Set;

/**
 * @author crac
 * @version $Id: MetaData.java,v 1.1 2004/06/05 18:20:47 crac Exp $
 */
public class MetaData {
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
    private Set metaFields;
    private Set metaEntities;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public Set getEntities() {
    	return metaEntities;
    }
    
    /**
     * 
     * @return
     */
    public Set getFields() {
    	return metaFields;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * 
     * @param fields
     */
    public void setFields(Set fields) {
    	this.metaFields = fields;
    }
    
    /**
     * 
     * @param entities
     */
    public void setEntities(Set entities) {
    	this.metaEntities = entities;
    }
}

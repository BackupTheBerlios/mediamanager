package ch.fha.mediamanager.data;

import java.util.Iterator;
import java.util.Set;

/**
 * @author crac
 * @version $Id: MetaData.java,v 1.2 2004/06/05 20:13:55 crac Exp $
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
    
    /**
     * 
     * @param o
     */
    public void add(Object o) {
    	if (o instanceof MetaEntity) {
            addEntity((MetaEntity) o);
        }
        else if (o instanceof MetaField) {
        	addField((MetaField) o);
        }
    }
    
    /**
     * 
     * @param entity
     */
    public void addEntity(MetaEntity entity) {
    	metaEntities.add(entity);
    }
    
    /**
     * 
     * @param field
     */
    public void addField(MetaField field) {
    	metaFields.add(field);
    }
    
    /**
     * 
     * @return
     */
    public Iterator entityIterator() {
    	return metaEntities.iterator();
    }
    
    /**
     * 
     * @return
     */
    public Iterator fieldIterator() {
    	return metaFields.iterator();
    }
    
    /**
     * 
     * @return
     */
    public boolean isEmpty() {
    	return entityIsEmpty() && fieldIsEmpty();
    }
    
    /**
     * 
     * @return
     */
    public boolean entityIsEmpty() {
    	return metaEntities.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    public boolean fieldIsEmpty() {
    	return metaFields.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    public int entitySize() {
    	return metaEntities.size();
    }
    
    /**
     * 
     * @return
     */
    public int fieldSize() {
    	return metaFields.size();
    }
    
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

package ch.fha.mediamanager.data;

import java.util.Set;

/**
 * Holds all meta-information about the repository.
 * 
 * <p>All entities and all its fields are propagated. 
 * 
 * @see MetaField
 * @see MetaEntity
 * 
 * @author crac
 * @version $Id: MetaData.java,v 1.6 2004/06/19 11:05:19 crac Exp $
 */
public final class MetaData {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private Set metaFields = new java.util.HashSet();
    private Set metaEntities = new java.util.HashSet();
    
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
    public java.util.Iterator entityIterator() {
    	return metaEntities.iterator();
    }
    
    /**
     * 
     * @return
     */
    public java.util.Iterator fieldIterator() {
    	return metaFields.iterator();
    }
    
    /**
     * Returns true if no entities and no fields 
     * are available.
     * 
     * @see #hasEntities()
     * @see #hasFields()
     * 
     * @return
     */
    public boolean isEmpty() {
    	return hasEntities() && hasFields();
    }
    
    /**
     * 
     * @return
     */
    public boolean hasEntities() {
    	return metaEntities.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    public boolean hasFields() {
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
    
    /**
     * 
     * @param e
     * @return
     */
    public Set getFields(MetaEntity e) {
        Set set = new java.util.HashSet();
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaField tmp = (MetaField) it.next();
            if (tmp.getEntity().equals(e)) {
                set.add(tmp);
            }
        }
        return set;
    }
}

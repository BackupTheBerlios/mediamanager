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
 * @version $Id: MetaData.java,v 1.8 2004/06/21 12:56:41 crac Exp $
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
    public boolean contains(MetaEntity e) {
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaEntity tmp = (MetaEntity) it.next();
            if (tmp.equals(e)) {
                return true;   
            }
        }
        return false;   
    }
    
    /**
     * 
     * @param f
     * @return
     */
    public boolean contains(MetaField f) {
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaField tmp = (MetaField) it.next();
            if (tmp.equals(f)) {
                return true;   
            }
        }
        return false;
    }
    
    /**
     * 
     * @param name
     * @param e
     * @return
     */
    public MetaField getMetaField(String name, MetaEntity e) {
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaField tmp = (MetaField) it.next();
            if (tmp.equals(new MetaField(name, e))) {
                return tmp;   
            }
        }
        return null;   
    }
    
    /**
     * 
     * @param f
     * @return
     */
    public MetaField getMetaField(MetaField f) {
        return getMetaField(f.getName(), f.getEntity());   
    }
    
    /**
     * 
     * @param e
     * @return
     */
    public Set getMetaFields(MetaEntity e) {
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

package ch.fha.mediamanager.data;

import java.util.HashSet;
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
 * @version $Id: MetaData.java,v 1.16 2004/06/26 13:49:15 crac Exp $
 */
public final class MetaData {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private HashSet metaFields = new HashSet();
    private HashSet metaEntities = new HashSet();
    
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
    protected void remove(Object o) {
        if (o instanceof MetaEntity) {
            removeEntity((MetaEntity) o);
        }
        else if (o instanceof MetaField) {
            removeField((MetaField) o);
        }
    }
    
    /**
     * 
     * @param entity
     */
    protected void removeEntity(MetaEntity entity) {
        metaEntities.remove(entity);
    }
    
    /**
     * 
     * @param field
     */
    protected void removeField(MetaField field) {
        metaFields.remove(field);
    }
    
    /**
     * 
     * @param o
     */
    protected void add(Object o) {
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
    protected void addEntity(MetaEntity entity) {
        if (! contains((MetaEntity) entity)) {
            metaEntities.add(entity);
        }
    }
    
    /**
     * 
     * @param field
     */
    protected void addField(MetaField field) {
        if (! contains((MetaField) field)) {
    	    metaFields.add(field);
        }
    }
    
    /**
     * 
     * @return
     */
    protected java.util.Iterator entityIterator() {
    	return metaEntities.iterator();
    }
    
    /**
     * 
     * @return
     */
    protected java.util.Iterator fieldIterator() {
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
    protected boolean isEmpty() {
    	return hasEntities() && hasFields();
    }
    
    /**
     * 
     * @return
     */
    protected boolean hasEntities() {
    	return metaEntities.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    protected boolean hasFields() {
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
    protected int fieldSize() {
    	return metaFields.size();
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return Returns clone of <code>MetaEntity</code>
     *      Set to prevent direct modification of 
     *      the elements
     */
    protected HashSet getEntities() {
    	return (HashSet) metaEntities.clone();
    }
    
    /**
     * 
     * @return Returns clone of <code>MetaField</code>
     *      Set to prevent direct modification of 
     *      the elements
     */
    protected HashSet getFields() {
    	return (HashSet) metaFields.clone();
    }
    
    /**
     * 
     * @param e
     * @return Returns true if the specified 
     *      <code>MetaEntity</code> is available
     */
    protected boolean contains(MetaEntity e) {
        java.util.Iterator it = entityIterator();
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
     * @return Returns true if the specified 
     *      <code>MetaField</code> is available
     */
    protected boolean contains(MetaField f) {
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
     * @return Returns requested <code>MetaEntity</code>
     */
    protected MetaEntity getMetaEntity(String name) {
        if (name == null)
            throw new IllegalArgumentException();
        
        java.util.Iterator it = entityIterator();
        while (it.hasNext()) {
            MetaEntity tmp = (MetaEntity) it.next();
            if (tmp.getName().equals(name)) {
                return tmp;
            }
        }
        
        throw new NoSuchMetaEntityException();
    }
    
    /**
     * 
     * @param name
     * @param e
     * @return Returns the <code>MetaField</code> with 
     *      the specified name of the <code>MetaEntity</code> 
     */
    protected MetaField getMetaField(String name, MetaEntity e) {
        if ((name == null) || (e == null)) 
            throw new IllegalArgumentException();
        
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
    protected MetaField getMetaField(MetaField f) {
        return getMetaField(f.getName(), f.getEntity());   
    }
    
    /**
     * 
     * @param entity
     * @param field
     * @return
     */
    protected MetaField getMetaField(String entity, String field) {
        return getMetaField(field, new MetaEntity(entity));
    }
    
    /**
     * 
     * @param e
     * @return Returns all <code>MetaField</code>s of 
     *      a <code>MetaEntity</code>
     */
    protected Set getMetaFields(MetaEntity e) {
        Set set = new java.util.HashSet();
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaField tmp = (MetaField) it.next();
            if (tmp.getEntity().equals(e)) {
                set.add((MetaField) tmp);
            }
        }
        return set;
    }
}

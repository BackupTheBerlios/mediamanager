package ch.fha.mediamanager.data;

import java.util.LinkedHashSet;

/**
 * Holds all meta information about the repository.
 * 
 * <p>All entities of the repository and all their 
 * fields are propagated through the 
 * <code>MetaData</code>. 
 * 
 * @see MetaField
 * @see MetaEntity
 * 
 * @author crac
 * @version $Id: MetaData.java,v 1.20 2004/06/30 22:11:59 crac Exp $
 */
public final class MetaData {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private LinkedHashSet metaFields = 
        new LinkedHashSet();
    private LinkedHashSet metaEntities = 
        new LinkedHashSet();
    
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
            removeMetaEntity((MetaEntity) o);
        }
        else if (o instanceof MetaField) {
            removeMetaField((MetaField) o);
        }
    }
    
    /**
     * 
     * @param entity
     */
    protected void removeMetaEntity(MetaEntity entity) {
        metaEntities.remove(entity);
    }
    
    /**
     * 
     * @param field
     */
    protected void removeMetaField(MetaField field) {
        metaFields.remove(field);
    }
    
    /**
     * 
     * @param o
     */
    protected void add(Object o) {
    	if (o instanceof MetaEntity) {
            addMetaEntity((MetaEntity) o);
        }
        else if (o instanceof MetaField) {
        	addMetaField((MetaField) o);
        }
    }
    
    /**
     * 
     * @param entity
     */
    protected void addMetaEntity(MetaEntity entity) {
        if (! contains((MetaEntity) entity)) {
            metaEntities.add(entity);
        }
    }
    
    /**
     * 
     * @param field
     */
    protected void addMetaField(MetaField field) {
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
     * @see #hasMetaEntities()
     * @see #hasMetaFields()
     * 
     * @return
     */
    protected boolean isEmpty() {
    	return hasMetaEntities() && hasMetaFields();
    }
    
    /**
     * 
     * @return
     */
    protected boolean hasMetaEntities() {
    	return metaEntities.isEmpty();
    }
    
    /**
     * 
     * @return
     */
    protected boolean hasMetaFields() {
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
    protected LinkedHashSet getMetaEntities() {
    	return (LinkedHashSet) metaEntities.clone();
    }
    
    /**
     * 
     * @return Returns clone of <code>MetaField</code>
     *      Set to prevent direct modification of 
     *      the elements
     */
    protected LinkedHashSet getMetaFields() {
    	return (LinkedHashSet) metaFields.clone();
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
        return getMetaField(f.getName(), f.getMetaEntity());   
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
    protected LinkedHashSet getMetaFields(MetaEntity e) {
        LinkedHashSet set = new LinkedHashSet();
        java.util.Iterator it = fieldIterator();
        while (it.hasNext()) {
            MetaField tmp = (MetaField) it.next();
            if (tmp.getMetaEntity().equals(e)) {
                set.add((MetaField) tmp);
            }
        }
        return set;
    }
}

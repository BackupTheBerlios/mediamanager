package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * @author crac
 * @version $Id: AbstractQuery.java,v 1.2 2004/06/26 10:01:07 crac Exp $
 */
public abstract class AbstractQuery implements Query {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private Repository repository = DataBus.getRepository();
    private DataSet dSet;
    private int type = 0;
    private Vector request;
    private MetaEntity entity;
    private Vector fields = new Vector();
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * Default constructor.
     */
    public AbstractQuery() {}
    
    /**
     * Prepares query to retrieve data from the 
     * repository.
     * 
     * @see Query.#LOAD
     * 
     * @see #run()
     * 
     * @param vec
     * @param type
     */
    public AbstractQuery(Vector vec, int type) {
        parse(vec);
        this.type = type;
        this.request = vec;
    }
    
    /**
     * Prepares query to edit a <code>DataSet</code> 
     * on the repository.
     * 
     * @see Query.#INSERT
     * @see Query.#UPDATE
     * @see Query.#DELETE
     * 
     * @see #run()
     * 
     * @param dSet
     * @param type
     */
    public AbstractQuery(DataSet dSet, int type) {
        this.dSet = dSet;
        this.type = type;
    }
    
    /**
     * Prepares query to perform admin operations on 
     * <code>MetaData</code> of the repository.
     * 
     * @see Query.#ENTITY_CREATE
     * @see Query.#ENTITY_DELETE
     * @see Query.#ENTITY_FIELD_CREATE
     * @see Query.#FIELD_CREATE
     * @see Query.#FIELD_DELETE
     * 
     * @see #admin(MetaEntity)
     * @see #admin(MetaEntity, MetaField[])
     * @see #admin(MetaField)
     * 
     * @param type
     */
    public AbstractQuery(int type) {
        this.type = type;
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    abstract protected String createCondition(QueryCondition qc);
    abstract protected String createRequest();
    
    /**
     * Parses the request vector and checks the
     * syntax for correctness.
     * 
     * @param vec
     */
    private void parse(Vector vec) {
        for(int i = 0; i < vec.size(); i++) {
            if (vec.elementAt(i) instanceof QueryCondition) {
                // create entitySet
                MetaEntity tmp = 
                    ((QueryCondition) vec.elementAt(i)).getEntity();
                
                entity = tmp;
                
                // create vector of fields
                fields.add(
                    ((QueryCondition) vec.elementAt(i)).getField()
                );
            }
        }
    }
    
    /**
     * 
     * @return 
     */
    public DataSet run() {
        if (this.type == 0)
            throw new RuntimeException("Querytype was not set.");
        
        switch (this.type) {
            case(LOAD):
                return repository.load(this);
            case(UPDATE):
                return repository.update(dSet);
            case(DELETE):
                return repository.delete(dSet);
            case(INSERT):
                return repository.insert(dSet);
        }
        
        return null;
    }
    
    /**
     * 
     * @param entity
     * @return Returns true if the administration  
     *      of the <code>MetaEntity</code> was successful
     */
    public boolean admin(MetaEntity entity) {
        if (this.type == 0)
            throw new RuntimeException("Querytype was not set.");
        
        switch (this.type) {
            case (ENTITY_CREATE):
                return repository.create(entity);
            case (ENTITY_DELETE):
                return repository.delete(entity);
        }

        return false;
    }
    
    /**
     * 
     * @param e
     * @param f
     * @return Returns true if the administration  
     *      of the <code>MetaEntity</code> and its
     *      <code>MetaField</code>s was successful
     */
    public boolean admin(MetaEntity e, MetaField[] f) {
        if (this.type == 0)
            throw new RuntimeException("Querytype was not set.");
        
        switch (this.type) {
            case (ENTITY_FIELD_CREATE):
                return repository.create(e, f);
        }
        return false;
    }
    
    /**
     * 
     * @param field
     * @return Returns true if the administration  
     *      of the <code>MetaField</code> was successful
     */
    public boolean admin(MetaField field) {
        if (this.type == 0)
            throw new RuntimeException("Querytype was not set.");
        
        switch (this.type) {
            case (FIELD_CREATE):
                return repository.create(field);
            case (FIELD_DELETE):
                return repository.delete(field);
        }

        return false;
    }

    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * 
     * @return Returns type of the QueryRequest
     */
    public int getType() {
        return type;
    }
    
    /**
     * 
     * @return
     */
    public Vector getQueryVector() {
        return request;
    }
    
    /**
     * 
     * @return
     */
    public MetaEntity getEntity() {
        return entity;
    }
    
    /**
     * 
     * @return
     */
    public Vector getFields() {
        return fields;   
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /**
     * Sets the type of the Query.
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }
}

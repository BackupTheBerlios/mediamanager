package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * @author luca
 * @version $Id: AbstractQuery.java,v 1.1 2004/06/25 16:04:33 crac Exp $
 */
public abstract class AbstractQuery implements Query {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private Repository repository = DataBus.getRepository();
    private DataSet dSet;
    private int type;
    private Vector request;
    private MetaEntity entity;
    private Vector fields = new Vector();
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     */
    public AbstractQuery() {}
    
    /**
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
     * 
     * @param dSet
     * @param type
     */
    public AbstractQuery(DataSet dSet, int type) {
        this.dSet = dSet;
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
     * @return  DataSet
     */
    public DataSet run() {
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
    public Vector getVector() {
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

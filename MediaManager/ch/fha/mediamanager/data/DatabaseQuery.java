package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: DatabaseQuery.java,v 1.4 2004/06/26 15:01:58 crac Exp $
 */
public final class DatabaseQuery extends AbstractQuery {
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     */
    public DatabaseQuery() {}
    
    /**
     * 
     * @param vec
     * @param type
     */
    public DatabaseQuery(java.util.Vector vec, int type) {
        super(vec, type);
    }
    
    /**
     * 
     * @param dSet
     * @param type
     */
    public DatabaseQuery(DataSet dSet, int type) {
        super(dSet, type);
    }
    
    /**
     * 
     * @param type
     */
    public DatabaseQuery(int type) {
        super(type);
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * 
     * Syntax: Field.name Comperator Value
     * 
     * @param qc
     * @return 
     */
    protected String createCondition(QueryCondition qc) {
        if (qc == null) 
            throw new IllegalArgumentException();
        
        String comp = (qc.getMetaEntity()).getName() + "." 
            + (qc.getField()).getName();
        
        switch(qc.getComparator()) {
            case(QueryCondition.EQUALS):
                comp += " = ";
                break;
            case(QueryCondition.GREATER):
                comp += " > ";
                break;
            case(QueryCondition.LESSER):
                comp += " < ";
                break;
            case(QueryCondition.GREATER_EQUALS):
                comp += " >= ";
                break;
            case(QueryCondition.LESSER_EQUALS):
                comp += " <= ";
                break;
            case(QueryCondition.DONT_EQUALS):
                comp += " != ";
                break;
        }
        
        comp += (qc.getValue()).toString();
        
        return comp;
    }
    
    /**
     * 
     * @param qr
     * @return 
     */
    protected String createRequest() {
        java.util.Vector tmp = getQueryVector();
        
        String output = (tmp.size() > 0) ? " WHERE ": "";
        
        for(int i = 0; i < tmp.size(); i++) {
            if (tmp.elementAt(i) instanceof QueryCondition) {
                output += 
                    createCondition((QueryCondition) tmp.elementAt(i));
            } else {
                
                Integer t = (Integer) tmp.elementAt(i);
                switch(t.intValue()) {
                    case(DatabaseQuery.OR):
                        output += " OR ";
                        break;
                    case(DatabaseQuery.AND):
                        output += " AND ";
                        break;
                    case(DatabaseQuery.BRACE_OPEN):
                        output += " ( ";
                        break;
                    case(DatabaseQuery.BRACE_CLOSE):
                        output += " ) ";
                        break;
                }
            }
        }
        
        return output;
    }

    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
}
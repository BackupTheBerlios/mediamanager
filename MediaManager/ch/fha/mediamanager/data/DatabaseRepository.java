package ch.fha.mediamanager.data;

/**
 *
 *
 * @author crac
 * @version $Id: DatabaseRepository.java,v 1.2 2004/05/21 17:45:28 crac Exp $
 */
public class DatabaseRepository implements Repository {
    
    private DatabaseConnection dbConnection;

    /**
     * 
     * @param ds
     * @return
     */
    public DataSet insert(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet update(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet delete(DataSet ds) {
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return 
     */
    public DataSet load(QueryRequest qr) {
        Vector tmp = qr.getVector();
        
        if (tmp.size() == 0)    return null;
        
        DataSet ds = new DataSet();
        String query = "SELECT * FROM ";
        
        // entities
        for(int i = 0; i < tmp.size(); i++) {
            if (tmp.elementAt(i) instanceof QueryCondition) {
                query += (qc.getEntity()).getName();
                if (tmp.size() > 1 && i + 1 < tmp.size()) {
                    query += ",";
                }
            }
        }
        
        query += createRequestStatement(qr);
        
        try {   
            ResultSet result = 
                dbConnection.executeQuery(query);
            
            while (result.next()) {
                DataElement e = new DataElement();
                
                // TODO
                
                ds.add(e);
            } else {
                throw new LogicException("No such data found in database.");
            }
        } catch (Exception e) {
            throw new LogicException("Error while loading DataSet from database.");
        }
        
        return ds;
    }
    
    /**
     * 
     * 
     * Syntax: Field.name Comperator Value
     * 
     * @param qc
     * @return 
     */
    private String createConditionStatement(QueryCondition qc) {
        String comp = (qc.getEntity()).getName() + "." + (qc.getEntity()).getName();
        
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
    private String createRequestStatement(QueryRequest qr) {
        Vector tmp = qr.getVector();
        String output = "";
        
        for(int i = 0; i < tmp.size(); i++) {
            if (tmp.elementAt(i) instanceof QueryCondition) {
                output += createConditionStatement(tmp.elementAt(i));
            } else {
                switch(tmp.elementAt(i)) {
                    case(QueryRequest.OR):
                        output += " OR ";
                        break;
                    case(QueryRequest.AND):
                        output += " AND ";
                        break;
                    case(QueryRequest.BRACE_OPEN):
                        output += " ( ";
                        break;
                    case(QueryRequest.BRACE_CLOSE):
                        output += " ) ";
                        break;
                }
            }
        }
        
        return output;
    }
}

package ch.fha.mediamanager.data;

import java.sql.ResultSet;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 *
 * @author crac
 * @version $Id: DatabaseRepository.java,v 1.10 2004/06/05 16:34:56 crac Exp $
 */
public class DatabaseRepository implements Repository {
    
    // --------------------------------
    // ATTRIBUTES
    // --------------------------------
    
    private DatabaseConnection dbConnection = 
        new DatabaseConnection();
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @param entity
     */
    public boolean create(MetaEntity entity){ 
    	return false;
    }
    
    /**
     * 
     * @param field
     */
    public boolean create(MetaField field){
        return false;   
    }
    
    /**
     * 
     * @param entity
     */
    public boolean delete(MetaEntity entity){
        return false;
    }
    
    /**
     * 
     * @param field
     */
    public boolean delete(MetaField field) {
        return false;
    }
    
    /**
     * 
     * @return
     */
    public Set loadMetaData() {
    	return null;
    }

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
        Set entities = qr.getEntities();
        Iterator it = entities.iterator();
        while (it.hasNext()) {
            query += ((MetaEntity) it.next()).getName();
        }
        
        query += createRequestStatement(qr);
        System.out.println(query);
        
        /*try {   
            ResultSet result = 
                dbConnection.executeQuery(query);
            
            while (result.next()) {
                DataElement e = new DataElement();
                
                // TODO
                
                ds.add(e);
            } 
        } catch (Exception e) {
            throw new RuntimeException("Error while loading DataSet from database.");
        }*/
        
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
    private String createCondStatement(QueryCondition qc) {
        String comp = (qc.getEntity()).getName() + "." 
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
    private String createRequestStatement(QueryRequest qr) {
        Vector tmp = qr.getVector();
        
        String output = (tmp.size() > 0) ? " WHERE ": "";
        
        for(int i = 0; i < tmp.size(); i++) {
            if (tmp.elementAt(i) instanceof QueryCondition) {
                output += 
                    createCondStatement((QueryCondition) tmp.elementAt(i));
            } else {
                
                Integer t = (Integer) tmp.elementAt(i);
                switch(t.intValue()) {
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

package ch.fha.mediamanager.data;

import java.sql.ResultSet;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JPanel;

/**
 *
 *
 * @author crac
 * @version $Id: DatabaseRepository.java,v 1.15 2004/06/15 12:02:38 crac Exp $
 */
public final class DatabaseRepository implements Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private DatabaseConnection dbConnection; 
        // = new DatabaseConnection(); // will be added when db is functional
    
    private final String name = "Mckoi Repository";
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public JPanel getConfPanel() {
        // combo box for other repositories
        
        // text field for username
        
        // text field for password
        return null;   
    }
    
    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
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
     * Loads all meta information from repository.
     * 
     * @see MetaField
     * @see MetaEntity
     * @see MetaData
     * 
     * @throws InterError if meta data could not be loaded.
     * @return
     */
    public MetaData loadMetaData() {
        try {
            String sql = 
                "SELECT * FROM Fld, Fldtype, Users, Ent " +
                "WHERE Fld.UserId = Users.UUID AND " +
                "Fld.FldFldtypeId = Fldtype.FldtypeId AND " +
                "Fld.FldEntId = Ent.EntId;";
            ResultSet result = 
                dbConnection.executeQuery(sql);
                
            if (result.next()) {
               
            }
        } catch (Exception e) {
            DataBus.logger.fatal("Could not load meta data.");
            throw new InternalError("Could not load meta data.");
        }
        return null;
    }

    /**
     * 
     * @param ds
     * @return
     */
    public DataSet insert(DataSet ds) {
        if (ds.isEmpty()) return null;
        
        String sql = "INSERT INTO ";
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            
        }
        
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet update(DataSet ds) {
        if (ds.isEmpty()) return null;
        
        String sql = "UPDATE ";
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            
        }
        
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet delete(DataSet ds) {
        if (ds.isEmpty()) return null;
        
        String sql = "DELETE FROM ";
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            
        }
        
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
                
                //
                
                ds.add(e);
            } 
        } catch (Exception e) {
            DataBus.logger.error("Data could not be loaded.");
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

package ch.fha.mediamanager.data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 *
 * @author crac
 * @version $Id: MckoiRepository.java,v 1.48 2004/06/28 14:18:54 crac Exp $
 */
public final class MckoiRepository extends Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    static {
        name = "Mckoi SQL DB Repository";
        query = DatabaseQuery.class;
    }
    
    /* Database connection settings */
    private static final String connFile = "conf" + 
        File.separator + "mckoi_repository.ini";
    private ConnectionSettings connSettings = 
        new ConnectionSettings(connFile);
    
    /* Mckoi SQL Database settings */
    private MckoiSettings mckoiSettings = null;
    private static final String mckoiFile = 
        "conf" + File.separator + "mckoi.conf";
    
    private DatabaseConnection dbConnection = 
        new DatabaseConnection(connSettings);
    
    /* SQL statements */
    private static java.sql.PreparedStatement insertEntity;
    private static java.sql.PreparedStatement insertField;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    ///////////////////////////
    /// CONNECTION OPS      ///
    ///////////////////////////
    
    /**
     * 
     */
    protected MetaData initialize() {
        connect();
        return loadMetaData();
    }
    
    /**
     * Connects to database.
     */
    protected void connect() {
        if (dbConnection != null) {
            dbConnection.connect();
            connected = true;
        }
    }
    
    /**
     * Disconnects from database.
     */
    protected void disconnect() {
        dbConnection.disconnect();
        connected = false;
        //dbConnection = null;
    }
    
    ///////////////////////////
    /// USER OPERATIONS     ///
    ///////////////////////////
    
    protected void insertUser(User user) {
        throw new UnsupportedOperationException(); }
    protected void updateUser(User user) {
        throw new UnsupportedOperationException(); }
    protected void deleteUser(User user) {
        throw new UnsupportedOperationException(); }
    
    ///////////////////////////
    /// META OPERATIONS     ///
    ///////////////////////////
    
    /**
    *
    * @param entity
    * @param fields
    * 
    * @return Returns true if the entity and its field 
    *     have been created
    */
    protected boolean create(MetaEntity entity, MetaField[] fields) {
       if ((entity == null) || (fields == null))
           throw new IllegalArgumentException();

       if (create(entity)) {
           for (int i = 0; i < fields.length; i++) {
               if (! create(fields[i])) {
                   delete(entity);
                   return false;
               }
           }
           return true;
       }
       
       return false;
   }
   
   /**
    * 
    * @param entity
    * @return Returns true if the entity has been created
    */
    protected boolean create(MetaEntity entity){ 
       if (entity == null)
           throw new IllegalArgumentException();
       
       insertEntity = 
           dbConnection.prepareStatement("INSERT INTO Ent " +
               "(EntId, EntName) VALUES (?, ?);");
       String createEntity = "CREATE TABLE " + entity.getName() + 
           "(EntryId INTEGER DEFAULT 0 NOT NULL," + 
           "FOREIGN KEY(EntryId) REFERENCES Entry (EntryId));";
       
       try {
           entity.setId(getNextPK("Ent", "EntId"));
           
           dbConnection.getConnection().setAutoCommit(false);
           
           insertEntity.setInt(1, entity.getId());
           insertEntity.setString(2, entity.getName());
           
           insertEntity.execute();
           dbConnection.executeQuery(createEntity);
           
           dbConnection.getConnection().commit();
       } catch (SQLException e) {
           DataBus.logger.warn("Entity " + entity.getName() + 
                " with Id " + entity.getId() + " not created.");
           return false;
       }
       
       DataBus.logger.info("Entity " + entity.getName() + 
           " with Id " + entity.getId() + " created.");
       DataBus.getMetaData().addMetaEntity(entity);
       return true;
   }
   
   /**
    * 
    * @param field
    * @return Returns true if the field has been created
    */
    protected boolean create(MetaField field){
       if ( (field == null) 
               || (field.getType() == MetaField.USERID)
			   || (field.getType() == MetaField.ENTRYID)
               || (field.getMetaEntity().getId() == 0)
               || (field.getId() != 0)
               || (field.getType() == MetaField.INVALID_TYPE)
               || (field.getName().equals(""))
           ) {
           throw new IllegalArgumentException();
       }
       
       String alter = "ALTER TABLE " + field.getMetaEntity().getName() + 
           " ADD " + field.getName();
       
       insertField = 
           dbConnection.prepareStatement("INSERT INTO Fld (" +
               "FldId,FldName,FldEntId,FldFldtypeId,FldLength," +
               "FldDefault,FldHidden,FldMandatory) VALUES (" +
               "?,?,?,?,?,?,?,?);");
           
       try {
           int id = getNextPK("Fld", "FldId");
           field.setId(id);
           
           String defaultValue;
           if (field.getDefaultValue() == null) defaultValue = "";
           else defaultValue = field.getDefaultValue().toString();
           
           dbConnection.getConnection().setAutoCommit(false);
           
           insertField.setInt(1, id);
           insertField.setString(2, field.getName());
           insertField.setInt(3, field.getMetaEntity().getId());
           insertField.setInt(4, field.getType());
           insertField.setInt(5, field.getLength());
           insertField.setString(6, defaultValue);
           insertField.setInt(7, (field.getHidden() == true)? 1: 0);
           insertField.setInt(8, (field.getMandatory() == true)? 1: 0);
           
           MetaField pk = null;
           
           switch (field.getType()) {
               case (MetaField.PK):
                   alter += " INTEGER DEFAULT UNIQUEKEY('" +
                       field.getMetaEntity().getName() + "') ";
                   pk = field;
                   break;
               case (MetaField.INT):
                   alter += " INTEGER ";
                   break;
               case (MetaField.TEXT):
                   alter += " TEXT ";
                   break;
               case (MetaField.VARCHAR):
               case (MetaField.LIST):
                   if (field.getLength() == 0) {
                       field.setLength(255);
                   }
                   alter +=  
                       " VARCHAR(" + field.getLength() +
                       ") ";
                   break;
               case (MetaField.BOOLEAN):
                   alter += " TINYINT ";
                   break;
               case (MetaField.DATE):
               	   alter += " DATE ";
                   break;
               case (MetaField.TIMESTAMP):
               	   alter += " TIMESTAMP ";
                   break;
           }
           
           alter += " NOT NULL ";
           
           insertField.execute();
           dbConnection.executeQuery(alter);
           
           if (pk != null) createPK(pk);
           
           dbConnection.getConnection().commit();
       } catch (SQLException e) {
           DataBus.logger.warn("Field " + field.getIdentifier() + 
               " with Id " + field.getId() + " not created.");
           return false;
       }      
       DataBus.logger.info("Field " + field.getIdentifier() + 
            " with Id " + field.getId() + " created.");
       DataBus.getMetaData().addMetaField(field);
       
       fireDataChanged(field.getMetaEntity());
       return true;
   }
   
   /**
    * Adds Primary Key to an entity.
    * 
    * @param field
    */
   private void createPK(MetaField field) {
       if ( (field == null) 
       	       || (field.getType() != MetaField.PK)
           ) {
           throw new IllegalArgumentException();
       }
       
       String sql = "ALTER TABLE " + 
           field.getMetaEntity().getName() + 
           " ADD PRIMARY KEY(" + field.getName() + ");";
       dbConnection.executeQuery(sql);
   }
   
   /**
    * 
    * @param entity
    * @return Returns true if the entity has been deleted
    */
   protected boolean delete(MetaEntity entity){
       if ( (entity == null) 
               || (entity.getName().equals("Entry")) 
               || (entity.getName().equals("Fld"))
               || (entity.getName().equals("Fldtype"))
               || (entity.getName().equals("Ent"))
               || (entity.getId() == 0)
           ) {
           throw new IllegalArgumentException();
       }
       
       // remove also Entry rows
       String delEntries = "DELETE FROM Entry WHERE EntryId IN " + 
           "(SELECT EntryId FROM " + entity.getName() + ");";
       String delEntity = "DROP TABLE " + entity.getName() + ";";
       String delEnt = "DELETE FROM Ent WHERE EntId = " + entity.getId();
       String delFld = "DELETE FROM Fld WHERE FldEntId = " + entity.getId();
       
       try {
           dbConnection.getConnection().setAutoCommit(false);
           
           dbConnection.executeQuery(delEntries);
           dbConnection.executeQuery(delEntity);
           dbConnection.executeQuery(delFld);
           dbConnection.executeQuery(delEnt);
           
           dbConnection.getConnection().commit();
       } catch (SQLException e) {
           DataBus.logger.warn("Entity " + entity.getName() + 
               " not deleted.");
           return false;
       }
       
       DataBus.logger.info("Entity " + entity.getName() +
           " deleted.");
       DataBus.getMetaData().remove(entity);
       
       fireDataChanged(entity);
       return true;
   }
   
   /**
    * 
    * @param field
    * @return Returns true if the field has been deleted
    */
   protected boolean delete(MetaField field) {
       if ( (field == null)
               || (field.getType() == MetaField.USERID)
               || (field.getType() == MetaField.PK)
               || (field.getType() == MetaField.ENTRYID)
               || (field.getId() == 0)
               || (field.getType() == MetaField.INVALID_TYPE)
           ) {
           throw new IllegalArgumentException();
       }
       
       String delFld = "DELETE FROM Fld WHERE FldId = " + 
           field.getId() + ";";
       String delField = "ALTER TABLE " + 
           field.getMetaEntity().getName() + " DROP " + field.getName() + ";";
           
       try {
           dbConnection.getConnection().setAutoCommit(false);
           
           dbConnection.executeQuery(delField);
           dbConnection.executeQuery(delFld);
           
           dbConnection.getConnection().commit();
       } catch (SQLException e) {
           DataBus.logger.warn("Field " + field.getIdentifier() + 
               " not deleted.");
           return false;
       }
       DataBus.logger.info("Field " + field.getIdentifier() + 
           " deleted.");
       DataBus.getMetaData().remove(field);
       
       fireDataChanged(field.getMetaEntity());
       return true;
   }
    
    /**
     * Loads all meta information of the repository.
     * 
     * @see Repository
     * @see MetaField
     * @see MetaEntity
     * @see MetaData
     * 
     * @throws InterError if meta data could not be loaded.
     * @return Returns <code>MetaData</code> of the 
     *      repository
     */
   private MetaData loadMetaData() {
        MetaData data = new MetaData();
        
        try {
            String sql = 
                "SELECT * FROM Fld " +
                "LEFT JOIN Fldtype ON Fld.FldFldtypeId = Fldtype.FldtypeId " +
                "LEFT JOIN Ent ON Fld.FldEntId = Ent.EntId " +
                "WHERE Fld.FldId > 0 " +
                "ORDER BY Ent.EntId ASC, Fld.FldId ASC;";
            ResultSet result = 
                dbConnection.executeQuery(sql);
                
            while (result.next()) {
                MetaEntity me = 
                    new MetaEntity(
                        result.getString("EntName"),
                        result.getInt("EntId")
                    );
                MetaField mf = 
                    new MetaField(
                        result.getString("FldName"),
                        me,
                        result.getInt("FldId")
                    );
                
                mf.setHidden(
                    result.getInt("FldHidden") == 0 ? false: true
                );
                mf.setLength(result.getInt("FldLength"));
                mf.setMandatory(
                    result.getInt("FldMandatory") == 0 ? false: true
                );
                mf.setType(result.getInt("FldtypeId"));
                
                switch(mf.getType()) {
                    case (MetaField.LIST):
                    case (MetaField.VARCHAR):
                    case (MetaField.TEXT):
                        mf.setDefaultValue(result.getString("FldDefault"));
                        break;
                    case (MetaField.DATE):
                        mf.setDefaultValue(result.getDate("FldDefault"));
                        break;
                    case (MetaField.TIMESTAMP):
                        if (! result.getString("FldDefault").equals("")) {
                            mf.setDefaultValue(
                                new Timestamp(Integer.parseInt(
                                    result.getString("FldDefault")
                                ))
                            );
                        } else {
                            mf.setDefaultValue(
                                new Timestamp(System.currentTimeMillis())
                            );
                        }
                        break;
                    case (MetaField.BOOLEAN):
                        if (! result.getString("FldDefault").equals("")) {
                            mf.setDefaultValue(
                                new Boolean(
                                    (Integer.parseInt(
                                            result.getString("FldDefault")
                                    ) == 1)? true: false
                                )
                            );
                        } else {
                            mf.setDefaultValue(new Boolean(false));
                        }
                        break;
                    case (MetaField.INT):
                        if (! result.getString("FldDefault").equals("")) {
                            mf.setDefaultValue(
                                new Integer(Integer.parseInt(
                                    result.getString("FldDefault")
                                ))
                            );
                        } else {
                            mf.setDefaultValue(new Integer(0));
                        }
                        break;
                }
                //data.addEntity(me);
                data.addMetaField(mf);
                DataBus.logger.debug("MetaField added: " + mf.getIdentifier() + 
                   " hashcode: " + mf.hashCode());
            }
            
            String loadEntities = "SELECT * FROM Ent WHERE EntId > 0;";
            ResultSet resEntities = 
                dbConnection.executeQuery(loadEntities);
            
            while (resEntities.next()) {
                MetaEntity entity = 
                    new MetaEntity(
                        resEntities.getString("EntName"),
                        resEntities.getInt("EntId")
                    );
                data.addMetaEntity(entity);
            }
        } catch (SQLException e) {
            DataBus.logger.fatal("Could not load MetaData.");
            throw new InternalError("Could not load MetaData.");
        }
        DataBus.logger.info("MetaData loaded.");
        return data;
    }
    
    ///////////////////////////
    /// DATA OPERATIONS     ///
    ///////////////////////////

    /**
     * Inserts a <code>DataSet</code> to the repository.
     * 
     * @param ds
     * @return Returns always null
     */
   protected DataSet insert(DataSet ds) {
        if ((ds.isEmpty()) || (ds == null))
            throw new IllegalArgumentException();
        
        // DataElement
        String sql = "INSERT INTO " + ds.getMetaEntity().getName();
        String values = " ) VALUES ( ";
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            // Entry / User
            DataElement el = (DataElement) it.next();
            Entry entry = el.getEntry();
            if (entry.getId() != 0) break;
            
            entry.setId(insertEntry(entry));
            
            sql += " ( EntryId ";
            values += entry.getId();
            
            // prepare statement
            Field[] fields = ds.getFields();
            for (int i = 1; i <= fields.length; i++) {
                sql += " , " + fields[i-1].getName();
                values += ",?";
            }
            
            java.sql.PreparedStatement insert = 
                dbConnection.prepareStatement(sql + values + ");");
            
            // set values
            try {
                for (int i = 1; i <= fields.length; i++) {
                    Field field = fields[i-1];
                    MetaField metaField = field.getMetaField();
                    
                    int value;
                    switch (metaField.getType()) {
                        case (MetaField.PK):
                            insert.setInt(i, getNextPK(
                                ds.getMetaEntity().getName(),
                                ds.getPKFieldname())
                            );
                            break;
                        case (MetaField.BOOLEAN):
                            if (fields[i-1].getValue().toString().equals("true")) {
                                value = 1;
                            } else {
                                value = 0;
                            }
                            insert.setInt(i, value);
                            break;
                        case (MetaField.INT):
                            value = Integer.parseInt(
                                fields[i-1].getValue().toString()
                            );
                            insert.setInt(i, value);
                            break;
                        case (MetaField.LIST):
                        case (MetaField.TEXT):
                        case (MetaField.VARCHAR):
                            insert.setString(
                                i,
                                field.getValue().toString()
                            );
                            break;
                        case (MetaField.DATE):
                            insert.setDate(
                                i,
                                (Date) field.getValue()
                            );
                            break;
                        case (MetaField.TIMESTAMP):
                            insert.setTimestamp(
                                i,
                                (Timestamp) field.getValue()
                            );
                            break;
                    }
                }
                insert.executeQuery();
            } catch (SQLException e) {
                DataBus.logger.fatal("DataElement not inserted.");
                e.printStackTrace();
                throw new InternalError("DataElement not inserted.");
            }
            DataBus.logger.debug("DataElement inserted.");
        }
        
        DataBus.logger.debug("DataSet inserted.");
        
        fireDataChanged(ds.getMetaEntity());
        return ds;
    }
    
    /**
     * Updates a <code>DataSet</code> in the repository.
     * 
     * @param ds
     * @return Returns always null
     */
    protected DataSet update(DataSet ds) {
        if (ds.isEmpty() || ds == null) 
            throw new IllegalArgumentException();
        
        String sql = "UPDATE " + ds.getMetaEntity().getName() + " SET ";
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            DataElement el = (DataElement) it.next();
            Field[] fields = el.getFields();
            
            // prepare statement
            for (int i = 0; i < fields.length; i++) {
                sql += fields[i].getName();
                sql += "=?";
                if (i < fields.length-1) sql += ",";
            }
            sql += " WHERE " + el.getPKField().getName();
            sql += " = " + (Integer) el.getPKField().getValue();
            
            java.sql.PreparedStatement update = 
                dbConnection.prepareStatement(sql);
            
            // set values
            try {
                Entry entry = el.getEntry();
                entry.setLastModified(updateEntry(entry));
                
                for (int i = 1; i <= fields.length; i++) {
                    MetaField metaField = fields[i-1].getMetaField();
                    
                    int value;
                    switch (metaField.getType()) {
                        case (MetaField.BOOLEAN):
                            boolean tmp;
                            if (fields[i-1].getValue().toString().equals("true")) {
                                value = 1;
                                tmp = true;
                            } else {
                                value = 0;
                                tmp = false;
                            }
                            update.setInt(i, value);
                            fields[i-1].setValue(new Boolean(tmp));
                            break;
                        case (MetaField.PK):
                        case (MetaField.INT):
                            value = Integer.parseInt(
                                fields[i-1].getValue().toString()
                            );
                            update.setInt(i, value);
                            fields[i-1].setValue(new Integer(value));
                            break;
                        case (MetaField.LIST):
                        case (MetaField.TEXT):
                        case (MetaField.VARCHAR):
                            update.setString(
                                i,
                                fields[i-1].getValue().toString()
                            );
                            fields[i-1].setValue(
                                fields[i-1].getValue().toString()
                            );
                            break;
                        case (MetaField.DATE):
                            update.setDate(
                                i,
                                (Date) fields[i-1].getValue()
                            );
                            fields[i-1].setValue(
                                (Date) fields[i-1].getValue()
                            );
                            break;
                        case (MetaField.TIMESTAMP):
                            update.setTimestamp(
                                 i,
                                 (Timestamp) fields[i-1].getValue()
                            );
                            fields[i-1].setValue(
                                (Timestamp) fields[i-1].getValue()
                            );
                            break;
                    }
                }
                update.executeQuery();
            } catch (SQLException e) {
                DataBus.logger.fatal("DataElement not updated.");
                e.printStackTrace();
                throw new InternalError("DataElement not updated.");
            }
            DataBus.logger.debug("DataElement updated.");
        }
        DataBus.logger.debug("DataSet updated.");
        
        fireDataChanged(ds.getMetaEntity());
        return ds;
    }
    
    /**
     * Deletes a <code>DataSet</code> from the repository.
     * 
     * @param ds
     * @return Returns always null
     */
    protected DataSet delete(DataSet ds) {
        if ((ds.isEmpty()) || (ds == null))
            throw new IllegalArgumentException();
        
        Iterator it = ds.iterator();
        
        while(it.hasNext()) {
            DataElement el = (DataElement) it.next();
            
            String query = "DELETE FROM " + 
                ds.getMetaEntity().getName() + " WHERE " +
				el.getPKField().getName() + "=" + 
				el.getPKField().getValue() + ";";
            
            dbConnection.executeUpdate(query);
            DataBus.logger.info("DataElement deleted.");
            deleteEntry(el.getEntry());
            ds.remove(el);
        }
        DataBus.logger.info("DataSet deleted.");
        fireDataChanged(ds.getMetaEntity());
        
        ds = null;
        return null;
    }
    
    /**
     * Retrieves a <code>DataSet</code> according to the 
     * defined <code>QueryRequest</code> from the repository.
     * 
     * @see DataSet
     * @see DatabaseQuery
     * 
     * @param qr
     * @return Returns the requested <code>DataSet</code>
     */
    protected DataSet load(AbstractQuery qr) {
    	Vector tmp = qr.getQueryVector();
        if (tmp.size() == 0)    return null;
        
        DataSet ds = new DataSet();
        
        String ent = qr.getMetaEntity().getName();
        String entryField = ent + ".EntryId";
        
        String query = "SELECT * FROM " + ent;
        query += " LEFT JOIN Entry ON " + entryField + " = Entry.EntryId ";
        //query += " LEFT JOIN Users ON Entry.EntryUsersId = Users.UsersUUID ";
        
        query += qr.createRequest();
        
        try {   
            ResultSet result = 
                dbConnection.executeQuery(query);
            ResultSetMetaData rsmd = result.getMetaData();
            
            MetaData metaData = DataBus.getMetaData();
            
            while (result.next()) {
                DataElement e = new DataElement(qr.getMetaEntity());
                
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    
                    MetaField mf = metaData.getMetaField(
                        rsmd.getTableName(i),
                        rsmd.getColumnLabel(i)
                    );
                    
                    if (mf != null) {
                        Field field = new Field(mf);
                        switch(mf.getType()) {
                            case (MetaField.INT):
                            case (MetaField.PK):
                                field.setValue(
                                    new Integer(result.getInt(mf.getName()))
                                );
                                break;
                            case (MetaField.LIST):
                            case (MetaField.TEXT):
                            case (MetaField.VARCHAR):
                                field.setValue(result.getString(mf.getName()));
                                break;
                            case (MetaField.BOOLEAN):
                                field.setValue(
                                    new Boolean(
                                        (result.getInt(mf.getName()) == 1)? true: false
                                    )
                                );
                                break;
                            case (MetaField.TIMESTAMP):
                                field.setValue(
                                    result.getTimestamp(mf.getName())    
                                );
                                break;
                            case (MetaField.DATE):
                                field.setValue(
                                    result.getDate(mf.getName())    
                                );
                        }
                        e.setField(field);
                    } else {
                        //DataBus.logger.debug("Field not in MetaData: " + mf.getIdentifier());   
                    }
                }
                
                // static fields from Users and Entry tables
                /*User owner = new User(
                    result.getString("UsersUUID"),
                    result.getString("UsersName"),
                    result.getString("UsersUsername")
                );*/
                
                Entry entry = new Entry(
                    result.getInt("EntryId"),
                    result.getTimestamp("EntryCreation"),
                    result.getTimestamp("EntryEdit")
                );
                
                e.setEntry(entry);
                ds.add(e);
            } 
        } catch (SQLException e) {
            DataBus.logger.error("DataSet could not be loaded.");
            return null;
        }
        DataBus.logger.debug("DataSet loaded.");
        return ds;
    }
    
    ///////////////////////////
    /// ENTRY OPERATIONS    ///
    ///////////////////////////
    
    /**
     * Deletes an entry from the repository.
     * 
     * @param entry
     */
    private void deleteEntry(Entry entry) {
        if ((entry == null) || (entry.getId() < 1))
            throw new IllegalArgumentException();
        
        String sql = "DELETE FROM Entry WHERE EntryId=" + 
            entry.getId() + ";";
        
        dbConnection.executeUpdate(sql);
        DataBus.logger.info("Entry deleted.");
    }
    
    /**
     * 
     * @param entry
     * @return Returns the Timestamp of the updated entry
     */
    private Timestamp updateEntry(Entry entry) {
        if ((entry == null) || (entry.getId() < 1))
            throw new IllegalArgumentException();
        
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        
        String sql = "UPDATE Entry SET EntryEdit = ? WHERE EntryId = ?";
        
        java.sql.PreparedStatement update = 
            dbConnection.prepareStatement(sql);
        
        try {
            update.setTimestamp(1, stamp);
            update.setInt(2, entry.getId());
            update.executeQuery();
        } catch (SQLException e) {
            DataBus.logger.fatal("Entry not updated.");
            e.printStackTrace();
            throw new InternalError();
        }
        
        DataBus.logger.info("Entry updated.");
        return stamp;
    }
    
    /**
     * 
     * @param entry
     * @return Returns the id of the inserted entry
     */
    private int insertEntry(Entry entry) {
        if ((entry == null) || (entry.getId() != 0))
            throw new IllegalArgumentException();
        
        int id = 0;
        String sql = "INSERT INTO Entry " +
            "(EntryId, EntryCreation, EntryEdit) " +
            "VALUES (?, ?, ?);";
        java.sql.PreparedStatement insertPS = 
            dbConnection.prepareStatement(sql);
        
        try {
            id = getNextPK("Entry", "EntryId");
            insertPS.setInt(1, id);
            insertPS.setTimestamp(2, entry.getCreation());
            insertPS.setTimestamp(3, entry.getLastModified());
            //insertPS.setString(4, entry.getUser().getUUID());
            insertPS.executeUpdate();
        } catch (SQLException e) {
            DataBus.logger.debug("Entry not inserted.");
            return 0;
        }
        DataBus.logger.info("Entry inserted.");
        return id;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @param entity
     * @param pk
     * @return Returns the next PrimaryKey of the entity
     */
    private int getNextPK(String entity, String pk) {
        if ((pk == null) || (entity == null)) 
            throw new IllegalArgumentException();
        
        String sqlNextPK = 
            "SELECT MAX(" + pk + ") FROM " + entity + ";";
        
        try {
            ResultSet result =  
                dbConnection.executeQuery(sqlNextPK);
            result.next();
            return (result.getInt(1) + 1);
        } catch (SQLException e) {
            DataBus.logger.fatal("Error while getting next primary key.");
            e.printStackTrace();
            throw new RuntimeException("Erroneous database query.");
        }
    }
    
    /**
     * 
     * @return Returns the config panel
     */
    public JPanel getConfPanel() {
        if (mckoiSettings == null) {
            mckoiSettings = new MckoiSettings(mckoiFile);
        }
        
        JPanel outer = new JPanel();
        outer.setLayout(new BorderLayout());
        
        // top
        JPanel top = new JPanel();
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,2));
        
        panel.add(new JLabel("DB Pfad"));
        final JTextField dbPath = 
            new JTextField(mckoiSettings.getDbPath());
        panel.add(dbPath);
        
        panel.add(new JLabel("Log Pfad"));
        final JTextField logPath = 
            new JTextField(mckoiSettings.getLogPath());
        panel.add(logPath);
        
        panel.add(new JLabel("Log Datei"));
        final JTextField logFile = 
            new JTextField(mckoiSettings.getLogFile());
        panel.add(logFile);
        
        panel.add(new JLabel("Ignoriere Gross- Kleinschreibung"));
        final JTextField ignoreCase = 
            new JTextField(mckoiSettings.getIgnoreCase());
        panel.add(ignoreCase);
        
        panel.add(new JLabel("Data Cache"));
        final JTextField dataCache = 
            new JTextField(mckoiSettings.getDataCache());
        panel.add(dataCache);
        
        panel.add(new JLabel("Entry Cache"));
        final JTextField entryCache = 
            new JTextField(mckoiSettings.getEntryCache());
        panel.add(entryCache);
        
        panel.add(new JLabel("Max. Worker Threads"));
        final JTextField workerThreads = 
            new JTextField(mckoiSettings.getWorkerThreads());
        panel.add(workerThreads);
        
        panel.add(new JLabel("Log Level"));
        final JTextField logLevel = 
            new JTextField(mckoiSettings.getLogLevel());
        panel.add(logLevel);
        
        panel.add(new JLabel("Nur Lesezugriff"));
        final JTextField readOnly = 
            new JTextField(mckoiSettings.getReadOnly());
        panel.add(readOnly);
        
        top.add(panel);
        
        // bottom
        JButton restore = new JButton("default");
        restore.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("default")) {
                        mckoiSettings.restoreDefaults();
                        dataCache.setText(mckoiSettings.getDataCache());
                        logPath.setText(mckoiSettings.getLogPath());
                        logFile.setText(mckoiSettings.getLogFile());
                        logLevel.setText(mckoiSettings.getLogLevel());
                        entryCache.setText(mckoiSettings.getEntryCache());
                        workerThreads.setText(mckoiSettings.getWorkerThreads());
                        dbPath.setText(mckoiSettings.getDbPath());
                        readOnly.setText(mckoiSettings.getReadOnly());
                        ignoreCase.setText(mckoiSettings.getIgnoreCase());
                    }
                }
            });
        
        JButton save = new JButton("save");
        save.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("save")) {
                        mckoiSettings.setDataCache(dataCache.getText());
                        mckoiSettings.setLogPath(logPath.getText());
                        mckoiSettings.setLogFile(logFile.getText());
                        mckoiSettings.setLogLevel(logLevel.getText());
                        mckoiSettings.setEntryCache(entryCache.getText());
                        mckoiSettings.setWorkerThreads(workerThreads.getText());
                        mckoiSettings.setDbPath(dbPath.getText());
                        mckoiSettings.setReadOnly(readOnly.getText());
                        mckoiSettings.setIgnoreCase(ignoreCase.getText());
                        mckoiSettings.saveConfig();
                    }
                }
            });

        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        bottom.add(restore);
        bottom.add(save);
        
        outer.add(top, BorderLayout.CENTER);
        outer.add(bottom, BorderLayout.SOUTH);
        
        JPanel full = new JPanel();
        full.setLayout(new FlowLayout());
        full.add(outer);
        
        return full;
    }
}

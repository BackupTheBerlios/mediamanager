package ch.fha.mediamanager.data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

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
 * @version $Id: MckoiRepository.java,v 1.15 2004/06/23 12:09:26 crac Exp $
 */
public final class MckoiRepository implements Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private static final String name = "Mckoi SQL DB Repository";
    
    private static final String connFile = "conf" + File.separator + 
        "mckoi_repository.ini";
    
    /* Database connection settings */
    private ConnectionSettings connSettings = 
        new ConnectionSettings(connFile);
    
    /* Mckoi SQL Database settings */
    private MckoiSettings mckoiSettings = null;
    private static final String mckoiFile = 
        "conf" + File.separator + "mckoi.conf";
    
    private DatabaseConnection dbConnection = 
        new DatabaseConnection(connSettings);
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * 
     */
    public MckoiRepository() {}
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * 
     */
    public MetaData initialize() {
        connect();
        return loadMetaData();
    }
    
    /**
     * 
     */
    public void connect() {
        dbConnection.connect();
    }
    
    /**
     * 
     *
     */
    public void disconnect() {
        dbConnection.disconnect();
        dbConnection = null;
    }
    
    /**
    *
    * @param entity
    * @param fields
    * @return Returns true if the entity and its field 
    *     have been created
    */
   public boolean create(MetaEntity entity, MetaField[] fields) {
       if ((entity == null) || (fields == null))
           throw new IllegalArgumentException();
       
       return false;
   }
   
   /**
    * 
    * @param entity
    * @return Returns true if the entity has been created
    */
   public boolean create(MetaEntity entity){ 
       if (entity == null)
           throw new IllegalArgumentException();
           
       return false;
   }
   
   /**
    * 
    * @param field
    * @return Returns true if the field has been created
    */
   public boolean create(MetaField field){
       if ( (field == null) 
               || (field.getType() == MetaField.USERID)
			   || (field.getType() == MetaField.ENTRYID)
           ) {
           throw new IllegalArgumentException();
       }
       
       String sql = "ALTER TABLE ? ADD ? ? ?;";
       
       java.sql.PreparedStatement create = 
           dbConnection.prepareStatement(sql);
           
       try {
           create.setString(1, field.getEntity().getName());
           create.setString(2, field.getName());
           
           int length = field.getLength();
           
           switch (field.getType()) {
               case (MetaField.INT):
               case (MetaField.TEXT):
               case (MetaField.VARCHAR):
               case (MetaField.LIST):
               case (MetaField.BOOLEAN):
               case (MetaField.DATE):
               case (MetaField.TIMESTAMP):
           }
           
           create.executeQuery();
       } catch (SQLException e) {
           DataBus.logger.warn("Field " + field.getIdentifier() + " not created.");
           return false;
       }      
       DataBus.logger.info("Field " + field.getIdentifier() + " created.");
       return true;
   }
   
   /**
    * 
    * @param entity
    * @return Returns true if the entity has been deleted
    */
   public boolean delete(MetaEntity entity){
       if ( (entity == null) 
               || (entity.getName().equals("Entry")) 
               || (entity.getName().equals("Fld"))
               || (entity.getName().equals("Fldtype"))
               || (entity.getName().equals("Ent"))
           ) {
           throw new IllegalArgumentException();
       }
       
       // remove also Entry rows
       String delEntries = "DELETE FROM Entry WHERE EntryId IN " + 
           "(SELECT EntryId FROM " + entity.getName() + ");";
       String delEntity = "DROP TABLE " + entity.getName() + ";";
       
       try {
           dbConnection.getConnection().setAutoCommit(false);
           dbConnection.executeQuery(delEntries);
           dbConnection.executeQuery(delEntity);
           dbConnection.getConnection().commit();
       } catch (SQLException e) {
           
       }
       
       DataBus.logger.info("Entity " + entity.getName() + " deleted.");
       return true;
   }
   
   /**
    * 
    * @param field
    * @return Returns true if the field has been deleted
    */
   public boolean delete(MetaField field) {
       if ( (field == null)
               || (field.getType() == MetaField.USERID)
               || (field.getType() == MetaField.PK)
               || (field.getType() == MetaField.ENTRYID)
           ) {
           throw new IllegalArgumentException();
       }
           
       String sql = "ALTER TABLE ? DELETE ?";
       
       java.sql.PreparedStatement delete = 
           dbConnection.prepareStatement(sql);
           
       try {
           delete.setString(1, field.getEntity().getName());
           delete.setString(2, field.getName());
           delete.executeQuery();
       } catch (SQLException e) {
           DataBus.logger.warn("Field " + field.getIdentifier() + " not deleted.");
           return false;
       }
       DataBus.logger.info("Field " + field.getIdentifier() + " deleted.");
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
    public MetaData loadMetaData() {
        MetaData data = new MetaData();
        
        try {
            String sql = 
                "SELECT * FROM Fld " +
                "LEFT JOIN Fldtype ON Fld.FldFldtypeId = Fldtype.FldtypeId " +
                "LEFT JOIN Ent ON Fld.FldEntId = Ent.EntId;";
            ResultSet result = 
                dbConnection.executeQuery(sql);
                
            while (result.next()) {
                MetaEntity me = 
                    new MetaEntity(result.getString("EntName"));
                MetaField mf = 
                    new MetaField(result.getString("FldName"), me);
                
                mf.setHidden(
                    result.getInt("FldHidden") == 0 ? false: true
                );
                mf.setLength(result.getInt("FldLength"));
                mf.setMandatory(
                    result.getInt("FldMandatory") == 0 ? false: true
                );
                mf.setType(result.getInt("FldtypeId"));
                
                switch(mf.getType()) {
                    case(MetaField.VARCHAR):
                    case(MetaField.TEXT):
                        mf.setDefaultValue(result.getString("FldDefault"));
                        break;
                    case(MetaField.DATE):
                        break;
                    case(MetaField.INT):
                        mf.setDefaultValue(
                            new Integer(result.getInt("FldDefault"))
                        );
                        break;
                }
                data.addEntity(me);
                data.addField(mf);
                DataBus.logger.debug("MetaField added: " + mf.getIdentifier());
            }
        } catch (SQLException e) {
            DataBus.logger.fatal("Could not load MetaData.");
            throw new InternalError("Could not load MetaData.");
        }
        DataBus.logger.info("MetaData loaded.");
        return data;
    }

    /**
     * Inserts a <code>DataSet</code> to the repository.
     * 
     * @param ds
     * @return Returns always null
     */
    public DataSet insert(DataSet ds) {
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
                    
                    switch (metaField.getType()) {
                        case (MetaField.PK):
                            insert.setInt(i, getNextPK(
                                ds.getMetaEntity().getName(),
                                ds.getPKField())
                            );
                            break;
                        case (MetaField.INT):
                            insert.setObject(i, (Integer) field.getValue());
                            break;
                        case (MetaField.TEXT):
                        case (MetaField.VARCHAR):
                            insert.setString(i, field.getValue().toString());
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
        return null;
    }
    
    /**
     * Updates a <code>DataSet</code> in the repository.
     * 
     * @param ds
     * @return Returns always null
     */
    public DataSet update(DataSet ds) {
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
                entry.setEdit(updateEntry(entry));
                
                for (int i = 1; i <= fields.length; i++) {
                    MetaField metaField = fields[i-1].getMetaField();
                    
                    switch (metaField.getType()) {
                        case (MetaField.PK):
                        case (MetaField.INT):
                            update.setObject(i, (Integer) fields[i-1].getValue());
                            break;
                        case (MetaField.TEXT):
                        case (MetaField.VARCHAR):
                            update.setString(i, fields[i-1].getValue().toString());
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
        return null;
    }
    
    /**
     * Deletes a <code>DataSet</code> from the repository.
     * 
     * @param ds
     * @return Returns always null
     */
    public DataSet delete(DataSet ds) {
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
        ds = null;
        return null;
    }
    
    /**
     * Retrieves a <code>DataSet</code> according to the 
     * defined <code>QueryRequest</code> from the repository.
     * 
     * @see DataSet
     * @see QueryRequest
     * 
     * @param ds
     * @return Returns the requested <code>DataSet</code>
     */
    public DataSet load(QueryRequest qr) {
    	Vector tmp = qr.getVector();
        if (tmp.size() == 0)    return null;
        
        DataSet ds = new DataSet();
        
        String ent = qr.getEntity().getName();
        String entryField = ent + ".EntryId";
        
        String query = "SELECT * FROM " + ent;
        query += " LEFT JOIN Entry ON " + entryField + " = Entry.EntryId ";
        query += " LEFT JOIN Users ON Entry.EntryUsersId = Users.UsersUUID ";
        
        query += createRequestStatement(qr);
        DataBus.logger.debug(query);
        
        try {   
            ResultSet result = 
                dbConnection.executeQuery(query);
            ResultSetMetaData rsmd = result.getMetaData();
            
            MetaData metaData = DataBus.getMetaData();
            
            while (result.next()) {
                DataElement e = new DataElement(qr.getEntity());
                
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    
                    MetaField mf = metaData.getMetaField(
                        rsmd.getTableName(i),
                        rsmd.getColumnLabel(i)
                    );
                    
                    if (mf != null) {
                        Field field = new Field(mf);
                        switch(mf.getType()) {
                            case(MetaField.INT):
                            case(MetaField.PK):
                                field.setValue(
                                    new Integer(result.getInt(mf.getName()))
                                );
                                break;
                            case(MetaField.TEXT):
                            case(MetaField.VARCHAR):
                                field.setValue(result.getString(mf.getName()));
                                break;
                            case(MetaField.BOOLEAN):
                                field.setValue(
                                    new Boolean(result.getBoolean(mf.getName()))
                                );
                                break;
                        }
                        e.setField(field);
                    } else {
                        //DataBus.logger.debug("Field not in MetaData: " + mf.getIdentifier());   
                    }
                }
                
                // static fields from Users and Entry tables
                User owner = new User(
                    result.getString("UsersUUID"),
                    result.getString("UsersName"),
                    result.getString("UsersUsername")
                );
                
                Entry entry = new Entry(
                    result.getInt("EntryId"),
                    result.getTimestamp("EntryCreation"),
                    result.getTimestamp("EntryEdit"),
                    owner
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
    
    /**
     * 
     * 
     * Syntax: Field.name Comperator Value
     * 
     * @param qc
     * @return 
     */
    private String createCondStatement(QueryCondition qc) {
        if (qc == null) 
            throw new IllegalArgumentException();
        
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
        if (qr == null) throw new IllegalArgumentException();
        
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
            "(EntryId, EntryCreation, EntryEdit, EntryUsersId) " +
            "VALUES (?, ?, ?, ?);";
        java.sql.PreparedStatement insertPS = 
            dbConnection.prepareStatement(sql);
        
        try {
            id = getNextPK("Entry", "EntryId");
            insertPS.setInt(1, id);
            insertPS.setTimestamp(2, entry.getCreation());
            insertPS.setTimestamp(3, entry.getEdit());
            insertPS.setString(4, entry.getUser().getUUID());
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
        
        java.sql.PreparedStatement sqlNextPK = 
            dbConnection.prepareStatement("SELECT MAX(?) FROM ?;");
        
        try {
            sqlNextPK.setString(1, pk);
            sqlNextPK.setString(2, entity);
            
            ResultSet result =  
                sqlNextPK.executeQuery();
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
     * @return Returns the name of the repository
     */
    public String getName() {
        return name;
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
        
        panel.add(new JLabel("Ignore Gross- Kleinschreibung"));
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
                        panel.repaint();
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

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

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 *
 * @author crac
 * @version $Id: MckoiRepository.java,v 1.5 2004/06/21 12:56:41 crac Exp $
 */
public final class MckoiRepository implements Repository {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private static final String name = "Mckoi SQL DB Repository";
    
    private static final String file = "conf" + File.separator + 
        "mckoi_repository.ini";
    
    private DatabaseSettings settings = 
        new DatabaseSettings(file);
    
    private DatabaseConnection dbConnection = 
        new DatabaseConnection(settings);
    
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
     * Loads all meta information of the repository.
     * 
     * @see Repository
     * @see MetaField
     * @see MetaEntity
     * @see MetaData
     * 
     * @throws InterError if meta data could not be loaded.
     * @return
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
                DataBus.logger.debug("MetaField added: " + mf.toString());
            }
        } catch (SQLException e) {
            DataBus.logger.fatal("Could not load MetaData.");
            throw new InternalError("Could not load MetaData.");
        }
        DataBus.logger.info("MetaData loaded.");
        return data;
    }

    /**
     * 
     * @param ds
     * @return
     */
    public DataSet insert(DataSet ds) {
        if (ds.isEmpty()) return null;
        
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
            
            // prepare sql statement
            Field[] fields = ds.getFields();
            for (int i = 0; i < fields.length; i++) {
                sql += " , " + fields[i].getName();
                values += ",?";
            }
            
            java.sql.PreparedStatement insert = 
                dbConnection.prepareStatement(sql + values + ");");
            
            try {
                // set values
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    MetaField metaField = field.getMetaField();
                    
                    switch (metaField.getType()) {
                        case (MetaField.PK):
                            int pk = dbConnection.getNextPrimaryKey(
                                field.getMetaField().getEntity().getName()
                            );
                            insert.setInt(i, pk);
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
                throw new InternalError("DataElement not inserted.");
            }
            DataBus.logger.debug("DataElement inserted.");
        }
        
        DataBus.logger.debug("DataSet inserted.");   
        return null;
    }
    
    /**
     * 
     * @param ds
     * @return
     */
    public DataSet update(DataSet ds) {
        if (ds.isEmpty()) return null;
        
        String sql = "UPDATE " + ds.getMetaEntity().getName();
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
     * Retrieves a <code>DataSet</code> according to the 
     * defined <code>QueryRequest</code> from the repository.
     * 
     * @see DataSet
     * @see QueryRequest
     * 
     * @param ds
     * @return 
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
            
            while (result.next()) {
                DataElement e = new DataElement();
                
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    MetaData metaData = DataBus.getMetaData();
                    MetaField mf = new MetaField(
                        rsmd.getColumnLabel(i), 
                        new MetaEntity(rsmd.getTableName(i))
                    );
                    
                    // only if known to MetaData
                    if (metaData.contains(mf)) {
                        mf = metaData.getMetaField(mf);
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
                        
                        e.add(field);
                    } else {
                        DataBus.logger.debug("Field not in MetaData: " + mf.toString());   
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
    
    /**
     * 
     * @param entry
     * @return
     */
    private int insertEntry(Entry entry) {
        if (entry == null) return 0;
        
        int id = 0;
        String sql = "INSERT INTO Entry " +
            "(EntryId, EntryCreation, EntryEdit, EntryUsersId) " +
            "VALUES (?, ?, ?, ?);";
        java.sql.PreparedStatement insertPS = 
            dbConnection.prepareStatement(sql);
        
        try {
            id = dbConnection.getNextPrimaryKey("Entry");
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
     * @return Returns the name of the repository.
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return Returns the config panel.
     */
    public JPanel getConfPanel() {
        JPanel outer = new JPanel();
        outer.setLayout(new BorderLayout());
        
        // top
        JPanel top = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        
        panel.add(new JLabel("Benutzername"));
        final JTextField user = 
            new JTextField(settings.getUser());
        panel.add(user);
        
        panel.add(new JLabel("Passwort"));
        final JPasswordField pwd = 
            new JPasswordField(settings.getPassword());
        panel.add(pwd);
        
        top.add(panel);
        
        // bottom
        JButton save = new JButton("save");
        save.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("save")) {
                        settings.setPassword( 
                            pwd.getPassword().toString()
                        );
                        settings.setUser(user.getText());
                        settings.saveConfig();
                    }
                }
            });

        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        bottom.add(save);
        
        outer.add(top, BorderLayout.CENTER);
        outer.add(bottom, BorderLayout.SOUTH);
        
        JPanel full = new JPanel();
        full.setLayout(new FlowLayout());
        full.add(outer);
        
        return full;
    }
}

package ch.fha.mediamanager.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * It connects to a database using the configuration
 * defined in the ini-file named by the static constant {@link #DB_CONF_FILE_NAME}.
 * If the configuration file can not be found, a new default one will be
 * created. 
 * 
 * <p>You can connect to the specified database invoking the <code>connect</code>
 * method. Afterwards the class provides several methods for using the database
 * in your java application.</p>
 * 
 * @author ia02vond, crac
 * @version $Id: DatabaseConnection.java,v 1.3 2004/06/18 12:04:44 crac Exp $
 */
public class DatabaseConnection {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private DatabaseSettings settings;
    
    /** 
     * The database connection. There's only one used for the whole
     * application
     */
    private Connection connection;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     */
    public DatabaseConnection() {}
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Executes the given SQL statement, which returns a single <code>ResultSet</code> object.
     * 
     * @param sql    an SQL statement to be sent to the database, typically
     *               a static SQL </code>SELECT</code> statement.
     * 
     * @return       a <code>ResultSet</code> object that contains the data 
     *               produced by the given query. It's never <code>null</code>
     * 
     * @throws       RuntimeException
     *               if a database access error ccurs of the given SQL statement
     *               produces anything other than a single <code>ResultSet</code>
     */
    public ResultSet executeQuery(String sql) throws RuntimeException {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            DataBus.logger.warn("Error while executing query '" + sql);
            e.printStackTrace();
            throw new RuntimeException("Erroneous database query.");
        }
    }
    
    /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>DELETE</code> statement or an SQL statement
     * that returns nothing, such as an SQL DDL statement.
     * 
     * @param sql   an SQL statement
     * 
     * @return      either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     *              or <code>DELETE</code> statements, or <code>0</code> for SQL
     *              statements that return nothing
     * 
     * @throws      RuntimeException
     *              if a database access error occurs of the given SQL statement
     *              produces a <code>ResultSet</code>
     */
    public int executeUpdate(String sql) throws RuntimeException {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println ("Error while executing update '" + sql);
            e.printStackTrace();
            throw new RuntimeException("Erroneous database query.");
        }
    }
    
    /**
     * Returns the next primarykey for the given entity name.
     * <p>ATTENTION: the entity's primarykey column name must be called
     * <code>id</code>
     * 
     * @param entityName   the entity name which the next primarykey should be acquired of
     * 
     * @return the next primarykey
     * 
     * @throws RuntimeException
     *         if a database access error occurs
     */
    public int getNextPrimaryKey(String entityName) throws RuntimeException {
        ResultSet result = executeQuery("select max(id) as max from " + entityName + ";");
        
        try {
            result.next();
            return result.getInt("max") + 1;
        } catch (SQLException e) {
            System.out.println ("Error while getting next primary key.");
            e.printStackTrace();
            throw new RuntimeException("Fehlerhafte Datenbank Anfrage.");
        }
    }
    
    /**
     * Creates a <code>PreparedStatement</code> object for sending
     * parameterized SQL statements to the database.
     * 
     * <p>A SQL statement with or without IN parameters can be
     * pre-compiled and stored in a <code>PreparedStatement</code> object. This
     * object can then be used to efficiently execute this statement
     * multiple times.</p>
     *
     * <p><b>Note:</b> This method is optimized for handling
     * parametric SQL statements that benefit from precompilation. If
     * the driver supports precompilation,
     * the method <code>prepareStatement</code> will send
     * the statement to the database for precompilation. Some drivers
     * may not support precompilation. In this case, the statement may
     * not be sent to the database until the <code>PreparedStatement</code> 
     * object is executed.  This has no direct effect on users; however, it does
     * affect which methods throw certain <code>SQLException</code> objects.
     * </p>
     * 
     * @param query  an SQL statement that may contain one or more '?' IN
     *               parameter placeholders
     * 
     * @return       a new default <code>PreparedStatement</code> object
     *               containing the pre-compiled SQL statement
     *  
     * @exception    RuntimeException
     *               if a database access error occurs
     */
    public PreparedStatement prepareStatement(String query) throws RuntimeException {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println ("Error while preparing statement '" + query);
            e.printStackTrace();
            throw new RuntimeException("Erroneous database query.");
        }
    }


    /**
     * Connects to the database in three steps:
     * <ol>
     *   <li>start mysql-server if the databasePath not equals <code>null</code>
     *   <li>load jdbc-driver
     *   <li>connect to database
     * </ol>
     * 
     * @throws RuntimeException
     *         if an error occurs while connecting to the database
     */
    public void connect() throws RuntimeException {
        boolean error = false;

        // start mysql-server
        /*if (!(databasePath == null || databasePath.equals(""))) {
            try {
                System.out.println ("***" + databasePath + "***");
                Runtime.getRuntime().exec( databasePath );
                if (debug) System.out.println ("[database started]");
            } catch (IOException e) {
                if (debug) {
                    System.out.println ("[error while starting database]");
                    e.printStackTrace();
                }
                error = true;
            }
        }*/
    
        // load driver
        try {
            Class.forName(settings.getDriver()).newInstance();
        } catch (Exception e) {
            DataBus.logger.warn("Database driver is not ready.");
            e.printStackTrace();
            error = true;
        }
    
        // get connection
        String con;
        if (settings.getSubprotocol().equals("mckoi:local")) { 
            con = settings.getHost();
        }
        else {
            con = settings.getHost() + ":" + 
                settings.getPort() + "/" + 
                settings.getDatabaseName();
        }
        
        String url = settings.getProtocol() + ":" + 
            settings.getSubprotocol() + "://" + con;
        
        try {
            connection = (java.sql.Connection)
                DriverManager.getConnection(url, 
                   settings.getUser(), 
                   settings.getPassword());
        } catch (SQLException e) {
            DataBus.logger.warn("Database connection not established.");
            e.printStackTrace();
            error = true;
        }
    
        if (error) {
            throw new RuntimeException("Beim Verbinden mit der Datenbank ist ein Fehler aufgetreten.");
        }
    }

}
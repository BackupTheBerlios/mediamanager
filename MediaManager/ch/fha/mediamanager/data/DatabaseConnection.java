package ch.fha.mediamanager.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;

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
 * @version $Id: DatabaseConnection.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */
public class DatabaseConnection {
    
    /** If true debug messages are printed out.*/
    //  It's defined as a public instance variable because the TestDatabaseConnection
    //  class makes use of it.
    public static boolean debug = false;
    
    /** The name of the database configration file.*/
    public final static String DB_CONF_FILE_NAME = 
        "conf" + File.separator + "dbconf.ini";
    
    
    /** 
     * Default database configuration. Used when creating a new
     * database configuration file.
     */
    private String driver       = "com.mckoi.JDBCDriver",
                   protocol     = "jdbc",
                   subprotocol  = "mckoi:local",
                   host         = "./conf/mckoi.conf",
                   port         = "{Port}",
                   databasePath = "{DatabasePath}",
                   databaseName = "{DatabaseName}",
                   user         = "{User}",
                   password     = "{Password}";
    
    /** 
     * The database connection. There's only one used for the whole
     * application
     */
    private Connection connection;
    
    
    /**
     * Configurates the database connection.
     *
     * @throws LogicException
     *         if an error occurs
     */
    public DatabaseConnection() throws LogicException {
        configureDatabaseConnection();
    }
    
    /**
     * Executes the given SQL statement, which returns a single <code>ResultSet</code> object.
     * 
     * @param sql    an SQL statement to be sent to the database, typically
     *               a static SQL </code>SELECT</code> statement.
     * 
     * @return       a <code>ResultSet</code> object that contains the data 
     *               produced by the given query. It's never <code>null</code>
     * 
     * @throws       LogicException
     *               if a database access error ccurs of the given SQL statement
     *               produces anything other than a single <code>ResultSet</code>
     */
    public ResultSet executeQuery(String sql) throws LogicException {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            if (debug) {
                System.out.println ("[error while executing query '" + sql + "']");
                e.printStackTrace();
            }
            throw new LogicException("Erroneous database query.");
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
     * @throws      LogicException
     *              if a database access error occurs of the given SQL statement
     *              produces a <code>ResultSet</code>
     */
    public int executeUpdate(String sql) throws LogicException {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if (debug) {
                System.out.println ("[error while executing update '" + sql + "']");
                e.printStackTrace();
            }
            throw new LogicException("Erroneous database query.");
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
     * @throws LogicException
     *         if a database access error occurs
     */
    public int getNextPrimaryKey(String entityName) throws LogicException {
        ResultSet result = executeQuery("select max(id) as max from " + entityName + ";");
        
        try {
            result.next();
            return result.getInt("max") + 1;
        } catch (SQLException e) {
            if (debug) {
                System.out.println ("[error while getting next primary key]");
                e.printStackTrace();
            }
            throw new LogicException("Fehlerhafte Datenbank Anfrage");
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
     * @exception    LogicException
     *               if a database access error occurs
     */
    public PreparedStatement prepareStatement(String query) throws LogicException {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            if (debug) {
                System.out.println ("[error while preparing statement '" + query + "']");
                e.printStackTrace();
            }
            throw new LogicException("Erroneous database query.");
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
     * @throws LogicException
     *         if an error occurs while connecting to the database
     */
    public void connect() throws LogicException {
        boolean error = false;

        // start mysql-server
        if (!(databasePath == null || databasePath.equals(""))) {
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
        }
    
        // load driver
        try {
            Class.forName(driver).newInstance();
            if (debug) System.out.println ("[database driver is ready]");
        } catch (Exception e) {
            if (debug) {
                System.out.println ("[database driver is not ready]");
                e.printStackTrace();
            }
            error = true;
        }
    
        // get connection
        String con;
        if (subprotocol.equals("mckoi:local")) con = host;
        else con = host + ":" + port + "/" + databaseName;
        
        String url = protocol + ":" + subprotocol + "://" + con;
        
        if (debug) System.out.println ("URL: " + url);
        try {
            connection = (java.sql.Connection)DriverManager.getConnection(url, 
                user, password);
            if (debug) System.out.println ("[database connection established]");
        } catch (SQLException e) {
            if (debug) {
                System.out.println ("[database connection established]");
                e.printStackTrace();
            }
            error = true;
        }
    
        if (error) {
            throw new LogicException("Beim Verbinden mit der Datenbank ist ein Fehler aufgetreten.");
        }
    }

    
    /**
     * Loads the database configuration from the propertie file. If none's found
     * a new one will be created according to the default database configuration.
     * 
     * @throws LogicException
     *         if an error occurs
     * 
     * @see    #createDefaultDatabaseConfiguration
     * @see    #loadDatabaseConfiguration 
     */
    private void configureDatabaseConnection() throws LogicException {
        File file = new File(DB_CONF_FILE_NAME);
    
        if (!file.exists()) {
            createDefaultDatabaseConfiguration(file);
        } else {
            loadDatabaseConfiguration();
        }
    }

    /**
     * Creates a new default database configuration according to the static defined variables.
     * 
     * @param file    a <code>File</code> object representing the database configuration file
     * 
     * @throws LogicException
     *         if an @see IOException is thrown while creating a new default
     *         database configuration file
     */
    private void createDefaultDatabaseConfiguration(File file) throws LogicException {
        try {
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            Properties outProp = new Properties();

            outProp.setProperty("driver", driver);
            outProp.setProperty("protocol", protocol);
            outProp.setProperty("subprotocol", subprotocol);
            outProp.setProperty("host", host);
            outProp.setProperty("port", port);
            outProp.setProperty("databasePath", databasePath);
            outProp.setProperty("databaseName", databaseName);
            outProp.setProperty("user", user);
            outProp.setProperty("password", password);

            outProp.store(output, "database configuration");
        
            output.close();

            if (debug) System.out.println ("[create new " + DB_CONF_FILE_NAME + "]");
        
        } catch (IOException e) {
            if (debug) {
                System.out.println ("[error while creating file: " + DB_CONF_FILE_NAME + "]");
                e.printStackTrace();
            }
            throw new LogicException("Error while creating db config file '" + DB_CONF_FILE_NAME + "'.");
        }
    }

    /**
     * Loads the database configuration from the dbconf ini-file.
     * 
     * @throws LogicException
     *         if an @see IOException is thrown while loading from the
     *         database connection file
     */
    private void loadDatabaseConfiguration() throws LogicException {
        try {
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(DB_CONF_FILE_NAME)));

            Properties property = new Properties();

            property.load(input);

            driver       = property.getProperty("driver");
            protocol     = property.getProperty("protocol");
            subprotocol  = property.getProperty("subprotocol");
            host         = property.getProperty("host");
            port         = property.getProperty("port");
            databasePath = property.getProperty("databasePath");
            databaseName = property.getProperty("databaseName");
            user         = property.getProperty("user", null);
            password     = property.getProperty("password", null);

            input.close();

            if (debug) System.out.println ("[database configuration loaded]");
        } catch (IOException e) {
            if (debug) {
                System.out.println ("[error while reading from file: " + DB_CONF_FILE_NAME + "]");
                e.printStackTrace();
            }
            throw new LogicException("Error while reading db config file.");
        }
    }
}
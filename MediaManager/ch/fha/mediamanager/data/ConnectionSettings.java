package ch.fha.mediamanager.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

/**
 * Database settings.
 * 
 * @author luca
 * @version $Id: ConnectionSettings.java,v 1.1 2004/06/23 12:01:05 crac Exp $
 */
public final class ConnectionSettings {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private String  user = "",
                    password = "",
                    driver = "",
                    port = "",
                    host = "",
                    protocol = "",
                    subprotocol = "",
                    databasePath = "",
                    databaseName = "";
    
    private String config;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param config
     */
    public ConnectionSettings(String config) {
        this.config = config;
        loadConfig();
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Loads settings from config file.
     * 
     * @throws InternalError
     */
    protected void loadConfig() {
        try {
            DataInputStream input = new DataInputStream(
               new BufferedInputStream(
                   new FileInputStream(config)
               )
            );

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
            
            DataBus.logger.info("Db config file loaded.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not read db config file.");
            throw new InternalError("Could not read db config file.");
        }
    }
    
    /**
     * Saves settings to the config file.
     *
     * @throws InternalError
     */
    protected void saveConfig() {
        try {
            DataOutputStream output = new DataOutputStream(
               new BufferedOutputStream(
                   new FileOutputStream(config)
               )
            );
            
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
            
            DataBus.logger.info("Db config file written.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not write db config file.");
            throw new InternalError("Could not write db config file.");
        }
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * @return Returns the databaseName.
     */
    protected String getDatabaseName() {
        return databaseName;
    }
    
    /**
     * @return Returns the databasePath.
     */
    protected String getDatabasePath() {
        return databasePath;
    }
    
    /**
     * @return Returns the driver.
     */
    protected String getDriver() {
        return driver;
    }
    
    /**
     * @return Returns the host.
     */
    protected String getHost() {
        return host;
    }
    
    /**
     * @return Returns the password.
     */
    protected String getPassword() {
        return password;
    }
    
    /**
     * @return Returns the port.
     */
    protected String getPort() {
        return port;
    }
    
    /**
     * @return Returns the protocol.
     */
    protected String getProtocol() {
        return protocol;
    }
    
    /**
     * @return Returns the subprotocol.
     */
    protected String getSubprotocol() {
        return subprotocol;
    }
    
    /**
     * @return Returns the user.
     */
    protected String getUser() {
        return user;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------

	/**
	 * @param databaseName The databaseName to set.
	 */
    protected void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	/**
	 * @param databasePath The databasePath to set.
	 */
    protected void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}
	
	/**
	 * @param driver The driver to set.
	 */
    protected void setDriver(String driver) {
		this.driver = driver;
	}
	
	/**
	 * @param host The host to set.
	 */
    protected void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @param password The password to set.
	 */
    protected void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @param port The port to set.
	 */
    protected void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * @param protocol The protocol to set.
	 */
    protected void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * @param subprotocol The subprotocol to set.
	 */
    protected void setSubprotocol(String subprotocol) {
		this.subprotocol = subprotocol;
	}
	
	/**
	 * @param user The user to set.
	 */
    protected void setUser(String user) {
		this.user = user;
	}
}

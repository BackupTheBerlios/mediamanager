package ch.fha.mediamanager.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

import com.eaio.uuid.UUID;

/**
 *
 * @author crac
 * @version $Id: User.java,v 1.5 2004/06/24 15:58:51 crac Exp $
 */
public final class User {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    private String uuId;
    private String name;
    private String username;
    
    /* config file */
    private final static String userFile = "conf" + 
         java.io.File.separator + "user.ini";
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param uuId
     * @param name
     * @param username
     */
    public User(String uuId, String name, String username) {
        this.uuId = uuId;
        this.name = name;
        this.username = username;
    }
    
    /**
     * 
     * @param uuId
     */
    public User(String uuId) {
        this.uuId = uuId;
    }
    
    /**
     * 
     */
    public User() {
        load();
    }
    
    // --------------------------------
    // OPERATIONS
    // --------------------------------
    
    /**
     * Loads user data from config file.
     */
    protected void load() {
        java.io.File file = new java.io.File(userFile);
        
        if (! file.exists()) createDefaultConf();
        
        try {
            DataInputStream input = new DataInputStream(
               new BufferedInputStream(
                   new FileInputStream(userFile)
               )
            );

            Properties property = new Properties();
            property.load(input);
            
            name       = property.getProperty("name", "name");
            username   = property.getProperty("username", "username");
            uuId       = property.getProperty("uuid", null);
            
            if ((uuId == null) || uuId.equals("")) {
                uuId = createUUID();
                save();
            }
            
            DataBus.logger.info("User config file loaded.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not read user config file.");
            throw new InternalError("Could not read user config file.");
        }
    }
    
    /**
     * Saves user data to config file.
     */
    protected void save() {
        try {
            DataOutputStream output = new DataOutputStream(
               new BufferedOutputStream(
                   new FileOutputStream(userFile)
               )
            );
            
            Properties outProp = new Properties();

            outProp.setProperty("name", name);
            outProp.setProperty("username", username);
            outProp.setProperty("uuid", uuId);

            outProp.store(output, "user configuration");
            output.close();
            
            DataBus.logger.info("User config file written.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not write user config file.");
            throw new InternalError("Could not write user config file.");
        }
    }
    
    /**
     * 
     */
    private void createDefaultConf() {
        username = "";
        name = "";
        uuId = "";
        save();
    }
    
    /**
     * Creates a unique UserId.
     * 
     * @see com.eaio.uuid.UUID
     */
    private String createUUID() {
        String uuid = new UUID().toString();
        DataBus.logger.info("UUID " + uuid + " created.");
        return uuid;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    public String getUUID() {
        return uuId;
    }
    
    /**
     * 
     * @return
     */
    public String getUsername() {
        return username;   
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}

package ch.fha.mediamanager.data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author crac
 * @version $Id: User.java,v 1.6 2004/06/24 17:33:55 crac Exp $
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
     * Creates a default config file.
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
        String uuid = new com.eaio.uuid.UUID().toString();
        DataBus.logger.info("UUID " + uuid + " created.");
        return uuid;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return Returns the unique user id
     */
    public String getUUID() {
        return uuId;
    }
    
    /**
     * 
     * @return Returns the username
     */
    public String getUsername() {
        return username;   
    }

    /**
     * 
     * @return Returns the full name of the 
     *      user
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return Returns configuration Panel 
     *      for the user settings
     */
    public JPanel getConfPanel() {
        JPanel outer = new JPanel();
        outer.setLayout(new BorderLayout());
        
        // top
        JPanel top = new JPanel();
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        
        panel.add(new JLabel("Voller Name"));
        final JTextField name = 
            new JTextField(getName());
        panel.add(name);
        
        panel.add(new JLabel("Benutzername"));
        final JTextField username = 
            new JTextField(getUsername());
        panel.add(username);
        
        JButton save = new JButton("save");
        save.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("save")) {
                        setUsername(username.getText());
                        setName(name.getText());
                        save();
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
    
    // --------------------------------
    // MUTATORS
    // --------------------------------

    /**
     * 
     * @param name
     */
    protected void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @param username
     */
    protected void setUsername(String username) {
        this.username = username;
    }
}

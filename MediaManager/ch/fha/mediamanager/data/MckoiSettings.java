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
 * @author luca
 * @version $Id: MckoiSettings.java,v 1.1 2004/06/23 12:00:09 crac Exp $
 */
public class MckoiSettings {
    
    // --------------------------------
    // FIELDS
    // --------------------------------
    
    /* Mckoi SQL Database default settings */
    private static final String DB_DATA_PATH = "./data";
    private static final String DB_LOG_PATH = "./data/log";
    private static final String DB_IGNORE_CASE_SENSITIV = "disabled";
    private static final String DB_READ_ONLY = "disabled";
    private static final String DB_DATA_CACHE = "1048576";
    private static final String DB_ENTRY_CACHE = "8192";
    private static final String DB_MAX_THREADS = "4";
    private static final String DB_LOG_LEVEL = "20";
    
    private String  dbPath = "",
                    logPath = "",
                    ignoreCase = "",
                    readOnly = "",
                    dataCache = "",
                    entryCache = "",
                    workerThreads = "",
                    logLevel = "";
    
    private String config;
    
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
    /**
     * 
     * @param config
     */
    public MckoiSettings(String config) {
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
            
            dbPath          = property.getProperty("database_path");
            logPath         = property.getProperty("log_path");
            ignoreCase      = property.getProperty("ignore_case_for_identifiers");
            readOnly        = property.getProperty("read_only");
            dataCache       = property.getProperty("data_cache_size");
            entryCache      = property.getProperty("max_cache_entry_size");
            workerThreads   = property.getProperty("maximum_worker_threads");
            logLevel        = property.getProperty("debug_level");
            
            DataBus.logger.info("mckoi config file loaded.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not read mckoi config file.");
            throw new InternalError("Could not read mckoi config file.");
        }
    }
    
    /**
     * Saves the settings to the config file.
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

            outProp.setProperty("database_path", dbPath);
            outProp.setProperty("log_path", logPath);
            outProp.setProperty("ignore_case_for_identifiers", ignoreCase);
            outProp.setProperty("read_only", readOnly);
            outProp.setProperty("data_cache_size", dataCache);
            outProp.setProperty("max_cache_entry_size", entryCache);
            outProp.setProperty("maximum_worker_threads", workerThreads);
            outProp.setProperty("debug_level", logLevel);

            outProp.store(output, "database configuration");
            output.close();
            
            DataBus.logger.info("Mckoi config file written.");
        } catch (IOException e) {
            DataBus.logger.fatal("Could not write mckoi config file.");
            throw new InternalError("Could not write mckoi config file.");
        }
    }
    
    /**
     * Restores default values.
     */
    protected void restoreDefaults() {
        dbPath = DB_DATA_PATH;
        logPath = DB_LOG_PATH;
        ignoreCase = DB_IGNORE_CASE_SENSITIV;
        readOnly = DB_READ_ONLY;
        dataCache = DB_DATA_CACHE;
        entryCache = DB_ENTRY_CACHE;
        workerThreads = DB_MAX_THREADS;
        logLevel = DB_LOG_LEVEL;
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
    
    /**
     * 
     * @return
     */
    protected String getConfig() {
        return config;
    }
    
    /**
     * 
     * @return
     */
    protected String getDataCache() {
        return dataCache;
    }
    
    /**
     * 
     * @return
     */
    protected String getDbPath() {
        return dbPath;
    }
    
    /**
     * 
     * @return
     */
    protected String getEntryCache() {
        return entryCache;
    }
    
    /**
     * 
     * @return
     */
    protected String getIgnoreCase() {
        return ignoreCase;
    }
    
    /**
     * 
     * @return
     */
    protected String getLogLevel() {
        return logLevel;
    }
    
    /**
     * 
     * @return
     */
    protected String getReadOnly() {
        return readOnly;
    }
    
    /**
     * 
     * @return
     */
    protected String getLogPath() {
        return logPath;
    }
    
    /**
     * 
     * @return
     */
    protected String getWorkerThreads() {
        return workerThreads;
    }
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
	
    /**
     * 
     * @param config
     */
	protected void setConfig(String config) {
		this.config = config;
	}
	
    /**
     * 
     * @param dataCache
     */
	protected void setDataCache(String dataCache) {
		this.dataCache = dataCache;
	}
	
    /**
     * 
     * @param dbPath
     */
	protected void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
	
    /**
     * 
     * @param entryCache
     */
	protected void setEntryCache(String entryCache) {
		this.entryCache = entryCache;
	}
	
    /**
     * 
     * @param ignoreCase
     */
	protected void setIgnoreCase(String ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
	
    /**
     * 
     * @param logLevel
     */
	protected void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	
    /**
     * 
     * @param logPath
     */
	protected void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	
    /**
     * 
     * @param readOnly
     */
	protected void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	
    /**
     * 
     * @param workerThreads
     */
	protected void setWorkerThreads(String workerThreads) {
		this.workerThreads = workerThreads;
	}
}

package plugins.cddb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


/**
* Settings provides access to several properties and resources that are used
* throughout the classes of this Antelmann.com framework. <p>
* The default settings are loaded from a well known location
* which denotes a file that is commonly distributed along with the
* antelmann.jar file.
* The default settings can be overwritten by simply altering the Properties
* returned by <code>getProperties()</code>, where a subsequent call to that
* method will reflect the changes (similar to <code>System.getProperties()</code>.
* <p>
* If the Settings are used in special environments (such as within an application
* server), you may need to manually use the <code>setClassLoader(ClassLoader)</code>
* method to specify a ClassLoader appropriate for the given context, before
* accessing any resources.
* @author Holger Antelmann
* @since 3/24/2002
*/
public final class Settings
{
    static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    /**
    * denotes the relative location of the file for the default Properties,
    * so that it can be found through the <code>ClassLoader</code>
    */
    public static String defaultPropertyFile = "plugins/cddb/properties";
    static Properties currentProperties = null;


    private Settings () {}


    /**
    * sets the ClassLoader to be used in <code>getResource(String)</code>;
    * by default, the system class loader is used
    * @see #getResource(String)
    */
    public static void setClassLoader (ClassLoader cl) {
        classLoader = cl;
    }


    /**
    * sets the properties to be used by Settings; this should not be used
    * unless you need to overwrite the default properties
    */
    public static void init (Properties newProperties) {
        currentProperties = newProperties;
    }


    /**
    * sets the class loader to be used to the system class loader (used by default)
    */
    public static void setSystemClassLoader () {
        setClassLoader(ClassLoader.getSystemClassLoader());
    }


    /*
    * sets the class loader to be used to the context class loader of the
    * current thread
    */
    public static void setContextClassLoader () {
        setClassLoader(Thread.currentThread().getContextClassLoader());
    }


    /** returns the ClassLoader currently used to locate resources */
    public static ClassLoader getClassLoader () { return classLoader; }


    /**
    * This method encapsulates calls to the embedded ClassLoader to ease
    * dealing with problems when not finding the resource.
    * Instead of just returning null if a resource is not found, an exception is
    * thrown (which is particularly helpful for image loading in swing).
    * If you are working in special environments where the system class loader
    * may not find the resource you are looking for, you need to set the class
    * loader of this class to the one you need.
    * @see #setClassLoader(ClassLoader)
    * @throws ResourceNotFoundException if the given resource could not be found
    */
    public static URL getResource (String resource) throws ResourceNotFoundException {
        URL url = classLoader.getResource(resource);
        if (url == null) {
            String s = "the given resource could not be found: \"" + resource + "\"";
            throw new ResourceNotFoundException(s, resource);
        } else {
            return url;
        }
    }


    /**
    * This method will try to find the value for the given key
    * and return it - or return the defaultValue if the property
    * was not found or the default property file could not be opened
    */
    public static String getProperty (String key, String defaultValue) {
        String value = null;
        try {
            value = getProperty(key);
        } catch (ResourceNotFoundException e) {
            return defaultValue;
        }
        if (value == null) return defaultValue;
        return value;
    }


    /**
    * provides a shortcut for <code>getProperties().getPropterty(key)</code>
    * @see #getProperties()
    * @throws ResourceNotFoundException if the property file could not be located
    */
    public static String getProperty (String key) throws ResourceNotFoundException {
        return getProperties().getProperty(key);
    }


    /**
    * This method returns the current application properties;
    * changes to the Properties will be reflected in subsequent
    * calls to this method.
    * @throws ResourceNotFoundException if the property file could not be located
    * @see #reset()
    */
    public static Properties getProperties () throws ResourceNotFoundException {
        if (currentProperties == null) {
            try {
                Properties p = new Properties();
                InputStream in = getResource(defaultPropertyFile).openStream();
                p.load(in);
                currentProperties = p;
                return currentProperties;
            } catch (IOException e) {
                RuntimeException ex =  new ResourceNotFoundException("cannot locate default property file");
                e.initCause(e);
                throw ex;
            }
        } else {
            return currentProperties;
        }
    }


    /**
    * This method forces to reload the default settings from file.
    * Any user settings that were set before are not present anymore when
    * calling <code>getProperties()</code> hereafter.
    * @see #getProperties()
    * @see #defaultPropertyFile
    */
    public static void  reset () {
        currentProperties = null;
        getProperties();
    }


    /** returns a fresh set of default Properties directly from the Antelmann.com website */
    public static Properties getOnlineProperties () {
        Properties properties = new Properties();
        try {
            URL url = new URL(getProperties().getProperty("application.online.properties.url"));
            properties.load(url.openStream());
        } catch (IOException e) {}
        return properties;
    }
}
package plugins.cddb;

import java.io.Serializable;


/**
* Level classifies the logging of LogEntry objects.
* The implementation is inspired by java.util.logging, but simpler.
* @author Holger Antelmann
* @see LogEntry
* @see Logger
*/
public class Level implements Serializable
{
    public static final Level CONFIG  = new Level("CONFIG");
    public static final Level FINE    = new Level("FINE");
    public static final Level FINER   = new Level("FINER");
    public static final Level FINEST  = new Level("FINEST");
    public static final Level INFO    = new Level("INFO");
    public static final Level WARNING = new Level("WARNING");
    public static final Level SEVERE  = new Level("SEVERE");
    String text;

    public Level (String text) {
        this.text = text;
    }


    public String toString () { return text; }
}

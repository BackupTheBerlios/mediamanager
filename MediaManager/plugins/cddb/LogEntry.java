package plugins.cddb;

import java.io.Serializable;

/**
* LogEntry represents a single record of a log logged by
* a Logger object and handled by a LogWriter object.
* As opposed to java.util.logging.LogRecord, this class
* also maintains a stack trace.
* Note on serialization: as this class contains an array
* of Object, the serialization of an instance may throw
* an IOException if one of the contained objects is not
* serializable. A workaround may be to use the
* <code>Object.toString()</code> function to serialize
* the object; this could either be done through subclassing
* or through the LogWriter object that handles the serialization.
* @author Holger Antelmann
* @see Logger
* @see LogWriter
*/
public class LogEntry implements Serializable
{
    public Level               level;
    public String              message;
    public long                time;
    public String              sourceClass;
    public Throwable           thrown;
    public String              threadName;
    public StackTraceElement[] stack;
    public Object[]            parameters;


    public LogEntry () {}


    public LogEntry (
        Level               level,
        String              message,
        long                time,
        String              sourceClass,
        Throwable           thrown,
        String              threadName,
        StackTraceElement[] stack,
        Object[]            parameters)
    {
        this.level       = level;
        this.message     = message;
        this.time        = time;
        this.sourceClass = sourceClass;
        this.thrown      = thrown;
        this.threadName  = threadName;
        this.stack       = stack;
        this.parameters  = parameters;
    }
}

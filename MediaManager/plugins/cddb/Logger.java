package plugins.cddb;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
* Logger somewhat leans on the functionality of the java.util.logging
* functionality of J2SE 1.4, but it's not quite the same (and was implemented
* before J2SE 1.4). <p>
* In addition to the J2SE functionality, it provides the full stack trace
* for the log.
* Whether or not the logging here is written synchronously or asynchronously,
* this is left to the implementation for each LogWriter that is used with the
* Logger. <p>
* All logging methods may throw LogException - propagated from the
* LogWriter objects contained in the logger to the calling thread.
* The writing to the different LogWriters is always performed sequentially
* in the order the LogWriters were added to the Logger. <p>
* It is recommended to always use a log method with the origin parameter and
* pass the 'this' argument from the calling function; this will provide
* useful additional information for the log.
* @author Holger Antelmann
* @see LogWriter
* @see LogEntry
* @see LogException
*/
public class Logger
{
    protected LinkedHashSet writers;
    boolean includeStack = false;


    /** creates an empty Logger that does not write to any log */
    public Logger () {
        writers = new LinkedHashSet(3);
    }

    public Logger (LogWriter handler) {
        this();
        writers.add(handler);
    }


    public Logger (LogWriter[] handler) {
        this();
        for (int i = 0; i < handler.length; i++) {
            writers.add(handler[i]);
        }
    }


    /**
    * returns true if a stack trace is produced when a Logger
    * method creates a LogEntry (false by default)
    */
    public boolean includesStack () { return includeStack; }


    /*
    * determines whether a stack trace is produced when a Logger
    * method creates a LogEntry. Having this set to true will slow
    * down the logging (as the stack trace needs to be produced),
    * but provides more information.
    */
    public void setIncludeStack (boolean on) { includeStack = on; }


    /** adds all LogWriter handler from the given logger */
    public Logger (Logger logger) {
        this();
        addHandlersFromLogger(logger);
    }


    public LogWriter[] getHandlers () {
        return (LogWriter[]) writers.toArray(new LogWriter[writers.size()]);
    }


    /** adds all LogWriter handler from the given logger */
    public synchronized void addHandlersFromLogger (Logger logger) {
        writers.addAll(logger.writers);
    }


    /**
    * adds the given handler to the list of handlers this Logger writes to
    * @return true if the set did not already contain the handler
    */
    public synchronized boolean addHandler (LogWriter handler) {
        return writers.add(handler);
    }


    /**
    * removes the given handler from the list of handlers this Logger writes to
    * @return true if the set contained the handler
    */
    public synchronized boolean removeHandler (LogWriter handler) {
        return writers.remove(handler);
    }


    public boolean containsHandler (LogWriter handler) {
        return writers.contains(handler);
    }


    public void log (String message) throws LogException {
        log(null, null, message, null, null);
    }


    public void log (Object origin, String message) throws LogException {
        log(origin, null, message, null, null);
    }


    public void log (Level level, String message) throws LogException {
        log(null, level, message, null, null);
    }


    public void log (String message, Throwable thrown) throws LogException {
        log(null, null, message, thrown, null);
    }


    public void log (Object origin, Level level, String message) throws LogException {
        log(origin, level, message, null, null);
    }


    public void log (Object origin, String message, Object[] parameters) throws LogException {
        log(origin, null, message, null, parameters);
    }


    public void log (Object origin, Level level, String message, Object parameter1) throws LogException {
        log(origin, level, message, null, new Object[] { parameter1 });
    }


    public void log (Object origin, Level level, String message, Object[] parameters) throws LogException {
        log(origin, level, message, null, parameters);
    }


    public void log (Object origin, Level level, String message, Throwable thrown) throws LogException {
        log(origin, level, message, thrown, null);
    }


    public void log (Object origin, Level level, String message, Throwable thrown, Object[] parameters) throws LogException {
            StackTraceElement[] stack = null;
        if (includeStack) {
            try {
                throw new RuntimeException("log entry");
            } catch (RuntimeException e) {
                stack = e.getStackTrace();
            }
        }
        log(level, message, System.currentTimeMillis(),
            (origin != null)? origin.getClass().getName() : null,
            thrown,
            Thread.currentThread().getName(),
            stack,
            parameters);
    }


    public void log (
        Level level,
        String message,
        long time,
        String sourceClass,
        Throwable thrown,
        String threadName,
        StackTraceElement[] stack,
        Object[] parameters) throws LogException
    {
        log(new LogEntry(level, message, time, sourceClass, thrown, threadName, stack, parameters));
    }


    public synchronized void log (LogEntry entry) throws LogException {
        Iterator i = writers.iterator();
        while (i.hasNext()) {
            ((LogWriter)i.next()).write(entry);
        }
    }
}
package plugins.cddb;

/**
* The LogWriter interface defines objects that can be used
* as a handler for the Logger class.
* It is left to the discretion of the LogWriter implementation
* to perform the write synchronously or asynchronously.
* @author Holger Antelmann
* @see LogException
* @see Logger
*/
public interface LogWriter
{
    /**
    * writes the given LogEntry to the log of this LogWriter
    * @throws LogException if an error occurred while writing the log
    */
    void write (LogEntry entry) throws LogException;
}

package plugins.cddb;


/**
* LogException is thrown when a problem is encountered
* while writing to a LogWriter
* @author Holger Antelmann
* @see LogWriter
*/
public class LogException extends RuntimeException
{
    public LogException () {
        super();
    }


    public LogException (String text) {
        super(text);
    }


    public LogException (String text, Throwable cause) {
        super(text, cause);
    }
}
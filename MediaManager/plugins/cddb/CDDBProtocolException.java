package plugins.cddb;

import java.io.IOException;


/**
* CDDBProtocolException is thrown when the response from a CDDB
* source did not comply to the expected protocol.
* @author Holger Antelmann
*/
public class CDDBProtocolException extends IOException
{
    String protocol;
    String query;
    String result;

    public CDDBProtocolException () {}


    public CDDBProtocolException (String message) {
        super(message);
    }


    public CDDBProtocolException (String message, Throwable cause) {
        super(message);
        initCause(cause);
    }


    public CDDBProtocolException (String message, String protocol,
        String query, String result, Throwable cause)
    {
        super(message);
        initCause(cause);
        this.protocol = protocol;
        this.query    = query;
        this.result   = result;
    }


    public void initProtocol (String protocol) { this.protocol = protocol; }


    public void initQuery (String query) { this.query = query; }


    public void initResult (String result) { this.result = result; }


    /** returns information about the protocol used (URL, port, POST/GET-method if applicable) */
    public String getProtocol () { return protocol; }


    /** returns the query sent to the server */
    public String getQuery () { return query; }


    /** returns the result received from the server - if available */
    public String getResult () { return result; }
}

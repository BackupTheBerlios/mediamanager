package plugins.cddb;


/**
* Thrown to indicate that
* @author Holger Antelmann
*/
public class XmcdFormatException extends RuntimeException
{
    String location;


    public XmcdFormatException (String message, String location, Throwable cause)
    {
        super(message, cause);
        this.location = location;
    }


    public void setLocation (String location) { this.location = location; }


    /**
    * returns either the DiskID or the File name that the exception is
    * associated with
    */
    public String getLocation () { return location; }
}
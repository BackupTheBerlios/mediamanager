package ch.fha.mediamanager.data;

/**
 * @author crac
 * @version $Id: NoSuchMetaFieldException.java,v 1.1 2004/06/26 12:19:07 crac Exp $
 */
public class NoSuchMetaFieldException extends RuntimeException {

    /**
     * 
     */
    public NoSuchMetaFieldException() {}
    
    /**
     * 
     * @param e
     */
    public NoSuchMetaFieldException(String e) {
        super(e);
    }
}

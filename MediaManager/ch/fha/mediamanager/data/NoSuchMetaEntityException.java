package ch.fha.mediamanager.data;

/**
 * 
 * @author crac
 * @version $Id: NoSuchMetaEntityException.java,v 1.1 2004/06/26 12:19:07 crac Exp $
 */
public class NoSuchMetaEntityException extends RuntimeException {
    
    /**
     * 
     */
    public NoSuchMetaEntityException() {}
    
    /**
     * 
     * @param e
     */
    public NoSuchMetaEntityException(String e) {
        super(e);
    }
}

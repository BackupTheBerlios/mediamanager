package ch.fha.mediamanager.data;

/**
 * 
 * @author crac
 * @version $Id: CurrentRepositoryConnectedException.java,v 1.1 2004/06/27 12:26:24 crac Exp $
 */
public class CurrentRepositoryConnectedException extends RuntimeException {

    /**
     * 
     */
    public CurrentRepositoryConnectedException() {}
    
    /**
     * 
     * @param e
     */
    public CurrentRepositoryConnectedException(String e) {
        super(e);
    }
}
package ch.fha.mediamanager.data;

/**
 *
 * @author crac
 * @version $Id: User.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */
public class User {
    
    private String uuId;
    private String name;
    
    public String getUUID() {
        return uuId;
    }

    public String getName() {
        return name;
    }

    public void setUUID(String uuId) {
        this.uuId = uuId;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package ch.fha.mediamanager.data;

/**
 * 
 * 
 * 
 * @author luca
 */
public class DataEntity {
    
}

/**
 *
 *
 * @author crac
 * @version $Id: DataEntity.java,v 1.1 2004/05/20 14:40:43 crac Exp $
 */ 
class DataField {

    private Object data;
    private String key;

    /**
     * 
     * 
     * @param key
     * @param data
     */
    public DataField(String key, Object data) {
       this.key = key;
       this.data = data;
    }
    
    /**
     * Performs the request.
     * 
     * @return DataSet
     */
    public DataSet run() {
        return null;
    }
    
    /**
     * 
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }
}

package ch.fha.mediamanager.data;

import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author crac
 * @version $Id: DataSet.java,v 1.3 2004/05/22 07:35:56 crac Exp $
 */
public class DataSet {
    private Set dSet = new HashSet();
    
    /**
     * 
     * @param e
     */
    public void add(DataElement e) {
        dSet.add(e);
    }
}

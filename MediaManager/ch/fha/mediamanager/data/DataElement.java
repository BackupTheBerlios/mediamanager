package ch.fha.mediamanager.data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author crac
 * @version $Id: DataElement.java,v 1.2 2004/05/22 07:35:56 crac Exp $
 */
public class DataElement {
    private DataEntity dEntity;
    private Set dFields = new HashSet();
    
    /**
     * 
     * @param df
     */
    public void add(DataField df) {
        dFields.add(df);
    }
}

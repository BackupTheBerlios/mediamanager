package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * 
 * 
 * @author crac
 * @version $Id: TestQuery.java,v 1.2 2004/05/29 18:10:41 crac Exp $
 */
public class TestQuery {
    
    public static void main(String[] args) {
        DataEntity de = new DataEntity("Movies");
        DataField df = new DataField("id", new Integer(0));
        QueryCondition qca = 
            new QueryCondition(
                de, 
                df, 
                QueryCondition.GREATER, 
                new Integer(1)
            );
        QueryCondition qcb = 
            new QueryCondition(
                de, 
                df, 
                QueryCondition.LESSER, 
                new Integer(1)
            );
        QueryCondition qcc = 
            new QueryCondition(
                de, 
                df, 
                QueryCondition.EQUALS, 
                new Integer(20)
            );
        Vector vec = new Vector();
        vec.add(qca);
        vec.add(new Integer(QueryRequest.AND));
        vec.add(qcb);
        vec.add(new Integer(QueryRequest.OR));
        vec.add(qcc);
        QueryRequest qr = new QueryRequest(vec, QueryRequest.LOAD);
        DataSet ds = qr.run();

    }
}

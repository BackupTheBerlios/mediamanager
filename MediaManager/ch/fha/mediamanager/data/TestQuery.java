package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * 
 * 
 * @author crac
 * @version $Id: TestQuery.java,v 1.3 2004/06/05 14:13:06 crac Exp $
 */
public class TestQuery {
    
    public static void main(String[] args) {
        MetaEntity ent = new MetaEntity("Movies");
        Field field = new Field("id", ent, new Integer(0));
        QueryCondition qca = 
            new QueryCondition(
                field, 
                QueryCondition.GREATER, 
                new Integer(1)
            );
        QueryCondition qcb = 
            new QueryCondition(
                field, 
                QueryCondition.LESSER, 
                new Integer(10)
            );
        QueryCondition qcc = 
            new QueryCondition(
                field, 
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
package ch.fha.mediamanager.data;

import java.util.Vector;

/**
 * Explains how to send a query to the repository.
 * 
 * @see Repository
 * @see QueryRequest
 * @see QueryCondition
 * @see MetaEntity
 * @see MetaField
 * @see Field
 * @see DataSet
 * 
 * @author crac
 * @version $Id: TestQuery.java,v 1.7 2004/06/21 21:42:17 crac Exp $
 */
public class TestQuery {
    
    public static void main(String[] args) {
        
        // NB: this will be called on the application startup.
        DataBus.initialize();
        DataBus.logger.info("App started.");
        DataBus.loadRepository();
        
        // insert example:
        /*MetaEntity ent = new MetaEntity("Test");
        DataElement el = DataBus.getDefaultElement(ent);
        el.setOwner(new User("A"));
        el.setField("Interpret", new String("Bobo"));
        el.setField("Stil", new String("Pop"));
        DataSet set = new DataSet();
        set.add(el);
        QueryRequest qr = new QueryRequest(set, QueryRequest.INSERT);
        DataSet ds = qr.run();*/
        
        // query example: return all with id > 0
        MetaEntity ent = new MetaEntity("Test");
        Field field = new Field("TestId", ent, new Integer(0));
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.GREATER, 
                new Integer(0)
            );
        Vector vec = new Vector();
        vec.add(qc);
        QueryRequest qr = new QueryRequest(vec, QueryRequest.LOAD);
        
        DataSet ds = qr.run();
        
        if (ds != null) {
            java.util.Iterator it = ds.iterator();
            while(it.hasNext()) {
                DataElement e = (DataElement) it.next();
                System.out.println(e.toString());
            }
        }
        
        /*MetaEntity ent = new MetaEntity("Movies");
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
        DataSet ds = qr.run();*/
        
        DataBus.logger.info("App stoped.");
    }
}
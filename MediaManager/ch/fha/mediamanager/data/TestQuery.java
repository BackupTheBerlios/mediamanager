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
 * @version $Id: TestQuery.java,v 1.10 2004/06/23 17:20:24 crac Exp $
 */
public class TestQuery {
    
    public static void main(String[] args) {
        
        // NB: this will be called on the application startup.
        DataBus.initialize();
        DataBus.logger.info("App started.");
        DataBus.loadRepository();
        
        DataSet ds = null;
        
        // delete example:
        /*MetaEntity ent = new MetaEntity("Test");
        Field field = new Field("TestId", ent, new Integer(0));
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.EQUALS, 
                new Integer(1)
            );
        Vector vec = new Vector();
        vec.add(qc);
        QueryRequest qr = new QueryRequest(vec, QueryRequest.LOAD);
        DataSet set = qr.run();
        
        if ((set != null) && (set.size() == 1)) {
            qr = new QueryRequest(set, QueryRequest.DELETE);
            ds = qr.run();
        }*/
        
        // update example:
        /*MetaEntity ent = new MetaEntity("Test");
        Field field = new Field("TestId", ent, new Integer(0));
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.EQUALS, 
                new Integer(1)
            );
        Vector vec = new Vector();
        vec.add(qc);
        QueryRequest qr = new QueryRequest(vec, QueryRequest.LOAD);
        DataSet set = qr.run();
        
        if ((set != null) && (set.size() == 1)) {
            java.util.Iterator it = set.iterator();
            DataElement e = (DataElement) it.next();
            set.remove(e);
            e.setField("Interpret", "DJ Bobo");
            set.add(e);
            qr = new QueryRequest(set, QueryRequest.UPDATE);
            ds = qr.run();
        }*/
        
        
        // insert example:
        /*MetaEntity ent = new MetaEntity("Test");
        DataElement el = DataBus.getDefaultElement(ent);
        el.setOwner(new User("A"));
        el.setField("Interpret", new String("Bobo"));
        el.setField("Stil", new String("Pop"));
        DataSet set = new DataSet();
        set.add(el);
        QueryRequest qr = new QueryRequest(set, QueryRequest.INSERT);
        ds = qr.run();*/
        
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
        ds = qr.run();
        
        if (ds != null) {
            System.out.println("QUERY RESULTS:");
            java.util.Iterator it = ds.iterator();
            while(it.hasNext()) {
                DataElement e = (DataElement) it.next();
                System.out.println(e.toString());
            }
        }
        
        // how to get a default DataElement
        DataElement e = DataBus.getDefaultElement("Test");
        Field[] flds = e.getFields();
        for (int i = 0; i < flds.length; i++) {
            System.out.println(flds[i].toString());
        }
        
        DataBus.logger.info("App stoped.");
    }
}
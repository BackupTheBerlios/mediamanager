package ch.fha.mediamanager.testing;

import java.util.Vector;

import ch.fha.mediamanager.data.AbstractQuery;
import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.DatabaseQuery;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.data.MetaEntity;
import ch.fha.mediamanager.data.MetaField;
import ch.fha.mediamanager.data.QueryCondition;
import ch.fha.mediamanager.data.AbstractRepository;

/**
 * Explains how to send a query to the repository.
 * 
 * @see AbstractRepository
 * @see DatabaseQuery
 * @see QueryCondition
 * @see MetaEntity
 * @see MetaField
 * @see Field
 * @see DataSet
 * 
 * @author crac
 * @version $Id: TestQuery.java,v 1.7 2004/06/29 14:48:19 crac Exp $
 */
public class TestQuery {
    
    public static void main(String[] args) {
        
        // NB: this will be called on the application startup.
        DataBus.initialize();
        DataBus.logger.info("App started.");
        DataBus.connect();
        
        DataSet ds = null;
        
        createCDDB();
        
        // create entity
        /*MetaEntity entity = new MetaEntity("Lied");
        MetaField f1 = new MetaField(MetaField.PK, "LiedId", entity);
        MetaField f2 = new MetaField(MetaField.INT, "Laenge", entity);
        MetaField f3 = new MetaField(MetaField.INT, "Jahr", entity);
        MetaField f4 = new MetaField(MetaField.VARCHAR, "Interpret", entity);
        MetaField[] fields = {f1,f2,f3,f4};
        
        AbstractQuery query = 
            DataBus.getQueryInstance(Query.ENTITY_FIELDS_CREATE);
        
        if (query.admin(entity, fields)) {
            System.out.println("Created, YEAH!");
        }*/
        
        // delete entity
        /*MetaEntity entity = 
            DataBus.getMetaData().getMetaEntity("Lied");
        AbstractQuery query = 
            DataBus.getQueryInstance(Query.ENTITY_DELETE);
        
        if (query.admin(entity)) {
            System.out.println("Deleted, YEAH!");
        }*/
        
        // delete example:
        /*MetaEntity ent = new MetaEntity("Test");
        Field field = DataBus.getPKField(ent);
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.EQUALS, 
                new Integer(1)
            );
        Vector vec = new Vector();
        vec.add(qc);
        AbstractQuery qr =
            DataBus.getQueryInstance(vec, AbstractQuery.LOAD);
        DataSet set = qr.run();
        
        if ((set != null) && (set.size() == 1)) {
            qr = DataBus.getQueryInstance(vec, AbstractQuery.DELETE);
            ds = qr.run();
        }*/
        
        // update example (1):
        /*Field f = DataBus.getPKField("Test");
        QueryCondition qc = 
            new QueryCondition(
                f, 
                QueryCondition.EQUALS, 
                new Integer(1)
            );
        Vector vec = new Vector();
        vec.add(qc);
        AbstractQuery qr =
            DataBus.getQueryInstance(vec, AbstractQuery.LOAD);
        DataSet set = qr.run();
        
        if ((set != null) && (set.size() == 1)) {
            java.util.Iterator it = set.iterator();
            DataElement e = (DataElement) it.next();
            set.remove(e);
            e.setField("Interpret", "DJ Bobo");
            set.add(e);
            qr = DataBus.getQueryInstance(set, AbstractQuery.UPDATE);
            ds = qr.run();
        }*/
        
        // update example (2):
        /*Field field = DataBus.getPKField("Test");
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.EQUALS, 
                new Integer(1)
            );
        Vector vec = new Vector();
        vec.add(qc);
        AbstractQuery qr =
            DataBus.getQueryInstance(vec, AbstractQuery.LOAD);
        DataSet set = qr.run();
        
        if ((set != null) && (set.size() == 1)) {
            java.util.Iterator it = set.iterator();
            DataElement e = (DataElement) it.next();
            set.remove(e);
            e.setField("Interpret", "DJ Bobo");
            set.add(e);
            qr = DataBus.getQueryInstance(set, AbstractQuery.UPDATE);
            ds = qr.run();
        }*/
        
        // insert example (1):
        /*DataElement el = DataBus.getDefaultElement("Lied");
        el.setField("Interpret", new String("DJ Bobo"));
        DataSet set = new DataSet();
        set.add(el);
        AbstractQuery qr = 
            DataBus.getQueryInstance(set, AbstractQuery.INSERT);
        ds = qr.run();*/
        
        // insert example (2):
        /*MetaEntity ent = new MetaEntity("Test");
        DataElement el = DataBus.getDefaultElement(ent);
        el.setField("Interpret", new String("Bobo"));
        el.setField("Stil", new String("Pop"));
        DataSet set = new DataSet();
        set.add(el);
        AbstractQuery qr = 
            DataBus.getQueryInstance(set, AbstractQuery.INSERT);
        ds = qr.run();*/
        
        // query example: return all with PK > 0
        /*MetaEntity ent = new MetaEntity("Test");
        Field field = DataBus.getPKField(ent);
        QueryCondition qc = 
            new QueryCondition(
                field, 
                QueryCondition.GREATER, 
                new Integer(0)
            );
        Vector vec = new Vector();
        vec.add(qc);
        AbstractQuery qr = 
            DataBus.getQueryInstance(vec, AbstractQuery.LOAD);
        ds = qr.run();
        
        if (ds != null) {
            System.out.println("QUERY RESULTS:");
            java.util.Iterator it = ds.iterator();
            while(it.hasNext()) {
                DataElement e = (DataElement) it.next();
                System.out.println(e.toString());
            }
        }*/
        
        // how to get a default DataElement
        DataElement e = DataBus.getDefaultElement("Lied");
        Field[] flds = e.getFields();
        for (int i = 0; i < flds.length; i++) {
            System.out.println(flds[i].toString());
        }
        
        DataBus.logger.info("App stoped.");
    }
    
    private static void createCDDB() {
        
        MetaEntity cdEnt = new MetaEntity("CDs");
        MetaField fa = new MetaField(MetaField.PK, "CDId", cdEnt);
        MetaField fb = new MetaField(MetaField.VARCHAR, "Interpret", cdEnt);
        MetaField fc = new MetaField(MetaField.VARCHAR, "Kategorie", cdEnt);
        MetaField[] flds = {fa,fb,fc};
        
        AbstractQuery qry = 
            DataBus.getQueryInstance(AbstractQuery.ENTITY_FIELDS_CREATE);
        
        if (qry.admin(cdEnt, flds)) {
            System.out.println("CD Entity Created, YEAH!");
            
            MetaEntity songEnt = new MetaEntity("Songs");
            MetaField f1 = new MetaField(MetaField.PK, "SongId", songEnt);
            MetaField f2 = new MetaField(MetaField.VARCHAR, "CDName", songEnt);
            MetaField f3 = new MetaField(MetaField.INT, "TrackNr", songEnt);
            MetaField f4 = new MetaField(MetaField.VARCHAR, "Titel", songEnt);
            MetaField f5 = new MetaField(MetaField.INT, "Laenge", songEnt);
            MetaField f6 = new MetaField(MetaField.TEXT, "Beschreibung", songEnt);
            MetaField f7 = new MetaField(MetaField.VARCHAR, "Komposition", songEnt);
            MetaField[] fields = {f1,f2,f3,f4,f5,f6,f7};
            
            AbstractQuery query = 
                DataBus.getQueryInstance(AbstractQuery.ENTITY_FIELDS_CREATE);
            
            if (query.admin(songEnt, fields)) {
                System.out.println("Songs Entity Created, YEAH!");
            }
        }
    }
}
package plugins.cddb;

import java.io.Serializable;
import java.util.Properties;


/**
* Represents the additional information for a CD
* that can be retrieved from a CDDB database.
* Any further information that may be available for
* a specific CDInfo object, is left to whatever the
* implementing class supports.
* In fact, there may be instances that are derived
* from a special local database, that support far more
* features/information than the standard CDDB file format
* provides. Also, the organization/storage of these instances
* should be entirely up to the application, that's why this
* is an interface.
* @author Holger Antelmann
* @see CDDBXmcdParser
*/
public interface CDInfo extends Serializable
{
    /** returns the associated CDID object */
    CDID getCDID ();

    /**
    * returns the associated CDDBRecord entry.
    * Note: as the returned CDDBRecord is not derived directly from a CDDB
    * query, the <code>isExactMatch()</code> value of this returned instance
    * is meaningless. This record (with the embedded category and one discID)
    * allows to identify the CDInfo object as to how it is to be filed.
    * @see CDDB#queryCD(CDID)
    */
    CDDBRecord getCDDBRecord ();

    /**
    * returns the content in xmcd file format as specified by CDDB.
    * As specified in xmcd CDDB format, there may be multiple discIDs
    * present, and there is no category information. To retrieve that info,
    * use the associated CDDBRecord.
    * @see #getCDDBRecord()
    */
    public String getXmcdContent ();

    /**
    * a convenience method that returns a map view of all known properties
    * of this entry. Unless otherwise specified by the implementing class,
    * changes in the returned properties are not reflected in the actual
    * properties of this instance.
    * (I.e. it is asumed that the return value is a generated view per call.)
    */
    Properties getProperties ();
}

package plugins.cddb;

import java.io.IOException;

/**
* The interface CDDB specifies the methods that a CD database
* needs to provide to serve as a database in this framework. <p>
* These methods include ways to query, retrieve and store information
* for a CD. <p>
* Obviously, any implementations - particularly local database applications
* - may provide many more features, like sorted inventory listings,
* flexible queries, etc. This interface just provides the bare
* necessities to serve as a CDDB. <br>
* Even implementations of remote public CDDB services can provide additional
* information, such as a listing of their mirror sites or the alike
* (see FreeDB for a reference implementation).
* <p>
* In regards to exception handling, you may want to keep in mind that
* many of the implementing classes may actually throw a CDDBProtocolException
* rather than an IOException (which is its super class), which may provide
* you with additional information about what went wrong.
* @author Holger Antelmann
* @see FreeDB
* @see CDID
* @see CDDBRecord
* @see CDInfo
* @see CDDBProtocolException
*/
public interface CDDB
{
    /**
    * returns the available categories under which the CDInfo entries
    * are stored - in accordance to the CDDB standard
    */
    String[] getCategories () throws IOException;

    /**
    * queries the database and returns an array of matches. These
    * matches can be exact matches as well as inexact matches,
    * which can be determined by calling <code>isExactMatch()</code>
    * on any returned element.
    * @see CDDBRecord#isExactMatch()
    */
    CDDBRecord[] queryCD (CDID cd) throws IOException;

    /**
    * reads the information for the specified record from the database
    * and returns it in form of a CDInfo object. The record should be
    * derived from a previous query.
    * @see #queryCD(CDID)
    */
    CDInfo readCDInfo (CDDBRecord record) throws IOException;

    /**
    * allows to submit CD information to the database - if the
    * operation is permitted by the database
    */
    void writeCDInfo (CDInfo cdinfo) throws IOException, OperationNotAllowed;
}
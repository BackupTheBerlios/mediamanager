package plugins.cddb;

import java.io.Serializable;

/**
* Represents a unique entry in a CDDB database. <p>
* This CDDBRecord object can be used to read the content
* from a CDDB in form of a CDInfo object.
* @author Holger Antelmann
* @see CDDB
* @see CDInfo
*/
public class CDDBRecord implements Serializable
{
    String discid, category, title;
    boolean exactMatch;

    /**
    * produces an exact match record with the given parameters;
    * the order of the parameters is according to the output of the CDDB  protocol.
    */
    public CDDBRecord (String category, String discid, String title) {
        this(category, discid, title, true);
    }


    /**
    * initializes a record that can be used to query CDDB for this entry.
    * The exactMatch property is only relevant when the instance is created
    * through a CDDB query.
    * @see CDDB#queryCD(CDID)
    */
    public CDDBRecord (String category, String discid, String title, boolean exactMatch) {
        this.discid   = discid;
        this.category = category;
        this.title    = title;
        this.exactMatch = exactMatch;
    }


    /** returns the title of this record */
    public String getTitle () { return title; }


    /** returns the category under which this record is filed */
    public String getCategory () { return category; }


    /**
    * returns the DiscID that can be used to match the record with a
    * CDID object
    * @see CDID#getDiscID()
    */
    public String getDiscID () { return discid; }


    /**
    * relevant only when derived from a CDDB query: returns whether the
    * CDDBRecord was the result of an exact match of the CD used for the query.
    * @see CDDB#queryCD(CDID)
    */
    public boolean isExactMatch () { return exactMatch; }


    /** hashes the discID */
    public int hashCode () {
        return discid.hashCode();
    }


    public boolean equals (Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof CDDBRecord))     return false;
        CDDBRecord other = (CDDBRecord) obj;
        if (!other.category.equals(category)) return false;
        if (!other.discid.equals(discid))     return false;
        if (!other.title.equals(title))       return false;
        if (other.exactMatch != exactMatch)   return false;
        return true;
    }


    public String toString () {
        String s = super.toString() + " - ";
        s += "Category: " + category;
        s += ", DiscID: " + discid;
        s += ", Title: " + title;
        s += ", " + (exactMatch? "exact match" : "inexact match");
        return s;
    }
}

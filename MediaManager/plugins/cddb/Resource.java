package plugins.cddb;

import java.io.Serializable;


/**
* Resource is the abstract notion of 'something' or 'someone' that can
* contribute to a composition via a given role.
* A resource could be an artist, an orchestra, a location, an instrument
* or anything else you may think of.
* @author Holger Antelmann
* @see Role
* @see Composition
*/
public class Resource implements Comparable, Serializable
{
    String name;
    String description;


    /** Resource objects are to be uniquely identifiable by their name */
    public Resource (String name, String description) {
        if (name == null) throw new NullPointerException();
        this.name        = name;
        this.description = description;
    }


    public String getName () { return name; }


    public String getDescription () { return description; }


    public void setDescription (String description) { this.description = description; }


    public int hashCode () { return name.hashCode(); }


    /** compares by the name */
    public int compareTo (Object o) {
        return name.compareTo(((Resource)o).name);
    }


    /**
    * checks the name to be equal
    * (after checking being an instance of Resource)
    */
    public boolean equals (Object obj) {
        if (!(obj instanceof Resource)) return false;
        return (name.equals(((Resource)obj).name));
    }


    public String toString () { return name; }
}

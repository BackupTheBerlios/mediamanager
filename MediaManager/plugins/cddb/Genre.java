package plugins.cddb;

import java.io.Serializable;


/**
* A Genre specifies the category for a composition.
* @author Holger Antelmann
* @see Contribution
* @see Track
* @see Composition
* @see Resource
*/
public class Genre implements Serializable
{
    public static final Genre BLUES      = new Genre("blues");
    public static final Genre CLASSICAL  = new Genre("classical");
    public static final Genre COUNTRY    = new Genre("country");
    public static final Genre DATA       = new Genre("data");
    public static final Genre FOLK       = new Genre("folk");
    public static final Genre JAZZ       = new Genre("jazz");
    public static final Genre MISC       = new Genre("misc");
    public static final Genre NEWAGE     = new Genre("newage");
    public static final Genre REGGAE     = new Genre("reggae");
    public static final Genre ROCK       = new Genre("rock");
    public static final Genre SOUNDTRACK = new Genre("soundtrack");
    public static final Genre UNKNOWN    = new Genre("unknown");
    private String description;


    protected Genre (String description) {
        if (description == null) throw new NullPointerException();
        this.description = description;
    }


    /**
    * convenience method that checks whether the given description
    * matches one of the standard categories (using case-insensitive
    * comparison), in which case the static member is returned;
    * otherwise, a new object will be created and returned
    */
    public static Genre getGenre (String description) {
        description = description.trim().toLowerCase();
        if (description.equals("blues"))      return BLUES;
        if (description.equals("classical"))  return CLASSICAL;
        if (description.equals("country"))    return COUNTRY;
        if (description.equals("data"))       return DATA;
        if (description.equals("folk"))       return FOLK;
        if (description.equals("jazz"))       return JAZZ;
        if (description.equals("misc"))       return MISC;
        if (description.equals("newage"))     return NEWAGE;
        if (description.equals("reggae"))     return REGGAE;
        if (description.equals("rock"))       return ROCK;
        if (description.equals("soundtrack")) return SOUNDTRACK;
        if (description.equals("unknown"))    return UNKNOWN;
        return new Genre(description);
    }


    public int hashCode () { return description.hashCode(); }


    public boolean equals (Object obj) {
        return (description.equals(((Genre)obj).description))? true : false;
    }


    public String toString () { return description; }
}

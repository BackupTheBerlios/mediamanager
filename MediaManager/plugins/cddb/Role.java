package plugins.cddb;

import java.io.Serializable;


/**
* A Role defines the relationship of what a resource contributes
* to a composition or track.
* @author Holger Antelmann
* @see Contribution
* @see Track
* @see Composition
* @see Resource
*/
public class Role implements Serializable
{
    public static final Role ARTIST     = new Role("Artist");
    public static final Role BAND       = new Role("Band");
    public static final Role COMPOSER   = new Role("Composer");
    public static final Role CONDUCTOR  = new Role("Conductor");
    public static final Role INSTRUMENT = new Role("Instrument");
    public static final Role LOCATION   = new Role("Location");
    public static final Role MUSICIAN   = new Role("Musician");
    public static final Role ORCHESTRA  = new Role("Orchestra");
    public static final Role OTHER      = new Role("Other");
    public static final Role PERFORMER  = new Role("Performer");
    public static final Role SINGER     = new Role("Singer");
    public static final Role SOLIST     = new Role("Solist");
    private String description;


    /**
    * This protected constructor allows for further customized roles
    * through subclassing; otherwise it is prefered to stick to the
    * pre-defined ones.
    */
    protected Role (String description) {
        if (description == null) throw new NullPointerException();
        this.description = description;
    }


    public String getDescription () {
        return description;
    }


    public int hashCode () { return description.hashCode(); }


    public boolean equals (Object obj) {
        return (description.equals(((Role)obj).description))? true : false;
    }


    /** returns getDescription() */
    public String toString () { return getDescription(); }
}

package plugins.cddb;


/**
* Artist is an individual contributor to a composition and
* offers additional attributes applicable to a person.
* The Artist class offers some additional attributes over the
* standard resource and allows customized ordering.
* @author Holger Antelmann
* @see Role
* @see Composition
* @see Group
*/
public class Artist extends Resource
{
    public static final int UNKNOWN = 0;
    public static final int MALE    = 1;
    public static final int FEMALE  = 2;

    int gender = 0;
    String sortName;


    public Artist (String name, String description) {
        super(name, description);
    }


    /**
    * @see #setSortName(String)
    */
    public String getSortName () { return sortName; }


    /**
    * the sortName can be used to provide an ordering different from
    * the name of the artist. E.g. if the name of the artist were
    * "John Williams" and you wanted him to be filed under
    * "Williams, John", you can use the latter as the sortName.
    */
    public void setSortName (String sortName) {
        this.sortName = sortName;
    }



    /**
    * uses sortName if not null and name otherwise; if the
    * object is equal, however, 0 is returned to provide consistency
    * @see #setSortName(String)
    */
    public int compareTo (Object o) {
        if (equals(o)) return 0;
        Artist a = (Artist) o;
        return ((sortName == null)? name : sortName).compareTo(
            ((a.sortName == null)? a.name : a.sortName));
    }


    /** can be either MALE, FEMALE or UNKNOWN */
    public int getGender () { return gender; }


    /** can be either MALE, FEMALE or UNKNOWN */
    public void setGender (int gender) {
        if ((gender != MALE) && (gender != FEMALE) && (gender != UNKNOWN))
            throw new IllegalArgumentException("gender unknown");
        this.gender = gender;
    }
}

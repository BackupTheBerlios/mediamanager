package plugins.cddb;

import java.io.Serializable;


/**
* Contribution represents the association between a resource and a Composition
* or a Track,
* where the resource contributes different roles to the composition.
* @author Holger Antelmann
* @see Role
* @see Composition
* @see Track
* @see Resource
*/
public class Contribution implements Serializable, Comparable
{
    Resource    resource;
    Role        role;
    int         importance;
    String      comment;


    public Contribution (Resource resource, Role role, int importance, String comment)
    {
        if ((resource == null) || (role == null))
            throw new IllegalArgumentException("artist cannot be null");
        this.resource    = resource;
        this.role        = role;
        this.importance  = importance;
        this.comment     = comment;
    }


    public Resource getResource () { return resource; }


    public Role getRole () { return role; }


    public int getImportance () { return importance; }


    public void setImportance (int n) { importance = n; }


    public String getComment () { return comment; }


    public void setComment (String comment) { this.comment = comment; }


    public int hashCode () {
        return (resource.hashCode() * role.hashCode());
    }


    /**
    * compares objects by their importance
    * (returns 0 if objects are equal for consitency, though)
    */
    public int compareTo (Object o) {
        if (equals(o)) return 0;
        return (importance - ((Contribution)o).importance);
    }


    /** object is equal if composition, resouce and role are equal */
    public boolean equals (Object obj) {
        if (!(obj instanceof Contribution)) return false;
        Contribution con = (Contribution) obj;
        if (!role.equals(con.role))         return false;
        if (!resource.equals(con.resource)) return false;
        return true;
    }
}

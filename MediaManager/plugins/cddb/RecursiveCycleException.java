package plugins.cddb;


/**
* Thrown if a Composition object tries to use a parent object
* that already has itself as a parent, or if a Group object
* tries to insert a group that is already contained,
* which constitues an illegal recursive cycle.
* @author Holger Antelmann
* @see Composition
* @see Group
*/
public class RecursiveCycleException extends RuntimeException
{
    Object o;


    public RecursiveCycleException(Composition c) {
        super();
        o = c;
    }


    public RecursiveCycleException(Group g) {
        super();
        o = g;
    }


    /** returns either a Composition or a Group */
    public Object getObject () { return o; }
}
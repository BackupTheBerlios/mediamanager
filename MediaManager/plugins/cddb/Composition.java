package plugins.cddb;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
* Composition represents a musical opus that can span anything from
* a single track on a CD to various tracks over multiple CDs recursively
* including other sub-compositions. <p>
* A composition is an abstraction that allows to aggregate
* Track objects or even multiple compositions to a single composition,
* so that they can share the same contributions (same artists, same
* recording, etc.) that serve as attributes for a track.
* Contributions added to a composition may 'overwrite' a similar contribution
* that is already associated with its parent composition.
* Compositions then consist of Track objects (the leaf elements of any
* composition).
* <p>
* Through the recursive abstraction of Compositions independent from
* the concept of CDs, one can eliminate most redundancy of defining
* attributes (in form of Contribution objects) on various levels that
* then apply to a single track by association.
* <p>
* While a Track can inherit attributes (Contribution objects) from many
* Compositions, a Composition can only inherit from a single parent,
* to allow for a clean tree structure. However, Composition objects may
* share the same Contribution objects with other compositions, even though
* they may have no other association.
* <p>
* Examples for a composition would be a specific recording of a
* specific symphony (sharing composer, conductor, orchestra, recording
* location etc., but put on the same CD with another recording), a
* single song on a CD with a collection of various songs, a movie
* soundtrack (that could span many CDs), a standard pop album by an artist,
* etc.
* <p>
* The title of the composition is used as a unique identifier, i.e. if you
* have similar compositions (e.g. multiple recordings of a symphony by
* the same orchestra/conductor that only differ in recording year), you need
* to find some way to distinguish the compositions in the title.
* <p>
* Any operation on the embedded Set objects must be synchronized externally;
* note that <code>setParent(Composition)</code> also accesses embedded Sets
* from all parents.
* <p>
* This class requires the Externalizable interface to not throw a
* NullPointerException when deserializing the HashSet from a Track object
* containing Composition objects that require already serialized fields when
* accessing the <code>hashCode()</code> method to populate the HashSet in Track.
* This is the only way to properly ensure that the title is deserialized before
*
* @author Holger Antelmann
* @see Contribution
* @see Track
*/
public class Composition implements Serializable, Comparable, Externalizable
{
    String      title;
    Genre       genre;
    Date        recordingDate;
    String      description;
    Composition parent;
    HashSet     children      = new HashSet();
    HashSet     tracks        = new HashSet();
    HashSet     contributions = new HashSet();


    /**
    * only to be used by the Externalizable interface; constructor is not
    * usable for other purposes
    */
    public Composition () {
        children = null;
        tracks = null;
        contributions = null;
    }


    /** required to support proper serialization of the bi-directional relationship */
    public void writeExternal (ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeObject(genre);
        out.writeObject(recordingDate);
        out.writeObject(description);
        out.writeObject(parent);
        out.writeObject(children);
        out.writeObject(tracks);
        out.writeObject(contributions);
    }


    /** required to support proper de-serialization of the bi-directional relationship */
    public void readExternal (ObjectInput in) throws IOException, ClassNotFoundException {
        title         = (String)      in.readObject();
        genre         = (Genre)       in.readObject();
        recordingDate = (Date)        in.readObject();
        description   = (String)      in.readObject();
        parent        = (Composition) in.readObject();
        children      = (HashSet)     in.readObject();
        tracks        = (HashSet)     in.readObject();
        contributions = (HashSet)     in.readObject();
    }


    /**
    * @param title must be a non-null unique identifier for this composition
    */
    public Composition (String title, Genre genre, String description, Composition parent) {
        if (title == null) throw new NullPointerException();
        this.title       = title;
        this.genre       = genre;
        this.description = description;
        setParent(parent);
    }


    /**
    * returns the parent composition from which additional contributions
    * are inherited
    * @see #getChildren()
    */
    public Composition getParent () { return parent; }


    /**
    * sets the parent Composition object from which this composition then
    * inherits further contributions
    * @throws RecursiveCycleException if the given parent equals this
    *                                 composition or has a parent that equals
    *                                 this
    */
    public void setParent (Composition newParent) throws RecursiveCycleException {
        if (newParent != null) {
            if (equals(newParent)) throw new RecursiveCycleException(this);
            newParent.checkParent(this);
            newParent.children.add(this);
        }
        if (parent != null) parent.children.remove(this);
        parent = newParent;
    }


    void checkParent (Composition c) {
        if (parent != null) {
            if (parent.equals(c)) throw new RecursiveCycleException(c);
            parent.checkParent(c);
        }
    }


    /**
    * returns an unmodifiable view of the children compositions that
    * add both additional tracks and sub-compositions to this object.
    * To change the tree structure, use <code>setParent(Composition)</code>
    * #see setParent(Composition)
    */
    public Set getChildren () {
        return Collections.unmodifiableSet(children);
    }


    /**
    * returns the title that also serves as the unique identifier
    * for this composition
    */
    public String getTitle () { return title; }


    public String getDescription () { return description; }


    public void setDescription (String description) {
        this.description = description;
    }


    public Genre getGenre () { return genre; }


    public void setGenre (Genre genre) {
        this.genre = genre;
    }


    public Date getRecordingDate () { return recordingDate; }


    public void setRecordingDate (Date date) { recordingDate = date; }


    /**
    * convenience method that will set the recording date to
    * January 1st of the given year according to the Gregorian calendar
    */
    public void setRecordingYear (int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, cal.JANUARY, 1);
        setRecordingDate(cal.getTime());
    }


    /**
    * convenience method that will calculate the recording year based
    * on the Gregorian calendar; if no date is set, 0 is returned
    */
    public int getRecordingYear () {
        if (recordingDate == null) return 0;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(recordingDate);
        return cal.get(cal.YEAR);
    }


    public boolean contains (Contribution con) {
        return contributions.contains(con);
    }


    public boolean add (Contribution con) {
        return contributions.add(con);
    }


    public boolean remove (Contribution con) {
        return contributions.remove(con);
    }


    /**
    * returns an unmodifiable view of only the directly added
    * Contribution objects
    */
    public Set getCompositionContributions () {
        return Collections.unmodifiableSet(contributions);
    }


    /**
    * generates an unmodifiable view of all associated contributions
    * (including contributions from parent Compositions);
    * this view will not reflect subsequent changes to the actual embedded sets
    * @return Set of Contribution objects
    */
    public Set getAllContributions () {
        HashSet all = new HashSet(contributions);
        if (parent != null) all.addAll(parent.getAllContributions());
        return Collections.unmodifiableSet(all);
    }


    /** only checks for directly added tracks */
    public boolean contains (Track t) {
        return tracks.contains(t);
    }


    public boolean add (Track t) {
        t.compositions.add(this);
        return tracks.add(t);
    }


    public boolean remove (Track t) {
        t.compositions.remove(this);
        return tracks.remove(t);
    }


    /**
    * provides an unmodifiable view only of the Track objects
    * directly added to this composition
    * @return Set of Track objects
    */
    public Set getCompositionTracks () {
        return Collections.unmodifiableSet(tracks);
    }


    /**
    * generates an unmodifiable view of all Track objects, including those
    * from children Composition objects;
    * this view will not reflect subsequent changes to the actual embedded sets
    * @return Set of Track objects
    */
    public Set getAllTracks () {
        HashSet all = new HashSet(tracks);
        Iterator i = children.iterator();
        while (i.hasNext()) {
            all.addAll(((Composition)i.next()).getAllTracks());
        }
        return Collections.unmodifiableSet(all);
    }


    public int hashCode () { return title.hashCode(); }


    /** compares by the title for convenient sorting */
    public int compareTo (Object o) {
        return title.compareTo(((Composition)o).title);
    }


    /** checks only for the title that is to be unique */
    public boolean equals (Object obj) {
        if (!(obj instanceof Composition)) return false;
        return (title.equals(((Composition)obj).title))? true : false;
    }


    /** returns getTitle() */
    public String toString () { return title; }
}
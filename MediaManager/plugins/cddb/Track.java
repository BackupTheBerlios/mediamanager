package plugins.cddb;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
* Track objects represent the leaf elements of Composition objects
* defined as a specific track on a particular music CD.
* <p>
* A Track object may be contained by several Composition objects, while
* the association is maintained soley from the Composition object.
* Contributions added to a track may 'overwrite' an equal contribution
* that is already associated with this track through its membership in
* a composition.
* <p>
* Any operation on the embedded Set objects must be synchronized externally.
*
* @author Holger Antelmann
* @see Composition
* @see Contribution
* @see CDID
*/
public class Track implements Serializable
{
    CDID    cd;
    int     trackNo;
    String  title;
    String  description;
    HashSet contributions = new HashSet();
    /** accessed by Composition */
    HashSet compositions  = new HashSet();


    public Track (CDID cd, int trackNo, String title, String description) {
        if ((trackNo < 0) || (trackNo >= cd.getNumberOfTracks())) {
            throw new IllegalArgumentException("track cannot belong to CD");
        }
        this.cd          = cd;
        this.trackNo     = trackNo;
        this.title       = title;
        this.description = description;
    }

		public String[] getTrackArray() {
			String artists = "";
			Iterator it = getTrackContributions().iterator();
			while (it.hasNext()) {
				artists += ((Contribution)it.next()).getResource().getName() + " ";
			}
			String[] ret = {
				new Integer(trackNo).toString(),
				title,
				description,
				new Integer(getLength()).toString(),
				artists
			};
			return ret;
		}

    public CDID getCD () { return cd; }


    public int getTrackNo () { return trackNo; }


    /**
    * returns the length in seconds
    * @see CDID#getTrackLength(int)
    */
    public int getLength () {
        return cd.getTrackLength(trackNo);
    }


    public String getTitle () { return title; }


    public void setTitle (String title) { this.title = title; }


    public String getDescription () { return description; }


    public void setDescription (String description) {
        this.description = description;
    }


    public boolean contains (Contribution con) {
        return contributions.contains(con);
    }


    /** adds a contribution specific to this single track */
    public boolean add (Contribution con) {
        return contributions.add(con);
    }


    public boolean remove (Contribution con) {
        return contributions.remove(con);
    }


    /**
    * provides an unmodifiable view of contributions
    * that were directly added to this track
    * @return Set of Contribution objects
    */
    public Set getTrackContributions () {
        return Collections.unmodifiableSet(contributions);
    }


    /**
    * generates an unmodifiable view of all associated contributions
    * (including contributions from parent Compositions);
    * this view will not reflect subsequent changes to the actual embedded sets
    * @return Set of Contribution objects
    * @see #getCompositions()
    */
    public Set getAllContributions () {
        HashSet all = new HashSet(contributions);
        Iterator i = compositions.iterator();
        while (i.hasNext()) {
            all.addAll(((Composition)i.next()).getAllContributions());
        }
        return Collections.unmodifiableSet(all);
    }


    /**
    * provides an unmodifiable view of the Composition objects that this
    * track is associated with
    * @see Composition#add(Track)
    * @see Composition#remove(Track)
    */
    public Set getCompositions () {
        return Collections.unmodifiableSet(compositions);
    }


    public int hashCode () {
        return ((cd.hashCode() * 100) + trackNo);
    }


    /** a track is equal if cd and trackNo are equal */
    public boolean equals (Object obj) {
        if (!(obj instanceof Track)) return false;
        Track t = (Track) obj;
        if (!cd.equals(t.cd))        return false;
        if (trackNo != t.trackNo)    return false;
        return true;
    }
}

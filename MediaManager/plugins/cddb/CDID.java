package plugins.cddb;

import java.io.Serializable;
import java.util.StringTokenizer;


/**
* CDID represents a unique music CD with the characteristics
* usually provided by a CDDrive.
* This entry can be used to query additional information
* for the CD from services like CDDB
* @author Holger Antelmann
* @see CDDrive
* @see CDDB
*/
public class CDID implements Serializable
{
    static final int FRAMES_PER_SECOND = 75;
    String id;
    int[]  offset;
    int    length;


    /** constructs the CDID manually (doesn't perform a consistency check) */
    public CDID (String id, int[] offset, int length) {
        this.id     = id;
        this.offset = offset;
        this.length = length;
    }


    /** reads the CD information directly from the drive (preferred construction method) */
    public CDID (CDDrive cddrive) throws IllegalArgumentException {
        this(cddrive.getQueryString());
    }


    /**
    * parses the CDID information from the given CDDB query string. <p>
    * This constructor is useful for importing CDIDs from a file or the alike;
    * it does not check the validity/consistency of the string, though.
    * @param queryString a string in the exact format specified by the
    *        CDDB protocol for querying a CD.
    * @throws NumberFormatException          if the string cannot be parsed properly
    * @throws ArrayIndexOutOfBoundsException if the string cannot be parsed properly
    * @see CDDrive#getQueryString()
    */
    public CDID (String queryString) throws
        NumberFormatException, ArrayIndexOutOfBoundsException
    {
        StringTokenizer st = new StringTokenizer(queryString, " ");
        id = st.nextToken();
        offset = new int[Integer.parseInt(st.nextToken())];
        for (int i = 0; i < offset.length; i++) {
            offset[i] = Integer.parseInt(st.nextToken());
        }
        length = Integer.parseInt(st.nextToken());
    }


    /**
    * The returned discid can be used to match this CD to
    * a CDDBRecord.
    * @see CDDBRecord#getDiscID()
    */
    public String getDiscID () { return id; }


    /** returns the duration of the CD in seconds */
    public int getLength () { return length; }


    /**
    * returns the offset of the frame on the CD where the given track starts.
    * Note: track is to be specified from 0 to <code>getNumberOfTracks()-1</code>.
    */
    public int getFrameOffset (int track) { return offset[track]; }


    /** this is equivalent to the number of frame offsets */
    public int getNumberOfTracks () { return offset.length; }


    /**
    * returns the duration of the track in seconds;
    * trackNo specifies track 0 to (getNumberOfTracks() - 1).
    * The last track is currently over-calculated;
    * this over-estimate for the last track worsens if there
    * is a data track at the end.
    */
    public int getTrackLength (int trackNo) {
        if ((trackNo < 0) || (trackNo >= offset.length )) return -1;
        if (trackNo == (offset.length - 1)) {
            // this is overcalculating the actual length
            return (length - (offset[trackNo] / FRAMES_PER_SECOND));
        } else {
            return ((offset[trackNo + 1] - offset[trackNo]) / FRAMES_PER_SECOND);
        }
    }


    /** returns the query string in CDDB format */
    public String getQueryString () {
        String s = id + " " + offset.length + " ";
        for (int i = 0; i < offset.length; i++) {
            s += offset[i] + " ";
        }
        s += length;
        return s;
    }


    /** hashes the discID */
    public int hashCode () {
        return id.hashCode();
    }


    /** compares the query string for identity */
    public boolean equals (Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof CDID)) return false;
        if (((CDID)obj).getQueryString().equals(getQueryString())) return true;
        return false;
    }


    public String toString () {
        return "CDID " + id;
    }
}
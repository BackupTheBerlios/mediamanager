package plugins.cddb;

import java.util.StringTokenizer;


/**
* The class CDDrive represents the physical CD-ROM drive in a platform
* independently.
* The drive can be queried on certain properties that are available
* from the device. In particular, it can generate a CDID object.
* <p>
* This class has one abstract method that needs to be implemented
* in a platform dependant manner (most probably with a native method)
* by classes that implement an actual drive. The method is: <br>
* <code>String getCDDBQuery()</code> <p>
* This method returns a string that contains all information about
* the inserted CD in a way that is conform with a query according
* to the CDDB protocol. An example return value would be:
* <dfn>"cddb query a10c9b0c 12 150 15767 32142 52485 79740 102907 121765 141400 161912 184252 204920 238702 3229"</dfn>
* @author Holger Antelmann
* @see CDID
*/
public abstract class CDDrive
{
    final int FRAMES_PER_SECOND = 75;
    String cdString = null;
    String discID   = null;
    int[] track     = null;
    int   length    = -1;
    boolean firstAccess = true;


    CDDrive () {
    }


    public abstract void eject ();


    public abstract void play ();


    public abstract void stop ();


    public abstract void backward ();


    public abstract void forward ();


    public abstract void pause ();


    void checkFirst () throws NoCDInDriveException {
        if (firstAccess) {
            firstAccess = false;
            refresh();
        }
        if (cdString == null) throw new NoCDInDriveException();
    }

    /**
    * This method is a platform dependant call that queries
    * available CD-ROM drives to get the query string
    * processed for further analysis.
    */
    abstract String getCDDBQuery ();


    /** also performs a refresh() */
    public boolean isCDAvailable () {
        refresh();
        return (cdString != null);
    }


    /** (re-)queries the CD-ROM drive */
    public void refresh () {
        cdString = getCDDBQuery();
        if (cdString != null) {
            try {
                StringTokenizer st = new StringTokenizer(cdString, " ");
                st.nextToken(); // "cddb"
                st.nextToken(); // "query"
                discID = st.nextToken();
                track = new int[Integer.parseInt(st.nextToken())];
                int offset     = Integer.parseInt(st.nextToken()); // offset first track
                int lastOffset = offset;
                int sum = 0;
                for (int i = 0; i < track.length - 1; i++) {
                    offset = Integer.parseInt(st.nextToken()); // offset of next track
                    track[i] = (offset - lastOffset) / FRAMES_PER_SECOND;
                    sum += track[i];
                    lastOffset = offset;
                }
                // last track plays to the end, so the calculation is different
                length = Integer.parseInt(st.nextToken());
                track[track.length - 1] = length - sum;
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cdString = null;
        discID   = null;
        track    = null;
        length   = -1;
    }


    public CDID getCDID () throws NoCDInDriveException{
        checkFirst();
        return new CDID(this);
    }


    /**
    * returns the exact query string as specified by the CDDB query format.
    * This format is as follows: <br>
    * <dfn>&lt;discid&gt; &lt;numberOfTracks&gt; &lt;trackOffset1&gt; &lt;trackOffset2&gt; ... &lt;..n&gt; &lt;lengthInSeconds&gt;</dfn>
    * <p> Where <discid> is the calculated discID according to CDDB specification
    * (see http://www.freedb.org for details on the entire query string).
    *
    */
    public String getQueryString () {
        ArgumentParser p = new ArgumentParser(cdString);
        p.nextArgument(); // "cddb"
        p.nextArgument(); // "query"
        return p.remainder();
    }
}
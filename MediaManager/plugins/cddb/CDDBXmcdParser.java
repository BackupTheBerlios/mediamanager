package plugins.cddb;

//import com.antelmann.io.MyFile;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


/**
* CDDBXmcdParser provides methods to parse the file content
* from a CDDB/FreeDB raw file in xmcd format. <p>
* @author Holger Antelmann
* @see CDInfo
*/
public class CDDBXmcdParser implements Serializable
{
    final String content;


    /** file must be in the CDDB/FreeDB file format */
    public CDDBXmcdParser (File file) throws IOException, ParseException {
        this(new MyFile(file).getContentAsString());
    }


    /** fileContent must be derived from CDDB/FreeDB file format */
    public CDDBXmcdParser (String fileContent) throws ParseException {
        this.content = fileContent;
        String e = checkForErrors();
        if (e != null) throw new ParseException(e, 0);
    }


    /**
    * checks for consistency and returns either a string with
    * the problem found or null if the file is found to be ok
    */
    String checkForErrors () {
        try {
            String tmp;
            if (content.indexOf("# xmcd") != 0) {
                return ("not a xmcd file (CDDB format); xmcd tag is missing");
            }
            StringTokenizer st = new StringTokenizer(content, "\r\n");
            int lcount = 0;
            while (st.hasMoreTokens()) {
                lcount++;
                String line = st.nextToken();
                if (line.length() > 254) {
                    return ("line " + lcount + " has too many characters");
                }
                if (line.equals("") && st.hasMoreTokens()) {
                    return ("illegal empty line at " + lcount);
                }
            }
            String[] ids = readDiscIDs();
            if (ids.length < 1) return "no discID found";
            for (int i = 0; i < ids.length; i++) {
                tmp = ids[i];
                if ((tmp == null) || (tmp.length() != 8)) {
                    return "discID " + i + " invalid";
                }
            }
            tmp = readTitle();
            if ((tmp == null) || (tmp.length() < 1)) return "no CD title found";
            tmp = getTagText("DYEAR");
            try {
                Integer.parseInt(tmp);
            } catch (NumberFormatException e) {
                if (!tmp.equals("")) return "year is not readable";
            }
            int[] os = readOffsets();
            if (os.length < 1) return "no track offsets found";
            for (int i = 0; i < os.length; i++) {
                readTrackTitle(i);
                readTrackExtension(i);
                //readTrackArtist(i);
            }
            readGenre();
            readLength();
            readRevision();
            readSubmitter();
            //readprocessedBy();
            readPlayOrder();
            try {
                readYear();
            } catch (XmcdFormatException e) {
                if (getTagText("DYEAR").length() > 0) {
                    return "the year value is not readable";
                }
            }
            if (Strings.count(content, "\nDYEAR=") != 1) {
                return "multiple year entries present";
            }
            readGenre();
            readExtension();
            readPlayOrder();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }


    /** return the content in xcmd format (used during construction) */
    public String getContent () { return content; }


    /**
    * this static method provides a blank string template for an xmcd format
    * entry based on the given cd
    */
    public static String createXmcdTemplate (CDID cd) {
        String lb = System.getProperty("line.separator", "\r\n");
        String template = "# xmcd" + lb + "#" + lb + "# Track frame offsets:";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "#\t" + cd.getFrameOffset(i);
        }
        template += lb + "#" + lb + "# Disc length: " + cd.getLength() + " seconds";
        template += lb + "#" + lb + "# Revision: 0";
        //template += lb + "# Processed by: ";
        template += lb + "# Submitted via: " + Settings.getProperty("cddb.client");
        template += " " + Settings.getProperty("cddb.client.version") + lb + "#";
        template += lb + "DISCID=" + cd.getDiscID();
        template += lb + "DTITLE=";
        template += lb + "DYEAR=";
        template += lb + "DGENRE=";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "TTITLE" + i + "=";
        }
        template += lb + "EXTD=";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "EXTT" + i + "=";
        }
        template += lb + "PLAYORDER=";
        template += lb;
        return template;
    }


    /**
    * this static method provides a string template for an xmcd format
    * entry based on the given cd containing easily replaceable entries
    */
    public static String createXmcdTemplateWithReplacer (CDID cd) {
        String lb = System.getProperty("line.separator", "\r\n");
        String template = "# xmcd" + lb + "#" + lb + "# Track frame offsets:";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "#\t" + cd.getFrameOffset(i);
        }
        template += lb + "#" + lb + "# Disc length: " + cd.getLength() + " seconds";
        template += lb + "#" + lb + "# Revision: 0";
        //template += lb + "# Processed by: ";
        template += lb + "# Submitted via: " + Settings.getProperty("cddb.client");
        template += " " + Settings.getProperty("cddb.client.version") + lb + "#";
        template += lb + "DISCID=${discid}";
        template += lb + "DTITLE=${dtitle}";
        template += lb + "DYEAR=${dyear}";
        template += lb + "DGENRE=${dgenre}";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "TTITLE" + i + "=${ttitle" + i + "}";
        }
        template += lb + "EXTD=${extd}";
        for (int i = 0; i < cd.getNumberOfTracks(); i++) {
            template += lb + "EXTT" + i + "=${extt" + i + "}";
        }
        template += lb + "PLAYORDER=";
        template += lb;
        return template;
    }


    /**
    * checks the xmcd file for completeness (this should return null before
    * the underlying content would be subject to a CDDB submission).
    * @return an array of warnings discovered during processing
    *          or null if no warnings present
    */
    public String[] checkWarnings () {
        Vector v = new Vector();
        String tmp = readTitle();
        if (tmp.indexOf(" / ") < 1) {
            v.add("Artist and CD title are not properly separated");
        } else if (tmp.indexOf(" / ") != tmp.lastIndexOf(" / ")) {
            v.add("multiple ' / ' encountered in title");
        }
        if (readGenre().length() < 1) v.add("no genre entered");
        try {
            int year = readYear();
            if ((year < 1900) || (year >
                (new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) + 1)))
            {
                v.add("the year seems a bit off; you entered " + year);
            }
        } catch (XmcdFormatException e) {
            v.add("no year value entered");
        }
        int[] os = readOffsets();
        for (int i = 0; i < os.length; i++) {
            if (readTrackTitle(i).length() < 1) {
                v.add("no title added for track " + i);
            }
        }
        if (v.size() == 0) return null;
        return (String[]) v.toArray(new String[v.size()]);
    }


    /**
    * parses for the offsets and returns a CDID object using the first discID
    * found in the DISCID field
    */
    public CDID readCDID () {
        return new CDID(readDiscIDs()[0], readOffsets(), readLength());
    }


    public int readNumberOfTracks () {
        return readOffsets().length;
    }


    /** parses for the track offsets */
    public int[] readOffsets () {
        int pos = content.indexOf("\n# Track frame offsets:");
        StringTokenizer st = new StringTokenizer(content.substring(pos), "\r\n");
        Vector v = new Vector();
        st.nextToken(); // skip over the beginning line
        while (st.hasMoreTokens()) {
            String line = st.nextToken().substring(1).trim();
            try {
                v.add(new Integer(line));
            } catch (NumberFormatException e) {
                break;
                /*
                   the lines below would actually be correct, but again,
                   CDDB entries do not always have that blank comment line
                */
                //if (line.equals("")) {
                //    break;
                //} else {
                //    throw new XmcdFormatException(
                //        "track offset could not be read as a number");
                //}
            }
        }
        int[] os = new int[v.size()];
        for (int i = 0; i < os.length; i++) {
            os[i] = ((Integer)v.get(i)).intValue();
        }
        return os;
    }


    /** parses for the disc length and returns the seconds */
    public int readLength () {
        String tag = "\n# Disc length:";
        String s = null;
        try {
            s = parseTag(tag, "seconds", 0);
        } catch (XmcdFormatException e) {
            // this is an inconsistency widely found in CDDB entries
            // that is against their own specs, I'm afraid
            s = parseTag(tag, "secs", 0);
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new XmcdFormatException("Disc length tag not a proper number", null, null);
        }
    }


    /** parses for the revision number of the entry */
    public int readRevision () {
        String tag = "\n# Revision: ";
        try {
            return Integer.parseInt(parseTag(tag, "\n", 0));
        } catch (NumberFormatException e) {
            throw new XmcdFormatException("Revision tag not a proper number", null, null);
        }
    }


    /** parses for the "Submitted via" entry */
    public String readSubmitter () {
        return parseTag("\n# Submitted via: ", "\n", 0);
    }


    /** parses for the "Processed by" entry (an optional entry) */
    public String readProcessedBy () {
        try {
            return parseTag("\n# Processed by: ", "\n", 0);
        } catch (XmcdFormatException e) { return null; }
    }


    /**
    * parses for possibly multiple discIDs to support
    * searching for discIDs that link to the same file
    */
    public String[] readDiscIDs () {
        String line = getTagText("DISCID");
        StringTokenizer st = new StringTokenizer(line, ", ");
        Vector v = new Vector();
        while (st.hasMoreTokens()) {
            v.add(st.nextToken());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }


    /** parses for the full title */
    public String readTitle () {
        return getTagText("DTITLE");
    }


    /**
    * parses the title for the part after ' / ' which by convention refers
    * to the CD title; if no title separator is found, the full title
    * (same as <code>readTitle()</code>) is returned
    */
    public String readCDTitle () {
        String t = readTitle();
        int i = t.indexOf(" / ");
        if (i < 0) {
            return t;
        } else {
            return t.substring(i + 3);
        }
    }


    /**
    * parses the title for the part before ' / ' which refers
    * to the artist specified as &lt;firstName lastName&gt; by convention;
    * null is returned if no artist separator could be found
    */
    public String readArtist () {
        String t = readTitle();
        int i = t.indexOf(" / ");
        if (i < 0) {
            return null;
        } else {
            return t.substring(0, i);
        }
    }


    /** parses for extended CD information */
    public String readExtension () {
        return getTagText("EXTD");
    }


    /** parses for the year; returns 0 if no year was entered */
    public int readYear () {
        try {
            return Integer.parseInt(getTagText("DYEAR"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    /** parses for the genre */
    public String readGenre () {
        return getTagText("DGENRE");
    }


    /** parses for the entire track title (including artist if available) */
    public String readTitle (int track) {
        return getTagText("TTITLE" + track);
    }


    /** parses for the track title (without artist info if applicable) */
    public String readTrackTitle (int track) {
        String t = readTitle(track);
        int i = t.indexOf(" / ");
        if (i > 0) {
            // eliminate the track artist information
            return t.substring(i + 3);
        } else {
            return t;
        }
    }


    /**
    * parses for the track artist (using first the track artist info, then
    * the CD artist info)
    */
    public String readTrackArtist(int track) {
        String t = readTitle(track);
        int i = t.indexOf(" / ");
        if (i > 0) {
            return t.substring(0, i);
        } else {
            return readArtist();
        }
    }


    /** parses for extended track info */
    public String readTrackExtension (int track) {
        return getTagText("EXTT" + track);
    }


    /** parses for the play order */
    public int[] readPlayOrder () {
        try {
            StringTokenizer st = new StringTokenizer(getTagText("PLAYORDER"), ",\n\r\t ");
            int[] list = new int[st.countTokens()];
            for (int i = 0; i < list.length; i++) {
                list[i] = Integer.parseInt(st.nextToken());
            }
            return list;
        } catch (Exception e) {
            XmcdFormatException ex = new XmcdFormatException("could not extract play order", null, null);
            ex.initCause(e);
            throw ex;
        }
    }


    /**
    * returns a list of properties.
    * The property keys - as far as they are found in the content - are as follows:
    * <ul>
    * <li>cd.discids</li>
    * <li>cd.title</li>
    * <li>cd.cdtitle</li>
    * <li>cd.artist</li>
    * <li>cd.length</li>
    * <li>cd.revision</li>
    * <li>cd.genre</li>
    * <li>cd.submitter</li>
    * <li>cd.processedBy</li>
    * <li>cd.title</li>
    * <li>cd.extension</li>
    * <li>cd.year</li>
    * <li>track.n.title (where n is the number of the track)</li>
    * <li>track.n.artist (where n is the number of the track)</li>
    * <li>track.n.ext (where n is the number of the track)</li>
    * </ul>
    */
    public Properties getProperties () {
        Properties prop = new Properties();
        prop.put("cd.discids",     getTagText("DISCID"));
        prop.put("cd.title",       readTitle());
        prop.put("cd.cdtitle",     readCDTitle());
        prop.put("cd.artist",      readArtist());
        prop.put("cd.length",      "" + readLength());
        prop.put("cd.revision",    "" + readRevision());
        prop.put("cd.genre",       readGenre());
        prop.put("cd.submitter",   readSubmitter());
        prop.put("cd.processedBy", readProcessedBy());
        prop.put("cd.title",       readTitle());
        prop.put("cd.extension",   readExtension());
        prop.put("cd.year",        "" + readYear());
        int[] tracks = readOffsets();
        for (int i = 0; i < tracks.length; i++) {
            prop.put("track." + i + ".title", readTrackTitle(i));
            prop.put("track." + i + ".artist", readTrackArtist(i));
            String tmp = readTrackExtension(i);
            if ((tmp != null) && (tmp.length() > 0)) {
                prop.put("track." + i + ".ext", tmp);
            }
        }
        return prop;
    }


    String translate (String txt) {
        txt = Strings.replace(txt, "\\n", "\n");
        txt = Strings.replace(txt, "\\t", "\t");
        txt = Strings.replace(txt, "\\\\", "\\");
        return txt;
    }


    /**
    * returns the text for the tag that could be found in multiple lines
    */
    protected String getTagText (String tag) throws XmcdFormatException {
        String ftag = "\n" + tag + "=";
        int pos = content.indexOf(ftag);
        if (pos < 0) throw new XmcdFormatException("tag \"" + tag + "\" not found", null, null);
        String result = "";
        while (pos > -1) {
            result += " " + content.substring(
                pos + tag.length() + 2,
                content.indexOf("\n", pos + tag.length() + 2)
            ).trim();
            pos = content.indexOf(ftag, pos + tag.length() + 2);
        }
        return (translate(result.trim()));
    }


    /**
    * returns the text between btag and etag after index;
    * leading and tailing white space is removed
    */
    protected String parseTag (String btag, String etag, int index)
        throws XmcdFormatException
    {
        int begin = content.indexOf(btag);
        if (begin < 1) throw new XmcdFormatException(
            "begin tag \"" + btag + "\" not found", null, null);
        int end = content.indexOf(etag, begin + btag.length());
        if (end < 1) throw new XmcdFormatException(
            "end tag \"" + etag + "\" after \"" + btag + "\" not found", null, null);
        return content.substring(begin + btag.length(), end).trim();
    }
}

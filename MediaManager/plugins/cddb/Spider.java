package plugins.cddb;

//import com.antelmann.util.Monitor;
//import com.antelmann.util.Strings;
//import com.antelmann.util.logging.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;


/**
* Spider provides several useful static functions for accessing
* web content and parsing HTML most based on a simple URL.
* Note that because this class uses functionality from the javax.swing
* package (although no GUIs are used in this class), there are non-terminating
* javax.swing threads that get created when using this class.
* I.e. an application using this class without using any other
* javax.swing GUI components may end up with unwanted non-terminated
* threads possible forcing calls to e.g. <code>System.exit(0)</code>
* to terminate a simple program.
* @author Holger Antelmann
* @see CrawlerSetting
* @see URLCache
*/
public final class Spider
{
    static String whoisURLLocation = "http://www.netsol.com/cgi-bin/whois/whois?STRING=${domain}&SearchType=do";
    static String betterWhoisURLLocation = "http://www.betterwhois.com/bwhois.cgi?domain=${domain}";
    static ParserDelegator parser     = new ParserDelegator();
    static HTMLEditorKit   docKit     = new HTMLEditorKit();
    static String          lineBreak  = System.getProperty("line.separator");
    static int             bufferSize = 2048;


    private Spider () {}


    public static String fullHeaderAsString (URL url) throws IOException {
        String s = null;
        URLConnection con = url.openConnection();
        Map map = con.getHeaderFields();
        Iterator i = map.keySet().iterator();
        if (i.hasNext()) s = "";
        while (i.hasNext()) {
            String key = (String) i.next();
            s += ((key == null)? "" : key + ": ");
            s += ((List)map.get(key)).get(0) + lineBreak;
        }
        return s;
    }


    /**
    * returns the time it takes to establish a live connection to the given URL
    * and returns -1 only if the URL is unreachable.
    */
    public static long ping (URL url) {
        long start = System.currentTimeMillis();
        try {
            java.net.URLConnection con = url.openConnection();
            con.connect();
            if (con instanceof java.net.HttpURLConnection) {
                ((java.net.HttpURLConnection)con).disconnect();
            }
            return (System.currentTimeMillis() - start);
        } catch (IOException e) {
            return -1;
        }
    }


    /** saves the content of the given URL to the given file */
    public static void saveURLtoFile (URL url, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        byte[] bytes = new byte[bufferSize];
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        int n = 0;
        while (n > -1) {
            n = in.read(bytes);
            if (n < 0) break;
            out.write(bytes, 0, n);
        }
        out.flush();
        out.close();
    }


    /**
    * returns links filtered by the given protocol
    * @see #getLinks(URL, boolean)
    */
    public static URL[] getLinks (URL url, boolean allowDuplicates, String protocol)
        throws IOException
    {
        URL[] links = getLinks(url, allowDuplicates);
        if (links == null) return null;
        Vector v = new Vector(links.length);
        for (int i = 0; i < links.length; i++) {
            if (links[i].getProtocol().equals(protocol)) v.add(links[i]);
        }
        return (URL[]) v.toArray(new URL[v.size()]);
    }


    /**
    * returns an array containing URLs that the given URL links to;
    * if the page is a frameset, the frame sources are returned.
    * If no links are present within the given URL, an empty array
    * is returned
    */
    public static URL[] getLinks (URL url, boolean allowDuplicates) throws IOException {
        return getLinks(getReader(url), url, allowDuplicates);
    }


    /**
    * allows to read the content from another location but the url itself
    * @see #getLinks(URL, boolean)
    */
    public static URL[] getLinks (Reader reader, final URL url, final boolean allowDuplicates)
        throws IOException
    {
        final HashSet links = new HashSet();
        // not complete, but handles at least a common case
        if (url.getProtocol().equals("mailto")) return null;
        HTMLEditorKit.ParserCallback linkSniffer = new HTMLEditorKit.ParserCallback() {
            public void handleStartTag (HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
                if (tag == HTML.Tag.A) {
                    try {
                        URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.HREF), url);
                        if (!allowDuplicates && (links.contains(lurl))) return;
                        links.add(lurl);
                    } catch (MalformedURLException e) {}
                }
            }
            public void handleSimpleTag (HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
                if (tag == HTML.Tag.FRAME) {
                    try {
                        URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.SRC), url);
                        if (!allowDuplicates && (links.contains(lurl))) return;
                        links.add(lurl);
                    } catch (MalformedURLException e) {}
                }
            }
        };
        new ParserDelegator().parse(reader, linkSniffer, true);
        return (URL[]) links.toArray(new URL[links.size()]);
    }


    /**
    * Assuming the URL points to a HTML page, only links that are not accessible
    * are returned. If all links are valid (or the page didn't contain links),
    * an empty array is returned.
    * Only links with 'http', 'ftp' or 'file' protocol are checked.
    */
    public static URL[] getBrokenLinks (URL url) throws IOException {
        URL[] links = getLinks(url, false);
        Vector broken = new Vector();
        for (int i = 0; i < links.length; i++) {
            String p = links[i].getProtocol();
            if (!"http".equals(p) && !"ftp".equals(p) && !"file".equals(p)) {
                continue;
            }
            try {
                links[i].getContent();
            } catch (IOException e) {
                broken.add(links[i]);
            }
        }
        return (URL[]) broken.toArray(new URL[broken.size()]);
    }


    /**
    * returns an array of images that are embedded in the given URL
    */
    public static URL[] getImages (URL url, boolean allowDuplicates) throws IOException {
        return getImages(getReader(url), url, allowDuplicates);
    }


    /**
    * allows to read the content from another location but the url itself
    * @see #getImages(URL, boolean)
    */
    public static URL[] getImages (Reader reader, final URL url, final boolean allowDuplicates)
        throws IOException
    {
        final HashSet images = new HashSet();
        HTMLEditorKit.ParserCallback imageSniffer = new HTMLEditorKit.ParserCallback() {
            public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
                if (tag == HTML.Tag.IMG) {
                    try {
                        URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.SRC), url);
                        if (images.contains(lurl)) return;
                        images.add(lurl);
                    } catch (MalformedURLException e) {}
                }
            }
        };
        new ParserDelegator().parse(reader, imageSniffer, true);
        return (URL[]) images.toArray(new URL[images.size()]);
    }


    /** translates a relative URL to an absolute URL */
    static URL getURLFromLink (String link, URL context) throws MalformedURLException {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            // See if it is a relative URL that can be translated
            // into an absolute URL
            // Note that this translation will be incorrect if the context
            // is e.g. "http://localhost/myweb" vs. "http://localhost/myweb/"
            return new URL(context, link);
        }
    }


    /**
    * searches the content of the given URL for the presence of one of the
    * searchPatterns given; returns true if one of the patterns was found
    * @param url             URL specifying the location of the content to be searched
    * @param searchPattern   array of search patterns this function will look for
    * @param includeHTMLCode if true, this function will search through all content of
    *                        the URL, including HTML code; if false, it will only search
    *                        through text found
    */
    public static boolean includesPattern (URL url, String[] searchPattern, boolean includeHTMLCode)
        throws IOException
    {
        String text = null;
        if (includeHTMLCode) {
            text = getContentAsString(url);
        } else {
            text = stripText(url);
        }
        if (text == null) throw new IOException("The given URL's text is not accessible");
        for (int i = 0; i < searchPattern.length; i++) {
            if (text.indexOf(searchPattern[i]) > -1) return true;
        }
        return false;
    }


    /** returns the title of the document */
    public static String getTitle (URL url) throws IOException {
        return getTagText(url, HTML.Tag.TITLE, null);
    }


    /**
    * returns all text found in the given desiredTag delimited by
    * the given delimiter
    */
    public static String getTagText (URL url, HTML.Tag desiredTag, String delimiter)
        throws IOException
    {
        return getTagText(getReader(url), url, desiredTag, delimiter);
    }



    /**
    * allows to read the content from another location but the url itself
    * @see #getTagText(URL, HTML.Tag, String)
    */
    public static String getTagText (Reader reader, final URL url, final HTML.Tag desiredTag, final String delimiter)
        throws IOException
    {
        final StringBuffer title = new StringBuffer();
        class Wrapper {
            boolean found = false;
            boolean first = true;
        }
        final Wrapper item = new Wrapper();
        HTMLEditorKit.ParserCallback tagSniffer = new HTMLEditorKit.ParserCallback() {
            public void handleStartTag (HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
                if (tag == desiredTag) item.found = true;
            }

            public void handleEndTag (HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
                if (tag == desiredTag) item.found = false;
            }

            public void handleText(char[] data, int pos) {
                if (item.found) {
                    if ((!item.first) && (delimiter != null)) title.append(delimiter);
                    title.append(data);
                    item.found = false;
                    item.first = false;
                }
            }
        };
        parser.parse(reader, tagSniffer, true);
        return title.toString();
    }


    /** a line break is put after each separate text occurrence */
    public static String stripText (URL url) throws IOException {
        return stripText(url, lineBreak);
    }


    /**
    * returns a String containing the text of all HTML tag types from
    * the given URL
    */
    public static String stripText (URL url, String delimiter)
        throws IOException
    {
        return stripText(getReader(url), url, delimiter);
    }


    /**
    * allows to read the content from another location but the url itself
    * @see #stripText(URL, String)
    */
    public static String stripText (Reader reader, URL url, final String delimiter)
        throws IOException
    {
        final StringBuffer text = new StringBuffer();
        if (reader == null) return null;
        class Wrapper { boolean first = false; }
        final Wrapper found = new Wrapper();
        HTMLEditorKit.ParserCallback textSniffer = new HTMLEditorKit.ParserCallback() {
            public void handleText(char[] data, int pos) {
                if (found.first) text.append(delimiter);
                text.append(data);
                found.first = true;
            }
        };
        parser.parse(reader, textSniffer, true);
        return text.toString();
    }


    /**
    * returns an HTMLDocument object with the parsed content of the given
    * URL for further examination
    */
    public static HTMLDocument getHTMLDocument (URL url) throws IOException {
        return getHTMLDocument(getReader(url));
    }


    /**
    * returns an HTMLDocument object with the parsed content from the given
    * reader for further examination
    */
    public static HTMLDocument getHTMLDocument (Reader reader) throws IOException
    {
        HTMLDocument doc = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            docKit.read(reader, doc, 0);
            return doc;
        } catch (BadLocationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
    * This function constructs a reader appropriate for reading the content
    * from the given URL.
    * Currently, this function only supports HTTP, FTP and FILE protocol.
    * @throws IOException
    * @throws UnsupportedOperationException if the given URL is of another
    *                                       protocol than HTTP or FILE
    */
    public static Reader getReader (URL url) throws IOException {
        return getReader(url, null);
    }


    /**
    * @see #getReader(URL url)
    */
    public static Reader getReader (URL url, String charsetName) throws IOException {
        Reader reader;
        if (url.getProtocol().equals("http")) {
            if (charsetName == null) {
                reader = new InputStreamReader(url.openConnection().getInputStream());
            } else {
                reader = new InputStreamReader(url.openConnection().getInputStream(), charsetName);
            }
        } else if (url.getProtocol().equals("ftp")) {
            if (charsetName == null) {
                reader = new InputStreamReader(url.openConnection().getInputStream());
            } else {
                reader = new InputStreamReader(url.openConnection().getInputStream(), charsetName);
            }
        } else if (url.getProtocol().equals("file")) {
            reader = new FileReader(url.getPath());
        } else {
            String s = "Protocol " + url.getProtocol();
            s += " not supported for obtaining a Reader";
            throw new IOException(s);
        }
        return reader;
    }


    /**
    * retrieves the raw content from the URL. <p>
    * Note that if the content doesn't fit into memory due to its size,
    * null is returned.
    */
    public static byte[] getBytes (URL url) throws IOException {
        try {
            byte[] bytes = new byte[0];
            byte[] buffer = new byte[bufferSize];
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            //con.connect();
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            int n = 0;
            int pos;
            while (n > -1) {
                n = in.read(buffer);
                if (n < 0) break;
                pos = bytes.length;
                byte[] tmp = new byte[pos + n];
                System.arraycopy(bytes,  0, tmp, 0,   pos);
                System.arraycopy(buffer, 0, tmp, pos, n);
                bytes = tmp;
            }
            return bytes;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


    /**
    * retrieves the entire content accessible through the given URL as a String.
    * If the URL points to an HTML page, the full HTML code is returned.
    * This method is not suitable for retrieving binary data as it uses a
    * BufferedReader and also places platform specific line breaks between the
    * lines read with readLine().
    * If the URL could not be accessed and an IOException was caught, null is returned.
    */
    public static String getContentAsString (URL url) throws IOException
    {
        String s = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inline = null;
        while ((inline = in.readLine()) != null) {
            if (!s.equals("")) s+= lineBreak;
            s += inline;
        }
        in.close();
        return s;
    }


    /**
    * returns the registrant information from the Internic database;
    * the URL must use the host name and not the IP address
    */
    public static String whois (URL url) throws IOException {
        String host = url.getHost();
        if (host != null) {
            int dot = host.lastIndexOf(".");
            int begin = host.lastIndexOf(".", dot - 1);
            return whois(host.substring(begin + 1));
        }
        return null;
    }


    /**
    * returns the registrant information from the Internic database
    */
    public static String whois (String domainName) throws IOException {
        if ((domainName == null) || (domainName.length() < 1)) return null;
        String urlLoc = Strings.replace(whoisURLLocation, "${domain}", domainName);
        //return Spider.getTagText(new URL(url), Tag.PRE, "\n");
        String all = Spider.getContentAsString(new URL(urlLoc));
        int begin = Strings.indexOfIgnoreCase(all, "<pre>");
        if (begin < 0) return null;
        int end   = Strings.indexOfIgnoreCase(all, "</pre>");
        if (end < 0) return null;
        return all.substring(begin + 5, end);
    }


    /**
    * searches the web from the given root for URLs based on the criteria
    * given in the  crawler; search is performed breadth-first
    * @param root               root URL for the search
    * @param crawler            criteria for crawling
    * @param numberOfURLsToFind if >0 the search is stopped when the given number
    *                           of URLs are found to match the crawler's criteria
    * @return an array containing URLs found that satisfy the crawler's criteria
    *         as defined by the crawler
    */
    public static URL[] crawlWeb (URL root, CrawlerSetting crawler, int numberOfURLsToFind) {
        Vector searchList = new Vector();
        searchList.add(new URLWrapper(root, null));
        return crawlWeb(searchList, new Vector(), new Vector(), crawler, 0, numberOfURLsToFind);
    }


    /**
    * usually called by crawlWeb(URL root, CrawlerSetting crawler)
    * @param searchList List of Spider.URLWrapper objects containing nodes to be examined
    * @param resultList List of URL objects
    * @param closedList List of URL objects
    * @param crawler    criteria for crawling
    * @param depth      link distance from the root of the search
    * @return an array containing URLs found that satisfy the criteria
    *         as defined by the crawler
    * @see #crawlWeb(URL, CrawlerSetting, int)
    */
    public static URL[] crawlWeb (List searchList, List resultList, List closedList, CrawlerSetting crawler, int depth, int numberOfURLsToFind) {
        Vector newLinks = new Vector();
        URL url, referer;
        // first, working through the list just to search for matches,
        // not for gathering new links
        // (to avoid out-of-memory-errors before the list has been worked through)
        for (int current = 0; current < searchList.size(); current++) {
            url     = ((URLWrapper)searchList.get(current)).url;
            referer = ((URLWrapper)searchList.get(current)).referer;
            // ask the crawler whether it matches its criteria
            if (crawler.matchesCriteria(url, referer, depth, resultList, closedList)) {
                resultList.add(url);
            } else {
                closedList.add(url);
            }
            // stop the search if the number of reqired URLs is found
            if ((numberOfURLsToFind > 0) && (numberOfURLsToFind <= resultList.size()))
                return (URL[]) resultList.toArray(new URL[resultList.size()]);
        }
        // another walk through the list; this time for generating links
        // for the next level
        for (int current = 0; current < searchList.size(); current++) {
            url     = ((URLWrapper)searchList.get(current)).url;
            referer = ((URLWrapper)searchList.get(current)).referer;
            // ask the crawler whether or not to follow the links of this url
            if (crawler.followLinks(url, referer, depth, resultList, closedList, newLinks)) {
                try {
                    URL[] link = Spider.getLinks(url, false);
                    linkLoop:
                    for (int i = 0; i < link.length; i++) {
                        // checking whether the page was encountered before
                        for (int x = 0; x < closedList.size(); x++) {
                            if (link[i].sameFile((URL)closedList.get(x))) continue linkLoop;
                        }
                        for (int x = 0; x < resultList.size(); x++) {
                            if (link[i].sameFile((URL)resultList.get(x))) continue linkLoop;
                        }
                        for (int x = 0; x < newLinks.size(); x++) {
                            if (link[i].sameFile(((URLWrapper)newLinks.get(x)).url)) continue linkLoop;
                        }
                        // adding the new link to the list of links for the next level
                        newLinks.add(new URLWrapper(link[i], url));
                    }
                } catch (IOException e) {}
            }
        }
        if (newLinks.size() == 0) return (URL[]) resultList.toArray(new URL[resultList.size()]);
        return crawlWeb(newLinks, resultList, closedList, crawler, (depth + 1), numberOfURLsToFind);
    }


    /**
    * This special web search function returns all URLs found that contain
    * the desired searchString given the constrains of the other parameter.
    * The search starts at the entryPoint and goes recursively through the
    * tree derived from the links from that URL as deep as suggested by the
    * level parameter; the search is conducted in a bredth-first-search manner.
    * For more flexible web searches, consider the use of a com.antelmann.net.CrawlerSetting.
    * <br>Use of Monitor - if present (i.e. monitor may be null, in which case
    * no feedback is provided while the function is executing):
    * <ul>
    * <li>search is stopped when monitor is disabled (enabling e.g. to stop
    *     searching after 10 pages were found etc.)</li>
    * <li>for each examined node, monitor.increment() is called</li>
    * <li>for each examined node, monitor.runTask() is called</li>
    * <li>monitor.getObject() contains the current URL being examined</li>
    * <li>monitor.getObject(0) contains the current searchList</li>
    * <li>monitor.getObject(1) contains the current excludeList</li>
    * <li>monitor.getObject(2) contains the current resultSet</li>
    * <li>monitor.getNumber() contains the number of total pages examined so far</li>
    * <li>monitor.getNumber(0) contains the number of result pages found so far</li>
    * <li>monitor.getNumber(1) contains the current level (counting down to 0)</li>
    * <li>monitor.getNumber(2) contains the number of URLs to be searched in the current level
    *     (giving an indication on how many URLs are ahead in a new level)</li>
    * <li>thus, monitor.getSize() is required to be at least 3 if present</li>
    * </ul>
    * @param searchPattern an array containing String patterns to search for;
    *        wildcards are not supported
    * @param entryPoint the URL from where to start the search
    * @param includeHTMLCode if true, the search will include not only the text,
    *        but also the HTML code of a page
    * @param level limits the depth of the search; only pages that are reachable
    *        with less or equal than the given number of recursive links will be included
    * @param currentSiteOnly if true, the search is limited to the host of the entryPoint
    * @param searchURLExclusionPatterns if not null it contains an array of String patterns
    *        which will be used to filter out unwanted URLs, i.e. if any of the patterns
    *        are present in the URL's path, that URL will be disregarded;
    *        wildcards are not supported
    * @param monitor see above for usage; may be null
    * @see #crawlWeb(URL, CrawlerSetting, int)
    */
    public static URL[] searchWebFor (
        String[] searchPattern,
        URL entryPoint,
        boolean includeHTMLCode,
        int level,
        boolean currentSiteOnly,
        String[] searchURLExclusionPatterns,
        Monitor monitor)
    {
        Vector searchList = new Vector();
        searchList.add(entryPoint);
        List resultSet = searchWebFor(
            searchPattern,
            searchList,
            includeHTMLCode,
            level,
            currentSiteOnly,
            new Vector(),  // excludeList
            new Vector(), // resultSet
            searchURLExclusionPatterns,
            monitor);
        return (URL[]) resultSet.toArray(new URL[resultSet.size()]);
    }


    /**
    * usually called by the other searchWebFor() function; all Lists contain URL objects
    * @see #searchWebFor(String[], URL, boolean, int, boolean, String[], Monitor)
    */
    public static List searchWebFor (
        String[] searchPattern,
        Vector searchList,
        boolean includeHTMLCode,
        int level,
        boolean currentSiteOnly,
        List excludeList,
        List resultList,
        String[] searchURLExclusionPatterns,
        Monitor monitor)
    {
        if ((monitor != null) && (monitor.getSize() < 3)) {
            String s = "The given monitor is expected to have at least a length of 3";
            throw new IllegalArgumentException(s);
        }
        if (searchList.size() == 0) return resultList;
        Vector newLinks = new Vector();
        if (monitor != null) monitor.setNumber(1, level);
        if (monitor != null) monitor.setNumber(2, searchList.size());
        searchListLoop:
        for (int current = 0; current < searchList.size(); current++) {
            if (monitor != null) {
                monitor.setObject(0, searchList);
                monitor.setObject(1, excludeList);
                monitor.setObject(2, resultList);
                if (monitor.disabled()) return resultList;
            }
            URL entryPoint = (URL) searchList.get(current);
            if (monitor != null) monitor.setObject(entryPoint);
            if (monitor != null) monitor.increment();
            if (monitor != null) monitor.runTask();
            // filtering out unwanted URLs
            if (searchURLExclusionPatterns != null) {
                for (int i = 0; i < searchURLExclusionPatterns.length; i++) {
                    if (searchURLExclusionPatterns[i] == null) continue;
                    if (entryPoint.getPath().indexOf(searchURLExclusionPatterns[i]) > -1) continue searchListLoop;
                }
            }
            // stripping the text (with or without HTML code) from the given URL
            String text = null;
            try {
                if (includeHTMLCode) {
                    text = getContentAsString(entryPoint);
                } else {
                    text = stripText(entryPoint);
                }
            } catch (IOException e) {
                continue;
            }
            // test whether the current URL inclues any of the searchPattern
            for (int i = 0; i < searchPattern.length; i++) {
                if (text.indexOf(searchPattern[i]) > -1) {
                    resultList.add(entryPoint);
                    if (monitor != null) monitor.increment(0);
                    break;
                } else {
                    excludeList.add(entryPoint);
                    break;
                }
            }
            if (level <= 0) continue;
            // there's another level to go, so collect all links
            try {
                URL[] linksArray = getLinks(entryPoint, false, "http");
                smallLoop:
                for (int i = 0; i < linksArray.length; i++) {
                    if (currentSiteOnly && (!linksArray[i].getHost().equals(entryPoint.getHost()))) continue smallLoop;
                    for (int x = 0; x < excludeList.size(); x++) {
                        if (linksArray[i].sameFile((URL)excludeList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < searchList.size(); x++) {
                        if (linksArray[i].sameFile((URL)searchList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < resultList.size(); x++) {
                        if (linksArray[i].sameFile((URL)resultList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < newLinks.size(); x++) {
                        if (linksArray[i].sameFile((URL)newLinks.get(x))) continue smallLoop;
                    }
                    newLinks.add(linksArray[i]);
                }
            } catch (IOException e) {}
        }
        return searchWebFor(
                searchPattern,
                newLinks,
                includeHTMLCode,
                (level - 1),
                currentSiteOnly,
                excludeList,
                resultList,
                searchURLExclusionPatterns,
                monitor);
    }



    /**
    * a specialized Monitor that can be used with a Spider;
    * it simply provides some useful progress information during execution
    * of the searchWebFor() function
    * @see #searchWebFor(String[], URL, boolean, int, boolean, String[], Monitor)
    */
    public static class SMonitor extends Monitor implements Runnable
    {
        int found = 0;
        int level = 0;
        int maxFound = 0;
        Logger logger = null;


        /**
        * @param log if not null, log will be used to log all messages there
        * @param maxFound if set to a number >0, run() will disable the
        *        given monitor after the given number of pages were reported
        *        found by the monitor
        * @see Spider#searchWebFor(String[], URL, boolean, int, boolean, String[], Monitor)
        */
        public SMonitor (Logger logger, int maxFound) {
            super(3);
            setTask(this);
            this.maxFound = maxFound;
            this.level = getNumber(1);
            this.logger = logger;
        }


        /**
        * prints progress information from the given monitor to either
        * the console or a specified log file
        */
        public void run () {
            if ((level != 0) && (getNumber(1) != level)) {
                say("levels to go: " + level);
                say("pages ahead in current level: " + getNumber(2));
            }
            level = getNumber(1);
            if (getNumber(0) > found) {
                say("** entry found (#" + ++found + ")");
                if ((maxFound > 0) && (found >= maxFound)) disable();
            }
            say("examining #" + getNumber() + ": " + getObject());
        }


        public int getNumberOfPagesSearched () { return getNumber(); }


        public int getCurrentLevel () { return getNumber(1); }


        public int getNumberOfPagesFound () { return getNumber(2); }


        public URL getCurrentURL () { return (URL) getObject(); }


        public List getSearchList () { return (List) getObject(0); }


        public List getExcludeList () { return (List) getObject(1); }


        public List getResultList () { return (List) getObject(2); }


        void say (String s) {
            if (logger == null) {
                System.out.println("[MonitorTaskMessage]: " + s);
            } else {
                logger.log(s);
            }
        }


        public void setMaxFound (int maxFound) {
            this.maxFound = maxFound;
        }
    }



    /**
    * wrappes a java.net.URL and keeps a reference to its referer
    */
    public static class URLWrapper
    {
        public URLWrapper (URL url, URL referer) {
            this.url = url;
            this.referer = referer;
        }

        public URL url, referer;
    }
}
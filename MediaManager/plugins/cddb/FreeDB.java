package plugins.cddb;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.StringTokenizer;


/**
* FreeDB implements the connection to a freedb.org server
* or one of its mirrors. <p>
* The FreeDB.org server fully supports the CDDB protocol.
* You can configure the default connection
* settings by altering the following properties in
* <code>com.antelmann.util.Settings</code>:
* <ul>
* <li><code>cddb.client</code></li>
* <li><code>cddb.client.version</code></li>
* </ul>
* Also, the user name is taken from the system properties:
* <code>user.name</code> to identify the user connecting to the service.
* @author Holger Antelmann
* @see com.antelmann.util.Settings
* @see #setServer(CDDBServer)
*/
public class FreeDB implements CDDB
{
    static String LINEBREAK  = "\r\n";
    //String FreeDBHost = "freedb.freedb.org";
    //int    FreeDBPort = 8880;
    int    CDDBPort  = 80;
    String CDDBSite  = "http://freedb.freedb.org/~cddb/cddb.cgi";
    String CDDBAddress = "/~cddb/cddb.cgi";
    String defaultUser = "unknown";
    String user;
    String client;
    String version;
    String queryTemplate;
    final String COMMAND          = "@command@";
    final String COMMAND_CAT      = "cddb lscat";
    final String COMMAND_DISCID   = "discid ";
    final String COMMAND_MSG      = "motd";
    final String COMMAND_QUERY    = "cddb query ";
    final String COMMAND_READ     = "cddb read ";
    final String COMMAND_SITES    = "sites";


    /**
    * uses CDDBServer.DEFAULT_SERVER
    * @see CDDBServer#DEFAULT_SERVER
    */
    public FreeDB () throws IOException {
        this(CDDBServer.DEFAULT_SERVER);
    }


    /**
    * @see #setServer(CDDBServer)
    */
    public FreeDB (CDDBServer server) throws IOException {
        user = System.getProperty("user.name");
        if (user == null) user = defaultUser;
        if (user.indexOf(" ") > -1) {
            StringTokenizer st = new StringTokenizer(user, " ");
            user = st.nextToken();
        }
        if (user.length() < 1) user = defaultUser;
        client  = Settings.getProperty("cddb.client");
        version = Settings.getProperty("cddb.client.version");
        setServer(server);
    }


    /**
    * changes the site location used to access the service; the protocol
    * for the server must be <dfn>http</dfn>
    * @throws IllegalArgumentException if the server protocol is not <dfn>http</dfn>
    * @throws IOException if the local hostname could not be resolved
    * @see #listSites()
    * @see CDDBServer
    */
    public void setServer (CDDBServer server) throws IOException, IllegalArgumentException {
        if (!"http".equals(server.protocol)) {
            throw new IllegalArgumentException("given server doesn't support http");
        }
        queryTemplate = "http://" + server.getSite() + ":";
        queryTemplate += server.getPort() + server.getAddress();
        queryTemplate += "?cmd=" + COMMAND + "&hello=" + user;
        queryTemplate +=  "+" + InetAddress.getLocalHost().getHostName();
        queryTemplate += "+" + client  + "+" + version;
        queryTemplate += "&" + "proto=5";
    }


    /**
    * This method is called for every command supported by the interface
    * and returns the result as it is provided by the service. <p>
    * Currently, this method is implemented to use the HTTP protocol
    * with GET. If other protocols were to be supported (i.e. HTTP POST or
    * Telnet), only this method needs to be changed/overwritten.
    * @param command the entire client command as specified in the CDDB
    *        protocol; example: "<dfn>cddb lscat</dfn>"
    */
    protected String performCommand (String command) throws IOException {
        String query = queryTemplate.replaceFirst(COMMAND,
            URLEncoder.encode(command, "UTF-8"));
        URL url = new URL(query);
        String result = Spider.getContentAsString(url);
        return result;
    }


    /** returns a 'message of the day' quote from freedb.org */
    public String getMessageOfTheDay () throws IOException {
        return performCommand(COMMAND_MSG);
    }


    /**
    * returns sites that can be used as a mirror for this service. <p>
    * @see #setServer(CDDBServer)
    * @see com.antelmann.util.Settings
    * @see CDDBServer
    */
    public CDDBServer[] listSites () throws IOException {
        String result = performCommand(COMMAND_SITES);
        try {
            int code = Integer.parseInt(result.substring(0, 3));
            Vector v = new Vector();
            if (code == 210) {
                //sites avaliable
                StringTokenizer line = new StringTokenizer(result, LINEBREAK);
                line.nextToken(); // initial line w/ code
                String entry = line.nextToken();
                do {
                    ArgumentParser t = new ArgumentParser(entry);
                    CDDBServer server = new CDDBServer(t.nextArgument(),
                        t.nextArgument(), Integer.parseInt(t.nextArgument()),
                        t.nextArgument(), t.nextArgument(), t.nextArgument(),
                        t.remainder());
                    v.add(server);
                    entry = line.nextToken();
                } while (!entry.equals("."));
            } else {
                // probably a 401 code - no site information available
                // let's still return an empty array
            }
            return (CDDBServer[]) v.toArray(new CDDBServer[v.size()]);
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw new CDDBProtocolException("could not retrieve sites",
                queryTemplate, COMMAND_SITES, result, e);
        }
    }


    public String[] getCategories () throws IOException {
        String result = performCommand(COMMAND_CAT);
        try {
            Vector v = new Vector();
            StringTokenizer st = new StringTokenizer(result, LINEBREAK);
            st.nextToken(); // ignore the first result line
            while (st.hasMoreTokens()) {
                String item = st.nextToken();
                if (item.equals(".")) break;
                v.add(item);
            }
            return (String[]) v.toArray(new String[v.size()]);
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw new CDDBProtocolException("could not retrieve categories",
                queryTemplate, COMMAND_CAT, result, e);
        }
    }


    /**
    * tries to verify the discid embedded in the CDID object
    * by querying the service to recalculate the data
    * @return true only if the calculated discid by the service
    *         matches the id stored in teh CDID object
    */
    public boolean verifyDiscID (CDID cd) throws IOException {
        ArgumentParser p = new ArgumentParser(cd.getQueryString());
        p.nextArgument(); // embedded discid
        String command = COMMAND_DISCID +  p.remainder();
        String result = performCommand(command);
        try {
            int code = Integer.parseInt(result.substring(0, 3));
            if (code != 200) return false;
            p = new ArgumentParser(result);
            p.nextArgument(); // code
            p.nextArgument(); // "Disc"
            p.nextArgument(); // "ID"
            p.nextArgument(); // "is"
            if (cd.getDiscID().equals(p.nextArgument())) return true;
            return false;
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw new CDDBProtocolException("could not verify discid",
                queryTemplate, command, result, e);
        }
    }


    public CDDBRecord[] queryCD (CDID cd) throws IOException {
        String command = COMMAND_QUERY + cd.getQueryString();
        String result = performCommand(command);
        try {
            int code = Integer.parseInt(result.substring(0, 3));
            Vector v = new Vector();
            if (code == 200) {
                // found exact match
                ArgumentParser item = new ArgumentParser(result.substring(3));
                CDDBRecord r = new CDDBRecord(item.nextArgument(),
                    item.nextArgument(), item.remainder(), true);
                v.add(r);
            } else if ((code == 211) || (code == 210)) {
                // found inexact matches (211) or multiple excat matches (210)
                // code 210 is CDDB protocol level 4
                StringTokenizer line = new StringTokenizer(result, LINEBREAK);
                line.nextToken(); // ignore the first line
                while (line.hasMoreTokens()) {
                    String entry = line.nextToken();
                    if (entry.equals(".")) break;
                    ArgumentParser item = new ArgumentParser(entry);
                    CDDBRecord r = new CDDBRecord(item.nextArgument(),
                        item.nextArgument(), item.remainder(),
                        (code == 210)? true : false);
                    v.add(r);
                }
            } else {
                // nothing of value found; possible codes:
                // 202 no match found
                // 403 database entry is corrupt
                // 409 no handshake
            }
            return (CDDBRecord[]) v.toArray(new CDDBRecord[v.size()]);
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw new CDDBProtocolException("could not query cd",
                queryTemplate, command, result, e);
        }
    }


    /**
    * it is suggested that the given record was obtained through a call
    * to queryCD(), so that the record is known to exist.
    * @throws CDDBProtocolException if the record doesn't exist
    * @return a CDDBEntry instance
    * @see CDDBEntry
    */
    public CDInfo readCDInfo (CDDBRecord record) throws IOException {
        String command = COMMAND_READ + record.getCategory() + " " + record.getDiscID();
        String result = performCommand(command);
        try {
            int code = Integer.parseInt(result.substring(0, 3));
            if (code == 210) {
                BufferedReader in = new BufferedReader(new StringReader(result));
                in.readLine(); // ignore first line
                String fileContent = null;
                while (in.ready()) {
                    if (fileContent == null) {
                        fileContent = "";
                    } else {
                        fileContent += LINEBREAK;
                    }
                    String line = in.readLine();
                    if (line.length() > 255) throw new CDDBProtocolException(
                        "a line has been found to exceed 255 characters",
                        queryTemplate, command, result, null);
                    if (line.equals(".")) break;
                    fileContent += line;
                }
                return new CDDBEntry(record, fileContent);
            } else {
                String msg;
                switch (code) {
                    case 401: msg = "Specified CDDB entry not found."; break;
                    case 402: msg = "Server error.";                   break;
                    case 403: msg = "Database entry is corrupt.";      break;
                    case 409: msg = "No handshake.";                   break;
                    default:  msg = "Unknown error message";
                }
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            if (e instanceof IOException) throw (IOException) e;
            throw new CDDBProtocolException("could not read cd",
                queryTemplate, command, result, e);
        }
    }


    /** currently always throws OperationNotAllowed */
    public void writeCDInfo (CDInfo cdinfo) {
        throw new OperationNotAllowed(
            "method currently not implemented for FreeDB - sorry");
    }
}

package plugins.cddb;

import java.io.Serializable;


/**
* CDDBServer represents a server that can act as a mirror to a
* CDDB service. <p>
* This class has all elements as they can be retrieved from a CDDB
* service supporting protocol level 3-5.
* @see FreeDB#setServer(CDDBServer)
* @see FreeDB#listSites()
* @author Holger Antelmann
*/
public class CDDBServer implements Serializable
{
    /** the default server: freedb.freedb.org with http on port 80 */
    public static final CDDBServer DEFAULT_SERVER = new CDDBServer(
        "freedb.freedb.org", "http", 80, "/~cddb/cddb.cgi",
        "N000.00", "W000.00", "Random freedb server"
    );


    String site;
    String protocol;
    int    port;
    String address;
    String latitude;
    String longitude;
    String description;


    public CDDBServer (
        String site,
        String protocol,
        int    port,
        String address,
        String latitude,
        String longitude,
        String description)
    {
        this.site        = site;
        this.protocol    = protocol;
        this.port        = port;
        this.address     = address;
        this.latitude    = latitude;
        this.longitude   = longitude;
        this.description = description;
    }


    public String getSite        () { return site; }


    public String getProtocol    () { return protocol; }


    public int    getPort        () { return port; }


    public String getAddress     () { return address; }


    public String getLatitude    () { return latitude; }


    public String getLongitude   () { return longitude; }


    public String getDescription () { return description; }
}
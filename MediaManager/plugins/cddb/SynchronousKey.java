package plugins.cddb;


/**
* An interface to support very simple synchronous key encryption.
* The entire data to be encrypted needs to fit into memory, i.e.
* this scheme is not appropriate to encode/decode large files.
* @author Holger Antelmann
* @see Encoded
*/
public interface SynchronousKey
{
    /** decodes the given byte array */
    byte[] decode (byte[] encrypted);

    /** encodes the given byte array */
    byte[] encode (byte[] plainSource);

    /**
    * returns a signature that is used to identify the key that needs to be
    * known for both, encoding and decoding
    */
    String getKeySignature ();
}
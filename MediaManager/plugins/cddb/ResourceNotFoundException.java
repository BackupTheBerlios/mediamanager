package plugins.cddb;


/**
* Thrown to indicate that a specific resource (an image, a sound,
* etc.) needed to perform the requested operation was not found
* (while the resource was expected to be present with the distribution
* of this Antelmann.com framework).
* @see Settings#getResource(String)
* @author Holger Antelmann
* @since 4/6/2002
*/
public class ResourceNotFoundException extends RuntimeException
{
    String resource;


    public ResourceNotFoundException () {
        super();
    }


    public ResourceNotFoundException(String message) {
        super(message);
    }



    public ResourceNotFoundException(String message, String resource) {
        super(message);
        this.resource = resource;
    }


    public String getResourceNotFound () { return resource; }
}
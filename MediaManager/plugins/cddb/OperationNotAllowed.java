package plugins.cddb;


/**
* OperationNotAllowed is thrown when a certain operation
* called was not allowed by the CDDB service.
* @author Holger Antelmann
*/
public class OperationNotAllowed extends RuntimeException
{
    public OperationNotAllowed () {}


    public OperationNotAllowed (String message) {
        super(message);
    }
}

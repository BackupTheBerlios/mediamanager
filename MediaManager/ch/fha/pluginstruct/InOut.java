package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: InOut.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public interface InOut {

	public void message(String message);
	
	public int option(String message, String[] options);
	
	public void exception(PluginLogicException e);

}

package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: ContainerListener.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public interface ContainerListener {
	
	public void pluginAdded();
	
	public void pluginRemoved();

	public void pluginLoaded();

}

package ch.fha.mediamanager.gui.util;

/**
 * The listener interface for receiving <code>SortDecorator</code> events.
 * The class that is interested in processing an event implements
 * this interface, and the object created with that class is registered using
 * the SortDecorator's <code>addSortDecoratorListener</code> method. When the
 * data are resorted, that object's <code>sortingChanged</code> method is
 * invoked.
 * 
 * @author ia02vond
 * @version $Id: SortDecoratorListener.java,v 1.1 2004/06/23 18:24:03 ia02vond Exp $
 * @see SortDecorator
 */
public interface SortDecoratorListener {
	
	/**
	 * Invoked when the data are resorted 
	 */
	public void sortingChanged();

}
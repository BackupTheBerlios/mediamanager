package ch.fha.mediamanager.gui.util.dataInput;

/**
 * A class which implements the <code>DataInputListener</code>
 * interface can announce itself to a <code>DataInput</code>
 * object so that it will be notified when the field value
 * has been changed. If that happens, the <code>dataChanged</code>
 * method will be invoked.
 * 
 * @author ia02vond
 * @version $id$
 */
public interface DataInputListener {

	public void dataChanged(DataInput dataInput);
	
}

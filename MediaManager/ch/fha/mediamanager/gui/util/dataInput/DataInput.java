package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.fha.mediamanager.data.Field;

/**
 * @author ia02vond
 * @version $id$
 */
public interface DataInput {

	/**
	 * Returns an input swing component according to
	 * the <code>Field</code> type (int, varchar ...).
	 * The JComponent will probably be a <code>JTextField</code>,
	 * a <code>JTextArea</code>, a <code>JComboBox</code>
	 * or a <code>JCheckBox</code>.
	 * @return an input swing component
	 */
	public JComponent getInputComponent();

	/**
	 * Returns a <code>JLabel</code> with the <code>Field</code>
	 * name as label text.
	 * @return a <code>JLabel</code>
	 */
	public JLabel getLabel();
	
	/**
	 * Returns the <code>Field</code> object which is mounted
	 * to the input swing component.
	 * @return the <code>Field</code> object
	 */
	public Field getField();
	
	/**
	 * Returns a <code>Dimension</code> object which represents
	 * the grid size of the input swing component. This will
	 * be used for the <code>GridBagLayout</code>.
	 * @return the grid size
	 */
	public Dimension getGridSize();
	
	/**
	 * Sets the field value.
	 * @param value    the field value
	 */
	public void setValue(Object value);
	
	/**
	 * Adds a new <code>DataInputListener</code> which will be
	 * notified when the field value has been changed.
	 * @param listener    the <code>DataInputListener</code> to add
	 */
	public void addDataInputListener(DataInputListener listener);
	
	/**
	 * Removes the <code>DataInputListener</code>.
	 * @param listener    the <code>DataInputListener</code> which
	 *                    should be removed
	 */
	public void removeDataInputListener(DataInputListener listener);

}

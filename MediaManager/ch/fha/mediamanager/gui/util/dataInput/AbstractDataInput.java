package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.fha.mediamanager.data.Field;

/**
 * A first Implementation of the <code>DataInput</code>
 * interface.
 * 
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public abstract class AbstractDataInput implements DataInput {

	protected Dimension gridSize = new Dimension(1, 1);
	
	protected Field field;
	protected JLabel label;
	
	private LinkedList dataInputListener = new LinkedList();
	
	public AbstractDataInput(Field field) {
		this.field = field;
		this.label = new JLabel(field.getMetaField().getName());
	}
	
	public abstract JComponent getInputComponent();

	public JLabel getLabel() {
		return label;
	}

	public Field getField() {
		return field;
	}

	public Dimension getGridSize() {
		return gridSize;
	}

	public abstract void setValue(Object value);

	public void addDataInputListener(DataInputListener listener) {
		dataInputListener.add(listener);
	}

	public void removeDataInputListener(DataInputListener listener) {
		dataInputListener.remove(listener);
	}
	
	protected void fireDataInputChanged() {
		Iterator it = dataInputListener.iterator();
		while (it.hasNext()) {
			((DataInputListener)it.next()).dataChanged(this);
		}
	}
}

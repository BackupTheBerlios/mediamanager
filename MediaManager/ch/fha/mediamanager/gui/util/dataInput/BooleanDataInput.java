package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class BooleanDataInput extends AbstractDataInput
	implements ActionListener {
	
	private JCheckBox checkBox;
	
	public BooleanDataInput(Field field) {
		super(field);
		
		checkBox = new JCheckBox();
		
		setValue(field.getValue(), false);
		
		checkBox.addActionListener(this);
	}
	
	public JComponent getInputComponent() {
		return checkBox;
	}
	
	public void actionPerformed(ActionEvent event) {
		setValue(new Boolean(checkBox.isSelected()));
	}
	
	public void setValue(Object value) {
		setValue(value, true);
	}
	
	private void setValue(Object value, boolean fireEvent) {
		if (value instanceof Boolean) {
			field.setValue(value);
			checkBox.setSelected(((Boolean)field.getValue()).booleanValue());
			if (fireEvent) fireDataInputChanged();
		} else {
			throw new IllegalArgumentException("wrong value type");
		}
	}
}

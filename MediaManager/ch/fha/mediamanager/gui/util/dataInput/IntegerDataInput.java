package ch.fha.mediamanager.gui.util.dataInput;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class IntegerDataInput extends AbstractDataInput
	implements CaretListener {
	
	private JTextField textField;
	
	public IntegerDataInput(Field field) {
		super(field);

		textField = new JTextField();
		
		textField.setDocument(new DataInputDocument(
				textField, field.getMetaField().getLength(), true));
		
		setValue(field.getValue(), false);
		
		textField.setText(field.getValue().toString());
        
        textField.addCaretListener(this);
	}
	
	public void setValue(Object value) {
		setValue(value, true);
		textField.setText(value.toString());
	}
	
	public JComponent getInputComponent() {
		return textField;
	}
	
	private void setValue(Object value, boolean fireEvent) {
		Integer intval;
		
		if (value instanceof String) {
			try {
				int i = Integer.parseInt(textField.getText());
				intval = new Integer(i);
			} catch (NumberFormatException e) {
				intval = new Integer(0);
			}
		} else if (value instanceof Integer) {
			intval = (Integer)value;
		} else {
			intval = new Integer(0);
		}

		field.setTmpValue(intval);
		if (fireEvent) fireDataInputChanged();
	}

	public void caretUpdate(CaretEvent e) {
		setValue(textField.getText(), true);		
	}

}

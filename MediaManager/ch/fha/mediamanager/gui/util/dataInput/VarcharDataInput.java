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
public class VarcharDataInput extends AbstractDataInput
	implements CaretListener {

	protected JTextField textField;
	
	public VarcharDataInput(Field field) {
		super(field);
		
		textField = new JTextField();
		
		textField.setDocument(new DataInputDocument(
				textField, field.getMetaField().getLength(), false));
		
		setValue(field.getValue(), false);

		textField.setText(field.getValue().toString());
		
		textField.addCaretListener(this);
	}
	
	public JComponent getInputComponent() {
		return textField;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
		textField.setText(value.toString());
	}
	
	private void setValue(Object value, boolean fireEvent) {
		field.setValue(value);
        ch.fha.mediamanager.data.DataBus.logger.info(value.toString());
		if (fireEvent) fireDataInputChanged();
	}

	public void caretUpdate(CaretEvent e) {
		setValue(textField.getText(), true);
	}
	
}

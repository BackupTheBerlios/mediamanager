package ch.fha.mediamanager.gui.util.dataInput;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class VarcharDataInput extends AbstractDataInput
	implements DocumentListener {

	protected JTextField textField;
	
	public VarcharDataInput(Field field) {
		super(field);
		
		textField = new JTextField();
		
		textField.setDocument(new DataInputDocument(
				textField, field.getMetaField().getLength(), false));
		
		setValue(field.getValue(), false);
		
		textField.getDocument().addDocumentListener(this);
	}
	
	public JComponent getInputComponent() {
		return textField;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
	}
	
	private void setValue(Object value, boolean fireEvent) {
		field.setValue(value);
        ch.fha.mediamanager.data.DataBus.logger.info(value.toString());
		textField.setText(value.toString());
		if (fireEvent) fireDataInputChanged();
	}
	
	public void changedUpdate(DocumentEvent e) {
		setValue(textField.getText());
	}

	public void insertUpdate(DocumentEvent e) {}
    
	public void removeUpdate(DocumentEvent e) {}

}

package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class TextDataInput extends AbstractDataInput
	implements DocumentListener {

	private JTextArea textArea;
	
	public TextDataInput(Field field) {
		super(field);
		
		textArea = new JTextArea();

		setValue(field.getValue(), false);
		
		textArea.getDocument().addDocumentListener(this);
		
		gridSize = new Dimension(3, 1);
	}
	
	public JComponent getInputComponent() {
		return textArea;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
	}
	
	public void setValue(Object value, boolean fireEvent) {
		field.setValue(value);
		textArea.setText(value.toString());
		if (fireEvent) fireDataInputChanged();
	}

	public void changedUpdate(DocumentEvent e) {
		setValue(textArea.getText());
	}

	public void insertUpdate(DocumentEvent e) {}
	public void removeUpdate(DocumentEvent e) {}	
}

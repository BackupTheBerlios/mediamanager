package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class TextDataInput extends AbstractDataInput
	implements CaretListener {

	private JTextArea textArea;
	
	public TextDataInput(Field field) {
		super(field);
		
		textArea = new JTextArea();

		setValue(field.getValue(), false);
		
		textArea.setText(field.getValue().toString());
		
		textArea.addCaretListener(this);
		
		gridSize = new Dimension(1, 3);
	}
	
	public JComponent getInputComponent() {
		return textArea;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
		textArea.setText(value.toString());
	}
	
	public void setValue(Object value, boolean fireEvent) {
		field.setValue(value);
        ch.fha.mediamanager.data.DataBus.logger.info(value.toString());
		if (fireEvent) fireDataInputChanged();
	}

	public void caretUpdate(CaretEvent e) {
		setValue(textArea.getText(), true);
	}	
}

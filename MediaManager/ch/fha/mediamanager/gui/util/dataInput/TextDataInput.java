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
 * @version $Id: TextDataInput.java,v 1.6 2004/06/28 19:23:20 crac Exp $
 */
public class TextDataInput extends AbstractDataInput
	implements CaretListener {

	private JTextArea textArea;
    private javax.swing.JScrollPane scroller;
	
	public TextDataInput(Field field) {
		super(field);
		
		textArea = new JTextArea();
        textArea.setRows(6);
        textArea.setLineWrap(true);

		setValue(field.getValue(), false);
		
		textArea.setText(field.getValue().toString());
		
		textArea.addCaretListener(this);
        
        scroller = new javax.swing.JScrollPane(textArea);
        scroller.setPreferredSize(
            textArea.getPreferredScrollableViewportSize()
        );
		
		gridSize = new Dimension(1, 3);
	}
	
	public JComponent getInputComponent() {
		return scroller;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
		textArea.setText(value.toString());
	}
	
	public void setValue(Object value, boolean fireEvent) {
		field.setTmpValue(value);
		if (fireEvent) fireDataInputChanged();
	}

	public void caretUpdate(CaretEvent e) {
		setValue(textArea.getText(), true);
	}	
}

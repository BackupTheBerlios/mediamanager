package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond
 * @version $id$
 */
public class ListDataInput extends AbstractDataInput
	implements ActionListener {

	private JComboBox comboBox;
	
	public ListDataInput(Field field) {
		super(field);
		
		comboBox = new JComboBox();
		
		setValue(field.getValue(), false);
		
		comboBox.addActionListener(this);
	}
	
	public JComponent getInputComponent() {
		return comboBox;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
	}

	private void setValue(Object value, boolean fireEvent) {
		// TODO Auto-generated method stub	
		if (fireEvent) fireDataInputChanged();
	}
	
	public void actionPerformed(ActionEvent event) {
		// TODO
	}
}

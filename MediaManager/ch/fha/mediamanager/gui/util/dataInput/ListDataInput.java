package ch.fha.mediamanager.gui.util.dataInput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import ch.fha.mediamanager.data.Field;

/**
 * @see DataInput
 * 
 * @author ia02vond, crac
 * @version $Id: ListDataInput.java,v 1.4 2004/06/29 11:53:01 crac Exp $
 */
public class ListDataInput extends AbstractDataInput
	implements ActionListener {

	private JComboBox comboBox;
	
	public ListDataInput(Field field) {
		super(field);
		
		comboBox = new JComboBox();
        
		setValue(field.getValue(), false);
        
        String[] str = 
            (String[]) field.getMetaField().getDefaultValue();
		
        if (! field.getValue().equals("")) {
            comboBox.addItem(field.getValue());
        }
        for (int i = 0; i < str.length; i++) {
            if ((field.getValue().equals("")) || (! str[i].equals(field.getValue()))) {
                comboBox.addItem(str[i]);
            }
        }
        
		comboBox.addActionListener(this);
	}
	
	public JComponent getInputComponent() {
		return comboBox;
	}
	
	public void setValue(Object value) {
		setValue(value, true);
	}

	private void setValue(Object value, boolean fireEvent) {
		field.setTmpValue(value);	
		if (fireEvent) fireDataInputChanged();
	}
	
	public void actionPerformed(ActionEvent event) {
		setValue(comboBox.getSelectedItem(), true);
	}
}

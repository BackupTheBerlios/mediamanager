package ch.fha.mediamanager.gui.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.util.dataInput.DataInput;
import ch.fha.mediamanager.workflow.Workflow;

/**
 * @author ia02vond
 * @version $Id: InputFormular.java,v 1.2 2004/06/23 16:54:35 ia02vond Exp $
 */
public class InputFormular extends JPanel
	implements ActionListener {
	
	private DataElement element;
	private Workflow workflow;
	private JButton okB, cancelB;
	
	public InputFormular(DataElement element, Workflow workflow) {
		this.element = element;
		this.workflow = workflow;
		
		initInputFields();
		
		MainFrame.getInstance().loadMainPanel(this);
	}
	
	private void initInputFields() {
		Field[] field = element.getFields();
		
		JPanel panel1 = new JPanel(new GridLayout(field.length, 2));
		
		for (int i=0; i<field.length; i++) {
			DataInput input = DataInputFactory.create(field[i]);
			if (input != null) {
				panel1.add(input.getLabel());
				panel1.add(input.getInputComponent());
			}
		}
		
		this.setLayout(new BorderLayout());
		this.add(panel1, BorderLayout.NORTH);
		
		okB = new JButton("Ok");
		okB.addActionListener(this);
		cancelB = new JButton("Abbrechen");
		cancelB.addActionListener(this);
		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel2.add(cancelB);
		panel2.add(okB);
		
		this.add(panel2, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == okB) {
			workflow.go(this, true);
		} else if (source == cancelB) {
			workflow.go(this, false);
		}
	}
	
}

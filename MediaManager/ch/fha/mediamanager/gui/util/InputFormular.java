package ch.fha.mediamanager.gui.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.components.BarBorder;
import ch.fha.mediamanager.gui.util.dataInput.DataInput;
import ch.fha.mediamanager.workflow.Workflow;

/**
 * @author ia02vond
 * @version $Id: InputFormular.java,v 1.5 2004/06/27 13:51:52 radisli Exp $
 */
public class InputFormular extends JPanel implements
	ActionListener
{
	private DataElement element;
	private Workflow workflow;
	private String workflowName;
	private JButton okB, cancelB;

	private static final double labelWidth = 1.0;
	private static final double inputWidth = 3.0;
	
	public InputFormular(DataElement element, Workflow workflow, String workflowName) {
		this.element = element;
		this.workflow = workflow;
		this.workflowName = workflowName;
		
		this.setBorder(new BarBorder(element.getMetaEntity().getName() + " > " + workflowName));
		initInputFields();
		
		MainFrame.getInstance().loadCoverPanel(this);
	}
	
	private void initInputFields() {
		this.setLayout(new BorderLayout());
		Field[] field = element.getFields();
		
		JPanel gridBagHolderPanel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		gridBagHolderPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 5, 0);
		c.anchor = GridBagConstraints.NORTH;

		JLabel jl;
		JComponent cp;
		
		for (int i = 0; i < field.length; i++) {
			DataInput input = DataInputFactory.create(field[i]);
			if(input != null) {
				c.weightx = labelWidth;
				c.gridwidth = 1;
				jl = input.getLabel();
				gridbag.setConstraints(jl, c);
				gridBagHolderPanel.add(jl);

				c.weightx = inputWidth;
				c.gridwidth = GridBagConstraints.REMAINDER;
				cp = input.getInputComponent();
				gridbag.setConstraints(cp, c);
				gridBagHolderPanel.add(cp);
			}
		}
		
		c.weightx = labelWidth + inputWidth;
		c.weighty = 1.0;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(MainFrame.ep, c);
		gridBagHolderPanel.add(MainFrame.ep);
	
		JScrollPane sp = new JScrollPane(gridBagHolderPanel);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.add(sp, BorderLayout.CENTER);
		
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
		MainFrame.getInstance().removeCoverPanel();
	}
	
}

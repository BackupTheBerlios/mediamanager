package ch.fha.mediamanager.gui.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import ch.fha.mediamanager.data.DataBus;
import ch.fha.mediamanager.data.AbstractRepository;
import ch.fha.mediamanager.gui.components.BarBorder;

/**
 * @author ia02vond
 * @version $id$
 */
public class RepositoryConf extends JPanel
	implements ActionListener {
	
	private AbstractRepository[] repositories;
	private JPanel currentRepPanel = null;
	private JComboBox comboBox;
	
	public RepositoryConf(AbstractRepository[] repositories) {
		this.repositories = repositories;
		initSwing();
	}
	
	private void initSwing() {
		comboBox = new JComboBox();
		comboBox.addActionListener(this);
		for (int i=0; i<repositories.length; i++) {
			comboBox.addItem(repositories[i].getName());
		}
		
		this.setBorder(new BarBorder("Repository Einstellungen"));
		this.setLayout(new BorderLayout());
		this.add(comboBox, BorderLayout.NORTH);
		
		if (DataBus.getRepository() != null) {
			this.currentRepPanel = DataBus.getRepository().getConfPanel();
		} else {
			this.currentRepPanel = new JPanel();
		}
		this.add(currentRepPanel, BorderLayout.CENTER);
	}
		
	public void actionPerformed(ActionEvent event) {
		if (currentRepPanel != null) {
			remove(currentRepPanel);
		}
		int index = comboBox.getSelectedIndex();
		if (index > -1) {
			currentRepPanel = repositories[index].getConfPanel();
			currentRepPanel.repaint();
		} else {
			currentRepPanel = new JPanel();
		}
		add(currentRepPanel, BorderLayout.CENTER);
	}
	
	private void setCurrentRepository(int index) {
		if (index > -1) {
			currentRepPanel = repositories[index].getConfPanel();
		} else {
			currentRepPanel = new JPanel();
		}
		add(currentRepPanel, BorderLayout.CENTER);
	}
}

// $Id: MainView.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The main view of the <em>Mediamanager</em> application.
 *
 * @author Roman Rietmann
 */
public class MainView extends JPanel
	implements ActionListener
{
	private JButton exit = new JButton("Exit");
	private JTabbedPane tabbedPane;
	private MainTab mainTab;
	private TestTab testTab;

	public MainView() {
		this.setLayout(new BorderLayout());
		
		JPanel panelHolder = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(1, 1));

		buttonPanel.add(exit);
		exit.addActionListener(this);
//		panelHolder.add(buttonPanel, BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainTab = new MainTab();
		testTab = new TestTab();
		tabbedPane.addTab("Lala", testTab);
		tabbedPane.addTab("Main", mainTab);
		panelHolder.add(tabbedPane, BorderLayout.CENTER);
		
		this.add(panelHolder, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		Object source = e.getSource();
		if(source == exit) {
			Window w = (Window)getTopLevelAncestor();
			w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
		} else {
			System.err.println("TODO: implement functionality for " + source);
		}
	}
}

//$Id: TestTab.java,v 1.1 2004/05/27 13:38:16 radisli Exp $
package ch.fha.mediamanager.gui.components;
import javax.swing.*;
import java.awt.*;

public class TestTab extends JPanel {
	public TestTab() {
		this.setLayout(new GridLayout(3, 2));
		
		this.add(new JLabel("Field 1:"));
		this.add(new JTextArea());
		this.add(new JLabel("Field 2:"));
		this.add(new JTextArea());
		this.add(new JLabel("Field 3:"));
		this.add(new JTextArea());
	}
}

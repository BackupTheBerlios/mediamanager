package ch.fha.mediamanager.gui;
import javax.swing.*;
import java.awt.*;

public class MainTab extends JPanel {
	public MainTab() {
		this.setLayout(new BorderLayout());
		
		this.add(new JButton("North"), BorderLayout.NORTH);
		this.add(new JButton("South"), BorderLayout.SOUTH);
		this.add(new JButton("East"), BorderLayout.EAST);
		this.add(new JButton("West"), BorderLayout.WEST);
		this.add(new JButton("Center"), BorderLayout.CENTER);
	}
}

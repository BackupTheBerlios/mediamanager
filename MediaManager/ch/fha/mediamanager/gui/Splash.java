package ch.fha.mediamanager.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * @author ia02vond
 * @version $Id: Splash.java,v 1.3 2004/06/20 12:02:16 crac Exp $
 */
public class Splash extends JWindow {

	private final static String IMAGE_PATH = "images" + 
        java.io.File.separator + "splash.jpg";
	
	private static int windowWidth  = 350;
	private static int windowHeight = 250;
	
	private final static int LEFT_INSET = 10;
	private final static int RIGHT_INSET = 10;
	private final static int PROCESS_BAR_Y = 180;
	private final static int PROCESS_BAR_THICKNESS = 4;
	
	private final static Color BORDER_COLOR  = Color.DARK_GRAY;
	
	private final static Color PROCESS_BAR_COLOR = Color.BLUE;
	
	private final static Font PROCESS_TEXT_FONT = new Font(
			"SansSerif", Font.BOLD, 14);
	
	private final static int PROCESS_TEXT_Y = 170;
	
	private final static Color PROCESS_TEXT_FOREGROUND_COLOR = Color.WHITE;
	private final static Color PROCESS_TEXT_BACKGROUND_COLOR = Color.BLACK;
	
	private double process;
	private String processText;
	
	public static void main(String[] args) {
		Splash title = new Splash();
		
		String[] text = {
				"erster Process",
				"zweiter Process",
				"zweitletzter Process",
				"letzter Process",
				"noch einer"
		};
		double process = 0;
		for (int i=0; i<text.length; i++) {
			title.setProcess(process, text[i]);
			process += 0.25;
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}	
		
		System.exit(0);
	}
	
	public Splash() {
		process = 0;
		processText = "";
		
		// background image
		Color bg = new Color(194, 242, 242);
		JPanel aboutHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel aboutLabel = new JLabel();
		ImageIcon aboutImage = new ImageIcon(IMAGE_PATH);
		Dimension d = new Dimension(aboutImage.getIconWidth(), aboutImage.getIconHeight());
		aboutHolder.setPreferredSize(d);
		aboutLabel.setIcon(aboutImage);
		aboutHolder.add(aboutLabel);
		aboutHolder.setBackground(bg);
		getContentPane().add(aboutHolder, BorderLayout.CENTER);
		
		// window dimension
		pack();
		
		windowWidth  = getSize().width;
		windowHeight = getSize().height;
		
		// set location
		Dimension screen = getToolkit().getScreenSize();
		int x = (screen.width  - windowWidth)  / 2;
		int y = (screen.height - windowHeight) / 2;
		setLocation(x, y);
		
		setVisible(true);
	}
	
	public void setProcess(double process, String processText) {
		this.process = process;
		this.processText = processText;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		// process bar
		int width = (int)((windowWidth - (LEFT_INSET + RIGHT_INSET)) * process);
		g.setColor(PROCESS_BAR_COLOR);
		g.fillRect(LEFT_INSET, PROCESS_BAR_Y+1, width, PROCESS_BAR_THICKNESS-1);
		
		// process bar border
		g.setColor(BORDER_COLOR);
		g.drawRect(LEFT_INSET, PROCESS_BAR_Y, windowWidth - (LEFT_INSET + RIGHT_INSET), PROCESS_BAR_THICKNESS);
		
		// process text
		if (processText != null) {
			
			// background
			g.setColor(PROCESS_TEXT_BACKGROUND_COLOR);
			g.drawString(processText, LEFT_INSET + 1, PROCESS_TEXT_Y + 1);
			
			// foreground
			g.setColor(PROCESS_TEXT_FOREGROUND_COLOR);
			g.drawString(processText, LEFT_INSET, PROCESS_TEXT_Y);
		}
	}
}

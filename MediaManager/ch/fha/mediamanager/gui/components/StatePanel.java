//$Id: StatePanel.java,v 1.1 2004/06/05 13:49:35 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;
import ch.fha.mediamanager.gui.*;

public class StatePanel extends JPanel {
	private static final Font statusFont = new Font("arial", Font.BOLD, 12);
	private static boolean showStatePermanent;
	private JLabel statusText = new JLabel();
	
	
	public StatePanel() {
		Dimension d = new Dimension(17, 17);
		this.setPreferredSize(d);
		
		setLayout(new BorderLayout());
		setBorder(MainFrame.b);
		
		statusText.setFont(statusFont);
		add(statusText, BorderLayout.WEST);

		JPanel connection = new Connection();
		connection.setPreferredSize(new Dimension(14, 15));
		add(connection, BorderLayout.EAST);
	}
	
	public void setStatus(String s, boolean timeOut) {
		showStatePermanent = true;
		statusText.setText(s);
		
		if(timeOut) {
			showStatePermanent = false;
			final int time = 1000;
			final long tstmp = System.currentTimeMillis();
			new Thread() {
		        public void run() {
		        	try {
						Thread.sleep(time);
					} catch(InterruptedException e) {
						System.out.println(e);
					}
					if(!showStatePermanent && System.currentTimeMillis() >= tstmp + time) {
						statusText.setText("");
					}
		        }
			}.start();
		}
	}
	
	private class Connection extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, 12, 12);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 12, 12);
		}
	}
}
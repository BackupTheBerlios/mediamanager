//$Id: StatePanel.java,v 1.3 2004/06/25 14:18:24 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import javax.swing.*;
import ch.fha.mediamanager.gui.*;

/**
 * Statuspanel
 * 
 * Shows connection status and statustext on the buttom of the GUI
 *
 * @author Roman Rietmann
 */
public class StatePanel extends JPanel {
	// Default color for connected state
	private final static Color connected = Color.GREEN;
	// Default color for disconnected state
	private final static Color disconnected = Color.RED;
	// Font type for the status text
	private static final Font statusFont = new Font("arial", Font.BOLD, 12);
	
	private static boolean showStatePermanent;
	private JLabel statusTextLabel = new JLabel();
	private Connection connection;

	/**
	 * Constructor which creates the statspanel
	 */
	public StatePanel() {
		Dimension d = new Dimension(17, 17);
		this.setPreferredSize(d);
		
		setLayout(new BorderLayout());
		setBorder(MainFrame.b);
		
		statusTextLabel.setFont(statusFont);
		add(statusTextLabel, BorderLayout.WEST);

		connection = new Connection();
		connection.setPreferredSize(new Dimension(14, 15));
		add(connection, BorderLayout.EAST);
	}

	/**
	 * Method to set the connection status
	 * - true  --> connected
	 * - false --> disconnected 
	 * 
	 * @param isConnected sets the connection status to true / false
	 */
	public void setConnectionStatus(boolean isConnected) {
		if(isConnected) {
			connection.setColor(connected);
		} else {
			connection.setColor(disconnected);
		}
	}
	
	/**
	 * Returns the connection status from the <code>StatePanel</code>
	 * 
	 * @return connection state
	 */
	public boolean getConnectionStatus() {
		return connection.getColor().equals(connected);
	}
	
	/**
	 * Shows the <code>statusText</code> in the <code>statusTextLabel</code>
	 * The <code>timeOut</code> parameter defines whether the text stays
	 * (false) or disappears after the timeout 1 sec (true) 
	 * 
	 * @param statusText is the text which should be displayed
	 * @param timeOut defines whether the text stays or disapears automaticly
	 */
	public void setStatusText(String statusText, boolean timeOut) {
		showStatePermanent = true;
		statusTextLabel.setText(statusText);
		
		if(timeOut) {
			showStatePermanent = false;
			final int time = 2000;
			final long tstmp = System.currentTimeMillis();
			new Thread() {
		        public void run() {
		        	try {
						Thread.sleep(time);
					} catch(InterruptedException e) {
						System.out.println(e);
					}
					if(!showStatePermanent && System.currentTimeMillis() >= tstmp + time) {
						statusTextLabel.setText("");
					}
		        }
			}.start();
		}
	}
	
	/**
	 * A symbol which shows the connection state
	 */
	public class Connection extends JPanel {
		private Color col = disconnected;
		
		/**
		 * Sets the color of the rectangle
		 * 
		 * @param col color of the rectangle
		 */
		public void setColor(Color col) {
			this.col = col;
			repaint();
		}
		
		/**
		 * Gets the color of the rectangle
		 * 
		 * @return color of the rectangle
		 */
		public Color getColor() {
			return col;
		}
		
		/**
		 * Paint method which draws the rectangle
		 */
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(col);
			g.fillRect(0, 0, 12, 12);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 12, 12);
		}
	}
}
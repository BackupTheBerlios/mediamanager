//$Id: MainTab.java,v 1.5 2004/06/17 12:35:04 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;

/**
 * @author Roman Rietmann
 *
 * The panel which will be shown first contains connect-, config- and exit
 * button.
 */
public class MainTab extends JPanel {
	// Tool Tips which is displayed in the <code>StatePanel</code> too
	private final static String connectStr = "Zum Server verbinden";
	private final static String disconnectStr = "Verbindung trennen";
	// Images for connectbutton
	private final static ImageIcon connectImage = new ImageIcon("images/connect.gif");
	private final static ImageIcon disconnectImage = new ImageIcon("images/disconnect.gif");

	private NavButton topButton;
	private JLabel topLabel;
	
	/**
	 * Constructor which creates the <code>MainTab</code>
	 */
	public MainTab() {
		final MainFrame mainWindow = MainFrame.getInstance();
		setLayout(new BorderLayout());
		ActionListener mainActionListener = mainWindow.getMainActionListener();
		
		JPanel basePanel = new JPanel(new GridLayout(3, 1));
		JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middleButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		topButton = new NavButton(connectImage,
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(mainWindow.getConnectionStatus()) {
						mainWindow.setConnectionStatus(false);
						topButton.setIcon(connectImage);
						topButton.setToolTipText(connectStr);
						mainWindow.setStatusText("Wird getrennt ...", true);
						topLabel.setText("Verbinden");
					} else {
						mainWindow.setConnectionStatus(true);
						topButton.setIcon(disconnectImage);
						topButton.setToolTipText(disconnectStr);
						mainWindow.setStatusText("Wird verbunden ...", true);
						topLabel.setText("Trennen");
					}
				}
			} ,
			"Zum Server verbinden");
		topButtonPanel.add(topButton);
		topLabel = new RenderedJLabel("Verbinden");
		topLabel.setFont(MainFrame.titleFont);
		topButtonPanel.add(topLabel);
		basePanel.add(topButtonPanel);
		
		ImageIcon prefsImage = new ImageIcon("images/prefs.gif");
		NavButton middleButton = new NavButton(prefsImage,
				mainActionListener, "Haupteinstellungen ver\u00e4ndern");
		middleButton.setActionCommand("config");
		middleButtonPanel.add(middleButton);
		JLabel middleLabel = new RenderedJLabel("Einstellungen");
		middleLabel.setFont(MainFrame.titleFont);
		middleButtonPanel.add(middleLabel);
		basePanel.add(middleButtonPanel);

		ImageIcon exitImage = new ImageIcon("images/exit.gif");
		NavButton bottomButton = new NavButton(exitImage,
				mainActionListener, "Mediamanager beenden");
		bottomButton.setActionCommand("exit");
		bottomButtonPanel.add(bottomButton);
		JLabel bottomLabel = new RenderedJLabel("Beenden");
		bottomLabel.setFont(MainFrame.titleFont);
		bottomButtonPanel.add(bottomLabel);
		basePanel.add(bottomButtonPanel);

		add(basePanel, BorderLayout.WEST);
	}
	
	/**
	 * Class for a picture button
	 */
	private class NavButton extends JButton implements
		MouseListener
	{
		// Button dimension
		private final Dimension d = new Dimension(52, 52);
		
		/**
		 * Creates a button contains image <code>img</code>, shows
		 * <code>toolTip</code> and uses <code>ActionListener</code>
		 * <code>al</code>
		 * 
		 * @param img is the picture which will be displayed on the button
		 * @param al is the button specific actionlistener
		 * @param toolTip is the toolTip used by the button
		 */
		NavButton(ImageIcon img, ActionListener al, String toolTip) {
			setIcon(img);
			setToolTipText(toolTip);
			setMargin(new Insets(0, 0, 0, 0));
			setPreferredSize(d);
			addActionListener(al);
			addMouseListener(this);
		}

		public void mouseEntered(MouseEvent arg0) {
			MainFrame.getInstance().setStatusText(this.getToolTipText(), false);
		}

		public void mouseExited(MouseEvent arg0) {
			MainFrame.getInstance().removeStatusText();
		}
		
		public void mouseClicked(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	
	/**
	 * Simple Extension of <code>JLabel</code> which
	 * sets new rendering hints for better font displaying
	 * before invoking the <code>super.paint()</code>
	 * method.
	 */
	private class RenderedJLabel extends JLabel {
		public RenderedJLabel(String text) {
			super(text);
		}
		public void paint(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING, 
	            RenderingHints.VALUE_ANTIALIAS_ON);
	
	        super.paint(g2d);
	    }
	}
}
//$Id: MainTab.java,v 1.12 2004/06/29 13:03:17 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

/**
 * @author Roman Rietmann
 *
 * The panel which will be shown first contains connect-, config- and exit
 * button.
 */
public class MainTab extends JPanel implements
	KeyPointListener
{
	// Tool Tips which is displayed in the <code>StatePanel</code> too
	private final static String connectStr = "Zum Server verbinden";
	private final static String disconnectStr = "Verbindung trennen";
	// Images for connectbutton
	private final static ImageIcon connectImage = new ImageIcon("images/connect.gif");
	private final static ImageIcon connectingImage = new ImageIcon("images/connecting.gif");
	private final static ImageIcon disconnectImage = new ImageIcon("images/disconnect.gif");

	private NavButton topButton;
	private JLabel topLabel;
	
	/**
	 * Constructor which creates the <code>MainTab</code>
	 */
	public MainTab() {
		final MainFrame mainWindow = MainFrame.getInstance();
		setLayout(new BorderLayout());
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setBorder(new BarBorder("Hauptfenster"));

		JPanel basePanel = new JPanel(new GridLayout(3, 1));
		JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel middleButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		topButton = new NavButton(connectImage,
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(mainWindow.getConnectionStatus()) {
						mainWindow.disconnect();
					} else {
						mainWindow.connect();
					}
				}
			} ,
			"Zum Server verbinden");
		topButtonPanel.add(topButton);
		topLabel = new RenderedJLabel("Verbinden");
		topLabel.setFont(MainFrame.titleFont);
		topButtonPanel.add(topLabel);
		basePanel.add(topButtonPanel);
		
		ImageIcon prefsImage = new ImageIcon("images/config.gif");
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

		mainActionListener.addActionListener(this);
		add(basePanel, BorderLayout.WEST);
	}

	/**
	 * Method is called when a preregistered key-point is reached.
	 *  
	 * @param e is a <code>KeyPointEvent</code> which contains:
	 *          - specific <code>KeyPointEvent</code> (e.g. WINDOW_EXIT)
	 */
	public void runAction(KeyPointEvent e) {
		int kpe = e.getKeyPointEvent();
		if(kpe == KeyPointEvent.PRE_CONNECT || kpe == KeyPointEvent.PRE_DISCONNECT) {
			topButton.setIcon(connectingImage);
		} else if(kpe == KeyPointEvent.POST_CONNECT) {
			topButton.setIcon(disconnectImage);
			topButton.setToolTipText(disconnectStr);
			topLabel.setText("Trennen");
		} else if(kpe == KeyPointEvent.POST_DISCONNECT) {
			topButton.setIcon(connectImage);
			topButton.setToolTipText(connectStr);
			topLabel.setText("Verbinden");
		}
	}

	/**
	 * Class for a picture button
	 */
	private class NavButton extends JButton implements
		MouseListener
	{
		// Button dimension
		private final Dimension d = new Dimension(66, 66);
		
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
//$Id: StandartToolBar.java,v 1.3 2004/06/27 20:31:48 radisli Exp $
package ch.fha.mediamanager.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.framework.*;

public class StandartToolBar extends AbstractToolBar implements
	KeyPointListener
{
	private ToolBarButton connect;
	// Tool Tips which is displayed in the <code>StatePanel</code> too
	private final static String connectStr = "Zum Server verbinden";
	private final static String disconnectStr = "Verbindung trennen";
	// Images for connectbutton
	private final static ImageIcon connectImage = new ImageIcon("images/iconnect.gif");
	private final static ImageIcon disconnectImage = new ImageIcon("images/idisconnect.gif");


	public StandartToolBar() {
		super();
		final MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		
		connect = addToolbarElement("images/iconnect.gif",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(mainWindow.getConnectionStatus()) {
							mainWindow.disconnect();
						} else {
							mainWindow.connect();
						}
					}
				},
				null,
				connectStr);
		addToolbarElement("images/iconfig.gif",
				mainActionListener,
				"config",
				"Einstellungen");
		
		this.addSeparator();
		
		addToolbarElement("images/iabout.gif",
				mainActionListener,
				"about",
				"About");
		
		mainActionListener.addActionListener(this);
	}

	public void runAction(KeyPointEvent e) {
		if(e.getKeyPointEvent() == KeyPointEvent.CONNECTING) {
			connect.setIcon(disconnectImage);
			connect.setToolTip(disconnectStr);
		} else if(e.getKeyPointEvent() == KeyPointEvent.DISCONNECTING) {
			connect.setIcon(connectImage);
			connect.setToolTip(connectStr);
		}		
	}
}

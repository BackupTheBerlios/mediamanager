package ch.fha.mediamanager.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.framework.*;

/**
 * 
 * @author radisli
 * @version $Id: StandartToolBar.java,v 1.8 2004/06/30 18:54:56 crac Exp $
 */
public class StandartToolBar extends AbstractToolBar 
    implements KeyPointListener {
    
	private ToolBarButton connect;
	// Images for connectbutton
	private final static ImageIcon connectImage = new ImageIcon("images/iconnect.gif");
	private final static ImageIcon connectingImage = new ImageIcon("images/iconnecting.gif");
	private final static ImageIcon disconnectImage = new ImageIcon("images/idisconnect.gif");


    /**
     * 
     *
     */
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
				MainFrame.connectStr);
		addToolbarElement("images/iconfig.gif",
				mainActionListener,
				"config",
				"Einstellungen");
		
		this.addSeparator();
		
		addToolbarElement("images/iabout.gif",
				mainActionListener,
				"about",
				"About");
		
		this.addSeparator();
		
		addToolbarElement("images/iexit.gif",
				mainActionListener,
				"exit",
				"Beenden");

		mainActionListener.addActionListener(this);
	}

    /**
     * 
     * @param e
     */
	public void runAction(KeyPointEvent e) {
		int kpe = e.getKeyPointEvent();
		if (kpe == KeyPointEvent.PRE_CONNECT || kpe == KeyPointEvent.PRE_DISCONNECT) {
			connect.setIcon(connectingImage);
		} else if (kpe == KeyPointEvent.POST_CONNECT) {
			connect.setIcon(disconnectImage);
			connect.setToolTip(MainFrame.disconnectStr);
		} else if (kpe == KeyPointEvent.POST_DISCONNECT) {
			connect.setIcon(connectImage);
			connect.setToolTip(MainFrame.connectStr);
		}		
	}
}

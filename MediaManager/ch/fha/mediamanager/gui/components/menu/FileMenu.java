//$Id: FileMenu.java,v 1.5 2004/06/29 14:07:17 radisli Exp $
package ch.fha.mediamanager.gui.components.menu;

import javax.swing.*;
import java.awt.event.*;

import ch.fha.mediamanager.gui.*;
import ch.fha.mediamanager.gui.framework.*;

public class FileMenu extends JMenu implements
	KeyPointListener
{
	private JMenuItem connectState, prefs;
	
	public FileMenu() {
		final MainFrame mainWindow = MainFrame.getInstance();
		ActionHandler mainActionListener = mainWindow.getMainActionListener();
		setText("File");
		
		connectState = new JMenuItem(MainFrame.connectStrShort);
		connect();
		connectState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mainWindow.getConnectionStatus()) {
					mainWindow.disconnect();
				} else {
					mainWindow.connect();
				}
			}
		});
		add(connectState);

		prefs = new JMenuItem("Einstellungen");
		prefs.setActionCommand("config");
		prefs.setMnemonic(KeyEvent.VK_E);
		prefs.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, KeyEvent.CTRL_MASK));
		prefs.addActionListener(mainActionListener);
		add(prefs);

		addSeparator();

		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		exit.addActionListener(mainActionListener);
		add(exit);
		
		mainActionListener.addActionListener(this);
	}
	
	private void connect() {
		connectState.setText(MainFrame.connectStrShort);
		connectState.setMnemonic(KeyEvent.VK_V);
		connectState.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_V, KeyEvent.CTRL_MASK));
	}

	/**
	 * Method is called when a preregistered key-point is reached.
	 *  
	 * @param e is a <code>KeyPointEvent</code> which contains:
	 *          - specific <code>KeyPointEvent</code> (e.g. WINDOW_EXIT)
	 */
	public void runAction(KeyPointEvent e) {
		if(e.getKeyPointEvent() == KeyPointEvent.POST_CONNECT) {
			connectState.setText(MainFrame.disconnectStrShort);
			connectState.setMnemonic(KeyEvent.VK_T);
			connectState.setAccelerator(KeyStroke.getKeyStroke(
	                KeyEvent.VK_T, KeyEvent.CTRL_MASK));
		} else if(e.getKeyPointEvent() == KeyPointEvent.POST_DISCONNECT) {
			connect();
		}
	}
}

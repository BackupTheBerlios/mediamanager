package ch.fha.mediamanager.gui.util;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ch.fha.mediamanager.gui.MainFrame;
import ch.fha.mediamanager.gui.components.BarBorder;
import ch.fha.mediamanager.gui.framework.KeyPointEvent;
import ch.fha.mediamanager.gui.framework.KeyPointListener;
import ch.fha.pluginstruct.Plugin;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $id$
 */
public class PluginConf extends JPanel
	implements ActionListener, KeyPointListener {
	
	private PluginManager manager;

	private Plugin[] plugin;
	private String[] name;
	private String[] identifier;
	private boolean[] activated;
	private boolean[] deprecated;
	private JCheckBox[] checkBox;
	
	public PluginConf() {
		// Registeres event which is called when the <code>PluginsTab</code> is shown
		MainFrame.getInstance().getMainActionListener().addActionListener(this);
		
		
		manager = PluginManager.getInstance();
		loadPluginList();
		initSwing();
		refresh();
	}
	
	private void loadPluginList() {
		Iterator it = manager.getPluginIterator();
		LinkedList list = new LinkedList();
		
		while (it.hasNext()) {
			list.add(it.next());
		}
		
		int size = list.size();
		plugin     = new Plugin[size];
		name       = new String[size];
		identifier = new String[size];
		activated  = new boolean[size];
		deprecated = new boolean[size];
		
		it = list.iterator();
		
		for (int i=0; it.hasNext(); i++) {
			plugin[i]     = (Plugin)it.next();
			identifier[i] = plugin[i].getIdentifier();
			name[i]       = plugin[i].getName();
		}
	}
	
	private void refresh() {
		for (int i=0; i<plugin.length; i++) {
			activated[i]  = manager.isPluginActivated(identifier[i]);
			checkBox[i].setSelected(activated[i]);
			deprecated[i] = manager.isPluginDeprecated(identifier[i]);
			if (deprecated[i]) {
				checkBox[i].setEnabled(false);
			}
		}
	}
	
	private void initSwing() {
		JPanel panel = new JPanel(new GridLayout(plugin.length, 1));
		checkBox = new JCheckBox[plugin.length];
		for (int i=0; i<plugin.length; i++) {
			checkBox[i] = new JCheckBox(name[i]);
			checkBox[i].addActionListener(this);
			checkBox[i].setToolTipText(createToolTip(plugin[i]));
			panel.add(checkBox[i]);
		}
		this.setBorder(new BarBorder("Plugin Einstellungen"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(panel);
	}
	
	public void actionPerformed(ActionEvent event) {
		JCheckBox source = (JCheckBox)event.getSource();
		for (int i=0; i<checkBox.length; i++) {
			if (checkBox[i] == source) {
				activated[i] = source.isSelected();
				manager.setPluginActivity(identifier[i], activated[i]);
			}
		}
	}
	

	/**
	 * Method is called when a preregistered key-point is reached.
	 *  
	 * @param e is a <code>KeyPointEvent</code> which contains:
	 *          - specific <code>KeyPointEvent</code> (e.g. WINDOW_EXIT)
	 */
	public void runAction(KeyPointEvent e) {
		if(e.getKeyPointEvent() == KeyPointEvent.CONFIG_PANEL_LOAD) {
			refresh();
		}
	}
	
	private String createToolTip(Plugin plugin) {
		StringBuffer tt = new StringBuffer();
		String breakk = "<br>";
		tt.append("<html><b>");
		tt.append(plugin.getName());
		tt.append("</b>");
		tt.append(breakk);
		tt.append("Source: ");
		tt.append(plugin.getSource());
		tt.append("<hr>");
		
		String desc = plugin.getDescription();
		int last = 0;
		for (int i=50; i<desc.length(); i+=50) {
			while (desc.charAt(i) != ' ') {
				i++;
			}
			tt.append(desc.subSequence(last, i));
			tt.append(breakk);
			last = i;
		}
		tt.append(desc.substring(last));
		tt.append("</html>");
		return tt.toString();
	}
}
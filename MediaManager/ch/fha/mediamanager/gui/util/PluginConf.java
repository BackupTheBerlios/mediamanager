package ch.fha.mediamanager.gui.util;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import ch.fha.pluginstruct.Plugin;
import ch.fha.pluginstruct.PluginManager;

/**
 * @author ia02vond
 * @version $id$
 */
public class PluginConf extends JPanel
	implements ActionListener {
	
	private PluginManager manager;

	private Plugin[] plugin;
	private String[] name;
	private String[] identifier;
	private boolean[] activated;
	private boolean[] deprecated;
	private JCheckBox[] checkBox;
	
	public PluginConf() {
		manager = PluginManager.getInstance();
		loadPluginList();
		initSwing();
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
			activated[i]  = manager.isPluginActivated(identifier[i]);
			deprecated[i] = manager.isPluginDeprecated(identifier[i]);
		}
	}
	
	private void initSwing() {
		JPanel panel = new JPanel(new GridLayout(plugin.length, 1));
		checkBox = new JCheckBox[plugin.length];
		for (int i=0; i<plugin.length; i++) {
			checkBox[i] = new JCheckBox(name[i], activated[i]);
			if (deprecated[i]) {
				checkBox[i].setEnabled(false);
			}
			checkBox[i].addActionListener(this);
			panel.add(checkBox[i]);
		}
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
}
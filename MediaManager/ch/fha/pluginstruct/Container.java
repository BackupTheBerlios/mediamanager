package ch.fha.pluginstruct;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ia02vond
 * @version $Id: Container.java,v 1.2 2004/05/14 09:40:58 ia02vond Exp $
 */
public final class Container {
	
	/** Path to the plugins.xml file.*/
	public final static String PLUGINSXML_FILE = 
		"ch" + File.separator + "fha" + File.separator +
		"pluginstruct" + File.separator + "plugins.xml";
	
	/** Root path of the plugins.*/
	public final static String PLUGIN_DIR = 
		"plugins" + File.separator;
	
	
	/** Hashmap of all plugins. */
	private HashMap pluginList = new HashMap();
	
	protected Container() {}
	
	/**
	 * Initializes the plugin container by using a
	 * <code>PluginListInitializer</code>.
	 * @see PluginListInitializer
	 */
	protected void init() {
		PluginListInitializer initializer = new PluginListInitializer();
		initializer.initialize(
				PluginManager.getContainer(),
				PluginManager.getEventHandler());
	}
		
	
	/**
	 * Adds the given plugin to the plugin list, using the specified
	 * identifier name as key. The plugin will not be added if a
	 * plugin with the same identifier already exists.
	 * @param plugin       the plugin
	 * @param identifier   the plugins identifier
	 * @param activated    true if the plugin is activated
	 * @return true if adding was successful.
	 */
	protected boolean addPlugin(Plugin plugin, String identifier, boolean activated) {
		if (pluginList.containsKey(identifier)) {
			return false;
		} else {
			PluginStruct struct = new PluginStruct(plugin, activated);
			pluginList.put(identifier, struct);
			storeActivities();
			return true;
		}
	}
	
	/**
	 * Removes and returns the plugin with the specified identifier.
	 * @param identifier    identifier whose plugin should be removed
	 * @return the removed plugin
	 */
	protected Plugin removePlugin(String identifier) {
		PluginManager.getEventHandler().removeEventListener(identifier);
		return ((PluginStruct)pluginList.remove(identifier)).plugin;
	}
	
	/**
	 * Replaces the plugin with the specified identifier by a new
	 * plugin. Nothing happens if no plugin exists for replacing.
	 * @param identifier    identifier whose plugin should be replaced
	 * @param plugin        the new plugin
	 * @return true if replacing was successful
	 */
	protected boolean replacePlugin(String identifier, Plugin plugin) {
		if (pluginList.containsKey(identifier)) {
			PluginStruct oldStruct = (PluginStruct)pluginList.remove(identifier);
			PluginStruct struct = new PluginStruct(plugin, oldStruct.activated);
			pluginList.put(identifier, struct);
			return true;
		}
		return false;
	}
	
	/**
	 * Sets whether the plugin, specified by the given identifier, is
	 * activated or not.
	 * @param identifier    the plugin identifier
	 * @param activated     true if the plugin should be activated
	 * @throws IllegalArgumentException if the plugin does not exist.
	 */
	protected void setPluginActivity(String identifier, boolean activated) {
		Object obj = pluginList.get(identifier);
		if (obj != null) {
			PluginStruct struct = (PluginStruct) obj;
			if (!struct.deprecated) {
				struct.activated = activated;
			} else {
				struct.activated = false;
			}
			storeActivities();
		} else {
			throw new IllegalArgumentException("plugin '"+identifier+"' does not exist");
		}
	}
	

	protected void setPluginAsDeprecated(String identifier) {
		Object obj = pluginList.get(identifier);
		if (obj != null) {
			PluginStruct struct = (PluginStruct) obj;
			struct.activated = false;
			struct.deprecated = true;
		} else {
			throw new IllegalArgumentException("plugin '"+identifier+"' does not exist");
		}
	}
	
	/**
	 * @param identifier    the plugin identifier
	 * @return true if the plugin, specified by the given
	 *         identifier, is activated.
	 */
	protected boolean isPluginActivated(String identifier) {
		Object obj = pluginList.get(identifier);
		if (obj != null) {
			PluginStruct struct = (PluginStruct) obj;
			return struct.deprecated ? false : struct.activated;
		} else {
			throw new IllegalArgumentException("plugin '"+identifier+"' does not exist");
		}
	}
	
	protected boolean isPluginDeprecated(String identifier) {
		Object obj = pluginList.get(identifier);
		if (obj != null) {
			return ((PluginStruct)obj).deprecated;
		} else {
			throw new IllegalArgumentException("plugin '"+identifier+"' does not exist");
		}
	}
	
	/**
	 * @param  identifier    the plugin identifier
	 * @return the plugin with the specified identifier
	 */
	protected Plugin getPlugin(String identifier) {
		Object obj = pluginList.get(identifier);
		return obj != null ? ((PluginStruct)obj).plugin : null;
	}
	
	/** Clears the plugin container. */
	protected void clear() {
		pluginList.clear();
	}
	
	
	/** @return a plugin iterator */
	protected Iterator iterator() {
		return new PluginIterator(this, pluginList);
	}
	
	/*
	 * Stores about every known plugin whether it is
	 * activated or not.
	 */
	private void storeActivities() {
		File file = new File (Container.PLUGINSXML_FILE);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			Iterator it = pluginList.keySet().iterator();
			PluginStruct struct;
			
			writer.write("<?xml version=\"1.0\"?>");
			writer.write("<pluginlist>");
			while (it.hasNext()) {
				struct = (PluginStruct)pluginList.get(it.next());;
				String id = struct.plugin.getIdentifier();
				String activated = "" + struct.activated;
				writer.write ("<plugin id=\""+id+"\" active=\""+activated+"\" />");
			}
			writer.write("</pluginlist>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Simple structure about a plugin and whether it is
	 * activated or not. Used in hashmap.
	 */
	private class PluginStruct {
		Plugin plugin;
		boolean activated;
		boolean deprecated;
		public PluginStruct(Plugin plugin, boolean activated) {
			this.plugin = plugin;
			this.activated = activated;
			this.deprecated = false;
		}
	}
	
	/*
	 * Plugin iterator
	 */
	private class PluginIterator implements Iterator {

		private Container container;
		private Iterator i;
		private String identifier;
		
		public PluginIterator(Container container, HashMap pluginList) {
			this.container = container;
			i = pluginList.keySet().iterator();
		}
		
		public void remove() {
			if (identifier == null) throw new IllegalStateException();
			container.removePlugin(identifier);
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public Object next() {
			identifier = i.next().toString();
			return ((PluginStruct)container.getPlugin(identifier)).plugin;
		}
	}
}


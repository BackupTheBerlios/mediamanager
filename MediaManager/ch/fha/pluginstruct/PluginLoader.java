package ch.fha.pluginstruct;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author ia02vond
 * @version $Id: PluginLoader.java,v 1.2 2004/05/14 09:40:58 ia02vond Exp $
 */
public final class PluginLoader extends AbstractPlugin
	implements ContentHandler {
	
	private File xml;
	
	private HashMap props = new HashMap();
	private LinkedList depList = new LinkedList();
	
	protected PluginLoader(
			String identifier,
			File xml) {
		
		setIdentifier(identifier);
		
		this.xml = xml;
		
		this.parse();
		
		// check requested system version
		Version v1 = getRequestedSystemVersion();
		Version v2 = PluginManager.getSystemVersion();
		if (v1.compareTo(v2) < 0) {
			String message = 
				"Das Plugin '" + getName() + "' basiert auf der" +
				"Applikationsversion " + v1 + ". Die aktuelle Version is " +
				v1 + ". Das Plugin wird deshalb deaktiviert.";
			
			PluginManager.getContainer().setPluginActivity(getIdentifier(), false);
		}
	}
	
	private Plugin load() throws PluginLogicException {
		if (!isActivated()) return null;
		
		// check dependency
		Iterator it = depList.iterator();
		while (it.hasNext()) {
			try {
				Dependency dep = (Dependency) it.next();
				Plugin plg = PluginManager.getContainer().getPlugin(dep.pluginId);
				if (plg == null || plg.getVersion().compareTo(dep.version) < 0) {
					String message =
						"Das Plugin '"+getName()+"' verlangt das Plugin '" +
						dep.pluginId + "' V" + dep.version + ". Dieses kann jedoch " +
						"nicht gefunden werden. Das Plugin wird deshalb deaktiviert.";
					
					throw new PluginLogicException(this, message);
				}
			} catch (RuntimeException e) {
				throw new PluginLogicException(this, "unknown plugin runtime exception", e);
			}
		}
		
		// load plugin
		try {
			Class clazz = Class.forName(getSource());
			Object obj = clazz.newInstance();
			if (obj instanceof Plugin) {
				Plugin ap = (Plugin) obj;
				
				// set attributes
				try {
					ap.setIdentifier(getIdentifier());
					ap.setName(getName());
					ap.setDescription(getDescription());
					ap.setRequestedSystemVersion(getRequestedSystemVersion());
				} catch (RuntimeException e) {
					throw new PluginLogicException(this, "unknown plugin runtime exception", e);
				}
				
				// replace plugin
				PluginManager.getContainer().replacePlugin(getName(), ap);
				
				// add properties
				Iterator it2 = props.keySet().iterator();
				while (it2.hasNext()) {
					String key = it2.next().toString();
					Collection coll = (Collection)props.get(key);
					String[] values = new String[coll.size()];
					Iterator it3 = coll.iterator();
					int index = 0;
					while (it3.hasNext()) {
						values[index++] = it3.next().toString();
					}
					try {
						ap.addPropertie(key, values);
					} catch (RuntimeException e) {
						throw new PluginLogicException(this, "unknown plugin runtime exception", e);
					}
				}
				return ap;
			} else {
				throw new PluginLogicException(this, "source does not implements the interface 'Plugin'");
			}
		} catch (ClassNotFoundException e) {
			throw new PluginLogicException(this, "source does not exist", e);
		} catch (InstantiationException e) {
			throw new PluginLogicException(this, "unable to instantiate plugin", e);
		} catch (IllegalAccessException e) {
			throw new PluginLogicException(this, "source access permitted", e);
		}
	}
	
	private int state = DEFAULT;
	private final static int DEFAULT = 1,
	                         PLUGIN = 2,
							 SOURCE = 3,
							 DESCRIPTION = 4,
							 DEPENDENCY = 5,
							 MOUNT = 6,
							 ON_EVENT = 7,
							 ICON = 10;
	
	private void parse() {
		XMLReader parser = new SAXParser();
		try {
			parser.setContentHandler(this);
			parser.parse(Container.PLUGIN_DIR + getIdentifier() + ".xml");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public void addPropertie(String key, String[] values) {
		throw new UnsupportedOperationException();
	}
	
	public void startElement(
			String namespaceURI,
			String localName,
			String qName,
			Attributes atts) throws SAXException {
	
		localName = localName.toLowerCase();
		switch (state) {
			case DEFAULT:
				if (localName.equals("plugin")) {
					String name = atts.getValue("name");
					Version ver = new Version(atts.getValue("version"));
					Version rsv = new Version(atts.getValue("reqversion"));
					
					if (name!=null && !name.equals("") &&
						ver.validate() && rsv.validate() ) {					
						
						setName(name);
						setVersion(ver);
						setRequestedSystemVersion(rsv);
						state = PLUGIN;
					}
				}
				break;
			case PLUGIN:
				if (localName.equals("source")) state = SOURCE;
				else if (localName.equals("description")) state = DESCRIPTION;
				else if (localName.equals("dependency")) state = DEPENDENCY;
				else if (localName.equals("mount")) state = MOUNT;
				else if (localName.equals("prop")) {
					String key = atts.getValue("key");
					String val = atts.getValue("value");
					if ( !key.equals("") ) {
						if (props.containsKey(key)) {
							Collection list = (Collection)props.get(key);
							list.add(val);
						} else {
							LinkedList list = new LinkedList();
							list.add(val);
							props.put(key, list);
						}
					}
				}
				break;
			case DEPENDENCY:
				if (localName.equals("plugin")) {
					String id   = atts.getValue("id");
					Version ver = new Version(atts.getValue("version"));
					
					if (id != null && !id.equals("") && ver.validate()) {
						depList.add(new Dependency(id, ver));
					}	
				}
				break;
			case MOUNT:
				try {
					if (localName.equals("onevent")) {
						String id = atts.getValue("id");
						String condition = atts.getValue("cond");
						PluginManager.getEventHandler().addEventListener(
								this, getIdentifier(), id, condition);	
					}
				} catch (PluginLogicException e) {
					e.show();
					state = DEFAULT;
				}
		}
	}
	
	public void endElement(
			String namespaceURI,
			String localName,
			String qName) throws SAXException {
		
		localName = localName.toLowerCase();
		switch (state) {
			case PLUGIN:
				if (localName.equals("plugin")) state = DEFAULT;
				break;
			case SOURCE:
				if (localName.equals("source")) state = PLUGIN;
				break;
			case DESCRIPTION:
				if (localName.equals("description")) state = PLUGIN;
				break;
			case DEPENDENCY:
				if (localName.equals("dependency")) state = PLUGIN;
				break;
			case MOUNT:
				if (localName.equals("mount")) state = PLUGIN;
				break;
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String (ch, start, length);
		if (!str.equals("")) {
			switch (state) {
				case SOURCE:
					setSource(str);
					break;
				case DESCRIPTION:
					setDescription(str);
					break;
			}
		}
	}
	
	public void run(PluginEvent event) throws OperationCancelException {
		try {
			Plugin ap = load();
			if (ap != null) {
				ap.run(event);
			}
		} catch (PluginLogicException e) {
			e.show();
		}
	}
	
	protected LinkedList getDependencies() {
		return depList;
	}
	
	public void startDocument() throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}
	public void setDocumentLocator(Locator locator) {}
	public void processingInstruction(String target, String data) throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void endDocument() throws SAXException {}
	
	private class Dependency {
		String pluginId;
		Version version;
		public Dependency(String pluginId, Version version) {
			this.pluginId = pluginId;
			this.version = version;
		}
	}
}

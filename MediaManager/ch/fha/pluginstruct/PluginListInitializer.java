package ch.fha.pluginstruct;

import java.io.File;
import java.io.IOException;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author ia02vond
 * @version $Id: PluginListInitializer.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public final class PluginListInitializer implements ContentHandler {
	
	/**
	 * State of the state-machine while parsing
	 * the plugins.xml file.
	 */
	private int state = DEFAULT;
	
	/**
	 * Possible states of the state-machine while
	 * parsing the plugins.xml file. Each state
	 * accords to a xml-element.
	 */
	private final static int DEFAULT = 0,
	                         PLUGINLIST = 1,
						     PLUGIN = 2;

	/*
	 * Lists of all, in the plugins.xml mentioned, plugins.
	 * One list contains the plugin names, the other one contains
	 * true if the appropriate plugin is activated.
	 */
	private final static int MAX_NOF_PLUGINS = 100;
	private String[]  plugins = new String[MAX_NOF_PLUGINS];
	private boolean[] active  = new boolean[MAX_NOF_PLUGINS];
	private int nofPlugins = 0;
	
	
	
	protected void initialize(Container container, EventHandler eventHandler) {
		XMLReader parser = new SAXParser();
		
		// parse plugins.xml
		if ( (new File(Container.PLUGINSXML_FILE)).exists() ) {
			parser.setContentHandler(this);
			try {
				parser.parse(Container.PLUGINSXML_FILE);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		
		// get all *.xml files
		File dir = new File(Container.PLUGIN_DIR);
		String[] xmlList = getFileList(dir, ".xml");

		// initialize plugin loaders and add them to the
		// plugin container.
		container.clear();
		for (int i=0; i<xmlList.length; i++) {
			if (xmlList[i].equals("plugins.xml")) continue;
			String identifier = xmlList[i].substring(0, xmlList[i].length()-4);
			File xml = new File(xmlList[i]);
			
			// look whether plugin is activated
			boolean active = false;
			boolean registered = false;
			for (int k=0; k<nofPlugins; k++) {
				if (plugins[k].equals(identifier)) {
					active = this.active[k];
					registered = true;
				}
			}
			
			// if the plugin is a new one, ask whether it should be activated
			if (!registered) {
				String message = "activate new plugin: " + identifier + "?";
				String[] options = {"yes", "no"};
				if (PluginManager.getInOut().option(message, options) == 0) {
					active = true;
				}
			}
			
			// add new plugin loader
			Plugin p = new PluginLoader(
					identifier,
					new File(xmlList[i]));
			container.addPlugin(p, identifier, active);
		}
	}
	
	public void startElement(
			String namespaceURI,
			String localName,
			String qName,
			Attributes atts) throws SAXException {
		
		switch (state) {
			case DEFAULT:
				if (localName.equals("pluginlist")) state = PLUGINLIST;
				break;
			case PLUGINLIST:
				if (localName.equals("plugin")) {
					state = PLUGIN;
					
					plugins[nofPlugins] = atts.getValue("id");
					active[nofPlugins]  = atts.getValue("active").equalsIgnoreCase("true");
					nofPlugins++;
				}
				break;
			case PLUGIN:
				break;
		}
	}
	
	public void endElement(
			String namespaceURI,
			String localName,
			String qName) throws SAXException {

		switch (state) {
			case DEFAULT:
				break;
			case PLUGINLIST:
				if (localName.equals("pluginlist")) state = DEFAULT;
				break;
			case PLUGIN:
				if (localName.equals("plugin")) state = PLUGINLIST;
				break;
		}
	}
	
	/**
	 * Returns a String array of all files of the given directory,
	 * whose filename ends with the specified suffix.
	 * @param dir        the source directory
	 * @param suffix     the suffix which the filenames has to end with
	 * @return String array of all filenames
	 */
	private static String[] getFileList(File dir, String suffix) {
		if (!dir.exists()) return new String[0];
		String[] list = dir.list();
		int nIni = 0;
		for (int i=0; i<list.length; i++) {
			if (list[i].endsWith(suffix)) {
				nIni++;
			} else {
				list[i] = null;
			}
		}
		String[] list2 = new String[nIni];
		int index = 0;
		for (int i=0; i<list.length; i++) {
			if (list[i] != null) {
				list2[index] = list[i];
				index++;
			}
		}
		return list2;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {}
	public void endDocument() throws SAXException {}
	public void startDocument() throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}
	public void setDocumentLocator(Locator locator) {}
	public void processingInstruction(String target, String data) throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
}

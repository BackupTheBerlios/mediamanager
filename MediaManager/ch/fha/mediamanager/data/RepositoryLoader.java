package ch.fha.mediamanager.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author ia02vond
 * @version $Id: RepositoryLoader.java,v 1.3 2004/06/18 12:37:31 ia02vond Exp $
 */
public class RepositoryLoader implements ContentHandler {
	
	/** Path to the xml repository configuration file. */
	private final static String XML_FILE = "conf/repositoryconf.xml";
	
	/* list of founded repositories */
	private LinkedList repList = new LinkedList();

	/**
	 * Parses the xml repository configuration file and
	 * returns every founded repository as an array of
	 * <code>Repository</code> instances.
	 * @return array of <code>Repository</code> instances
	 */
	public static Repository[] loadRepositories() throws FileNotFoundException {
		RepositoryLoader loader = new RepositoryLoader();
		return loader.load();
	}
	
	/** private constructor */
	private RepositoryLoader() {}
	
	private Repository[] load() throws FileNotFoundException {
		XMLReader parser = new SAXParser();
		
		if ( (new File(XML_FILE)).exists() ) {
			parser.setContentHandler(this);
			try {
				parser.parse(XML_FILE);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			
			return (Repository[])repList.toArray(new Repository[repList.size()]);
		} else {
			throw new InternalError("xml repository configuration file not found");
		}		
	}

	public void startElement(
			String namespaceURI,
			String localName,
			String qName,
			Attributes atts) throws SAXException {
		
		if (localName.equalsIgnoreCase("repository")) {
			String name  = atts.getValue("name");
			String clazz = atts.getValue("class");
			if (!name.equals("") && !clazz.equals("")) {
				try {
					Class c    = Class.forName(clazz);
					Object obj = c.newInstance();
					if (obj instanceof Repository) {
						repList.add(obj);
					}
				} catch (ClassNotFoundException e) {
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {}
	public void endDocument() throws SAXException {}
	public void startDocument() throws SAXException {}
	public void characters(char[] ch, int start, int length) throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}
	public void setDocumentLocator(Locator locator) {}
	public void processingInstruction(String target, String data) throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

}

package ch.fha.pluginstruct;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ia02vond
 * @version $Id: AbstractPlugin.java,v 1.4 2004/06/28 18:17:24 ia02vond Exp $
 */
public abstract class AbstractPlugin implements Plugin {

	/* Plugin attributes */
	private String  identifier;
	private String  name;
	private String  description;
	private Version version;
	private Version requestedSystemVersion;
	private String  source;
	
	/* propertie string[] list */
	private HashMap propertieList = new HashMap();

	private EventHandler eventHandler;
	
	public boolean run(PluginEvent event, EventHandler eventHandler){
		this.eventHandler = eventHandler;
		return run(event);
	}
	
	public abstract boolean run(PluginEvent event);
	
	/**
	 * Invoke this method of the plugin has finished its tasks
	 * so that the application continues with its operation.
	 */
	protected void finish() {
		eventHandler.finish();
	}
	
	protected void cancelOperation() {
		eventHandler.cancelOperation();
	}
	
	/**
	 * Sends a <code>String</code> message to application. Usually
	 * the application displays this message as dialog to the user.
	 * @param message - the message
	 */
	protected void message(String message) {
		PluginManager.getInOut().message(message);
	}
	
	/**
	 * The application usually displays an option dialog and asks
	 * the user to choose an option.
	 * @param message - the message which will be displayed
	 * @param options - the options which the user can choose
	 * @return the array index of the chosen option
	 */
	protected int option(String message, String[] options) {
		return PluginManager.getInOut().option(message, options);
	}
	
	/**
	 * Removes this plugin as event listener from the event handler.
	 * @param event - the event, which the listener has listen to.
	 * @throws PluginLogicException if the given event doesn't exist.
	 */
	protected void removeEventListener(String event)
		throws PluginLogicException {
		
		removeEventListener(event, "");
	}
	
	/**
	 * Removes this plugin as event listener from the event handler.
	 * @param event - the event, which the listener has listen to.
	 * @param condition - the condition, which has had to be true.
	 * @throws PluginLogicException if the given event doesn't exist.
	 */
	protected void removeEventListener(String event, String condition)
		throws PluginLogicException {
		
		PluginManager.getEventHandler().removeEventListener(this, event, condition);
	}	
	
	/**
	 * Adds this plugin as event listener to the event handler.
	 * The event handler notifies if the specified event occurs
	 * by invoking the <code>run()</code> method. The condition
	 * doesn't matter.
	 * @param event - the event, which this plugin will listen to.
	 * @throws PluginLogicException if the event doesn't exist.
	 */
	protected void addEventListener(String event)
		throws PluginLogicException {
		
		addEventListener(event, "");
	}
	
	/**
	 * Adds this plugin as event listener to the event handler.
	 * The event handler notifies if the specified event %auftreten
	 * by invoking the <code>run()</code> method. The event condition
	 * mustn't be empty or has to equal to the specified condition
	 * %damit the listener will be notified.
	 * @param event - the event, which this plugin will listen to.
	 * @param condition - the condition, which has to be true.
	 * @throws PluginLogicException if the event doesn't exist.
	 */
	protected void addEventListener(String event, String condition)	
		throws PluginLogicException{
		
		PluginManager.getEventHandler().addEventListener(this, event, condition);
	}
	
	/**
	 * @eturn an iterator over all plugins in the container.
	 */
	protected Iterator getPluginIterator() {
		return PluginManager.getContainer().iterator();
	}
	
	/**
	 * Returns the plugin with the specified identifier or
	 * <code>null</code> if that plugin does not exist in the
	 * container.
	 * @param identifier - the plugin identifier
	 * @return the plugin
	 */
	protected Plugin getPlugin(String identifier) {
		return PluginManager.getContainer().getPlugin(identifier);
	}
	
	/**
	 * Removes this plugin from the container, so that this
	 * plugin will not be considered anymore by the application.
	 */
	protected void destroy() {
		PluginManager.getContainer().removePlugin(getIdentifier());
	}
	
	/**
	 * Associates the specified value with the specified key in the
	 * propertie list. If the list previously contained a mapping
	 * for this key, the old value is replaced.
	 * 
	 * <p><b>how to:</b>
	 * Properties can be defined in the plugin *.xml file with
	 * following statement:
	 * <br>&nbsp;&nbsp;&nbsp;
	 * <code>&lt;prop key="xyz" value="xyz" /&gt;</code>
	 * 
     * <p><b>note:</b>This method is invoked for each, in the plugin
     * *.xml file founded, propertie after loading the plugin.
     * Overload this method if special properties should be stored
     * on another way.
     * 
	 * @param key      key with which the specified values are to be 
     *                 associated.
     * @param value    value to be associated with the specified key.
     */
	public void addPropertie(String key, String[] values) {
		propertieList.put(key, values);
	}
	
	/**
     * Returns the propertie values to which the specified key is mapped,
     * or <code>null</code> if there'are no values mapped for this key.
     *
     * @param key    the key whose associated values are to be returned.
     * @return the values to which this map maps the specified key, or
     *         <code>null</code> if the map contains no mapping for 
     *         this key.
     */
	public String[] getPropertie(String key) {
		Object obj = propertieList.get(key);
		if (obj != null && obj instanceof String[]) {
			return (String[])obj;
		} else {
			return null;
		}
	}
	
	
	//-------------------
	// Getter / Setter
	//-------------------
	
	/**
	 * @return true if the plugin is activated.
	 */
	public boolean isActivated() {
		return PluginManager.getContainer().isPluginActivated(getIdentifier());
	}
	/**
	 * Sets whether this plugin is activated or not.
	 * @param the plugin activity
	 */
	public void setActivated(boolean active) {
		PluginManager.getContainer().setPluginActivity(getIdentifier(), active);
	}
	
	/**
	 * @return the plugin description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the plugin description.
	 * @param description    the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the plugin identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Sets the plugin identifier.
	 * @param identifier    the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * @return the plugin name.
	 */
	public String getName() {
		return name == null || name.equals("") ? identifier : name;
	}
	
	/**
	 * Sets the plugin name.
	 * @param name    the plugin name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the requested system version
	 */
	public Version getRequestedSystemVersion() {
		return requestedSystemVersion;
	}
	
	/**
	 * Sets the requested system version.
	 * @param rsv    the requested system version
	 */
	public void setRequestedSystemVersion(Version rsv) {
		this.requestedSystemVersion = rsv;
	}
	
	/**
	 * @return the plugin source.
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Sets the source path of the plugin in a form like
	 * <code>[package].[classname]</code>.
	 * @param source    the source path
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * @return the plugin version.
	 */
	public Version getVersion() {
		return version;
	}
	
	/**
	 * Sets the plugin version.
	 * @param version    the version
	 */
	public void setVersion(Version version) {
		this.version = version;
	}
}

package ch.fha.pluginstruct;

import java.util.Iterator;


/**
 * @author ia02vond
 * @version $Id: PluginManager.java,v 1.6 2004/06/28 11:25:33 ia02vond Exp $
 */
public final class PluginManager {
	
	/** the only instance of <code>PluginManager</code>. (Singelton-Pattern) */
	private static PluginManager manager = null;
	
	/** the <code>InOut</code> interface for sending and receiving messages. */
	private InOut inOut;
	
	/** the system version. */
	private Version systemVersion;
	
	/** <code>String</code> array of all possible events. */
	private String[] eventList;
	
	/** the plugin container. */
	private Container container;
	
	/** the event handler. */
	private EventHandler eventHandler;
	
	/** <code>true</code> if the plugin manager has been initialized. */
	private boolean initialized = false;
	
	/** private constructor (singelton pattern). */
	private PluginManager() {}
	
	/** 
	 * Initializes the plugin manager.
	 */
	public void initialize() {
		if (inOut == null) {
			inOut = new KeyboardInOut();
		}
		
		container    = new Container();
		eventHandler = new EventHandler(eventList);
		container.init();
		
		initialized = true;
	}
	
	/**
	 * Returns an instance of the plugin manager.<br>
	 * <b>Attention:</b> Use this method only the first time. After that invoke
	 * the getInstance() without any parameters for getting the instance.
	 * @return the plugin manager instance
	 */
	public static PluginManager getInstance(String[] eventList, Version systemVersion) {
		if (eventList == null || systemVersion == null ||
			!systemVersion.validate()) {
			throw new IllegalArgumentException();
		}
	
		if (manager != null) {
			throw new IllegalStateException("already created");
		}
		
		manager = new PluginManager();
		manager.eventList = eventList;
		manager.systemVersion = systemVersion;
		
		return manager;
	}
	
	/**
	 * @return the instance of the plugin manager. (singelton pattern)
	 */
	public static PluginManager getInstance() {
		if (manager == null) {
			throw new IllegalStateException("pluginmanager has not been initialized yet, use getInstance(String[], Version)");
		}
		return manager;
	}
	
	/**
	 * Fires an event and notifies all announced plugins which
	 * are interested in the specified event with the given
	 * condition.
	 * @param pluginEvent    a plugin event object
	 * @param event          the event name
	 * @param condition      the condition
	 * @throws OperationCancelException if a plugin wants to
	 *         cancel the current operation which has caused
	 *         the event.
	 */
	public void fireEvent(
			Returnable returnable,
			PluginEvent pluginEvent,
			String event,
			String condition) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		eventHandler.fireEvent(returnable, pluginEvent, event, condition);
	}
	
	/**
	 * @return a plugin iterator over <i>all</i> (not only over those
	 *         which are activated) plugins.
	 * @throws IllegalStateException if the plugin manager has not
	 *         been initialized yet.
	 */
	public Iterator getPluginIterator() {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return container.iterator();
	}
	
	/**
	 * @return a plugin iterator over those plugins, which are interested
	 *         interested in the given event.
	 * @throws IllegalStateException if the plugin manager has not
	 *         been initialized yet.
	 */
	public Iterator getPluginIterator(String event) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return eventHandler.iterator(event);
	}
	
	/**
	 * @param  identifier    the plugin identifier
	 * @return <code>true</code> if the plugin with the specified
	 *         identifier is activated.
	 * @throws IllegalStateException if the plugin manager has not
	 *         been initialized yet.
	 */
	public boolean isPluginActivated(String identifier) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return getContainer().isPluginActivated(identifier);
	}
	
	/**
	 * Sets whether the plugin, specified by the given identifier, should
	 * be activated or not. The plugin will not be activated if it's already
	 * marked as deprecated.
	 * @param  identifier    the plugin's identifier
	 * @param  activated     <code>true</code> if plugin should be activated
	 * @throws IllegalStateException if the plugin manager has not
	 *         been initialized yet.
	 */
	public void setPluginActivity(String identifier, boolean activated) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getContainer().setPluginActivity(identifier, activated);
	}
	
	/**
	 * @param  identifier    the plugin identifier
	 * @return <code>true</code> if the plugin with the specified
	 *         identifier is deprecated.
	 * @throws IllegalStateException if the plugin manager has not
	 *         been initialized yet.
	 */
	public boolean isPluginDeprecated(String identifier) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return getContainer().isPluginDeprecated(identifier);
	}
	
	/** 
     * Adds an <code>EventHandlerListener</code>. 
     * <p>
     * The <code>EventHandlerListener</code> will be notified if a plugin
     * is announced or signed off to the event handler.
     *
     * @param listener    the <code>EventHandlerListener</code> that is to be notified
     */
    public void addEventHandlerListener(EventHandlerListener listener) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getEventHandler().addEventHandlerListener(listener);
	}
	
    /**
     * Removes an <code>EventHandlerListener</code>.
     * <p>
     * The <code>EventHandlerListener</code> will not be notified anymore.
     */
	public void removeEventHandlerListener(EventHandlerListener listener) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getEventHandler().removeEventHandlerListener(listener);
	}
	
	/**
	 * @return the internally used plugin container.
	 */
	protected static Container getContainer() {
		return manager.container;
	}
	
	/**
	 * @return the internally used event handler.
	 */
	protected static EventHandler getEventHandler() {
		return manager.eventHandler;
	}
	
	/**
	 * @return the specified system version for system
	 *         controlling.
	 */
	protected static Version getSystemVersion() {
		return manager.systemVersion;
	}
	
	/**
	 * @return the specified <code>InOut</code> object.
	 */
	protected static InOut getInOut() {
		return manager.inOut;
	}
	
}

package ch.fha.pluginstruct;

import java.util.Iterator;


/**
 * @author ia02vond
 * @version $Id: PluginManager.java,v 1.3 2004/06/14 20:16:54 ia02vond Exp $
 */
public final class PluginManager {
	
	private static PluginManager manager = null;
	
	private InOut inOut;
	private Version systemVersion;
	private String[] eventList;
	
	private Container container;
	private EventHandler eventHandler;
	
	private boolean initialized = false;
	
	private PluginManager() {}
	
	public void initialize() {
		if (inOut == null) {
			inOut = new KeyboardInOut();
		}
		
		container    = new Container();
		eventHandler = new EventHandler(eventList);
		container.init();
		
		initialized = true;
	}
	
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
	
	public static PluginManager getInstance() {
		if (manager == null) {
			throw new IllegalStateException("pluginmanager has not been initialized yet, use getInstance(String[], Version)");
		}
		return manager;
	}
	
	public void fireEvent(
			PluginEvent pluginEvent,
			String event,
			String condition) throws OperationCancelException {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		eventHandler.fireEvent(pluginEvent, event, condition);
	}
	
	public Iterator getPluginIterator() {
		return container.iterator();
	}
	
	public boolean isPluginActivated(String identifier) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return getContainer().isPluginActivated(identifier);
	}
	
	public void setPluginActivity(String identifier, boolean activated) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getContainer().setPluginActivity(identifier, activated);
	}
	
	public boolean isPluginDeprecated(String identifier) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		return getContainer().isPluginDeprecated(identifier);
	}
	
	public void addEventHandlerListener(EventHandlerListener listener) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getEventHandler().addEventHandlerListener(listener);
	}
	
	public void removeEventHandlerListener(EventHandlerListener listener) {
		
		if (!initialized) throw new IllegalStateException("not initialized");
		
		getEventHandler().removeEventHandlerListener(listener);
	}
	
	protected static Container getContainer() {
		return manager.container;
	}
	
	protected static EventHandler getEventHandler() {
		return manager.eventHandler;
	}
	
	protected static Version getSystemVersion() {
		return manager.systemVersion;
	}
	
	protected static InOut getInOut() {
		return manager.inOut;
	}
	
}

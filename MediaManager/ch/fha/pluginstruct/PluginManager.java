package ch.fha.pluginstruct;


/**
 * @author ia02vond
 * @version $Id: PluginManager.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public final class PluginManager {
	
	private static InOut inOut;
	private static Version systemVersion;
	private static String[] eventList;
	
	private static Container container;
	private static EventHandler eventHandler;
	
	private static boolean initialized = false;
	
	public static void initialize() {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		}
		if (systemVersion == null) {
			throw new IllegalStateException("no system version specified");
		}
		if (eventList == null) {
			throw new IllegalStateException("no event list specified");
		}
		if (inOut == null) {
			inOut = new KeyboardInOut();
		}
		container = new Container();
		eventHandler = new EventHandler(eventList);
		container.init();
		initialized = true;
	}
	
	public static void setSystemVersion(Version version) {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		}
		if (version != null && version.validate()) {
			systemVersion = version;
		} else {
			throw new IllegalArgumentException("illegal system version");
		}
	}
	
	public static void setEventList(String[] events) {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		} else if (events == null) {
			throw new IllegalStateException("illegal event list");
		}
		eventList = events;
	}
	
	public static void fireEvent(
			PluginEvent pluginEvent,
			String event,
			String condition) throws OperationCancelException {
		
		eventHandler.fireEvent(pluginEvent, event, condition);
	}
	
	public static boolean isPluginActivated(String identifier) {
		return getContainer().isPluginActivated(identifier);
	}
	
	public static void setPluginActivity(String identifier, boolean activated) {
		getContainer().setPluginActivity(identifier, activated);
	}
	
	public static void addEventHandlerListener(EventHandlerListener listener) {
		getEventHandler().addEventHandlerListener(listener);
	}
	
	public static void removeEventHandlerListener(EventHandlerListener listener) {
		getEventHandler().removeEventHandlerListener(listener);
	}
	
	protected static Container getContainer() {
		return container;
	}
	
	protected static EventHandler getEventHandler() {
		return eventHandler;
	}
	
	protected static Version getSystemVersion() {
		return systemVersion;
	}
	
	protected static InOut getInOut() {
		return inOut;
	}
	
}

package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: PluginLogicException.java,v 1.2 2004/05/14 09:40:58 ia02vond Exp $
 */
public class PluginLogicException extends Exception {
	
	private Plugin plugin;
	
	public PluginLogicException(Plugin plugin) {
		super();
		this.plugin = plugin;
		this.setPluginAsDeprecated(plugin);
	}
	
	public PluginLogicException(Plugin plugin, String message) {
		super(message);
		this.plugin = plugin;
		this.setPluginAsDeprecated(plugin);
	}
	
	public PluginLogicException(Plugin plugin, String message, Throwable cause) {
		super(message, cause);
		this.plugin = plugin;
		this.setPluginAsDeprecated(plugin);
	}
	
	public PluginLogicException(Plugin plugin, Throwable cause) {
		super(cause);
		this.plugin = plugin;
		this.setPluginAsDeprecated(plugin);
	}
	
	public void show() {
		String message;
		String[] options = {"Details", "Deaktivieren"};
		
		if (plugin != null) {
			message = "Das Plugin '"+plugin.getIdentifier()+
			          "' hat einen unerwarteten Fehler verursacht.";
		} else {
			message = "Der Plugin Container hat einen unbekannten Fehler " +
			          "verursacht.";
		}
		
		// show details if demanded
		if (PluginManager.getInOut().option(message, options) == 0) {
			PluginManager.getInOut().exception(this);
		}
		
		// deactivate plugin
		if (plugin != null) {
			PluginManager.getEventHandler().removeEventListener(plugin.getIdentifier());
			PluginManager.getContainer().setPluginActivity(plugin.getIdentifier(), false);
		}
	}
	
	private void setPluginAsDeprecated(Plugin plugin) {
		if (plugin != null) {
			try {
				PluginManager.getContainer().setPluginAsDeprecated(plugin.getIdentifier());
			} catch (IllegalArgumentException e) {
			}
		}
	}
	
}

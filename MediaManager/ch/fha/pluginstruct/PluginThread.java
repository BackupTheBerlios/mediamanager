package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $id$
 */
public class PluginThread extends Thread {


	private EventHandler eventHandler;
	private Thread applThread;
	private Plugin plugin;
	private PluginEvent event;
	private OperationCancelException oce = null;
	private PluginLogicException     ple = null;
	
	public PluginThread(
			EventHandler eventHandler,
			Thread applThread,
			Plugin plugin,
			PluginEvent event) {
		
		this.eventHandler = eventHandler;
		this.applThread   = applThread;
		this.plugin       = plugin;
		this.event        = event;
	}
	
	public void run() {
		try {
			plugin.run(this, event);
		} catch (OperationCancelException e) {
			this.oce = e;
			finish();
		} catch (RuntimeException e) {
			this.ple = new PluginLogicException(
					plugin, "unknown plugin runtime exception", e);
			finish();
		}
	}
	
	public void finish() {
		eventHandler.finish(applThread,	oce, ple);
	}
	
	
}

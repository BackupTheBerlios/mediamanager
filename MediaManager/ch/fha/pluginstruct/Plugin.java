package ch.fha.pluginstruct;

public interface Plugin {
	
	/**
	 * @param pluginThread
	 * @param event
	 */
	public void run(PluginThread pluginThread, PluginEvent event)
		throws OperationCancelException;
	
	/**
	 * @return the identifier of the plugin.
	 */
	public String getIdentifier();
	
	/**
	 * Sets the identifier of the plugin.
	 * @param identifier    the identifier
	 */
	public void setIdentifier(String identifier);
	
	/**
	 * @return the name of the plugin. If the name is not defined
	 *         the identifier should be returned.
	 */ 
	public String getName();
	
	/**
	 * Sets the name of the plugin.
	 * @param name    the name
	 */
	public void setName(String name);
	
	/**
	 * @return the plugin description or <code>null</code> if not defined.
	 */
	public String getDescription();
	
	/**
	 * Sets the plugin description.
	 * @param description    the plugin description
	 */
	public void setDescription(String description);
	
	/**
	 * @return the plugin version.
	 */
	public Version getVersion();
	
	/**
	 * Sets the plugin version which should be validated
	 * by using the <code>validate()</code> method.
	 * @param version    the plugin version
	 */
	public void setVersion(Version version);
	
	/**
	 * @return the system version of the application which
	 *         the plugin requests for running correctly.
	 */
	public Version getRequestedSystemVersion();
	
	/**
	 * Sets the system version of the application which
	 * the plugin requests for running correctly.
	 * @param version    the requested system version
	 */
	public void setRequestedSystemVersion(Version version);
	
	/**
	 * @return the source path of the plugin as <code>String</code>.
	 */
	public String getSource();
	
	/**
	 * Sets the source path of the plugin in a form like
	 * <code>[package].[classname]</code>.
	 * @param source    the source path
	 */
	public void setSource(String source);
	
	/**
	 * This method is invoked after loading the plugin and
	 * can be used for storing properties.<p>
	 * Properties can be defined in the plugin *.xml file with
	 * <code>&lt;prop key="xxx" value="yyy" /&gt;</code>.
	 * @param key       the propertie key
	 * @param values    string array of all values which are
	 *                  mounted to this key.
	 */
	public void addPropertie(String key, String[] values);
}

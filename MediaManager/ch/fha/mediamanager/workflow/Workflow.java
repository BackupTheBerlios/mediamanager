package ch.fha.mediamanager.workflow;

import javax.swing.JPanel;

/**
 * @author ia02vond
 * @version $Id: Workflow.java,v 1.2 2004/06/23 17:03:41 ia02vond Exp $
 */
public interface Workflow {

	public void start();
	
	public void go(JPanel form, boolean continuee);

}

package plugins.xmms;

import java.io.IOException;
import java.util.Iterator;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.data.DataSet;
import ch.fha.mediamanager.data.MetaField;
import ch.fha.mediamanager.plugin.MMPlugin;
import ch.fha.mediamanager.plugin.MMPluginEvent;

/**
 * @author ia02vond
 * @version $Id: XMMSPlugin.java,v 1.1 2004/06/28 21:07:57 ia02vond Exp $
 */
public class XMMSPlugin extends MMPlugin {

	public boolean run(MMPluginEvent event) {
		String[] tmp1 = getPropertie("command");
		String[] tmp2 = getPropertie("metafieldname");
		
		if (tmp1 != null && tmp2 != null) {
			
			String cmd = tmp1[0];
			String col = tmp2[0];
			
			if (event.getDataElement() != null) {
				
				play(cmd + " " + execute(col, event.getDataElement()));
				
			} else if (event.getDataSet() != null) {
				
				play(cmd + " " + execute(col, event.getDataSet()));
				
			}
		}
		return true;
	}
		
	private void play(String command) {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String execute(String mfname, DataElement element) {
		
		MetaField metaField = searchMetaField(mfname, element.getMetaFields());
		if (metaField != null) {
			return element.getField(metaField).getValue().toString();
		}
		return "";
	}
	
	private String execute(String mfname, DataSet set) {
		String cmd = "";
		Iterator it = set.iterator();
		if (it.hasNext()) {
			DataElement element = (DataElement)it.next();
			MetaField metaField = searchMetaField(mfname, element.getMetaFields());
			it = set.iterator();
			while (it.hasNext()) {
				element = (DataElement)it.next();
				cmd += element.getField(metaField).getValue().toString() + " ";
			}
			return cmd;
		}
		return "";
	}
	
	private MetaField searchMetaField(String cmd, MetaField[] mf) {
		for (int i=0; i<mf.length; i++) {
			if (mf[i].getName().equals(cmd)) {
				return mf[i];
			}
		}
		return null;
	}
		

}

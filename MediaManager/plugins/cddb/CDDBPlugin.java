package plugins.cddb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import ch.fha.mediamanager.data.DataElement;
import ch.fha.mediamanager.plugin.MMPlugin;
import ch.fha.mediamanager.plugin.MMPluginEvent;

import java.io.IOException;

/**
 * CDDB Plugin is a Plugin to Media Manager. It alows
 * you to query the CDDB Database for CD Information.
 * The class runs for Linux and Windows. For Mac it is
 * not tested. 
 * <p>
 * Important: You need to install a tool into your
 * systems path to calculate
 * the cds id. For Linux it is cd-discid 
 * (http://frantica.lly.org/~rcw/cd-discid)
 * and for windows please use cddbidgen 
 * (http://www.freedb.org/software/cddbidgen.zip).
 *
 * @author David Meyer @ FHA (25.5.2004)
 * @version 1.0
 */

public class CDDBPlugin extends MMPlugin
	implements ActionListener {
	/* Constant values */
	private static final String[] CDColumnNames = {
		"DiscId",
		"Titel",
		"Kategorie" };
	
	private static final String[] trackColumnNames = {
		"TrackNr",
		"Titel",
		"Beschreibung",
		"L\u00e4nge",
		"K\u00fcnstler" };
	
	/* DefaultTableModel */
	private DefaultTableModel CDTableModel = new DefaultTableModel(CDColumnNames, 0) {
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	};
	
	private DefaultTableModel trackTableModel = new DefaultTableModel(trackColumnNames, 0) {
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	};
	
	/* Tables */
	private JTable CDTable = new JTable(CDTableModel);//, CDTableColumnModel);
	private JTable trackTable = new JTable(trackTableModel);
	
	/* panels */
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel northPanel = new JPanel(new GridLayout());
	private JPanel centerPanel = new JPanel(new GridLayout());
	private JPanel buttonPanel = new JPanel(new GridLayout(1,6));
	
	/* scroll panes */
	private JScrollPane CDTableScrollPane = new JScrollPane(CDTable);
	private JScrollPane trackTableScrollPane = new JScrollPane(trackTable);
	
	/* buttons */
	private JButton refreshButton = new JButton("CDDB Suchen");
	private JButton okButton = new JButton("Weiter");
	private JButton cancelButton = new JButton("Abbrechen");
	private JButton skipButton = new JButton("\u00dcberspringen");
	
	/* CDDB */
	private CDDBRecord[] rec = null;
	private Track[] track = null;
	private FreeDB fdb = null;
	
	/* Plugin */
	private MMPluginEvent pluginEvent;
	
	/** creates a new CDDBPlugin */
	public CDDBPlugin() {
		initTables();
		initPanel(); 
	}
	
	public boolean run(MMPluginEvent event) {
		pluginEvent = event;
		
		addPanel(getPanel());
		
		return false;	
	}
	
	/* Initialize the panel */
	private void initPanel() {
		refreshButton.addActionListener(this);
		cancelButton.addActionListener(this);
		skipButton.addActionListener(this);
		okButton.addActionListener(this);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		buttonPanel.add(refreshButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(skipButton);
		buttonPanel.add(okButton);
		
		// nortPanel
		////////////
		northPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black),
					"Gefundene CDs",
					TitledBorder.LEFT,
					TitledBorder.ABOVE_TOP));
		CDTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		northPanel.add(CDTableScrollPane);
		
		// centerPanel
		///////////////
		centerPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black),
					"Tracks",
					TitledBorder.LEFT,
					TitledBorder.ABOVE_TOP));
		trackTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		centerPanel.add(trackTableScrollPane);
		
		// panel
		/////////
		panel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createBevelBorder(BevelBorder.RAISED),
					"CDDB Plugin",
					TitledBorder.LEFT,
					TitledBorder.TOP));
		panel.add(northPanel,BorderLayout.NORTH);
		panel.add(centerPanel,BorderLayout.CENTER);
		panel.add(buttonPanel,BorderLayout.SOUTH);		
	}
	
	/* Initialize Tables */
	private void initTables() {
		try {
			fdb = new FreeDB();
		} catch (IOException ex){
			message(
				"Es traten Probleme mit der FreeDB Verbindung\n" +
				"auf. Vergewissern sie sich, dass sie mit dem\n" +
				"Internet verbunden sind.");
		}
		CDTable.setAutoCreateColumnsFromModel(true);
		CDTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CDTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				refreshTrackTable(CDTable.getSelectedRow());
			}
		});
		trackTable.setAutoCreateColumnsFromModel(true);
	}
	
	/* Queries a CD 
			This function contains the decision witch 
			OS you have. You can add more if you want.
			The only thing you dont have to forget is
			to implement another CDDrive$OS$ for the 
			addes OS (state pattern).
	*/
	private CDDBRecord[] queryCD() throws 
		UnsupportedOperatingSystemException, 
		NoCDInDriveException,
		IOException
	{
		String osName = System.getProperty("os.name");
		CDDrive cd = null;
		if (osName.startsWith("Windows")) {
			cd = new CDDriveWin();
		}
		else if (osName.startsWith("Linux")) {
			cd = new CDDriveLin();
		}
		else throw new UnsupportedOperatingSystemException();
		
		CDDBRecord[] rec = fdb.queryCD(cd.getCDID());
		return rec;
	}

	/**
	 * Refreshes the panel with all its labeltexts.
	 * Use this fuction to query the CD in the Drive.
	 */
	public void refreshPanel() {
		try {
			rec = queryCD();
			refreshCDTable(rec);
		}
		catch (Exception ex){ 
		JOptionPane.showMessageDialog(
				panel,
				ex.getMessage(),
				"CDDBPlugin",
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/* Refreshes the CDTable */
	private void refreshCDTable(CDDBRecord[] rec) {
		clearTable(CDTable);
		clearTable(trackTable);
		for(int i = 0; i<rec.length; i++) {
			String[] data = {
				rec[i].getDiscID(),
				rec[i].getTitle(),
				rec[i].getCategory() };
			CDTableModel.addRow(data);
		}
	}
	
	/* Refreshes the TrackTable */
	private void refreshTrackTable(int index) {
		clearTable(trackTable);
		CDDBEntry entry = null;
		try {
			entry = (CDDBEntry)fdb.readCDInfo(rec[index]);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(
				panel,
				ex.getMessage(),
				"CDDBPlugin",
				JOptionPane.ERROR_MESSAGE);
		}
		Track[] track = entry.extractTracks(true);
		for(int i = 0; i<track.length; i++) {
			trackTableModel.addRow(track[i].getTrackArray());
		}
	}
	
	/* clears a table */
	private void clearTable (JTable table) {
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
	}
	
	/** 
	 * gets the panel of the Plugin 
	 * @return the panel of the Plugin
	 */
	public JPanel getPanel() {
		return panel;
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == okButton) {
			if (store()) {
				removePanel();
				finish();
			}
		} else if (source == cancelButton) {
			removePanel();
			cancelOperation();
		} else if (source == skipButton) {
			removePanel();
			finish();
		} else if (source == refreshButton) {
			refreshPanel();
		}
	}
	
	private boolean store() {
		if (CDTable.getSelectedRowCount() == 0) {
			message("Es wurde keine CD ausgew\u00e4hlt.");
			return false;
		} else {
		
			DataElement element = pluginEvent.getDataElement();
			int index = CDTable.getSelectedRow();
			
			String[] tmp1 = getPropertie("titlefieldname");
			String[] tmp2 = getPropertie("categoryfieldname");
			String[] tmp3 = getPropertie("artistFieldname");
			
			if (tmp1 != null) {
				String titleFieldname = tmp1[0];
				if (element.hasField(titleFieldname)) {
					element.setField(titleFieldname, rec[index].getTitle());
				}
			}
			if (tmp2 != null) {
				String categoryFieldname = tmp2[0];
				if (element.hasField(categoryFieldname)) {
					element.setField(categoryFieldname, rec[index].getCategory());
				}
			}
			if (tmp3 != null) {
				String artistFieldname = tmp3[0];
				if (element.hasField(artistFieldname)) {
					// TODO how to get artist?
					element.setField(artistFieldname, rec[index].getTitle());
				}
			}
			return true;
		}
	}
}

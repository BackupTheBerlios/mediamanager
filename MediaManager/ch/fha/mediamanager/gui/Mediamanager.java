// $Id: Mediamanager.java,v 1.1 2004/05/24 11:32:38 radisli Exp $
package ch.fha.mediamanager.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.prefs.*;

/**
 * Mediamanager application
 *
 * @author Roman Rietmann
 */
public class Mediamanager extends WindowAdapter implements
	ActionOccurredListener
{
	public MainActionListener mainActionListener;
	
	private static JFrame mainWindow;
	private static JToolBar toolBar;
	private static Preferences prefs = Preferences.userNodeForPackage(Mediamanager.class);

	// default settings
	private static final int defaultWindowWidth = 400;
	private static final int defaultWindowHeight = 300;
	private static final int defaultScreenMode = JFrame.NORMAL;

	// information loaded from the package/manifest
	private static final String apptitle = "MediaManager";

	// keywords that name the settings
	private static final String WINDOW_WIDTH = "windowWidth";
	private static final String WINDOW_HEIGHT = "windowHeight";
	private static final String SCREEN_MODE = "screenMode";
	private static final String NOTSAVED = "notSaved";

	/**
	 *  Private to prevent instantiation.
	 */
	private Mediamanager() {
		Package p = getClass().getPackage();
	}

	public static void main(String[] args) {
		drawLayout();
	}

	/**
	 * Saves the current settings.
	 */
	public static void savePrefs() throws BackingStoreException {
		Dimension windowSize = mainWindow.getSize();
		int windowWidth = windowSize.width;
		int windowHeight = windowSize.height;
		int screenMode = mainWindow.getExtendedState(); 
		
		try {
			prefs.putBoolean(NOTSAVED, false);
			prefs.putInt(WINDOW_WIDTH, windowWidth);
			prefs.putInt(WINDOW_HEIGHT, windowHeight);
			prefs.putInt(SCREEN_MODE, screenMode);
			prefs.flush();
		} catch(BackingStoreException e) {
			display(e);
		}
	}

	public static void loadPrefs() throws BackingStoreException {
		boolean notSaved = prefs.getBoolean(NOTSAVED, true);
		try {
			// store preferences if run for the first time
			if(notSaved) { 
				savePrefs();
			}
		}
		catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}

	public static void display(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			mainWindow,
			e.getMessage(),
			"Fehler",
			JOptionPane.ERROR_MESSAGE);
	}

	public static void drawLayout() {
		mainWindow = new JFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle(apptitle);
		mainWindow.setSize(
				prefs.getInt(WINDOW_WIDTH, defaultWindowWidth),
				prefs.getInt(WINDOW_HEIGHT, defaultWindowHeight));
		
		mainWindow.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					close();
				}
			}
		);
		
		final JMenuBar menuBar = new JMenuBar();
		mainWindow.setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		// FILE MENU
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					close();
				}
			}
		);
		
		JMenuItem add = new JMenuItem("Add");
		fileMenu.add(add);
		add.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					addToolbarElement(getToolBarButton("images/LINE.gif", "Select"));
				}
			}
		);

		Container cp = mainWindow.getContentPane();
		cp.setLayout(new BorderLayout());

		toolBar = new JToolBar();
		addToolbarElement(getToolBarButton("images/SELECTION.gif", "Select"));
		cp.add(toolBar, BorderLayout.NORTH);
		
		JPanel modPanelHolder = new JPanel(new BorderLayout());
		JPanel modPanel = new JPanel(new GridLayout(2, 1));
		
		
		JButton jb1 = new JButton("Test1");
		JButton jb2 = new JButton("Test2");
		modPanel.add(jb1);
		modPanel.add(jb2);
		modPanelHolder.add(modPanel, BorderLayout.NORTH);
		cp.add(modPanelHolder, BorderLayout.WEST);
		
		cp.add(new MainView(), BorderLayout.CENTER);
		mainWindow.show();
		mainWindow.setExtendedState(
				prefs.getInt(SCREEN_MODE, defaultScreenMode));
	}
	
	public static JButton getToolBarButton(String image, String toolTipText) {
		Icon icon = new ImageIcon(image);
		JButton newButton = new JButton(icon);
		newButton.setMargin(new Insets(0,0,0,0));
		newButton.setToolTipText(toolTipText);
		return newButton;
	}
	
	public static void addToolbarElement(JButton newButton) {
		Container cp = mainWindow.getContentPane();
		toolBar.add(newButton);
		mainWindow.show();
	}

	public static void close() {
		try {
			savePrefs();
		} catch(Exception e) {
			display(e);
		}
	}

	public void windowClosed(WindowEvent e) {
		close();
	}

	/**
	 * Get the extension of a file, converted to lower case.
	 * From <a target="_blank" href="http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html#filters">Sun's Java Tutorial</a>.
	 */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if(i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	/**
	 * Very simple file filter.
	 */
	private static class FF extends javax.swing.filechooser.FileFilter {
		private String suffix;
		private String desc;

		/** Creates a new file filter. */
		private FF(String suffix, String desc) {
			this.suffix = suffix;
			this.desc = desc;
		}

		public boolean accept(File f) {
			return f.isDirectory() || suffix.equals(Mediamanager.getExtension(f));
		}

		public String getDescription() {
			return desc;
		}
	}

	/**
	 * Deserializes an object from a file.
	 * Presents a file chooser to the user to select a file to load.
	 *
	 * @param filter a file filter specifying the files to be displayed.
	 * @return the first object found in the file,
	 *         or <code>null</code> if the user cancelled the action.
	 */
	public static Object loadFile(javax.swing.filechooser.FileFilter filter) throws IOException, ClassNotFoundException {
		Object o = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		if(fc.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
			o = is.readObject();
			is.close();
		}
		return o;
	}

	/**
	 * Serializes an object to a file.
	 * Presents a file chooser to the user to either select a file to
	 * overwrite, or to enter a filename.
	 *
	 * @param o the object to be serialized.
	 * @param filter a file filter specifying the files to be displayed.
	 * @param defaultName a default name for the file.
	 * @return the first object found in the file,
	 *         or <code>null</code> if the user cancelled the action.
	 * @throws IllegalArgumentException if the object is <code>null</code>.
	 * @throws IOException if an I/O error occurs.
	 */
	private static void save(Serializable o, javax.swing.filechooser.FileFilter filter, String defaultName) throws IOException {
		if(o == null) throw new IllegalArgumentException("Keine Daten zum speichern.");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		if(defaultName != null) fc.setSelectedFile(new File(defaultName));
		if(fc.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()));
			os.writeObject(o);
			os.flush();
			os.close();
		}
	}

	public void colorChanged(MainActionListener mal) {
		System.out.println("Action: " + mal);
	}
}

package ch.fha.mediamanager.gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author ia02vond
 * @version $Id: SortDecorator.java,v 1.1 2004/06/23 18:24:03 ia02vond Exp $
 */
public class SortDecorator
	extends AbstractTableModel
	implements TableModelListener, MouseListener {

	/** The table where this <code>SortDecorator</code> set as table model. */
	private JTable table;
	
	/** 
	 * The real table model. This <code>SortDecorator</code> will switch itself
	 * between the table and the real table model.
	 */
	private TableModel realModel;

	/** The new indexes after sorting */
	private int indexes[];
	
	/** The last column index which was used to sort */
	private int lastSort = -1;
	/** True if the data are ascended sorted */
	private boolean ascended;
	/** True if the data are sorted */
	private boolean isSorted;
	
	/** List of announced <code>SortDecoratorListener</code> */
	private LinkedList sortDecoratorListenerList = new LinkedList();
	
    // --------------------------------
    // CONSTRUCTORS
    // --------------------------------
    
	/**
	 * Initializes a <code>SortDecorator</code>. 
	 * @param table   a table
	 * @param model   a table model
	 */
	public SortDecorator(JTable table, TableModel model) {
		if (table == null) {
			throw new IllegalArgumentException("null tables are not allowed");
		}
		this.table = table;

		if (model == null) {
			throw new IllegalArgumentException("null models are not allowed");
		}
		this.realModel = model;

		table.setModel(this);
		
		realModel.addTableModelListener(this);
		
		ascended = true;
		isSorted = false;
		allocate();
		
		JTableHeader hdr = (JTableHeader)table.getTableHeader();
		
		hdr.addMouseListener(this);
	}
	
    /**
     * 
     *
     */
	private void allocate() {
		indexes = new int[getRowCount()];
		
		for (int i=0; i<indexes.length; i++) {
			indexes[i] = i;
		}
		
		if (lastSort != -1) {
			sort(lastSort, false);
		}
	}
	
	/**
	 * Sorts the data by using the given column.
	 * @param column    the column
	 */
	public void sort(int column) {
		sort(column, true);
	}

	/**
	 * Private method which is used in <code>sort()</code>
	 * @param column    the column
	 * @param reverse   if true the indexes are only reversed
	 * @see #sort()
	 */
	private void sort(int column, boolean reverse) {
		int rowCount = getRowCount();
		
		if (lastSort == column && reverse) {
			// reverse
			for (int i=0; i<rowCount/2; i++) {
				swap (i, rowCount-1-i);
			}
			ascended = !ascended;
		} else {
			// sort
			for (int i=0; i<rowCount; i++) {
				for (int j=i+1; j<rowCount; j++) {
					if (compare(indexes[i], indexes[j], column) > 0) {
						swap(i,j);
					}
				}
			}
			ascended = true;
		}
		lastSort = column;
		isSorted = true;
		
		fireTableStructureChanged();
		fireSortingChanged();
	}
	
	/**
	 * Swaps the two <code>indexes</code> elements with the specified indexes
	 * <code>i</code> and <code>j<code>. It's used for sorting.
	 * @param i    first <code>indexes</code> element
	 * @param j    second <code>indexes</code> element
	 */
	private void swap(int i, int j) {
		int tmp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = tmp;
	}
	
	/**
	 * Compares the two column values of the given row indexes by invoking the
	 * <code>Object</code>'s <code>compareTo</code> method. It's used for sorting. 
	 * @param i       first row index
	 * @param j       second row index
	 * @param column  the column index
	 * @return the value <code>0</code> if the argument <code>i</code> is equal to
     *         <code>j</code>; a value less than <code>0</code> if <code>i</code>
     *         is less than the <code>j</code>; and a value greater than 
     *         <code>0</code> if <code>i</code> is greater than <code>j<code>.
	 */
	private int compare(int i, int j, int column) {
		Object io = realModel.getValueAt(i, column);
		Object jo = realModel.getValueAt(j, column);
		
		if (io instanceof Comparable && jo instanceof Comparable) {
			return ((Comparable)io).compareTo(jo);
		} else {
			int c = jo.toString().compareTo(io.toString());
			return (c < 0) ? -1 : ((c > 0) ? 1 : 0);
		}
	}
    
    // --------------------------------
    // MUTATORS
    // --------------------------------
    
    /** Delegated to the real table model.<p>{@inheritDoc} */
    public void setValueAt(Object value, int row, int column) {
        realModel.setValueAt(value, indexes[row], column);
    }
    
    // --------------------------------
    // ACCESSORS
    // --------------------------------
	
	/**
	 * Returns the new index after sorting of the row with the specified index.
	 * @param  index    row index
	 * @return row index after sorting
	 */ 
	public int getRowSortIndex(int index) {
		return indexes[index];
	}
	
	/** 
	 * Returns the index of the column which was used to sort the table.
	 * @return column index which the data are sorted by
	 */
	public int getColumnSortIndex() {
		return table.convertColumnIndexToModel(lastSort);
	}
	
	/**
	 * Returns the name of the column which was used to sort the table.
	 * @return column name which the data are sorted by
	 */
	public String getColumnSortName() {
		return table.getColumnName(getColumnSortIndex());
	}
	
	/** Delegated to the real table model.<p>{@inheritDoc} */
	public Object getValueAt(int row, int column) {
		return realModel.getValueAt(indexes[row], column);
	}

	/** Delegated to the real table model.<p>{@inheritDoc} */
	public int getColumnCount() {
		return realModel.getColumnCount();
	}

	/** Delegated to the real table model.<p>{@inheritDoc} */
	public int getRowCount() {
		return realModel.getRowCount();
	}
	
	/** Delegated to the real table model.<p>{@inheritDoc} */
	public String getColumnName(int columnIndex) {
		return realModel.getColumnName(columnIndex);
	}
	
	/** Delegated to the real table model.<p>{@inheritDoc} */
	public Class getColumnClass(int columnIndex) {
		return realModel.getColumnClass(columnIndex);
	}
    
    /**
     * Returns true if the data are ascended sorted.
     * @return true if ascended sorted
     */
    public boolean isAscended() {
        return ascended;
    }
    
    /**
     * Returns true if the data are sorted by a column index.
     * @return true if sorted
     */
    public boolean isSorted() {
        return isSorted;
    }
	
	/** Delegated to the real table model.<p>{@inheritDoc} */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return realModel.isCellEditable(rowIndex, columnIndex);
	}
	
	/**
	 * Adds a <code>TableModelListener</code> to the real table model.
	 * @param listener    the tablemodellistener
	 * @see   TableModel.#addTableModelListener()
	 */
	public void addTableModelListener(TableModelListener listener) {
		realModel.addTableModelListener(listener);
	}
	
	/**
	 * Removes a <code>TableModelListener</code> from the real table model.
	 * @param listener    the tablemodellistener
	 * @see   TableModel.#removeTableModelListener()
	 */
	public void removeTableModelListener(TableModelListener listener) {
		realModel.removeTableModelListener(listener);
	}
	
	/**
	 * Adds a listener to the list that is notified each time the sort decorator
	 * changes.
	 * @param listener    a <code>SortDecoratorListener</code> to add
	 * @see SortDecoratorListener
	 */
	public void addSortDecoratorListener(SortDecoratorListener listener) {
		sortDecoratorListenerList.add(listener);
	}
	
	/**
	 * Removes a listener from the list that is notified each time the sort decorator
	 * changes.
	 * @param listener    a <code>SortDecoratorListener</code> to remove
	 * @see SortDecoratorListener
	 */
	public void removeSortDecoratorListener(SortDecoratorListener listener) {
		sortDecoratorListenerList.remove(listener);
	}
    
    // --------------------------------
    // EVENTHANDLERS
    // --------------------------------
    
    /**
     * Invoked when the table model changes. If needed the data
     * are resorted.
     * @param e    the <code>TableModelEvent</code>
     * @see TableModel.#tableChanged()
     */
    public void tableChanged(TableModelEvent e) {
        allocate();
    }
	
	/**
	 * Informs every announced <code>SortDecoratorListener</code> by
	 * invoking the <code>sortingChanged</code> method.
	 * @see SortDecoratorListener
	 */
	public void fireSortingChanged() {
		Iterator i = sortDecoratorListenerList.iterator();
		while (i.hasNext()) {
			((SortDecoratorListener)i.next()).sortingChanged();
		}
	}

	/**
	 * Sorts if the user clicks on a table column header.
	 * @param e    MouseEvent
	 * @see   MouseListener.#mouseClicked()
	 */
	public void mouseClicked(MouseEvent e) {
		TableColumnModel tcm = table.getColumnModel();
		int vc = tcm.getColumnIndexAtX(e.getX());
		int mc = table.convertColumnIndexToModel(vc);
		
		sort(mc);
	}

	/** @see MouseListener.#mouseEntered() */
	public void mouseEntered(MouseEvent e) {}
	/** @see MouseListener.#mouseExited() */
	public void mouseExited(MouseEvent e) {}
	/** @see MouseListener.mousePressed() */
	public void mousePressed(MouseEvent e) {}
	/** @see MouseListener.mouseReleased() */
	public void mouseReleased(MouseEvent e) {}
}

package ch.fha.mediamanager.gui.util.dataInput;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * A </code>Document</code> class which checks whether
 * the input string is too long and - if demanded - 
 * whether the string is about an integer number.
 * 
 * @author ia02vond
 * @version $id$
 */
public class DataInputDocument extends PlainDocument {
	
	private int maxLength;
	private JTextComponent tc;
	private boolean integerValues;
	
	public DataInputDocument(JTextComponent tc, int maxLength, boolean integerValues) {
		this.maxLength = maxLength;
		this.tc = tc;
		this.integerValues = integerValues;
	}
	
	public void insertString(int offs, String str, AttributeSet a)
		throws BadLocationException {
		
		if (tc.getText().length() + str.length() > maxLength) {
			return;
		} else if (integerValues) {
			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return;
			}
		}

		super.insertString(offs, str, a);
	}
}
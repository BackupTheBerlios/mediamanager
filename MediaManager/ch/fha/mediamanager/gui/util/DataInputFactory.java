package ch.fha.mediamanager.gui.util;

import ch.fha.mediamanager.data.Field;
import ch.fha.mediamanager.data.MetaField;
import ch.fha.mediamanager.gui.util.dataInput.*;

/**
 * @author ia02vond
 * @version $id$
 */
public class DataInputFactory {
	
	/**
	 * Returns an instance of an <code>DataInput</code>
	 * implementation according to the specified
	 * <code>Field</code> type (int, string ...). A
	 * <code>DataInput</code> object can be used for
	 * guis which contains input fields without knowing
	 * what type the field has.
	 */
	public static DataInput create(Field field) {
		switch (field.getMetaField().getType()) {
			case MetaField.BOOLEAN:
				return new BooleanDataInput(field);
			case MetaField.INT:
				return new IntegerDataInput(field);
			case MetaField.PK:
				return null;
			case MetaField.USERID:
				return null;
			case MetaField.VARCHAR:
				return new VarcharDataInput(field);
			case MetaField.TEXT:
				return new TextDataInput(field);
			case MetaField.LIST:
				return new ListDataInput(field);
			default:
				return null;
		}
	}
}

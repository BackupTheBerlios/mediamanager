package ch.fha.pluginstruct;

/**
 * @author ia02vond
 * @version $Id: Version.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public class Version implements Comparable {

	private String value;
	private int[] splits;
	
	public Version(String v) {
		if (v == null) {
			splits = null;
			return;
		}
		value = v;
		String[] strSplits = v.split("_");
	 	splits = new int[strSplits.length];
		
		for (int i=0; i<strSplits.length; i++) {
			try {
				splits[i] = Integer.parseInt(strSplits[i]);
			} catch (NumberFormatException e) {
				splits = null;
			}
		}
	}
	
	public String toString() {
		return validate() ? value : "invalidate version";
	}
	
	public boolean validate() {
		return (splits != null && splits.length > 0);
	}

	public int compareTo(Object o) {
		if (o instanceof Version) {
			Version v = (Version)o;
			if (validate() && !v.validate()) {
				return 1;
			} else if (!validate() && v.validate()) {
				return -1;
			} else if (!validate() && !v.validate()) {
				return 0;
			}
			int i = 0;
			while (i < splits.length && i < v.splits.length) {
				if (splits[i] > v.splits[i]) {
					return 1;
				} else if (splits[i] < v.splits[i]) {
					return -1;
				}
				i++;
			}
			if (splits.length > v.splits.length) {
				return 1;
			} else if (splits.length < v.splits.length) {
				return -1;
			} else {
				return 0;
			}
		} else throw new IllegalArgumentException();
	}
	
	public String getValue() {
		return value;
	}
	
	public int[] split() {
		return splits;
	}
	
	/*
	 * version testing
	 */
	public static void main(String[] args) {
		String[] v1 = {"1","2","3", "1_1", "1_1", "1_1", "1_1_1", "1_1_1",   "1_1_1", "1_1_1", };
		String[] v2 = {"2","2","2", "1_0", "1_1", "1_2", "1_1",   "1_1_1_1", "1_1_2", "2"};
		int[]   req = {-1,  0,  1,   1,     0,     -1,    1,       -1,        -1,      -1};
		for (int i=0; i<v1.length; i++) {
			Version ver1 = new Version(v1[i]);
			Version ver2 = new Version(v2[i]);
			if (ver1.compareTo(ver2) == req[i]) {
				System.out.println ("ok");
			} else {
				System.out.println ("ERR: v1=" + v1[i] + " v2=" + v2[i]);
			}
		}
	}
}

package ch.fha.pluginstruct;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ia02vond
 * @version $Id: KeyboardInOut.java,v 1.1 2004/05/13 12:09:40 ia02vond Exp $
 */
public class KeyboardInOut implements InOut {

	public void message(String message) {
		System.out.println(message);
	}

	public int option(String message, String[] options) {
		System.out.println(message);
		for (int i=0; i<options.length; i++) {
			System.out.println("  "+i+". " + options[i]);
		}
		System.out.print("[0-"+(options.length-1)+"]$");
		int choice = -1;
		while (choice < 0 || choice >= options.length) {
			choice = readInt();
		}
		return choice;
	}
	
	public void exception(PluginLogicException e) {
		System.err.println("PluginLogicException Details:");
		e.printStackTrace();
	}
	
	private int readInt() {
		char[] inChar = new char[256];
		int length = 0;
		char ch;
		boolean repeat = true;
		while (repeat) {
			repeat = false;
			try {
				InputStreamReader keyboard = new InputStreamReader(System.in);
				ch = (char)keyboard.read();
				while (ch == '\n' || ch == '\r' || ch == ' ') {
					ch = (char) keyboard.read();
				}
				while (ch != ' ' && ch != '\n' && ch != '\r' && ch >= 0) {
					inChar[length] = ch;
					length++;
					ch = (char) keyboard.read();
				}
			} catch (IOException e) {
				repeat = true;
			}
			try {
				int value = Integer.parseInt(new String(inChar, 0, length));
				return value;
			} catch (NumberFormatException e) {
				repeat = true;
			}
		}
		return -1;
	}
}

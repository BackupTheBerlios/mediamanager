package plugins.cddb;

public class UnsupportedOperatingSystemException extends Exception {
	private static final String DEFAULT_MESSAGE = "Unsupported OS";
	
	public UnsupportedOperatingSystemException () {
		super(DEFAULT_MESSAGE);
	}
	
	public UnsupportedOperatingSystemException(String message) {
		super(message);
	}
}

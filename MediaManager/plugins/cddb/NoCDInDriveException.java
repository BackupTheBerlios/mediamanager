package plugins.cddb;

public class NoCDInDriveException extends Exception {
	private static final String DEFAULT_MESSAGE = "No CD in drive";
	
	public NoCDInDriveException() {
		super(DEFAULT_MESSAGE);
	}
	
	public NoCDInDriveException(String message) {
		super(message);
	}
}

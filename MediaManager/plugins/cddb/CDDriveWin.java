package plugins.cddb;

import java.io.*;

/**
* This class is a Win32 specific implementation of CDDrive,
* which uses native function calls to access the CD-ROM drive.
* In order to run this class, you will need the library
* <dfn>CDDriveWin.dll</dfn> in your <dfn>java.library.path</dfn>
* or your windows path environment. <p>
* If you encounter an <code>UnsatisfiedLinkError</code> because
* you don't have the required library for this class, you can downlad
* the file from http://www.antelmann.com/developer/.
* @author Holger Antelmann
*/
public class CDDriveWin extends CDDrive implements NativeCode
{
    static {
        System.loadLibrary("CDDriveWin");
        init();
    }

    protected int driveNum;


    public CDDriveWin () {
        this (0);
    }


    public CDDriveWin (int driveNum) {
        this.driveNum = driveNum;
    }


    static native void init ();


    public native String getCDDBQuery ();



    public void eject () {
        throw new UnsupportedOperationException();
    }


    public void play () {
        throw new UnsupportedOperationException();
    }


    public void stop () {
        throw new UnsupportedOperationException();
    }


    public void backward () {
        throw new UnsupportedOperationException();
    }


    public void forward () {
        throw new UnsupportedOperationException();
    }


    public void pause () {
        throw new UnsupportedOperationException();
    }


    /** old stuff; doesn't work properly */
    static String readCDDBID() throws IOException {
        String query = null;
				String osName = System.getProperty("os.name");
				Runtime rt = Runtime.getRuntime();
				Process p = null;
				try {
					if (osName.equals("Windows NT")||
							osName.equals("Windows 2000")||
							osName.equals("Windows XP")) {
						
						 p = rt.exec("cmd cddbidgen.exe");
						
					} else if (osName.equals("Windows 95")||
							osName.equals("Windows 98") ||
							osName.equals("Windows ME")) {
						
						 p = rt.exec("command cddbidgen.exe");
						
					} 
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //StringBuffer result = new StringBuffer();
        OutputStream out = p.getOutputStream();
        out.write(65);
        out.flush();
        out.close();
        String line = "";
        int i = -1;
        while (line != null) {
            line = in.readLine();
            //System.out.println("debug: " + line);
            if (line != null) {
                i = line.indexOf("cddb query");
                if (i > -1) break;
            }
        }
        in.close();
        p.destroy();
        if (line != null && (i > -1)) {
            return line.substring(i);
        } else {
            return null;
        }
    }
}

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
public class CDDriveLin extends CDDrive
{
    
		protected int driveNum;


    public CDDriveLin () {
        this (0);
    }


    public CDDriveLin (int driveNum) {
        this.driveNum = driveNum;
    }

    public String getCDDBQuery () {
			try {
				String cddbid = readCDDBID();
				if (cddbid == null) return null;
				else return "cddb query " + cddbid;
			}
			catch (IOException ex) {ex.printStackTrace();}
			/* if everything goes wrong */
			return null;
		}
		



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
					/* eventually not every linux has its cdrom
					   at the same device */
					if (osName.equals("Linux")) {
						 p = rt.exec("plugins/cddb/cd-discid /dev/cdrom");	
					} 
				} catch (IOException ex) {
					ex.printStackTrace();
				}
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        OutputStream out = p.getOutputStream();
        out.write(65);
        out.flush();
        out.close();
        String line = in.readLine();
				//System.out.println("debug: " + line);
				in.close();
				p.destroy();
				if (line != null) {
					return line;
				}
				else {
					return null;
				}	
    }
}

package plugins.cddb;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;


/**
* A specialized File class that provides some additional functionality.
* MyFile adds no members to class File, only methods.
* @author Holger Antelmann
* @since 3/23/2002
*/
public class MyFile extends File
{
    public MyFile (File file) { super(file.getPath()); }


    public MyFile (String pathname) { super(pathname); }


    public MyFile (String parent, String child) { super(parent, child); }


    public MyFile (File parent, String child) { super(parent, child); }


    public MyFile (URI uri) { super(uri); }


    /** returns <code>setLastModified(System.currentTimeMillis())</code> */
    public boolean touch () {
        return setLastModified(System.currentTimeMillis());
    }


    /**
    * returns the file type denoted by its file extension.
    * The extension is the String of those characters that
    * follow the last 'dot' (".") in the file name in lowercase.
    * If no extension is present, null is returned.
    */
    public String getFileExtension () {
        String fname = getName();
        int i = fname.lastIndexOf('.');
        if ((i > 0) && (i < (fname.length() - 1))) {
            return fname.substring(i+1).toLowerCase();
        }
        return null;
    }


    /** returns all files including those in subdirectories recursively */
    public MyFile[] listFilesInTree () {
        ArrayList list = new ArrayList();
        File[] files = listFiles();
        if (files == null) return null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                list.addAll(Arrays.asList(new MyFile(files[i]).listFilesInTree()));
            } else {
                list.add(new MyFile(files[i]));
            }
        }
        return (MyFile[]) list.toArray(new MyFile[list.size()]);
    }


    public MyFile[] listFilesInTree (FileFilter filter) {
        MyFile[] all  = listFilesInTree();
        if (all == null) return null;
        ArrayList list = new ArrayList();
        for (int i = 0; i < all.length; i++) {
            if (filter.accept(all[i])) {
                list.add(all[i]);
            }
        }
        return (MyFile[]) list.toArray(new MyFile[list.size()]);
    }


    public MyFile[] listFilesInTree (FilenameFilter filter) {
        MyFile[] all  = listFilesInTree();
        ArrayList list = new ArrayList();
        for (int i = 0; i < all.length; i++) {
            if (filter.accept(this, all[i].getName())) {
                list.add(all[i]);
            }
        }
        return (MyFile[]) list.toArray(new MyFile[list.size()]);
    }


    /**
    * copies this file to the given destination file.
    * This method can copy files of any size.
    */
    public void copyTo (File destinationFile) throws IOException {
        FileInputStream in = null;;
        FileOutputStream out = null;
        IOException exception = null;
        try {
            in = new FileInputStream(this);
            out = new FileOutputStream(destinationFile);
            byte[] bytes;
            try {
                bytes = new byte[(int)length()];
                in.read(bytes);
                out.write(bytes);
                out.flush();
            } catch (OutOfMemoryError e) {
                // try again in smaller steps
                long mem = Runtime.getRuntime().freeMemory();
                bytes = new byte[(int) mem / 10]; // 10 is an arbitrary number
                int n = 0;
                while (n > -1) {
                    n = in.read(bytes);
                    if (n < 0) break;
                    out.write(bytes, 0, n);
                    out.flush();
                }
            }
            out.close();
            in.close();
            destinationFile.setLastModified(lastModified());
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception != null) throw exception;
    }


    /**
    * Assuming the current MyFile is a directory, this method synchronizes
    * it with the destination directory by writing all files into the
    * destination directory that exist in the origin directory. <p>
    * The procedure follows the follwing rules:
    * <ul>
    * <li>All files that only exist in the first directory, will be copied over</li>
    * <li>If a file already exists at the destination, it will only be overwritten if it is more recent</li>
    * <li>Files at the destination that do not exist at the origin will be deleted</li>
    * <li>The above will be performed recursively for each subdirectory</li>
    * </ul>
    * Note that file attributes are ignored, i.e. if a file is copied, the default
    * attributes are kept and may be different from those of the originating file.
    * If a directory is to be removed in the destination dir, the method makes
    * a best effort to do so.
    * @throws IllegalArgumentException if either the current MyFile or the given
    *         destination are not directories
    * @return array of files that could not be copied or deleted; ideally,
    *         this array would be empty
    */
    public MyFile[] synchronizeDir (File destinationDir) throws IOException {
        return synchronizeDir(destinationDir, null);
    }


    /**
    * This method allows for intermediate feedback through the logger;
    * otherwise it's the same as the other synchronizeDir() method. <p>
    * This method allows e.g. GUIs to display status messages by implementing
    * a special LogWriter. <br>
    * The given logger may be null, otherwise the usage is as follows:
    * <ul>
    * <li>Every processed directory produces a level Level.FINE message naming
    * the given directory</li>
    * <li>Every copied/deleted file produces a level Level.FINER message naming the file</li>
    * <li>Every unsuccessfull copy/delete operation produces a level Level.WARNING
    * message naming the file/directory and containing the IOException as a parameter</li>
    * </ul>
    * @see #synchronizeDir(File)
    * @see com.antelmann.util.logging.LogWriter
    */
    public MyFile[] synchronizeDir (File destinationDir, Logger logger)
        throws IOException
    {
        return synchronizeDir(destinationDir, logger, null);
    }


    /**
    * This method allows for intermediate feedback and interactive stopping;
    * otherwise it's the same as the other synchronizeDir() method. <p>
    * This method allows monitoring threads to abort the synchronization by
    * disabling the given monitor during processing.
    * The monitor/logger may be null; monitor checks occur for each directory
    * and after each copy operation.
    * @see #synchronizeDir(File, Logger)
    * @see com.antelmann.util.logging.LogWriter
    */
    public MyFile[] synchronizeDir (File destinationDir, Logger logger, Monitor monitor)
        throws IOException
    {
        if (!isDirectory() || !destinationDir.isDirectory()) {
            String s = "both, this MyFile and the given parameters must be directories";
            throw new IllegalArgumentException(s);
        }
        ArrayList errors = new ArrayList();
        File[] d = destinationDir.listFiles();
        HashMap dmap = new HashMap();
        for (int i = 0; i < d.length; i++) {
            dmap.put(d[i].getName(), d[i]);
        }
        ArrayList newDirs  = new ArrayList();
        File[] o = listFiles();
        if (logger != null) logger.log(this, Level.FINE,
            "processing " + this + " (contains " + o.length + " files)");
        if (monitor != null && monitor.disabled())
            return (MyFile[]) errors.toArray(new MyFile[errors.size()]);
        for (int i = 0; i < o.length; i++) {
            if (o[i].isDirectory()) {
                newDirs.add(new MyFile(o[i]));
            } else {
                String name = o[i].getName();
                if (dmap.containsKey(name)) {
                    if (((File)dmap.get(name)).lastModified() >= o[i].lastModified()) {
                        dmap.remove(name);
                        continue;
                    }
                }
                try {
                    new MyFile(o[i]).copyTo(new File(destinationDir, name));
                    if (logger != null) logger.log(this, Level.FINER,
                        "copied file: " + o[i]);
                } catch (IOException e) {
                    errors.add(new MyFile(o[i]));
                    if (logger != null) logger.log(this, Level.WARNING,
                        "could not copy file: " + o[i],
                        new Object[] {e});
                }
                dmap.remove(name);
            }
            if (monitor != null && monitor.disabled())
                return (MyFile[]) errors.toArray(new MyFile[errors.size()]);
        }
        Iterator i = dmap.keySet().iterator();
        while (i.hasNext()) {
            MyFile tbd = new MyFile((File) dmap.get(i.next()));
            if (tbd.isDirectory()) {
                if (tbd.removeTree()) {
                    if (logger != null) logger.log(this, Level.FINER,
                        "deleted directory: " + tbd);
                } else {
                    errors.add(tbd);
                    if (logger != null) logger.log(this, Level.WARNING,
                        "could not delete directory: " + tbd);
                }
            } else {
                if (tbd.delete()) {
                    if (logger != null) logger.log(this, Level.FINER,
                        "deleted file: " + tbd);
                } else {
                    errors.add(tbd);
                    if (logger != null) logger.log(this, Level.WARNING,
                        "could not delete file: " + tbd);
                }
            }
        }
        i = newDirs.iterator();
        while (i.hasNext()) {
            MyFile nextdir = (MyFile) i.next();
            File ddir = new File(destinationDir, nextdir.getName());
            if (!ddir.mkdir()) errors.add(ddir);
            nextdir.synchronizeDir(ddir);
        }
        return (MyFile[]) errors.toArray(new MyFile[errors.size()]);
    }


    /**
    * If the MyFile at hand is a directory, this method attempts
    * to delete the entire subtree - <b>use with caution</b>!<p>
    * If the MyFile is not a directory, false is returned immediately (nothing
    * gets deleted).
    * If it is a directory but it is not empty, it will attempt to first
    * delete all content recursively and then again the top directory.
    * The method may return false as soon as any subdirectory could
    * not be removed, i.e. not necessarily everything that could be deleted
    * within the tree may be deleted if the method returns false in the case of
    * a directory.
    * @return true only if MyFile is a directory and the entire subtree was
    *              successfully removed
    */
    public boolean removeTree () {
        if (!isDirectory()) return false;
        if (delete())       return true;
        File[] f = listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].isDirectory()) {
                if (!new MyFile(f[i]).removeTree()) return false;
            } else {
                if (!f[i].delete()) return false;
            }
        }
        return (delete());
    }


    /**
    * returns the index within the content of this file of the first
    * occurrence of the specified pattern; -1 is returned if pattern
    * is not found.
    * This method is to be used with smaller/medium sized text files;
    * the entire file must fit into memory.
    * @throws OutOfMemoryError if the file doesn't fit into memory
    */
    public int indexOf (String pattern) throws IOException, OutOfMemoryError {
        return getContentAsString().indexOf(pattern);
    }


    /**
    * searches for the first pattern and replaces all occurrences with
    * the second pattern (no regular expressions used).
    * This method is to be used with smaller/medium sized text files;
    * the entire file must fit into memory (twice, actually).
    * @throws OutOfMemoryError if the file doesn't fit into memory
    */
    public void replace (String searchPattern, String replacePattern)
        throws IOException, OutOfMemoryError
    {
        String s = getContentAsString();
        s = Strings.replace(s, searchPattern, replacePattern);
        byte[] bytes = s.getBytes();
        IOException exception = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(this);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception != null) throw exception;
    }


    /**
    * searches for the first pattern and replaces all occurrences with
    * the second pattern.
    * Method not implemented, yet.
    */
    public void replace (byte[] searchPattern, byte[] replacePattern)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }


    /**
    * returns the entire file content as a String (using default encoding).
    * This method is better to be used with smaller/medium sized text files;
    * the entire file must fit into memory.
    * @see #getContentAsBytes()
    * @throws OutOfMemoryError if the file doesn't fit into memory
    */
    public String getContentAsString () throws IOException, OutOfMemoryError {
        return new String(getContentAsBytes());
    }


    /**
    * returns the entire file content as a byte array.
    * This method is better to be used with smaller/medium sized binary files;
    * the entire file must fit into memory.
    * @throws OutOfMemoryError if the file doesn't fit into memory
    * @see #getContentAsString()
    */
    public byte[] getContentAsBytes () throws IOException, OutOfMemoryError {
        IOException exception = null;
        FileInputStream in =  null;
        try {
            in = new FileInputStream(this);
            byte[] bytes = new byte[(int)length()];
            in.read(bytes);
            in.close();
            return bytes;
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        throw exception;
    }


    /**
    * writes the given text to the file and flushes;
    * if append is false, all previous content will be overwritten.
    * Suitable for text files only
    * @see #writeObject(Object)
    */
    public void writeText (String text, boolean append) throws IOException {
        IOException exception = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(this, append);
            fileWriter.write (text);
            fileWriter.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception != null) throw exception;
    }


    /** appends a platform specific linebreak into a text file */
    public void writeln () throws IOException {
        writeText(System.getProperty("line.separator", "\n"), true);
    }


    /**
    * writes the given bytes directly to the file and flushes;
    * if append is false, all previous content will be overwritten.
    * Suitable for binary files.
    * @see #writeObject(Object)
    */
    public void writeBytes (byte[] bytes, boolean append) throws IOException {
        IOException exception = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(this, append);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception != null) throw exception;
    }


    /**
    * serializes the given object, writes it to the file and flushes.
    * Multiple objects should be written with an ObjectOutputStream.
    * Use with binary files only.
    * @see #writeText(String, boolean)
    * @see #writeBytes(byte[], boolean)
    */
    public void writeObject (Object obj) throws IOException {
        IOException exception = null;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(this));
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            exception = e;
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception != null) throw exception;
    }


    /** loads a de-serialized object from the file into memory and returns it */
    public Object loadObject () throws IOException, ClassNotFoundException {
        Exception exception = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(this));
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            } catch (NullPointerException ignore) {}
        }
        if (exception instanceof IOException) {
            throw (IOException) exception;
        } else {
            throw (ClassNotFoundException) exception;
        }
    }


    /** the file must fit into memory */
    public void encrypt (SynchronousKey key) throws IOException {
        writeBytes(key.encode(getContentAsBytes()), false);
    }


    /** the file must fit into memory */
    public void decrypt (SynchronousKey key) throws IOException {
        writeBytes(key.decode(getContentAsBytes()), false);
    }


    /**
    * returns an Enumeration over the objects contained in a binary file
    * with serialized objects written with an ObjectOutputStream
    */
    public Enumeration getObjectEnumerator () {
        return new ObjectEnumerator();
    }



    /** Enumeration for <code>getObjectEnumerator()</code> */
    class ObjectEnumerator implements Enumeration
    {
        ObjectInputStream in;
        Object lastObject = null;

        ObjectEnumerator () {
            try {
                in = new ObjectInputStream(new FileInputStream(MyFile.this));
                lastObject = in.readObject();
            } catch (Exception e) {
                lastObject = null;
                //e.printStackTrace();
            }
        }

        protected void finalize () { try { in.close(); } catch (IOException e) {} }

        public boolean hasMoreElements () { return (lastObject != null); }

        public Object nextElement () {
            Object tmp = lastObject;
            lastObject = null;
            try {
                lastObject = in.readObject();
            } catch (Exception e) {
                //e.printStackTrace();
            } finally {
               	return tmp; 
            }
						
        }
    }
}

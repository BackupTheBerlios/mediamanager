package plugins.cddb;

import java.util.NoSuchElementException;
import java.util.Vector;


/**
* ArgumentParser is a helper class that parses arguments in results
* according to CDDB Protocol level 2.
* It works similarly to a StringTokenizer, but it properly handles
* arguments enclosed with quotes.
* @author Holger Antelmann
*/
public class ArgumentParser
{
    String  line;
    /** protected to allow custom delimiters for subclasses */
    protected String  delimiter = " \t\n\r\f";


    public ArgumentParser (String line) {
        this(line, 0);
    }


    public ArgumentParser (String line, int startingPosition) {
        this.line = line;
    }


    public String remainder () {
        while (hasMoreArguments() && isDelimiter(line.charAt(0)))
            line = line.substring(1);
        String rest = line;
        line = "";
        return rest;
    }


    public static String[] getAll (String line) {
        ArgumentParser ap = new ArgumentParser(line);
        Vector v = new Vector();
        while (ap.hasMoreArguments()) {
            v.add(ap.nextArgument());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }


    public boolean hasMoreArguments () {
        return (line.length() > 0)? true : false;
    }


    public String nextArgument () throws NoSuchElementException {
        if (!hasMoreArguments()) {
            throw new NoSuchElementException();
        }
        // strip slack on the beginning
        while (hasMoreArguments() && isDelimiter(line.charAt(0)))
            line = line.substring(1);
        if (line.length() < 1) {
            return "";
        }
        int pos = -1;
        String arg = null;
        if (line.charAt(0) == '\"') {
            // handle quoted argument
            pos = findNextQuote(1);
            arg = line.substring(1, pos);
        } else {
            // handle unquoted argument
            pos = findNextDelimiter(1);
            arg = line.substring(0, pos);
        }
        if (pos >= line.length()) {
            line = "";
        } else {
            line = line.substring(pos + 1);
        }
        return arg;
    }


    int findNextQuote (int from) {
        int i = from;
        for (; (i < line.length()) && !(line.charAt(i) == '\"'); i++);
        return i;
    }


    int findNextDelimiter (int from) {
        int i = from;
        for (; (i < line.length()) && !isDelimiter(line.charAt(i)); i++);
        return i;
    }


    boolean isDelimiter (char c) {
        for (int i = 0; i < delimiter.length(); i++) {
            if (c == delimiter.charAt(i)) return true;
        }
        return false;
    }


    public static void main (String[] args) {
        //String s = "one \"  two is this lenghy thing\" \"three\"";
        String s = "   one   two   \"  a long  line  \" ";
        System.out.println(s);
        ArgumentParser ap = new ArgumentParser(s);
        while (ap.hasMoreArguments()) {
            System.out.println("\"" + ap.nextArgument() + "\"");
        }
    }
}
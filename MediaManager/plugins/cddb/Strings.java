package plugins.cddb;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
* A utility class with some useful functions to manipulate String objects.
* @author Holger Antelmann
*/
public final class Strings
{
    /** represents the Euro currency symbol */
    public static final String EURO = "\u20AC";
    /** xml header line with utf8 */
    public static final String XML_HEADER = "<?xml version='1.0' encoding='utf-8'?>";
    /** represents the symbol for use in HTML or XML */
    public static final String LEFT_DOUBLE_QUOTE  = "&#147;";
    /** represents the symbol for use in HTML or XML */
    public static final String RIGHT_DOUBLE_QUOTE = "&#148;";
    /** represents the symbol for use in HTML or XML */
    public static final String TRADEMARK          = "&#153;";
    /** represents the symbol for use in HTML or XML */
    public static final String REGISTERED_TM      = "&#174;";
    /** represents the symbol for use in HTML or XML */
    public static final String COPYRIGHT          = "&#169;";

    /** used in encodeXML(String) and decodeXML(String) */
    static final String[][] translateArray  = new String[][] {
        { "  ", "&nbsp; " },
        { "<" , "&lt;"    },
        { ">" , "&gt;"    },
        { "\"", "&quot;"  },
        { "&" , "&amp;"   },
        { "'" , "&apos;"  },
        { "?" , "&szlig;" }
    };


    private Strings () {}


    /**
    * replaces every occurrence of oldSubString with
    * newSubString within the original String and returns the
    * resulting string (no regular expressions are used)
    */
    public static String replace (String original, String oldSubString, String newSubString)
    {
        String s = original;
        int pointer = s.indexOf(oldSubString);
        while (pointer > -1) {
            s = s.substring(0, pointer) + newSubString
                + s.substring(pointer
                + oldSubString.length(), s.length());
            pointer = s.indexOf(oldSubString, pointer + newSubString.length());
        }
        return s;
    }


    /**
    * replaces every occurrence of oldSubString (ignoring case) with
    * newSubString within the original String and returns the
    * resulting string (no regular expressions are used)
    */
    public static String replaceIgnoreCase (String original, String oldSubString, String newSubString)
    {
        String s = original;
        int pointer = indexOfIgnoreCase(s, oldSubString);
        while (pointer > -1) {
            s = s.substring(0, pointer) + newSubString
                + s.substring(pointer
                + oldSubString.length(), s.length());
            pointer = indexOfIgnoreCase(s, oldSubString, pointer + newSubString.length());
        }
        return s;
    }


    /** calls the method with the same name adding index 0 */
    public static int indexOfIgnoreCase (String textToSearch, String pattern) {
        return indexOfIgnoreCase(textToSearch, pattern, 0);
    }


    /*
    * works similar to <code>String.indexOf(String, int)</code>, but it ignores
    * case
    */
    public static int indexOfIgnoreCase (String textToSearch, String pattern, int fromIndex) {
        int n = pattern.length();
        while (textToSearch.length() > (fromIndex + n -1)) {
            if (textToSearch.regionMatches(true, fromIndex, pattern, 0, n)) {
                return fromIndex;
            }
            fromIndex++;
        }
        return -1;
    }


    /**
    * counts how many times the given pattern occurs in the given text.
    * Example: <code>count("ababababab", "abab")</code> returns 2.
    */
    public static int count (String text, String pattern) {
        int count = 0;
        int pos   = text.indexOf(pattern);
        while (pos > -1) {
            count++;
            pos = text.indexOf(pattern, pos + pattern.length());
        }
        return count;
    }


    /**
    * encodes a text string to use as text in an HTML or XML document
    * using the <code>translateArray</code>
    */
    public static String encodeXML (String text) {
        String s = text;
        for (int i = 0; i < translateArray.length; i++) {
            s = replace(s, translateArray[i][0], translateArray[i][1]);
        }
        return s;
    }


    /**
    * decodes an HTML or XML text sequence into human readable form
    * using the <code>translateArray</code>
    */
    public static String decodeXML (String text) {
        String s = text;
        for (int i = 0; i < translateArray.length; i++) {
            s = replace(s, translateArray[i][1], translateArray[i][0]);
        }
        return s;
    }


    public static String encodeUTF (String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String decodeUTF (String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
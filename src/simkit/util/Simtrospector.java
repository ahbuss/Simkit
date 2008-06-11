package simkit.util;

import java.lang.reflect.*;
import java.util.*;

/**
 *  A utility class to reduce the clutter in SimEntityBase and rationalize some
 *  common methods involving introspection.
 *
 *  @version $Id: Simtrospector.java 1000 2007-02-15 19:43:11Z ahbuss $
 *  @author Arnold Buss
**/

public class Simtrospector {

/**
* The list of prefixes that indicate an event method in addition to <code>prefix<code/>
**/
    private static Set<String> additionalPrefixes;

/**
* The primary prefix that indicates an event method (Default is "do").
**/
    private static String prefix;

    static {
        setPrefix("do");
        additionalPrefixes = null;
    }

/**
* Returns a Hashtable containing all of the public event methods for the given
* Class. Event methods are methods that begin with prefixes as determined by
* <code>setPrefix, addPrefix<code/> and other similar methods. The default prefix
* is "do"
**/
    public static Map<String, Method> getEventMethods(Class c) {
        Method[] allMethods = c.getMethods();
        List<Method> eventMethods = new ArrayList<Method>(allMethods.length);
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getName().startsWith(prefix)) {
                 eventMethods.add(allMethods[i]);
            }
            if (additionalPrefixes != null) {
                for (String prefix : additionalPrefixes) {
                    if (allMethods[i].getName().startsWith(prefix)) {
                         eventMethods.add(allMethods[i]);
                         break;
                    }
                }
            }
        }
        Map<String, Method> table = new LinkedHashMap<String, Method>(eventMethods.size());
        for (Method method : eventMethods) {
              table.put(method.getName(), method);
        }
        return table;
    }

/**
* Adds the given prefix to the list of prefixes that indicate an event method.
**/
    public static void addPrefix(String newPrefix) {
        if (additionalPrefixes == null) {
            additionalPrefixes = new LinkedHashSet<String>();
        }
        additionalPrefixes.add(newPrefix);
    }

/**
* Removes the given prefix from the list of event method prefixes. If the
* given prefix was not in the list, does nothing.
**/
    public static void removePrefix(String prefix) {
        additionalPrefixes.remove(prefix);
    }

/**
* Removes all additional prefixes.
**/
    public static void removeAdditionalPrefixes() {
         additionalPrefixes.clear();
    }

/**
* Sets the primary event method prefix. The default is "do"
**/
    public static void setPrefix(String p) { prefix = p; }

/**
* Returns the primary event method prefix.
**/
    public static String getPrefix() { return prefix; }

/**
* Returns an array containing the Classes of the given Obejects.
**/
    public static Class[] getSignature(Object[] params) {
        if (params == null) {return new Class[]{};}

        Class[] sig = new Class[params.length];
        for (int i = 0; i < sig.length; i++) {
            sig[i] = params[i].getClass();
        }
        return sig;
    }

/**
* Returns a String containing the names of the given Classes separated by 
* commas and enclosed in parenthesis.</br>
* For example: (Class1, Class2,)
**/
    public static String createParameterKey(Class[] c) {
        if (c == null) {return "()";}
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i = 0; i < c.length; i++) {
            buf.append(c[i].getName());
            if (i < c.length - 1) {buf.append(',');}
        }
        buf.append(')');
        return buf.toString();
    }

}

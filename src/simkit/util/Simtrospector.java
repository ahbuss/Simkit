package simkit.util;

import java.lang.reflect.*;
import java.util.*;

/**
 *  A utility class to reduce the clutter in SimEntityBase and rationalize some
 *  common methods involving introspection.
 *
 *  @version 1.0.9
 *  @author Arnold Buss
**/

public class Simtrospector {

    private static ArrayList additionalPrefixes;
    private static String prefix;

    static {
        setPrefix("do");
    }

    public static Hashtable getEventMethods(Class c) {
        Method[] allMethods = c.getMethods();
        ArrayList eventMethods = new ArrayList(allMethods.length);
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getName().startsWith(prefix)) {
                 eventMethods.add(allMethods[i]);
            }
            if (additionalPrefixes != null) {
                for (Iterator iter = additionalPrefixes.iterator(); iter.hasNext();) {
                    if (allMethods[i].getName().startsWith(iter.next().toString())) {
                         eventMethods.add(allMethods[i]);
                         break;
                    }
                }
            }
        }
        Hashtable table = new Hashtable(eventMethods.size());
        for (Iterator i = eventMethods.iterator(); i.hasNext();) {
              Method m = (Method) i.next();
              table.put(m.getName(), m);
        }
        return table;
    }

    public static void addPrefix(String newPrefix) {
       if (additionalPrefixes == null) {
            additionalPrefixes = new ArrayList();
       }
       if (!additionalPrefixes.contains(newPrefix)) {
           additionalPrefixes.add(newPrefix);
       }
    }

    public static void removePrefix(String prefix) {
        if (additionalPrefixes != null) {
            additionalPrefixes.remove(prefix);
        }
        if (additionalPrefixes.size() == 0) {
            additionalPrefixes = null;
        }
    }

    public static void removeAdditionalPrefixes() {
         additionalPrefixes.clear();
         additionalPrefixes = null;
    }

    public static void setPrefix(String p) { prefix = p; }
    public static String getPrefix() { return prefix; }

    public static Class[] getSignature(Object[] params) {
        if (params == null) {return new Class[]{};}

        Class[] sig = new Class[params.length];
        for (int i = 0; i < sig.length; i++) {
            sig[i] = params[i].getClass();
        }
        return sig;
    }

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

package simkit.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A utility class to reduce the clutter in SimEntityBase and rationalize some
 * common methods involving introspection.
 *
 * @author Arnold Buss
 *
 */
public class Simtrospector {

    /**
     * The list of prefixes that indicate an event method in addition to
     * <code>prefix</code>
     *
     */
    private static Set<String> additionalPrefixes;

    /**
     * The primary prefix that indicates an event method (Default is "do").
     *
     */
    private static String prefix;

    static {
        setPrefix("do");
        additionalPrefixes = null;
    }

    /**
     * Returns a Map containing all of the public event methods for the given
     * Class. Event methods are methods that begin with prefixes as determined
     * by <code>setPrefix, addPrefix</code> and other similar methods. The
     * default prefix is "do"
     *
     * @param c given class
     * @return Map of event methods keyed by the method name
     *
     */
    public static Map<String, Method> getEventMethods(Class<?> c) {
        Method[] allMethods = c.getMethods();
        List<Method> eventMethods = new ArrayList<>(allMethods.length);
        for (Method allMethod : allMethods) {
            if (allMethod.getName().startsWith(prefix)) {
                eventMethods.add(allMethod);
            }
            if (additionalPrefixes != null) {
                for (String additionalPrefix : additionalPrefixes) {
                    if (allMethod.getName().startsWith(additionalPrefix)) {
                        eventMethods.add(allMethod);
                        break;
                    }
                }
            }
        }
        Map<String, Method> table = new LinkedHashMap<>(eventMethods.size());
        for (Method method : eventMethods) {
            table.put(method.getName(), method);
        }
        return table;
    }

    /**
     * Adds the given prefix to the list of prefixes that indicate an event
     * method.
     *
     * @param newPrefix Given prefix
     */
    public static void addPrefix(String newPrefix) {
        if (additionalPrefixes == null) {
            additionalPrefixes = new LinkedHashSet<>();
        }
        additionalPrefixes.add(newPrefix);
    }

    /**
     * Removes the given prefix from the list of event method prefixes. If the
     * given prefix was not in the list, does nothing.
     *
     * @param prefix Given prefix to remove
     */
    public static void removePrefix(String prefix) {
        additionalPrefixes.remove(prefix);
    }

    /**
     * Removes all additional prefixes.
     */
    public static void removeAdditionalPrefixes() {
        additionalPrefixes.clear();
    }

    /**
     * Sets the primary event method prefix. The default is "do"
     *
     * @param p Given primary prefix
     */
    public static void setPrefix(String p) {
        prefix = p;
    }

    /**
     * @return the primary event method prefix.
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * @param params Given parameters
     * @return an array containing the Classes of the given Objects.
     */
    @SuppressWarnings("unchecked")
    public static Class<?>[] getSignature(Object[] params) {
        List<Class<?>> classList = new ArrayList<>();

        if (params != null) {
            for (Object param : params) {
                classList.add(param.getClass());
            }
        }
        return (Class<?>[]) classList.toArray();
    }

    /**
     * Returns a String containing the names of the given Classes separated by
     * commas and enclosed in parenthesis.<br>
     * For example: (Class1, Class2,)
     *
     * @param c Given array of classes
     * @return Stringified version of given class array
     */
    public static String createParameterKey(Class<?>[] c) {
        if (c == null) {
            return "()";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (int i = 0; i < c.length; i++) {
            builder.append(c[i].getName());
            if (i < c.length - 1) {
                builder.append(',');
            }
        }
        builder.append(')');
        return builder.toString();
    }

}

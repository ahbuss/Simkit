package simkit.random;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory for creating <CODE>RandomVariate</CODE> instances from "orders". The
 * "specifications" of each order are as generic as possible:
 * <code>String</code> (name of class), <code>Object...</code> (parameters),
 * <code>long</code> (seed), <code>long[]</code> (seeds),
 * <code>RandomNumber</code> (instance of supporting Un(0,1) generator).
 * <P>
 * The default supporting <code>RandomNumber</code> may be determined by the
 * implementation of the <code>RandomVariate</code>, but a "well-behaved"
 * implementation uses <code>DEFAULT_RNG</code> by default. This is done by
 * simply having <code>setRandomNumber(RandomNumber)</code> (required by
 * <code>RandomVariate</code> interface) that is a simple pass-through.
 * <p>
 * The (current) <code>DEFAULT_RNG</code> is a Mersenne Twister, but may be
 * changed by a static call to <code>setDefaultRandomNumber(RandomNumber)</code>
 * prior to any <code>RandomVariate</code> "orders."
 * <p>TODO: replace the searchPackage construct with finding &amp; loading given
 * RandomVariate classes on the classpath.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class RandomVariateFactory {

    public static final String _VERSION_
            = "$Id$";
    public static final Logger log = Logger.getLogger("simkit.random");

    public static final String RANDOM_VARIATE_CLASSNAME_KEY = "className";
    public static final String RANDOM_INSTANCE_KEY = "rngInstance";

    public static final String RANDOM_NUMBER_CLASSNAME_KEY = "rngClassName";
    public static final String RANDOM_NUMBER_STREAM_ID_KEY = "streamID";
    public static final String RANDOM_NUMBER_SUBSTREAM_ID_KEY = "subStreamID";

    private static Map<String, Map<String, Object>> defaultsMap;
    /**
     * Holds a cache of the RandomVariate Classes that have already been found
     * indexed by their name.
     */
    protected static Map<String, Class> cache;
    /**
     * A list of packages to search for RandomVariates if the class name given
     * is not fully qualified.
     */
    protected static final Set<String> searchPackages;
    /**
     * If true, print out information while searching for RandomVariate Classes.
     */
    protected static boolean verbose;
    /**
     * Default RandomNumber instance. If none specified in getInstance(), this
     * one instance is used for all RandomVariates obtained, ensuring
     * independence.
     */
    protected static RandomNumber DEFAULT_RNG;

    /**
     * Regular expression used to group name of RandomVariate when parsing a String
     */
    private static final String NAME_REGEX = "([\\w ?]+)";

    /**
     * The parameters portion of the String should split by these characters
     */
    private static final String PARAM_SPLITTER = "[ (),=]+";

    /**
     * This regular expression should match any number, integer or floating point,
     * including scientific notation.
     */
    private static final String NUMBER_REGEX = "[+-]?((\\d*)|(\\d*\\.\\d*))([Ee]\\d+)?";

    /**
     * This Pattern is used to match &amp; group the RandomVariate name in a String
     */
    private static final Pattern NAME_PATTERN;

    static {
        NAME_PATTERN = Pattern.compile(NAME_REGEX);
    }

    /**
     *
     * @param b true if printing out information while searching for
     * RandomVariate Classes.
     */
    public static void setVerbose(boolean b) {
        verbose = b;
    }

    /**
     *
     * @return true if printing out information while searching for
     * RandomVariate Classes.
     */
    public static boolean isVerbose() {
        return verbose;
    }

    /**
     *
     * @return shallow copy of cache
     */
    public static Map<String, Class> getCache() {
        return new WeakHashMap<>(cache);
    }

    /**
     *
     * @param rng New default RandomNumber instance
     */
    public static void setDefaultRandomNumber(RandomNumber rng) {
        DEFAULT_RNG = rng;
    }

    /**
     *
     * @return Default RandomNumber instance
     */
    public static RandomNumber getDefaultRandomNumber() {
        return DEFAULT_RNG;
    }

    static {
        searchPackages = new LinkedHashSet<>();
        searchPackages.add("simkit.random");
        cache = new WeakHashMap<>();
        setDefaultRandomNumber(RandomNumberFactory.getInstance());
    }

    /**
     * This factory Class should never by instantiated.
     *
     */
    protected RandomVariateFactory() {
    }

    /**
     * Creates a <CODE>RandomVariate</CODE> instance with default seed(s) and
     * the default supporting <CODE>RandomNumber</CODE>.
     *
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @return Instance of <CODE>RandomVariate</CODE> based on the
     * (fully-qualified) class name and the parameters. The default
     * <CODE>RandomNumber</CODE> instance is used with the default seed(s).
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(String className,
            Object... parameters) {

        if (className == null) {
            throw new IllegalArgumentException("null class name");
        }
        // First check cache
        Class randomVariateClass = cache.get(className);
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className);
            if (randomVariateClass == null) {
                // The name may be the distribution - try appending "Variate"
                randomVariateClass = findFullyQualifiedNameFor(className
                        + "Variate");
            }
            // All attempts have failed
            if (randomVariateClass == null) {
                throw new IllegalArgumentException(
                        "RandomVariate class not found for " + className);
            } else {
                cache.put(className, randomVariateClass);
            }
        }
        return getInstance(className, getDefaultRandomNumber(), parameters);
    }

    /**
     * Creates a <CODE>RandomVariate</CODE> instance supported by the
     * <CODE>RandomNumber</CODE> instance passed in.
     *
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @param rng An instance of <CODE>RandomNumber</CODE> to support this
     * RandomVariate.
     * @return Instance of <CODE>RandomVariate</CODE> based on the
     * (fully-qualified) class name and the parameters.
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(String className, RandomNumber rng,
            Object... parameters) {
        if (className == null) {
            throw new IllegalArgumentException(
                    "Name of RandomVariate class is null.");
        }
        RandomVariate instance = null;
        Class randomVariateClass = (Class) cache.get(className);
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className);
        }
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className
                    + "Variate");
        }
        if (randomVariateClass == null) {
            throw new IllegalArgumentException(
                    "Can't find RandomVariate class for " + className);
        } else {
            cache.put(className, randomVariateClass);
        }
        try {
            instance = (RandomVariate) randomVariateClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        instance.setParameters(parameters);
        instance.setRandomNumber(rng);
        return instance;
    }

    /**
     * Gets a new instance of the given RandomVariate. The random number stream
     * of the new instance should be the same reference to the one supporting
     * the RandomVariate passed in. Therefore (unlike previous implementations)
     * the new instance is independent of the original, since it draws from the
     * same RandomNumber source.
     *
     * @param rv given RandomVariate
     * @return a new instance, copy of the given RandomVariate.
     */
    public static RandomVariate getInstance(RandomVariate rv) {
        RandomVariate newInstance
                = getInstance(rv.getClass().getName(), rv.getRandomNumber(),
                        rv.getParameters());
        return newInstance;
    }

    /**
     * Parses and instantiates a RandomVariate based on a purely String
     * representation. Examples include <code>"Normal (5.2, 1.5)</code>, 
     * <code>"Gamma(1.2, 3.4)"</code>, <code>"Constant (10.3)"</code>, which
     * will create Normal, Gamma, and Constant RandomVariate instances,
     * respectively.
     * 
     * @param toString Given String that can be parsed to a RandomVariate
     * @return RandomVariate corresponding to given argument
     * @throws IllegalArgumentException if given argument cannot be parsed
     */
    public static RandomVariate getInstance(String toString) {
        RandomVariate newInstance = null;
        Matcher matcher = NAME_PATTERN.matcher(toString);
        matcher.find();
        if (matcher.groupCount() == 0) {
            String message = "No matching groups found for name: " + toString;
            log.severe(message);
            throw new IllegalArgumentException(message);
        }
        String name = matcher.group(0);
        String paramString = toString.replace(name, "");
        String adjustedParamString = paramString.replaceAll(PARAM_SPLITTER, " ");
        String[] paramsArray = adjustedParamString.trim().split(" ");
        String adjustedName = name.replaceAll(PARAM_SPLITTER, name);
        List<Double> paramsList = new ArrayList<>();
        for (int i = 0; i < paramsArray.length; ++i) {
            if (paramsArray[i].matches(NUMBER_REGEX)) {
                paramsList.add(Double.valueOf(paramsArray[i]));
            }
        }
        Object[] params = paramsList.toArray();
        try {
            newInstance = getInstance(adjustedName, params);
        } catch (IllegalArgumentException ex) {
            log.severe(String.format("Cannot find RandomVariate: %s %s", adjustedName,
                    Arrays.toString(params)));
            throw ex;
        }
        return newInstance;
    }

    /**
     * Adds the given fully qualified package name to the list of packages that
     * will be searched when attempting to find RandomVariates by name.
     *
     * @param newPackage given fully qualified package name
     */
    public static void addSearchPackage(String newPackage) {
        searchPackages.add(newPackage);
    }

    /**
     * Remove the given package from searchPackages
     * @param removedPackage Given package to remove
     * @return true if given package was in the searchPackages
     */
    public static boolean removeSearchPackage(String removedPackage) {
        return searchPackages.remove(removedPackage);
    }

    /**
     * @return Copy of searchPackages.
     */
    public static Set<String> getSearchPackages() {
        return new LinkedHashSet<>(searchPackages);
    }

    /**
     * Finds the RandomVariate Class corresponding to the given name. First
     * attempts to find the RandomVariate assuming the the name is fully
     * qualified. Then searches the "search packages." The search path defaults
     * to "simkit.random" but additional search packages can be added.
     *
     * @see #addSearchPackage(String)
     *
     * @param className given name of class
     * @return Class instances with given name or null if not found
     */
    public static Class<?> findFullyQualifiedNameFor(String className) {
        Class<?> theClass = null;
        //        First see if name passed is "fully qualified"
        try {
            theClass
                    = Thread.currentThread().getContextClassLoader().
                    loadClass(className);

            return theClass;
        } //        If not, then try the search path
        catch (ClassNotFoundException e) {
        }
        for (String searchPackage : searchPackages) {
            if (verbose) {
                System.out.println("Checking " + searchPackage + "."
                        + className);
            }
            try {
                theClass
                        = Thread.currentThread().getContextClassLoader().
                        loadClass(searchPackage + "." + className);

                if (!simkit.random.RandomVariate.class.
                        isAssignableFrom(theClass)) {
                    // make sure we don't return it (could happen if this is the
                    // last class found)
                    theClass = null;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        if (verbose) {
            System.out.println("returning " + theClass);
        }
        return theClass;
    }

    /**
     *
     * @param className Name of class
     * @param params parameters
     * @return instance of DiscreteRandomVariate with given name and parameters
     * @throws IllegalArgumentException if className does not implement
     * DiscreteRandomVariate
     */
    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(
            String className, Object... params) {
        RandomVariate instance = getInstance(className, params);
        if (instance instanceof DiscreteRandomVariate) {
            return (DiscreteRandomVariate) instance;
        } else {
            throw new IllegalArgumentException(className + " is not an instance of "
                    + "simkit.random.DiscreteRandomVariate");
        }
    }

    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(
            String className, RandomNumber rng, Object... params) {
        DiscreteRandomVariate instance
                = getDiscreteRandomVariateInstance(className, params);
        instance.setRandomNumber(rng);
        return instance;
    }

    /**
     *
     * @param className Name of RandomVariate class
     * @param params parameters of RandomVariate class
     * @return a RandomVariate instance with the given class name and parameters
     */
    public static RandomVariate getInstance(String className,
            Map<String, Object> params) {
        RandomVariate instance = getUnconfiguredInstance(className);
        LinkedHashMap<String, Method> writeMethods = new LinkedHashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
            PropertyDescriptor[] propertyDescriptors
                    = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    writeMethods.put(propertyDescriptor.getName(), writeMethod);
                }
            }
        } catch (IntrospectionException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        if (instance != null) {
            for (String property : params.keySet()) {
                Object value = params.get(property);
                if (value != null) {
                    Method setter = writeMethods.get(property);
                    if (setter != null) {
                        try {
                            setter.invoke(instance, value);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            log.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        instance.setRandomNumber(DEFAULT_RNG);
        return instance;
    }

    /**
     *
     * @param className
     * @return
     */
    protected static RandomVariate getUnconfiguredInstance(String className) {
        RandomVariate instance = null;

        Class randomVariateClass = (Class) cache.get(className);
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className);
        }
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className
                    + "Variate");
        }
        if (randomVariateClass == null) {
            throw new IllegalArgumentException(
                    "Can't find RandomVariate class for " + className);
        } else {
            cache.put(className, randomVariateClass);
        }
        try {
            instance = (RandomVariate) randomVariateClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}

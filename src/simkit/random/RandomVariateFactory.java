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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import simkit.util.ClassFinder;

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
 * <p>
 * TODO: replace the searchPackage construct with finding &amp; loading given
 * RandomVariate classes on the classpath.
 *
 * @author Arnold Buss
 */
public class RandomVariateFactory {

    public static final Logger LOGGER = Logger.getLogger("simkit.random");

    public static final String RANDOM_VARIATE_CLASSNAME_KEY = "className";
    public static final String RANDOM_INSTANCE_KEY = "rngInstance";

    public static final String RANDOM_NUMBER_CLASSNAME_KEY = "rngClassName";
    public static final String RANDOM_NUMBER_STREAM_ID_KEY = "streamID";
    public static final String RANDOM_NUMBER_SUBSTREAM_ID_KEY = "subStreamID";

    private static final String DISCRETE_INTEGER_REGEX = "(\\d+)\\s+(\\d\\.\\d+)\\s+(\\+?\\d?\\.\\d+)";

    private static final String DISCRETE_VARIATE_REGEX = "(\\d+\\.\\d+)\\s+(\\d\\.\\d+)\\s+(\\+?\\d?\\.\\d+)";

    private static final Pattern DISCRETE_INTEGER_PATTERN = Pattern.compile(DISCRETE_INTEGER_REGEX);

    private static final Pattern DISCRETE_VARIATE_PATTERN = Pattern.compile(DISCRETE_VARIATE_REGEX);

    private static Map<String, Map<String, Object>> DEFAULTS_MAP;
    /**
     * Holds a cache of the RandomVariate Classes that have already been found
     * indexed by their name.
     */
    protected static final Map<String, Class<? extends RandomVariate>> cache;
    /**
     * A list of packages to search for RandomVariates if the class name given
     * is not fully qualified.
     */
    protected static final Set<String> SEARCH_PACKAGES;
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
     * Regular expression used to group name of RandomVariate when parsing a
     * String
     */
    public static final String NAME_REGEX = "([\\w ?]+)";

    /**
     * The parameters portion of the String should split by these characters
     */
    public static final String PARAM_SPLITTER = "[ (),=]+";

    /**
     * This regular expression should match any number, integer or floating
     * point, including scientific notation.
     */
    public static final String NUMBER_REGEX = "[+-]?((\\d*)|(\\d*\\.\\d*))([Ee][+-]?\\d+)?";

    /**
     * This Pattern is used to match &amp; group the RandomVariate name in a
     * String
     */
    private static final Pattern NAME_PATTERN;

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
    public static Map<String, Class<? extends RandomVariate>> getCache() {
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
        NAME_PATTERN = Pattern.compile(NAME_REGEX);
        SEARCH_PACKAGES = new LinkedHashSet<>();
        SEARCH_PACKAGES.add("simkit.random");
        cache = ClassFinder.getINSTANCE().getRandomVariateClasses();
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
        Class<? extends RandomVariate> randomVariateClass;
        if (cache.containsKey(className)) {
            randomVariateClass = cache.get(className);
        } else if (cache.containsKey(className.concat("Variate"))) {
            randomVariateClass = cache.get(className);
        } else {
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
        Class<? extends RandomVariate> randomVariateClass = cache.get(className);
        if (cache.containsKey(className)) {
            randomVariateClass = cache.get(className);
        } else if (cache.containsKey(className.concat("Variate"))) {
            randomVariateClass = cache.get(className);
        }
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
            instance = (RandomVariate) randomVariateClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RandomVariateFactory.class.getName()).log(Level.SEVERE, null, ex);
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
        if (toString.startsWith("Discrete Integer")) {
            newInstance = parseDiscreteIntegerVariate(toString);
        } else if (toString.startsWith("Discrete Distribution")) {
            newInstance = parseDiscreteVariate(toString);
        } else {
            Matcher matcher = NAME_PATTERN.matcher(toString);
            matcher.find();
            if (matcher.groupCount() == 0) {
                String message = "No matching groups found for name: " + toString;
                LOGGER.severe(message);
                throw new IllegalArgumentException(message);
            }
            String name = matcher.group(0);
            String paramString = toString.replace(name, "");
            String adjustedParamString = paramString.replaceAll(PARAM_SPLITTER, " ");
            String[] paramsArray = adjustedParamString.trim().split(" ");
            String adjustedName = name.trim().replaceAll(PARAM_SPLITTER, "");
            List<Double> paramsList = new ArrayList<>();
            for (int i = 0; i < paramsArray.length; ++i) {
                if (!paramsArray[i].equals("") && paramsArray[i].matches(NUMBER_REGEX)) {
                    paramsList.add(Double.valueOf(paramsArray[i]));
                }
            }
            Object[] params = paramsList.toArray();
            try {
                newInstance = getInstance(adjustedName, params);
            } catch (IllegalArgumentException ex) {
                LOGGER.severe(String.format("Cannot find RandomVariate: %s %s", adjustedName,
                        Arrays.toString(params)));
                throw ex;
            }
        }
        return newInstance;
    }

    /**
     * Note: will strip any embedded, leading, or trailing spaces before
     * checking. Therefore "Log Normal" or " Gamma " will both return "true".
     *
     * @param toString Given name of RandomVariate - may or may not include
     * "Variate" suffix
     * @return true if given String can be parsed to a RandomVariate
     */
    public static boolean isRandomVariateString(String toString) {
        RandomVariate instance = null;
        try {
            instance = getInstance(toString);
        } catch (Throwable e) {
            LOGGER.log(Level.INFO,
                    "{0} not found as RandomVariate - Check spelling and/or parameters", toString);
        }
        return instance != null;
    }

    /**
     * Adds the given fully qualified package name to the list of packages that
     * will be searched when attempting to find RandomVariates by name.
     *
     * @param newPackage given fully qualified package name
     */
    public static void addSearchPackage(String newPackage) {
        SEARCH_PACKAGES.add(newPackage);
    }

    /**
     * Remove the given package from SEARCH_PACKAGES
     *
     * @param removedPackage Given package to remove
     * @return true if given package was in the SEARCH_PACKAGES
     */
    public static boolean removeSearchPackage(String removedPackage) {
        return SEARCH_PACKAGES.remove(removedPackage);
    }

    /**
     * @return Copy of SEARCH_PACKAGES.
     */
    public static Set<String> getSEARCH_PACKAGES() {
        return new LinkedHashSet<>(SEARCH_PACKAGES);
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
    @SuppressWarnings("unchecked")
    public static Class<? extends RandomVariate> findFullyQualifiedNameFor(String className) {
        Class<? extends RandomVariate> theClass = null;
        //        First see if name passed is "fully qualified"
        try {
            theClass
                    = (Class<? extends RandomVariate>) Thread.currentThread().getContextClassLoader().
                            loadClass(className);

            return theClass;
        } //        If not, then try the search path
        catch (ClassNotFoundException e) {
        }
        for (String searchPackage : SEARCH_PACKAGES) {
            if (verbose) {
                System.out.println("Checking " + searchPackage + "."
                        + className);
            }
            try {
                theClass
                        = (Class<? extends RandomVariate>) Thread.currentThread().getContextClassLoader().
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

    /**
     *
     * @param className Name of RandomVariate class, which may or may not
     * include "Variate"
     * @param rng Given RandomNumber instance to use
     * @param params Given parameters for this RandomVariate
     * @return A DiscreteRandomVariate instance with given parameters backed by
     * given RandomNumber instance, or null if none found
     */
    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(
            String className, RandomNumber rng, Object... params) {
        DiscreteRandomVariate instance
                = getDiscreteRandomVariateInstance(className, params);
        instance.setRandomNumber(rng);
        return instance;
    }

    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(String toString) {
        DiscreteRandomVariate drv = null;
        RandomVariate rv = getInstance(toString);
        if (rv instanceof DiscreteRandomVariate) {
            drv = (DiscreteRandomVariate) rv;
        } else {
            throw new IllegalArgumentException(String.format("Not an instance of DiscreteRandomVariate: %s",
                    toString));
        }

        return drv;
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
            LOGGER.log(Level.SEVERE, null, ex);
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
                            LOGGER.log(Level.SEVERE, null, ex);
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
     * @param className Given name of RandomVariate
     * @return a RandomVariate that has not had its parameters set
     */
    protected static RandomVariate getUnconfiguredInstance(String className) {
        RandomVariate instance = null;

        Class<? extends RandomVariate> randomVariateClass
                = (Class<? extends RandomVariate>) cache.get(className);
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
            instance = (RandomVariate) randomVariateClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RandomVariateFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return instance;
    }

    public static DiscreteIntegerVariate parseDiscreteIntegerVariate(String string) {
        DiscreteIntegerVariate variate;
        SortedMap<Integer, Double> frequencies = new TreeMap<>();
        String[] splits = string.split("\\n");
        for (String split : splits) {
            if (split.matches("(Discrete Integer|x\\s+f(x)\\s+F(x))")) {
                continue;
            }
            Matcher matcher = DISCRETE_INTEGER_PATTERN.matcher(split);
            if (matcher.matches()) {
                int value = Integer.parseInt(matcher.group(1));
                double frequency = Double.parseDouble(matcher.group(2));
                frequencies.put(value, frequency);
            }
        }

        int[] values = new int[frequencies.size()];
        double[] freqs = new double[frequencies.size()];
        int count = 0;
        for (int val : frequencies.keySet()) {
            values[count] = val;
            freqs[count] = frequencies.get(val);
            count += 1;
        }
        variate = (DiscreteIntegerVariate) RandomVariateFactory.getDiscreteRandomVariateInstance("DiscreteInteger", values, freqs);
        return variate;
    }

    public static DiscreteVariate parseDiscreteVariate(String string) {
        DiscreteVariate variate;
        SortedMap<Double, Double> frequencies = new TreeMap<>();
        String[] splits = string.split("\\n");
        for (String split : splits) {
            if (split.matches("(Discrete Integer|x\\s+f(x)\\s+F(x))")) {
                continue;
            }
            Matcher matcher = DISCRETE_VARIATE_PATTERN.matcher(split);
            if (matcher.matches()) {
                double value = Double.parseDouble(matcher.group(1));
                double frequency = Double.parseDouble(matcher.group(2));
                frequencies.put(value, frequency);
            }
        }

        double[] values = new double[frequencies.size()];
        double[] freqs = new double[frequencies.size()];
        int count = 0;
        for (double val : frequencies.keySet()) {
            values[count] = val;
            freqs[count] = frequencies.get(val);
            count += 1;
        }
        variate = (DiscreteVariate) RandomVariateFactory.getInstance("Discrete", values, freqs);
        return variate;
    }
}

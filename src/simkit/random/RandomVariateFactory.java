package simkit.random;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;

/**
 * Factory for creating <CODE>RandomVariate</CODE> instances from "orders".
 * The "specifications" of each order are as generic as possible:
 * String (name of class), Object[] (parameters), long (seed),
 * RandomNumber (instance of supporting Un(0,1) generator).
 * <P/>The default supporting RandomNumber is determined by the implementation
 * of the RandomVariate. In most cases it is <CODE>Congruential</CODE>.
 * 
 * @author Arnold Buss
 * @version $Id$
 */
public class RandomVariateFactory {

    public static final String _VERSION_ = 
            "$Id$";
    public static Logger log = Logger.getLogger("simkit.random");

    public static final String RANDOM_VARIATE_CLASSNAME_KEY = "className";
    public static final String RANDOM_INSTANCE_KEY = "rngInstance";

    public static final String RANDOM_NUMBER_CLASSNAME_KEY  = "rngClassName";
    public static final String RANDOM_NUMBER_STREAM_ID_KEY = "streamID";
    public static final String RANDOM_NUMBER_SUBSTREAM_ID_KEY = "subStreamID";

    private static Map<String, Map<String, Object>> defaultsMap;
    /**
     * Holds a cache of the RandomVariate Classes that have already been
     * found indexed by their name.
     **/
    protected static Map<String, Class> cache;
    /**
     * A list of packages to search for RandomVariates if the
     * class name given is not fully qualified.
     **/
    protected static Set<String> searchPackages;
    /**
     * If true, print out information while searching for RandomVariate
     * Classes.
     **/
    protected static boolean verbose;
    /**
     * Default RandomNumber instance.  If none specified in getInstance(),
     * this one instance is used for all RandomVariates obtained, ensuring
     * independence.
     */
    protected static RandomNumber DEFAULT_RNG;

    /**
     * If true, print out information while searching for RandomVariate
     * Classes.
     **/
    public static void setVerbose(boolean b) {
        verbose = b;
    }

    /**
     * If true, print out information while searching for RandomVariate
     * Classes.
     **/
    public static boolean isVerbose() {
        return verbose;
    }

    /**
     * If true, print out information while searching for RandomVariate
     * Classes.
     **/
    public static Map<String, Class> getCache() {
        return new WeakHashMap<String, Class>(cache);
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
        searchPackages = new LinkedHashSet<String>();
        searchPackages.add("simkit.random");
        cache = new WeakHashMap<String, Class>();
        setDefaultRandomNumber(RandomNumberFactory.getInstance());
    }

    /**
     * This factory Class should never by instantiated.
     **/
    protected RandomVariateFactory() {
    }

    /**
     * Creates a <CODE>RandomVariate</CODE> instance with default seed(s) and
     * the default supporting <CODE>RandomNumber</CODE>.
     * 
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @return Instance of <CODE>RandomVariate</CODE> based on the
     * (fully-qualified) class name and the parameters.  The default
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
                randomVariateClass = findFullyQualifiedNameFor(className + 
                        "Variate");
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
            randomVariateClass = findFullyQualifiedNameFor(className + 
                    "Variate");
        }
        if (randomVariateClass == null) {
            throw new IllegalArgumentException(
                    "Can't find RandomVariate class for " + className);
        } else {
            cache.put(className, randomVariateClass);
        }
        try {
            instance = (RandomVariate) randomVariateClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        instance.setParameters(parameters);
        instance.setRandomNumber(rng);
        return instance;
    }

    /**
     * Gets a new instance of the given RandomVariate. The random number stream
     * of the new instance should be the same reference to the one supporting
     * the RandomVariate passed in.  Therefore (unlike previous implementations)
     * the new instance is independent of the original, since it draws from
     * the same RandomNumber source.
     **/
    public static RandomVariate getInstance(RandomVariate rv) {
        RandomVariate newInstance =
                getInstance(rv.getClass().getName(), rv.getRandomNumber(), 
                rv.getParameters());
        return newInstance;
    }

    /**
     * Adds the given fully qualified package name to the list of packages
     * that will be searched when attempting to find RandomVariates by name.
     **/
    public static void addSearchPackage(String newPackage) {
        searchPackages.add(newPackage);
    }

    /**
     * Sets the list of packages that will be searched when attempting to find
     * a RandomVariate by name.  Replaces existing search packages.
     * @param packages New Set of search packages
     **/
    public static void setSearchPackages(Set<String> packages) {
        searchPackages = new LinkedHashSet<String>(packages);
    }

    /**
     * Returns a copy of the packages that will be searched when attempting to 
     * find a RandomVariate by name.
     * @return Copy of search packages Set.
     **/
    public static Set<String> getSearchPackages() {
        return new LinkedHashSet<String>(searchPackages);
    }

    /**
     * Finds the RandomVariate Class corresponding to the given name. First
     * attempts to find the RandomVariate assuming the the name is fully 
     * qualified. Then searches the "search packages." The search path defaults 
     * to "simit.random" but additional search packages can be added.
     * 
     * @see #addSearchPackage(String)
     * @see #setSearchPackages(Set)
     **/
    public static Class findFullyQualifiedNameFor(String className) {
        Class theClass = null;
        //        First see if name passed is "fully qualified"
        try {
            theClass = Thread.currentThread().getContextClassLoader().
                    loadClass(className);
            
            return theClass;
        } //        If not, then try the search path
        catch (ClassNotFoundException e) {
        }
        for (String searchPackage : searchPackages) {
            if (verbose) {
                System.out.println("Checking " + searchPackage + "." +
                        className);
            }
            try {
                theClass = Thread.currentThread().getContextClassLoader().
                        loadClass(searchPackage + "." + className);
                
                if (!simkit.random.RandomVariate.class.
                        isAssignableFrom(theClass)) {
                    // make sure we don't return it (could happen if this is the
                    // last class found)
                    theClass = null;
                    continue;
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        if (verbose) {
            System.out.println("returning " + theClass);
        }
        return theClass;
    }

    /**
     * @return instance of DiscreteRandomVariate
     */
    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(
            String className, Object... params) {
        RandomVariate instance = getInstance(className, params);
        if (instance instanceof DiscreteRandomVariate) {
            return (DiscreteRandomVariate) instance;
        } else {
            throw new RuntimeException(className + " is not an instance of " +
                    "simkit.random.DiscreteRandomVariate");
        }
    }

    public static DiscreteRandomVariate getDiscreteRandomVariateInstance(
            String className, RandomNumber rng, Object... params) {
        DiscreteRandomVariate instance = 
                getDiscreteRandomVariateInstance(className, params);
        instance.setRandomNumber(rng);
        return instance;
    }

    /**
     * Creates a variate with the named distribution.
     *  
     * @param variateClassName 
     * @param params a String keyed map of named parameters.
     *
     */
    public static RandomVariate getInstance(String variateClassName, 
            Map<String, Object> params) {
        RandomVariate result=null;
        return result;
    }
}

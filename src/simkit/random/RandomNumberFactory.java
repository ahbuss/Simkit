package simkit.random;

import java.util.*;

/**
* A factory for creating instances of <CODE>RandomNumber</CODE>. This factory
* can either create a <CODE>RandomNumnber</CODE> instance specified by the user, or
* create the default <CODE>RandomNumber</CODE> Class. The default Class
* is initially set to <CODE>Congruential</CODE>.
 * 
* @author A. Buss
* @version $Id$
**/
public class RandomNumberFactory {
    
/**
* Holds a String with the initial setting of the default Class. Currently
* "simkit.random.Congruential"
**/
    protected static final String DEFAULT_CLASS = 
            "simkit.random.MersenneTwister";

/**
* The default Class that will be generated if one is not specified by the user.
**/
    protected static Class defaultClass ;
    
/**
* A cache of RandomNumber Classes that have already been found.
**/
    protected static Map<String, Class> cache;

/**
* A list of packages to search when the Class name is not fully qualified.
* Initially set to "simkit.random"
**/
    protected static List<String> searchPackages;

/**
* If true, print debug/trace information (not yet implemented)
**/
    protected static boolean verbose;
    
    /**
     * If true, print debug/trace information (not yet implemented)
     * 
     * @param v Sets verbose mode (not implemented yet)
     */    
    public static void setVerbose(boolean v) { verbose = v; }
    /**
     * If true, print debug/trace information (not yet implemented)
     * 
     * @return verbose mode
     */    
    public static boolean isVerbose() { return verbose; }
    
    /**
     * A cache of RandomNumber Classes that have already been found.
     * @return Map containing unqualified names with corresponding
     * Class objects.
     */    
    public static Map<String, Class> getCache() { 
        return new WeakHashMap<String, Class>(cache); 
    }
    
    static {
        cache = new WeakHashMap<String, Class>();
        searchPackages = new ArrayList<String>();
        setDefaultClass(DEFAULT_CLASS);
        addSearchPackage("simkit.random");
    }
    
    /** The default class is instantiated when the user does not
     * specify a class.
     * 
     * @param className Unqualified name of default class
     */    
    public static void setDefaultClass(String className) {
        Class theClass = getClassFor(className);
        if (theClass != null) {
            defaultClass = theClass;
        }
        else {
            throw new IllegalArgumentException("Not found: " + className);
        }
    }
    
/**
* This factory should not be instantiated.
**/
    protected RandomNumberFactory() {
    }
    
    /**
     * Creates an instance of the default Class with its default seed(s).
     * 
     * @return Instance of default class with its default seed(s)
     */    
    public static RandomNumber getInstance() {
        RandomNumber instance = null;
        try {
            instance = (RandomNumber) defaultClass.newInstance();
        }
        catch (IllegalAccessException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        catch (InstantiationException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        return instance;
    }
    
    /**
     * Creates an instance of the default Class with the given seed.
     * Should be used only for <CODE>RandomNumber</CODE>s that take
     * a single seed.
     * 
     * @param seed Desired seed for instance
     * @return Instance of default class with given seed.
     */    
    public static RandomNumber getInstance(long seed) {
        RandomNumber instance = getInstance();
        if (instance != null) {
            instance.setSeed(seed);
        }
        return instance;
    }
    
    /**
     * Creates an instance of the default Class with the given seeds.
     * 
     * @param seed Desired seeds for default instance
     * @return Instance of default class with seeds
     */    
    public static RandomNumber getInstance(long[] seed) {
        RandomNumber instance = getInstance();
        if (instance != null) {
            instance.setSeeds(seed);
        }
        return instance;
    }
    
    /**
     * Creates an instance of the given Class with its default seed(s).
     * 
     * @param className (Unqualified) name of desired 
     * <code>RandomNumber</code> class
     * @return Instance of desired class with its default seed
     */    
    public static RandomNumber getInstance(String className) {
        RandomNumber instance = null;
        Class randomNumberClass = getClassFor(className);
        if (randomNumberClass == null) {
            throw new IllegalArgumentException("Class not found: " + className);
        }
        try {
            instance = (RandomNumber) randomNumberClass.newInstance();
        }
        catch (IllegalAccessException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        catch (InstantiationException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        return instance;
    }
    
    /**
     * Creates an instance of the given Class with the given seed.
     * Should be used only for <CODE>RandomNumber</CODE>s that take
     * a single seed.
     * 
     * @return Instance of desired class with the given seed.
     * @param seed Desired seed
     * @param className (Unqualified) name of desired 
     * <code>RandomNumber</code> class
     */    
    public static RandomNumber getInstance(String className, long seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeed(seed);
        }
        return instance;
    }
    
    /**
     * Creates an instance of the given Class with the given seeds.
     * 
     * @return Instance of desired class with the given seeds.
     * @param seed Desired seeds
     * @param className (Unqualified) name of desired 
     * <code>RandomNumber</code> class
     */    
    public static RandomNumber getInstance(String className, long[] seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeeds(seed);
        }
        return instance;
    }
    
    /**
     * Creates an instance that has the same Class as the given 
     * <CODE>RandomNumber</CODE> and initial seed(s) equal to the current seed
     * of the given <CODE>RandomNumber</CODE>. While immediately upon creation 
     * the new <CODE>RandomNumber</CODE> is the same as the given instance, the
     * two are not linked. The two instances will independently generate the 
     * same sequence of numbers.
     * 
     * @return Identical copy of passed-in instance
     * @param rng Instance to be copied */    
    public static RandomNumber getInstance(RandomNumber rng) {
        return getInstance(rng.getClass().getName(), rng.getSeeds());
    }
    
    /**
     * Creates an instance of a <CODE>Pooled RandomNumber</CODE> of the given
     * Class using the 2 given instances of <CODE>RandomNumber</CODE>.
     * @param className Desired class implementing <code>Pooled</code>
     * 
     * @param first First instance to be pooled
     * @param second second instance to be pooled
     * @return <code>Pooled</code> instance pooling first and second
     * @throws IllegalArgumentException If the given Class does
     * not implement the <CODE>Pooled</CODE> interface.
     */    
    public static Pooled getInstance(String className, RandomNumber first, 
            RandomNumber second) {
        Pooled pooled = null;
        Class pooledClass = getClassFor(className);
        if (!simkit.random.Pooled.class.isAssignableFrom(pooledClass)) {
            throw new IllegalArgumentException(
                    "Does not implement Pooled interface: " +
                className);
        }
        try {
            pooled = (Pooled) pooledClass.newInstance();
            pooled.setFirst(first);
            pooled.setSecond(second);
        }
        catch (IllegalAccessException e) {
            System.err.println(e);
            throw(new RuntimeException(e));}
        catch (InstantiationException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        return pooled;
    }
    
    /**
     * Creates an instance of a <CODE>Pooled RandomNumber</CODE> of the given
     * Class using the 2 given instances of <CODE>RandomNumber</CODE>.
     * 
     * @param className Desired (unqualified) class
     * @param rng Array (of length 2) to be pooled
     * @return <code>Pooled</code> instance
     * @throws IllegalArgumentException If the given RandomNumber array
     * does not contain 2 instances of <CODE>RandomNumber</CODE> or
     * if the given Class does not implement <CODE>Pooled</CODE>.
     */    
    public static Pooled getInstance(String className, RandomNumber[] rng) {
        if (rng.length != 2) {
            throw new IllegalArgumentException(
                    "Need 2 RandomNumber instances: " + rng.length);
        }
        return getInstance(className, rng[0], rng[1]);
    }
    
    /**
     * Creates an instance of a <CODE>RandomNumber</CODE> the will generate
     * numbers that are antithetic to the given instance. Note that the
     * antithetic instance uses the given instance to generate and does
     * not make a copy of it; therefore, if two negatively correlated streams
     * are desired, the argument should be a copy.
     * 
     * @param rng <code>RandomNumber</code> instance to be antitheticized
     * @return <code>RandomNumber</code> instance that is antithetic
     * to one passed in.
     * @see Antithetic
     */    
    public static RandomNumber getAntithetic(RandomNumber rng) {
        return new Antithetic(rng);
    }
    
    /**
     * Adds the given package to the search path for RandomNumber Classes.
     * 
     * @param packageName Name of package to be searched when qualifying class
     * names (Note: not checked whether package actually exists).
     */    
    public static void addSearchPackage(String packageName) {
        if (!searchPackages.contains(packageName)) {
            searchPackages.add(packageName);
        }
    }
    
    /**
     * Removes the given package from the search path.
     * 
     * @param packageName Name of package to be removed from search path
     */    
    public static void removeSearchPackage(String packageName) {
        searchPackages.remove(packageName);
    }
    
    /**
     * Returns an array containing the names of the packages in the 
     * current search path.
     * @return Search package names
     */    
    public static String[] getSearchPackages() {
        return (String[]) searchPackages.toArray(new String[0]);
    }
    
    /** Helper method - given an unqualified class name, searches
     * through search path for actual class, returning the first one it finds.
     * 
     * @param className (Unqualified) name for desired class
     * @return Class object for unqualified name.
     */    
    public static Class getClassFor(String className) {
        //        First try to load the class as-is
        Class theClass = (Class) cache.get(className);
        if (theClass == null) {
            ClassLoader loader = Thread.currentThread().
                    getContextClassLoader();
            try {
                theClass = loader.loadClass(className);
                if (simkit.random.RandomNumber.class.
                        isAssignableFrom(theClass)) {
                    cache.put(className, theClass);
                }
                else {
                    theClass = null;
                }
            } catch (ClassNotFoundException e) {}
        }
        if (theClass == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            for (int i = 0; i < searchPackages.size(); i++ ) {
                try {
                    theClass = loader.loadClass(searchPackages.get(i) + 
                            "." + className);
                    if (simkit.random.RandomNumber.class.isAssignableFrom(
                            theClass)) {
                        cache.put(className, theClass);
                        break;
                    }
                    else {
                        theClass = null;
                        continue;
                    }
                } catch (ClassNotFoundException f) {}
            }
        }
        return theClass;
    }
    
    /**
     * Temporary method for experimenting with RngStream.
     * 
     * @deprecated use getInstance(String Map) instead
     * @param stream
     * @param substream
     * @return
     */
    public static RandomNumberStream getRngStream(String className, 
            int stream, int substream){
        
        RandomNumberStream rs = (RandomNumberStream)getInstance(className);
        rs.setStreamAndSubstream(stream, substream);
        return rs;
    }
}

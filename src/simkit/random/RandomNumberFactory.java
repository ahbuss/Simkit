package simkit.random;

import java.util.*;

public class RandomNumberFactory {
    
    protected static final String DEFAULT_CLASS = "simkit.random.Congruential";
    protected static Class defaultClass ;
    
    protected static Map cache;
    protected static ArrayList searchPackages;
    protected static boolean verbose;
    
    /**
     * @param v Sets verbose mode (not implemented yet)
     */    
    public static void setVerbose(boolean v) { verbose = v; }
    /**
     * @return verbose mode
     */    
    public static boolean isVerbose() { return verbose; }
    
    /**
     * @return Map containing unqualified names with corresponding
     * Class objects.
     */    
    public static Map getCache() { return new WeakHashMap(cache); }
    
    static {
        cache = new WeakHashMap();
        searchPackages = new ArrayList();
        setDefaultClass(DEFAULT_CLASS);
        addSearchPackage("simkit.random");
    }
    
    /** The default class is instantiated when the user does not
     * specify a class.
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
    
    protected RandomNumberFactory() {
    }
    
    /**
     * @return Instance of default class with its default seed(s)
     */    
    public static RandomNumber getInstance() {
        RandomNumber instance = null;
        try {
            instance = (RandomNumber) defaultClass.newInstance();
        }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }
    
    /**
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
     * @param className (Unqualitfied) name of desired <code>RandomNumber</code> class
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
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }
    
    /**
     * @return Instance of desired class with its default seed
     * @param seed Desired seed
     * @param className (Unqualitfied) name of desired <code>RandomNumber</code> class
     */    
    public static RandomNumber getInstance(String className, long seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeed(seed);
        }
        return instance;
    }
    
    /**
     * @return Instance of desired class with its default seed
     * @param seed Desired seeds
     * @param className (Unqualitfied) name of desired <code>RandomNumber</code> class
     */    
    public static RandomNumber getInstance(String className, long[] seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeeds(seed);
        }
        return instance;
    }
    
    /**
     * @return Identical copy of passed-in instance
     * @param rng Instance to be copied */    
    public static RandomNumber getInstance(RandomNumber rng) {
        return getInstance(rng.getClass().getName(), rng.getSeeds());
    }
    
    /**
     * @param className Desired class implementing <code>Pooled</code>
     * @param first First instance to be pooled
     * @param second second instance to be pooled
     * @return <code>Pooled</code> instance pooling first and second
     */    
    public static Pooled getInstance(String className, RandomNumber first, RandomNumber second) {
        Pooled pooled = null;
        Class pooledClass = getClassFor(className);
        if (!simkit.random.Pooled.class.isAssignableFrom(pooledClass)) {
            throw new IllegalArgumentException("Does not implement Pooled interface: " +
                className);
        }
        try {
            pooled = (Pooled) pooledClass.newInstance();
            pooled.setFirst(first);
            pooled.setSecond(second);
        }
        catch (IllegalAccessException e) {System.err.println(e);}
        catch (InstantiationException e) {System.err.println(e);}
        return pooled;
    }
    
    /**
     * @param className Desired (unqualified) class
     * @param rng Array (of length 2) to be pooled
     * @return <code>Pooled</code> instance
     */    
    public static Pooled getInstance(String className, RandomNumber[] rng) {
        if (rng.length != 2) {
            throw new IllegalArgumentException("Need 2 RandomNumber instances: " +
            rng.length);
        }
        return getInstance(className, rng[0], rng[1]);
    }
    
    /**
     * @param rng <code>RandomNumber</code> instance to be antitheticized
     * @return <code>RandomNumber</code> instance that is antithetic
     * to one passed in.
     */    
    public static RandomNumber getAntithetic(RandomNumber rng) {
        return new Antithetic(rng);
    }
    
    /**
     * @param packageName Name of package to be searched when qualifying class
     * names (Note: not checked whether package actually exists).
     */    
    public static void addSearchPackage(String packageName) {
        if (!searchPackages.contains(packageName)) {
            searchPackages.add(packageName);
        }
    }
    
    /**
     * @param packageName Name of package to be removed from search path
     */    
    public static void removeSearchPackage(String packageName) {
        searchPackages.remove(packageName);
    }
    
    /**
     * @return Search package names
     */    
    public static String[] getSearchPackages() {
        return (String[]) searchPackages.toArray(new String[0]);
    }
    
    /** Helper method - given an unqualified class name, searches
     * through search path for actual class, returning the first one it finds.
     * @param className (Unqualified) name for desired class
     * @return Class object for unqualified name.
     */    
    public static Class getClassFor(String className) {
        //        First try to load the class as-is
        Class theClass = (Class) cache.get(className);
        if (theClass == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                theClass = loader.loadClass(className);
                if (simkit.random.RandomNumber.class.isAssignableFrom(theClass)) {
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
                    theClass = loader.loadClass(searchPackages.get(i) + "." + className);
                    if (simkit.random.RandomNumber.class.isAssignableFrom(theClass)) {
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
}

package simkit.random;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
/** 
 * Factory for creating <CODE>RandomVariate</CODE> instances from "orders".
 * The "specifications" of each order are as generic as possible:
 * String (name of class), Object[] (parameters), long (seed),
 * RandomNumber (instance of supporting Un(0,1) generator).
 * <P/>The default supporting RandomNumber is determined by the implementation
 * of the RandomVariate. In most cases it is <CODE>Congruential</CODE>.
 * @author Arnold Buss
 * @version $Id$
 */
public class RandomVariateFactory {
    
/**
* Holds a cache of the RandomVariate Classes that have already been 
* found indexed by their name.
**/
    protected static Map cache;
    
/**
* A list of packages to search for RandomVariates if the 
* class name given is not fully qualified.
**/
    protected static String[] searchPackages;

/**
* If true, print out information while searching for RandomVariate
* Classes.
**/
    protected static boolean verbose;

/**
* If true, print out information while searching for RandomVariate
* Classes.
**/
    public static void setVerbose(boolean b) { verbose = b; }
/**
* If true, print out information while searching for RandomVariate
* Classes.
**/
    public static boolean isVerbose() { return verbose; }
    
/**
* If true, print out information while searching for RandomVariate
* Classes.
**/
    public static Map getCache() { return new WeakHashMap(cache); }
    
    static {
        setSearchPackages(new String[] {"simkit.random"});
        cache = new WeakHashMap();
    }

/**
* This factory Class should never by instantiated.
**/
   protected RandomVariateFactory() {
   }
    
    /** 
     * Creates a <CODE>RandomVariate</CODE> instance with default seed(s) and
     * the default supporting <CODE>RandomNumber</CODE>.
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @return Instance of <CODE>RandomVariate</CODE> based on the 
     * (fully-qualified) class name and the parameters.  The default 
     * <CODE>RandomNumber</CODE> instance is used with the default seed(s).
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(String className, Object[] parameters) {
        if (className == null) {
            throw new IllegalArgumentException("null class name");
        }
        // First check cache
        Class randomVariateClass = (Class) cache.get(className);
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className);
            if (randomVariateClass == null) {
                // The name may be the distribution - try appending "Variate"
                randomVariateClass = findFullyQualifiedNameFor(className + "Variate");
            }
            // All attempts have failed
            if (randomVariateClass == null) {
                throw new IllegalArgumentException("RandomVariate class not found for " + className);
            }
            else {
                cache.put(className, randomVariateClass);
            }
        }
        return getInstance(randomVariateClass, parameters);
    }
    
    /** 
     * Creates a <CODE>RandomVariate</CODE> instance with 
     * the default supporting <CODE>RandomNumber</CODE>.
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @param seed The seed for the supporting <CODE>RandomNumber</CODE>
     * @return Instance of <CODE>RandomVariate</CODE> based on the 
     * (fully-qualified) class name and the parameters.  The default 
     * <CODE>RandomNumber</CODE> instance is used.
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(String className, Object[] parameters, long seed) {
        RandomVariate instance = getInstance(className, parameters);
        instance.getRandomNumber().setSeed(seed);
        return instance;
    }
    
    /** 
     * Creates a <CODE>RandomVariate</CODE> instance supported by the
     * <CODE>RandomNumber</CODE> instance passed in. 
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
     * @param rng An instance of <CODE>RandomNumber</CODE> to support this
     * RandomVariate.
     * @return Instance of <CODE>RandomVariate</CODE> based on the 
     * (fully-qualified) class name and the parameters.  
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(String className, Object[] parameters, RandomNumber rng) {
        if (className == null) {
            throw new IllegalArgumentException("Name of RandomVariate class is null.");
        }
        RandomVariate instance = null;
        Class randomVariateClass = (Class) cache.get(className);
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className);
        }
        if (randomVariateClass == null) {
            randomVariateClass = findFullyQualifiedNameFor(className + "Variate");
        }
        if (randomVariateClass == null) {
            throw new IllegalArgumentException("Can't find RandomVariate class for " + className);
        }
        else {
            cache.put(className, randomVariateClass);
        }
        instance = getInstance(randomVariateClass, parameters);
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
            getInstance(rv.getClass(), rv.getParameters(), rv.getRandomNumber());
        return newInstance;
    }
    
/**
* Creates a new RandomVariate using the default supporting RandomNumber.
* @param rvClass The Class of the desired RandomVariate.
* @param params The parameters required to contruct the RandomVariate.
* @param seed The starting seed for the supporting RandomNumber.
* @throws IllegalArgumentException If rvClass is not a RandomVariate or if
* it is <CODE>null</CODE>.
**/
    public static RandomVariate getInstance(Class rvClass, Object[] params, long seed) {
        RandomVariate instance = getInstance(rvClass, params);
        instance.getRandomNumber().setSeed(seed);
        return instance;
    }
    
/**
* Creates a new RandomVariate using the default supporting RandomNumber.
* Used if the supporting RandomNumber requires multiple seeds.
* @param rvClass The Class of the desired RandomVariate.
* @param params The parameters required to contruct the RandomVariate.
* @param seed The starting seeds for the supporting RandomNumber.
* @throws IllegalArgumentException If rvClass is not a RandomVariate or if
* it is <CODE>null</CODE>.
**/
    public static RandomVariate getInstance(Class rvClass, Object[] params, long[] seed) {
        RandomVariate instance = getInstance(rvClass, params);
        instance.getRandomNumber().setSeeds(seed);
        return instance;
    }
    
/**
* Creates a new RandomVariate using the default supporting RandomNumber and
* the default seed.
* @param rvClass The Class of the desired RandomVariate.
* @param params The parameters required to contruct the RandomVariate.
* @throws IllegalArgumentException If rvClass is not a RandomVariate or if
* it is <CODE>null</CODE>.
**/
    public static RandomVariate getInstance(Class rvClass, Object[] params) {
        if (rvClass == null) {
            throw new IllegalArgumentException("null class passed to RandomVariateFactory");
        }
        if (!simkit.random.RandomVariate.class.isAssignableFrom(rvClass)) {
            throw new IllegalArgumentException("Class " + rvClass +
            " does not implement simkit.random.RandomVariate interface");
        }
        RandomVariate instance = null;
        try {
            instance = (RandomVariate) rvClass.newInstance();
            instance.setParameters(params);
        }
        catch (IllegalAccessException e) {System.err.println(e);}
        catch (InstantiationException e) {System.err.println(e);}
        return instance;
    }
    
    /** 
     * Creates a <CODE>RandomVariate</CODE> instance supported by the
     * <CODE>RandomNumber</CODE> instance passed in. 
     * @param rvClass The class object of the desired instance
     * @param parameters The desired parameters for the instance
     * @param rng An instance of <CODE>RandomNumber</CODE> to support this
     * RandomVariate.
     * @return Instance of <CODE>RandomVariate</CODE> based on the 
     * class and the parameters.  
     * @throws IllegalArgumentException If the className is <CODE>null</CODE> or
     * a class with that name cannot be found.
     */
    public static RandomVariate getInstance(Class rvClass, Object[] params, RandomNumber rng) {
        RandomVariate instance = getInstance(rvClass, params);
        instance.setRandomNumber(rng);
        return instance;
    }
    
/**
* Adds the given fully qualified package name to the list of packages
* that will be searched when attempting to find RandomVariates by name.
**/
    public static void addSearchPackage(String newPackage) {
        ArrayList temp = new ArrayList();
        for (int i = 0; i < searchPackages.length; i++) {
            temp.add(searchPackages[i]);
        }
        temp.add(newPackage);
        searchPackages = (String[]) temp.toArray(new String[temp.size()]);
    }
    
/**
* Sets the list of packages that will be searched when attempting to find
* a RandomVariate by name.
**/
    public static void setSearchPackages(String[] packages) {
        searchPackages = (String[]) packages.clone();
    }
    
/**
* Returns a copy of the list of packages that will be searched when attempting to find
* a RandomVariate by name.
**/
    public static String[] getSearchPackages() { return (String[]) searchPackages.clone(); }
    
/**
* Finds the RandomVariate Class corresponding to the given name. First
* attempts to find the RandomVariate assuming the the name is fully qualified.
* Then searches the "search packages." The search path defaults to "simit.random"
* but additional search packages can be added.
* @see #addSearchPackage(String)
* @see #setSearchPackages(String[])
**/
    public static Class findFullyQualifiedNameFor(String className) {
        Class theClass = null;
        //        First see if name passed is "fully qualified"
        try {
            theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            return theClass;
        }
        //        If not, then try the search path
        catch (ClassNotFoundException e) {}
        for (int i = 0; i < searchPackages.length; i++) {
            if (verbose) {
                System.out.println("Checking " + searchPackages[i] + "." + className);
            }
            try {
                theClass = Thread.currentThread().getContextClassLoader().loadClass(
                searchPackages[i] + "." + className );
                if (!simkit.random.RandomVariate.class.isAssignableFrom(theClass)) {
                    continue;
                }
            }
            catch (ClassNotFoundException e) { continue; }
        }
        if (verbose) {System.out.println("returning " + theClass);}
        return theClass;
        
    }
/*
    public static Class getRandomVariateClass(String baseName) throws ClassNotFoundException {
        Class theClass = null;
        if (baseName.indexOf('.') > -1) {
            return Class.forName(baseName);
        }
        try {
 
        }
        catch (ClassNotFoundException e) {
        }
    }
 */
}

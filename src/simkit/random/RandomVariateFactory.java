package simkit.random;
import java.lang.reflect.*;
import java.util.*;
/** Factory for creating <CODE>RandomVariate</CODE> instances from "orders".
 * The "specifications" of each order are as generic as possible:
 * String (name of class), Object[] (parameters), long (seed),
 * RandomNumber (instance of supporting Un(0,1) generator).
 * @author Arnold Buss
 */
public class RandomVariateFactory {
    
    protected static Map cache;
    
    protected static String[] searchPackages;
    protected static boolean verbose;
    
    public static void setVerbose(boolean b) { verbose = b; }
    public static boolean isVerbose() { return verbose; }
    
    public static Map getCache() { return new WeakHashMap(cache); }
    
    static {
        setSearchPackages(new String[] {"simkit.random"});
        cache = new WeakHashMap();
    }
    
    /** Create a <CODE>RandomVariate</CODE> instance based on the parameters passed.
     * @return Instance of <CODE>RandomVariate</CODE> based on the (fully-qualified) class name and the parameters.  The default <CODE>RandomNumber</CODE> instance is used with the default seed(s).
     * @param className The fully-qualified class name of the desired instance
     * @param parameters The desired parameters for the instance
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
    
    public static RandomVariate getInstance(String className, Object[] parameters, long seed) {
        RandomVariate instance = getInstance(className, parameters);
        instance.getRandomNumber().setSeed(seed);
        return instance;
    }
    
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
    
    public static RandomVariate getInstance(RandomVariate rv) {
        RandomVariate newInstance = (RandomVariate) rv.clone();
        newInstance.setRandomNumber((RandomNumber) rv.getRandomNumber().clone());
        return newInstance;
    }
    
    public static RandomVariate getInstance(Class rvClass, Object[] params, long seed) {
        RandomVariate instance = getInstance(rvClass, params);
        instance.getRandomNumber().setSeed(seed);
        return instance;
    }
    
    public static RandomVariate getInstance(Class rvClass, Object[] params, long[] seed) {
        RandomVariate instance = getInstance(rvClass, params);
        instance.getRandomNumber().setSeeds(seed);
        return instance;
    }
    
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
    
    public static void addSearchPackage(String newPackage) {
        ArrayList temp = new ArrayList();
        for (int i = 0; i < searchPackages.length; i++) {
            temp.add(searchPackages[i]);
        }
        temp.add(newPackage);
        searchPackages = (String[]) temp.toArray(new String[temp.size()]);
    }
    
    public static void setSearchPackages(String[] packages) {
        searchPackages = (String[]) packages.clone();
    }
    
    public static String[] getSearchPackages() { return (String[]) searchPackages.clone(); }
    
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

package simkit.random;

public class RandomNumberFactory {
    
    protected static final String DEFAULT_CLASS = "simkit.random.Congruential";
    protected static Class defaultClass ;
    
    static {
        setDefaultClass(DEFAULT_CLASS);
    }
    
    public static void setDefaultClass(String className) {
        try {
            defaultClass = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) { System.err.println(e); }
        if (!simkit.random.RandomNumber.class.isAssignableFrom(defaultClass)) {
            throw new IllegalArgumentException("Class " + className +
            " does not implement simkit.random.RandomNumber interface");
        }
    }
    
    protected RandomNumberFactory() {
    }
    
    public static RandomNumber getInstance() {
        RandomNumber instance = null;
        try {
            instance = (RandomNumber) defaultClass.newInstance();
        }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }
    
    public static RandomNumber getInstance(long seed) {
        RandomNumber instance = getInstance();
        if (instance != null) {
            instance.setSeed(seed);
        }
        return instance;
    }
    
    public static RandomNumber getInstance(long[] seed) {
        RandomNumber instance = getInstance();
        if (instance != null) {
            instance.setSeeds(seed);
        }
        return instance;
    }
    
    public static RandomNumber getInstance(String className) {
        RandomNumber instance = null;
        Class randomNumberClass = null;
        try {
            randomNumberClass = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) { System.err.println(e); }
        if (!simkit.random.RandomNumber.class.isAssignableFrom(randomNumberClass)) {
            throw new IllegalArgumentException("Class " + className +
            " does not implement simkit.random.RandomNumber interface");
        }
        try {
            instance = (RandomNumber) randomNumberClass.newInstance();
        }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }
    
    public static RandomNumber getInstance(String className, long seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeed(seed);
        }
        return instance;
    }
    
    public static RandomNumber getInstance(String className, long[] seed) {
        RandomNumber instance = getInstance(className);
        if (instance != null) {
            instance.setSeeds(seed);
        }
        return instance;
    }
    
    public static RandomNumber getInstance(RandomNumber rng) {
        return getInstance(rng.getClass().getName(), rng.getSeeds());
    }
    
    public static RandomNumber getAntithetic(RandomNumber rng) {
        return new Antithetic(rng);
    }
}

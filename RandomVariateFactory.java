package simkit.random;

public class RandomVariateFactory {

    public static RandomVariate getInstance(String className, Object[] parameters) {
        RandomVariate instance = null;
        Class randomVariateClass = null;
        try {
            randomVariateClass = Class.forName(className);
        }                            
        catch (ClassNotFoundException e) { System.err.println(e); }
        if (!simkit.random.RandomVariate.class.isAssignableFrom(randomVariateClass)) {
            throw new IllegalArgumentException("Class " + className +
                " does not implement simkit.random.RandomVariate interface");
        }
        try {
            instance = (RandomVariate) randomVariateClass.newInstance();
            instance.setParameters(parameters);
        }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }

    public static RandomVariate getInstance(String className, Object[] parameters, long seed) {
        RandomVariate instance = getInstance(className, parameters);
        if (instance != null) {
            instance.getRandomNumber().setSeed(seed);
        }
        return instance;
    }

    public static RandomVariate getInstance(String className, Object[] parameters, RandomNumber rng) {
        RandomVariate instance = null;
        Class randomVariateClass = null;
        try {
            randomVariateClass = Class.forName(className);
        }                            
        catch (ClassNotFoundException e) { System.err.println(e); }
        if (!simkit.random.RandomVariate.class.isAssignableFrom(randomVariateClass)) {
            throw new IllegalArgumentException("Class " + className +
                " does not implement simkit.random.RandomVariate interface");
        }
        try {
            instance = (RandomVariate) randomVariateClass.newInstance();
            instance.setParameters(parameters);
            instance.setRandomNumber(rng);
        }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        return instance;
    }

    public static RandomVariate getInstance(RandomVariate rv) {
        return getInstance(rv.getClass().getName(), rv.getParameters(),
            RandomNumberFactory.getInstance(rv.getRandomNumber()));
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

package simkit.smd;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores Adjudicators indexed by the Munition and Target they should be used
 * for. The Adjudicators can either be created by the factory or instantiated
 * and then added. If an Adjudicator was not added to the container for a
 * Munition-Target pair, then the get methods will return the default
 * Adjudicator.
 *
 * @author Arnold Buss
 */
public class MunitionTargetAdjudicatorFactory {

    /**
     * A HashMap containing the Adjudicators.
     *
     */
    protected static final Map<Class<? extends Munition>, Map<Class<? extends Target>, Adjudicator>> adjudicators;

    /**
     * The Adjudicator that is returned if none for a Munition-Target pair has
     * been added to the factory. The default Adjudicator is an instance of
     * DefaultAdjudicator (which does nothing) unless another default has been
     * set.
     *
     */
    protected static Adjudicator defaultAdjudicator;

    static {
        adjudicators = new LinkedHashMap<>();
        setDefaultAdjudicator(new DefaultAdjudicator());
    }

    /**
     * This class should never be instantiated. All methods are static.
     *
     */
    private MunitionTargetAdjudicatorFactory() {
    }

    /**
     * Creates and adds to the container an Adjudicator of the specified Class.
     *
     * @param munitionClass The Class of the Munition that the Adjudicator is
     * associated with.
     * @param targetClass The Class of the Target that the Adjudicator is
     * associated with.
     * @param adjudicatorClass The Class of Adjudicator to create.
     *
     */
    public static void addAdjudicator(Class<? extends Munition> munitionClass,
            Class<? extends Target> targetClass,
            Class<? extends Adjudicator> adjudicatorClass) {
        Adjudicator adjudicator;
        try {
            adjudicator = adjudicatorClass.getConstructor().newInstance();
            addAdjudicator(munitionClass, targetClass, adjudicator);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MunitionTargetAdjudicatorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Adds an Adjudicator to the container.
     *
     * @param munitionClass The Class of the Munition that the Adjudicator is
     * associated with.
     * @param targetClass The Class of the Target that the Adjudicator is
     * associated with.
     * @param adjudicator An instance of an Adjudicator to add to the container.
     *
     */
    public static void addAdjudicator(Class<? extends Munition> munitionClass,
            Class<? extends Target> targetClass, Adjudicator adjudicator) {
        Map<Class<? extends Target>, Adjudicator> targetAdjudicators = adjudicators.get(munitionClass);
        if (targetAdjudicators == null) {
            targetAdjudicators = new LinkedHashMap<>();
            adjudicators.put(munitionClass, targetAdjudicators);
        }
        targetAdjudicators.put(targetClass, adjudicator);
    }

    /**
     * Returns the Adjudicator for the given Munition and Target. If an
     * Adjudicator has not been specified for the Munition-Target pair, then
     * returns the default Adjudicator.
     *
     * @param munition given Munition
     * @param target given Target
     * @return the Adjudicator for the given Munition and Target.
     */
    public static Adjudicator getAdjudicator(Munition munition, Target target) {
        return getAdjudicator(munition.getClass(), target.getClass());
    }

    /**
     * Gets the Adjudicator for the given Munition class and Target class. If an
     * Adjudicator has not been specified for the Munition-Target pair, then
     * returns the default Adjudicator.
     *
     * @param munitionClass Given Munition class
     * @param targetClass Given Target class
     * @return the Adjudicator for the given Munition class and Target class
     */
    public static Adjudicator getAdjudicator(Class munitionClass, Class targetClass) {
        Adjudicator adjudicator = getDefaultAdjudicator();
        Map targetAdjudicators = (Map) adjudicators.get(munitionClass);
        if (targetAdjudicators != null) {
            Object targetAdjudicator = targetAdjudicators.get(targetClass);
            if (targetAdjudicator != null) {
                adjudicator = (Adjudicator) targetAdjudicator;
            }
        }
        return adjudicator;
    }

    /**
     *
     * @param adjudicator Given default Adjudicator
     */
    public static void setDefaultAdjudicator(Adjudicator adjudicator) {
        defaultAdjudicator = adjudicator;
    }

    /**
     * @return the default Adjudicator
     */
    public static Adjudicator getDefaultAdjudicator() {
        return defaultAdjudicator;
    }

}

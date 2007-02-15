/*
 * MunitionTargetAdjudicatorFactory.java
 *
 * Created on July 29, 2002, 2:30 PM
 */

package simkit.smdx;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Stores Adjudicators indexed by the Munition and Target they should be used for. 
 * The Adjudicators can either be created by the factory or instantiated and then added.
 * If an Adjudicator was not
 * added to the container for a Munition-Target pair, then the get methods will
 * return the default Adjudicator. 
 * @author  Arnold Buss
 * @version $Id$
 */
public class MunitionTargetAdjudicatorFactory {
    
/**
* A HashMap containing the Adjudicators.
**/
    protected static final Map<Class, Map<Class, Adjudicator>> adjudicators;
    
/**
* The Adjudicator that is returned if none for a Munition-Target pair
* has been added to the factory. The default Adjudicator is an
* instance of DefaultAdjudicator (which does nothing) unless
* another default has been set.
**/
    protected static Adjudicator defaultAdjudicator;
    
    static {
        adjudicators = 
                Collections.synchronizedMap(
                new LinkedHashMap<Class, Map<Class, Adjudicator>>());
        setDefaultAdjudicator(new DefaultAdjudicator());
    }

/**
* This class should never be instantiated. All methods are static.
**/    
    private MunitionTargetAdjudicatorFactory() {}

/**
* Creates and adds to the container an Adjudicator of the specified Class.
* @param munitionClass The Class of the Munition that the Adjudicator is associated with.
* @param targetClass The Class of the Target that the Adjudicator is associated with.
* @param adjudicatorClass The Class of Adjudicator to create.
* @throws ClassCastException If adjudicatorClass is not an Adjudicator.
**/
    public static void addAdjudicator(Class munitionClass, Class targetClass,
        Class adjudicatorClass) {
            try {
                addAdjudicator(munitionClass, targetClass, (Adjudicator) adjudicatorClass.newInstance());
            }
            catch (IllegalAccessException e) { System.err.println(e); }
            catch (InstantiationException e) { System.err.println(e); }
    }
    
    
/**
* Adds an Adjudicator to the container.
* @param munitionClass The Class of the Munition that the Adjudicator is associated with.
* @param targetClass The Class of the Target that the Adjudicator is associated with.
* @param adjudicator An instance of an Adjudicator to add to the container.
**/
    public static void addAdjudicator(Class munitionClass, Class targetClass, Adjudicator adjudicator) {
        Map<Class, Adjudicator> targetAdjudicators = adjudicators.get(munitionClass);
        if (targetAdjudicators == null) {
            targetAdjudicators = new LinkedHashMap<Class, Adjudicator>();
            adjudicators.put(munitionClass, targetAdjudicators);
        }
        targetAdjudicators.put(targetClass, adjudicator);
    }
    
/**
* Gets the Adjudicator for the given Munition and Target. If an Adjudicator
* has not been specified for the Munition-Target pair, then returns the
* default Adjudicator.
**/
    public static Adjudicator getAdjudicator(Munition munition, Target target) {
        return getAdjudicator(munition.getClass(), target.getClass());
    }

/**
* Gets the Adjudicator for the given Munition class and Target class. If an Adjudicator
* has not been specified for the Munition-Target pair, then returns the
* default Adjudicator.
**/
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
* Sets the Adjudicator that will be returned for Munition-Target pairs that are not
* in the container.
**/
    public static void setDefaultAdjudicator(Adjudicator adjudicator) { defaultAdjudicator = adjudicator; }
    
/**
* Gets the default Adjudicator.
**/
    public static Adjudicator getDefaultAdjudicator() { return defaultAdjudicator; }
    
}

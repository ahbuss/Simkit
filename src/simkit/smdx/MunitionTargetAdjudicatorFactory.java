/*
 * MunitionTargetAdjudicatorFactory.java
 *
 * Created on July 29, 2002, 2:30 PM
 */

package simkit.smdx;
import java.util.*;
/**
 *
 * @author  Arnold Buss
 */
public class MunitionTargetAdjudicatorFactory {
    
    protected static final Map adjudicators;
    
    protected static Adjudicator defaultAdjudicator;
    
    static {
        adjudicators = Collections.synchronizedMap(new HashMap());
        
    }
    
    public static void addAdjudicator(Class munitionClass, Class targetClass,
        Class adjudicatorClass) {
            try {
                addAdjudicator(munitionClass, targetClass, (Adjudicator) adjudicatorClass.newInstance());
            }
            catch (IllegalAccessException e) { System.err.println(e); }
            catch (InstantiationException e) { System.err.println(e); }
    }
    
    
    public static void addAdjudicator(Class munitionClass, Class targetClass, Adjudicator adjudicator) {
        Map targetAdjudicators = (Map) adjudicators.get(munitionClass);
        if (targetAdjudicators == null) {
            targetAdjudicators = new HashMap();
            adjudicators.put(munitionClass, targetAdjudicators);
        }
        targetAdjudicators.put(targetClass, adjudicator);
    }
    
    public static Adjudicator getAdjudicator(Munition munition, Target target) {
        return getAdjudicator(munition.getClass(), target.getClass());
    }

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
    
    public static void setDefaultAdjudicator(Adjudicator adjudicator) { defaultAdjudicator = adjudicator; }
    
    public static Adjudicator getDefaultAdjudicator() { return defaultAdjudicator; }
    
}

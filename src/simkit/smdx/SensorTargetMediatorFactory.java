/*
 * SensorTargetMediatorFactory.java
 *
 * Created on March 6, 2002, 6:31 PM
 */

package simkit.smdx;
import simkit.smdx.*;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;
/**
 *
 * @author  Arnold Buss
 * @version
 */
public class SensorTargetMediatorFactory {
    
    private static WeakHashMap cache;
    
    /** Creates new SensorTargetMediatorFactory */
    protected SensorTargetMediatorFactory() {
    }
    
    public static void addMediatorFor(Class sensorClass, Class targetClass, Class mediatorClass) {
        if (!(simkit.smdx.Sensor.class.isAssignableFrom(sensorClass))) {
            throw new IllegalArgumentException(sensorClass + " is not a Sensor class");
        }
        if (!(simkit.smdx.Moveable.class.isAssignableFrom(targetClass))) {
            throw new IllegalArgumentException(targetClass + " is not a Moveable class");
        }
        if (!(simkit.smdx.SensorTargetMediator.class.isAssignableFrom(mediatorClass))) {
            throw new IllegalArgumentException(mediatorClass + " is not a SensorTargetMediator");
        }
        Object mediatorInstance = null;
        try {
            mediatorInstance = mediatorClass.newInstance();
        }
        catch (InstantiationException e) {System.err.println(e);}
        catch (IllegalAccessException e) {System.err.println(e);}
        
        if (!cache.containsKey(sensorClass)) {
            cache.put(sensorClass, new WeakHashMap());
        }
        Map targetClasses = (Map) cache.get(sensorClass);
        targetClasses.put(targetClass, mediatorInstance);
    }
    
    public static SensorTargetMediator getMediatorFor(Class sensorClass, Class targetClass) {
        SensorTargetMediator mediator = null;
        if (cache.containsKey(sensorClass)) {
            Map targetClasses = (Map) cache.get(sensorClass);
            mediator = (SensorTargetMediator) targetClasses.get(targetClass);
        }
        if (mediator == null) {
            throw new IllegalArgumentException("Mediator not set for (" +
            sensorClass.getName() + ", " + targetClass.getName() +")");
        }
        return mediator;
    }
    
    public static SensorTargetMediator getMediatorFor(Sensor sensor, Moveable target) {
        return getMediatorFor(sensor.getClass(), target.getClass());
    }
    
    public static Map getMediators() {
        Map copy = null;
        synchronized(cache) {
            copy = new WeakHashMap(cache);
            for (Iterator i = copy.keySet().iterator(); i.hasNext(); ) {
                Object key = i.next();
                copy.put(key, new WeakHashMap((Map) copy.get(key)));
            }
        }
        return copy;
    }
}

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
 * Holds SensorTargetMediators. The Mediators can either be constructed
 * then added to the factory or constructed by the factory.
 * @author  Arnold Buss
 * @version $Id$
 */
public class SensorTargetMediatorFactory implements MediatorFactory {
    
/**
* Holds the instance of SensorTargetMediatorFactory.
**/
    protected static final SensorTargetMediatorFactory instance = 
        new SensorTargetMediatorFactory();
    
/**
* Gets the instance of SensorTargetMediatorFactory.
**/
    public static MediatorFactory getInstance() { return instance; }
    
/**
* Constructs and adds a Mediator to the MediatorFactory.
* @param sensorClass The Class of the Sensor for the added Mediator
* @param targetClass The Class of the target for the added Mediator (Must be a
* Moveable
* @param mediatorClass The Class of the Mediator to add.
* @throws IllegalArgumentException if the sensorClass is not a Sensor, the
* targetClass is not a Moveable or the mediatorClass is not a Mediator
**/
    public static void addMediator(Class sensorClass, Class targetClass,
            Class mediatorClass) {
        getInstance().addMediatorFor(sensorClass, targetClass, mediatorClass);
    }
    
/**
* Gets the Mediator for the given Sensor Class and Moveable Class.
* @param sensorClass the Sensor Class for which to retreve the Mediator.
* @param targetClass the Moveable Class for which to retrieve the Mediator.
* @throws IllegalArgumentException if there is no Mediator set for the 
* given Sensor-target pair.
**/
    public static Mediator getMediator(Class sensorClass, Class targetClass) {
        return getInstance().getMediatorFor(sensorClass, targetClass);
    }
    
/**
* Holds the Mediators that have been added to this factory, key by
* Sensor and Moveable (target)
**/
    private WeakHashMap cache;
    
/** 
* Creates new empty SensorTargetMediatorFactory
**/
    protected SensorTargetMediatorFactory() {
        cache = new WeakHashMap();
    }
    
/**
* Constructs and adds a Mediator to this MediatorFactory.
* @param sensorClass The Class of the Sensor for the added Mediator
* @param targetClass The Class of the target for the added Mediator (Must be a
* Moveable
* @param mediatorClass The Class of the Mediator to add.
* @throws IllegalArgumentException if the sensorClass is not a Sensor, the
* targetClass is not a Moveable or the mediatorClass is not a Mediator
**/
    public void addMediatorFor(Class sensorClass, Class targetClass, Class mediatorClass) {
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
    
/**
* Gets the Mediator for the given Sensor Class and Moveable Class.
* @param sensorClass the Sensor Class for which to retreve the Mediator.
* @param targetClass the Moveable Class for which to retrieve the Mediator.
* @throws IllegalArgumentException if there is no Mediator set for the 
* given Sensor-target pair, sensorClass is not a Sensor, or targetClass
* is not a Moveable.
**/
    public Mediator getMediatorFor(Class sensorClass, Class targetClass) {
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
    
/**
* Returns a copy of the HashMap of Mediators that have been added to this
* MediatorFactory.
**/
    public Map getMediators() {
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
    
/**
* Constructs and adds a Mediator to the MediatorFactory.
* @param first The name of the Sensor class for the added Mediator.
* @param second The name of the Moveable (target) class for the added Meditor.
* @param mediator The name of the Mediator class to add.
* @throws ClassNotFoundException if any of the three parameters are not the name 
* of a valid Class
* @throws IllegalArgumentException if first is not the name of a Sensor, 
* second is not the name of a Moveable or mediator is not the name of a Mediator 
* (but all three are names of valid Classes)
**/
    public void addMediatorFor(String first, String second, String mediator) throws ClassNotFoundException {
        Class firstClass = Thread.currentThread().getContextClassLoader().loadClass(first);
        Class secondClass = Thread.currentThread().getContextClassLoader().loadClass(second);
        Class mediatorClass = Thread.currentThread().getContextClassLoader().loadClass(mediator);
        addMediatorFor(firstClass, secondClass, mediatorClass);
    }
    
/**
* Gets the Mediator for the given Sensor and Moveable (target)
* @param first The Sensor to get the Mediator for.
* @param second The Moveable to get the Mediator for.
* @throws IllegalArgumentException if there is no Mediator set for the 
* given Sensor-target pair or if first is not a Sensor or second is
* not a Moveable.
**/
    public Mediator getMeditorFor(Object first, Object second) {
        return getMediatorFor(first.getClass(), second.getClass());
    }
    
/**
* Removes all Mediators from this factory.
**/
    public void clear() {
        cache.clear();
    }
    
}

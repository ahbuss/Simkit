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
public class SensorTargetMediatorFactory implements MediatorFactory {
    
    protected static final SensorTargetMediatorFactory instance = 
        new SensorTargetMediatorFactory();
    
    public static MediatorFactory getInstance() { return instance; }
    
    public static void addMediator(Class sensorClass, Class targetClass,
            Class mediatorClass) {
        getInstance().addMediatorFor(sensorClass, targetClass, mediatorClass);
    }
    
    public static Mediator getMediator(Class sensorClass, Class targetClass) {
        return getInstance().getMediatorFor(sensorClass, targetClass);
    }
    
    private WeakHashMap cache;
    
    /** Creates new SensorTargetMediatorFactory */
    protected SensorTargetMediatorFactory() {
        cache = new WeakHashMap();
    }
    
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
    
    public void addMediatorFor(String first, String second, String mediator) throws ClassNotFoundException {
        Class firstClass = Thread.currentThread().getContextClassLoader().loadClass(first);
        Class secondClass = Thread.currentThread().getContextClassLoader().loadClass(second);
        Class mediatorClass = Thread.currentThread().getContextClassLoader().loadClass(mediator);
        addMediatorFor(firstClass, secondClass, mediatorClass);
    }
    
    public Mediator getMeditorFor(Object first, Object second) {
        return getMediatorFor(first.getClass(), second.getClass());
    }
    
    public void clear() {
        cache.clear();
    }
    
}

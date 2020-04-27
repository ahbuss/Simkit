/*
 * SensorTargetMediatorFactory.java
 *
 * Created on March 6, 2002, 6:31 PM
 */
package simkit.smdx;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds SensorTargetMediators. The Mediators can either be constructed then
 * added to the factory or constructed by the factory.
 *
 * @author Arnold Buss
 * @version $Id: SensorTargetMediatorFactory.java 1334 2014-10-06 20:20:05Z
 * ahbuss $
 */
public class SensorTargetMediatorFactory implements
        MediatorFactory<Sensor, Moveable, SensorTargetMediator> {

    /**
     * Holds the instance of SensorTargetMediatorFactory.
     *
     */
    protected static final SensorTargetMediatorFactory instance
            = new SensorTargetMediatorFactory();

    /**
     *
     * @return the instance of SensorTargetMediatorFactory
     */
    public static MediatorFactory<Sensor, Moveable, SensorTargetMediator> getInstance() {
        return instance;
    }

    /**
     * Constructs and adds a Mediator to the MediatorFactory.
     *
     * @param sensorClass The Class of the Sensor for the added Mediator
     * @param targetClass The Class of the target for the added Mediator (Must
     * be a Moveable)
     * @param mediatorClass The Class of the Mediator to add.
     * @throws IllegalArgumentException if the sensorClass is not a Sensor, the
     * targetClass is not a Moveable or the mediatorClass is not a Mediator
     *
     */
    public static void addMediator(Class<? extends Sensor> sensorClass, Class<? extends Moveable> targetClass,
            Class<? extends SensorTargetMediator> mediatorClass) {
        instance.addMediatorFor(sensorClass, targetClass, mediatorClass);
    }

    public static void addMediator(Class<? extends Sensor> sensorClass, Class<? extends Moveable> targetClass,
            SensorTargetMediator mediatorInstance) {
        getInstance().addMediatorFor(sensorClass, targetClass, mediatorInstance);
    }

    /**
     *
     * @param sensorClass the Sensor Class for which to retrieve the Mediator.
     * @param targetClass the Moveable Class for which to retrieve the Mediator.
     * @return the Mediator for the given Sensor Class and Moveable Class.
     * @throws IllegalArgumentException if there is no Mediator set for the
     * given Sensor-target pair.
     */
    public static Mediator getMediator(
            Class<? extends Sensor> sensorClass,
            Class<? extends Mover> targetClass) {

        return getInstance().getMediatorFor(sensorClass, targetClass);
    }

    /**
     * Holds the Mediators that have been added to this factory, key by Sensor
     * and Moveable (target)
     *
     */
    private WeakHashMap<Class<? extends Sensor>, Map<Class<? extends Moveable>, SensorTargetMediator>> cache;

    /**
     * Creates new empty SensorTargetMediatorFactory
     *
     */
    protected SensorTargetMediatorFactory() {
        cache = new WeakHashMap<>();
    }

    /**
     * Constructs and adds a Mediator to this MediatorFactory.
     *
     * @param sensorClass The Class of the Sensor for the added Mediator
     * @param targetClass The Class of the target for the added Mediator (Must
     * be a Moveable
     * @param mediatorClass The Class of the Mediator to add.
     * @throws IllegalArgumentException if the sensorClass is not a Sensor, the
     * targetClass is not a Moveable or the mediatorClass is not a Mediator
     *
     */
//    @Override
    public void addMediatorFor(
            Class<? extends Sensor> sensorClass,
            Class<? extends Moveable> targetClass,
            Class<? extends SensorTargetMediator> mediatorClass) {
        try {
            SensorTargetMediator mediatorInstance = mediatorClass.getConstructor().newInstance();
            addMediatorFor(sensorClass, targetClass, mediatorInstance);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(SensorTargetMediatorFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void addMediatorFor(Sensor sensor, Moveable target,
            SensorTargetMediator mediatorInstance) {
        addMediatorFor(sensor.getClass(), target.getClass(),
                mediatorInstance);
    }

//    @Override
    public <S extends SensorTargetMediator> void addMediatorFor(
            Class<? extends Sensor> sensorClass,
            Class<? extends Moveable> targetClass,
            S mediatorInstance) {
        if (!cache.containsKey(sensorClass)) {
            cache.put(sensorClass, new WeakHashMap<>());
        }
        Map<Class<? extends Moveable>, SensorTargetMediator> targetClasses
                = cache.get(sensorClass);
        targetClasses.put(targetClass, mediatorInstance);
    }

    /**
     * Gets the Mediator for the given Sensor Class and Moveable Class.
     *
     * @param sensorClass the Sensor Class for which to retreve the Mediator.
     * @param targetClass the Moveable Class for which to retrieve the Mediator.
     * @throws IllegalArgumentException if there is no Mediator set for the
     * given Sensor-target pair, sensorClass is not a Sensor, or targetClass is
     * not a Moveable.
     *
     */
    public Mediator getMediatorFor(Class<? extends Sensor> sensorClass,
            Class<? extends Moveable> targetClass) {
        SensorTargetMediator mediator = null;
        if (cache.containsKey(sensorClass)) {
            Map<Class<? extends Moveable>, SensorTargetMediator> targetClasses
                    = cache.get(sensorClass);
            mediator = targetClasses.get(targetClass);
        }
        if (mediator == null) {
            throw new IllegalArgumentException("Mediator not set for ("
                    + sensorClass.getName() + ", " + targetClass.getName() + ")");
        }
        return mediator;
    }

    /**
     * Returns a copy of the HashMap of Mediators that have been added to this
     * MediatorFactory.
     */
    public Map<Class<? extends Sensor>, Map<Class<? extends Moveable>, SensorTargetMediator>> getMediators() {
        Map<Class<? extends Sensor>, Map<Class<? extends Moveable>, SensorTargetMediator>> copy = null;
        synchronized (cache) {
            copy = new WeakHashMap<>(cache);
        }
        for (Class<? extends Sensor> key : copy.keySet()) {
            copy.put(key, new WeakHashMap<>(copy.get(key)));
        }
        return copy;
    }

    /**
     * Constructs and adds a Mediator to the MediatorFactory.
     *
     * @param first The name of the Sensor class for the added Mediator.
     * @param second The name of the Moveable (target) class for the added
     * Meditor.
     * @param mediator The name of the Mediator class to add.
     * @throws ClassNotFoundException if any of the three parameters are not the
     * name of a valid Class
     * @throws IllegalArgumentException if first is not the name of a Sensor,
     * second is not the name of a Moveable or mediator is not the name of a
     * Mediator (but all three are names of valid Classes)
     *
     */
    public void addMediatorFor(String first, String second, String mediator) throws ClassNotFoundException {
        Class<? extends Sensor> firstClass = Class.forName(first).asSubclass(Sensor.class);
        Class<? extends Moveable> secondClass = Class.forName(second).asSubclass(Moveable.class);
        Class<? extends SensorTargetMediator> mediatorClass = Thread.currentThread().getContextClassLoader().loadClass(mediator).asSubclass(SensorTargetMediator.class);
        addMediatorFor(firstClass, secondClass, mediatorClass);
    }

    /**
     * Gets the Mediator for the given Sensor and Moveable (target)
     *
     * @param first The Sensor to get the Mediator for.
     * @param second The Moveable to get the Mediator for.
     * @throws IllegalArgumentException if there is no Mediator set for the
     * given Sensor-target pair or if first is not a Sensor or second is not a
     * Moveable.
     *
     */
    public Mediator getMeditorFor(Sensor first, Moveable second) {
        return getMediatorFor(first.getClass(), second.getClass());
    }

    /**
     * Removes all Mediators from this factory.
     *
     */
    public void clear() {
        cache.clear();
    }

}

/*
 * SensorTargetReferee.java
 *
 * Created on March 6, 2002, 7:17 PM
 */

package simkit.smdx;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import simkit.SimEntity;
import simkit.SimEntityBase;

/**
 * A referee to manage interactions between Sensors and Movers (targets).<P/>
 * Usage:<UL>
 * <LI>First add a {@link SensorTargetMediator} 
 * to the {@link SensorTargetMediatorFactory} for each combination
 * of Sensors and Movers that will be registered. (<CODE>register</CODE> will
 * throw an exception if the mediator does not exist.</LI>
 * <LI>Then register each Sensor and Mover with this referee</LI></UL>
 * Operation:<UL>
 * <LI>Registering a Sensor or Mover sets up this referee to listen for
 * "movementState" property change events from the Sensor/Mover.</LI>
 * <LI>The SensorTargetMediator is set up to listen to SimEvents from
 * this referee.</LI>
 * <LI>When a Sensor or Mover fires a "movementState" property change, this referee
 * calls <CODE>processSensor</CODE> and/or <CODE>processTarget</CODE> as 
 * appropriate.</LI>
 * <LI><CODE>processSensor</CODE> determines when each Mover will cross the
 * boundary of the Sensor's coverage and schedules the ExitRange or EnterRange
 * event, depending on whether the Mover is currently inside the coverage or not.</LI>
 * <LI><CODE>processTarget</CODE> determines when the Mover will cross the
 * boundary of each Sensor's coverage and schedules the ExitRange or EnterRange
 * event, depending on whether the Mover is currently inside the coverage or not.</LI>
 * <LI>The EnterRange event in this referee schedules an ExitRange event at the time
 * when the Mover exits the Sensor's coverage area. (Note: The coverage area is
 * assumed to be convex, therefore ExitRange does not schedule EnterRange.)</LI>
 * <LI>The appropriate SensorTargetMediator is listening for the ExitRange
 * and EnterRange events. It is up to the implementation of those events in the
 * mediators that determine the effects of entering or leaving the 
 * coverage area.</LI></UL>
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class SensorTargetReferee extends SimEntityBase implements PropertyChangeListener {
    
/**
* Holds the Sensors registered with this referee.
**/
    protected Map sensors;

/**
* Holds the targets (Movers) registered with this referee.
**/
    protected Map targets;

/**
* Not currently used.
**/
    protected Map mediators;

/**
* Holds the instance of the SensorTargetMediatorFactory.
**/
    protected MediatorFactory sensorTargetMediatorFactory;
    
/**
* If true, all entities will be unregistered if <CODE>reset<CODE/>
* is called.
* @see #reset()
**/
    private boolean clearOnReset;
    
/** 
* Creates new SensorTargetReferee 
**/
    public SensorTargetReferee() {
        sensors = new WeakHashMap();
        targets = new WeakHashMap();
        mediators = new WeakHashMap();
        setClearOnReset(false);
        sensorTargetMediatorFactory = SensorTargetMediatorFactory.getInstance();
    }
    
/**
* Returns a HashSet containing the Sensors currently registered to this
* referee.
**/
    public Set getSensors() {
        return new HashSet(sensors.keySet());
    }
    
/**
* Returns a HashSet containing the Movers (targets) currently registered to this
* referee.
**/
    public Set getTargets() {
        return new HashSet(targets.keySet());
    }
    
/**
* Registers the given SimEntity with this referee. Prior to adding any
* SimEntities, SensorTargetMediators must be added to the 
* SensorTargetMeditorFactory for each Sensor-Mover pair that will be added
* to the referee.
* @throws NoMediatorDefinedException If there is not a SensorTargetMediator
* added to the SensorTargetMediatorFactory for each combination of Sensor/Mover
* registered with the referee.
**/
    public void register(SimEntity entity) {
        if (entity instanceof simkit.smdx.Sensor) {
            sensors.put(entity, null);
            //            entity.addSimEventListener(this);
            entity.addPropertyChangeListener("movementState", this);
            for (Iterator i = targets.keySet().iterator(); i.hasNext(); ) {
                Object target = i.next();
                Mediator mediator = sensorTargetMediatorFactory.getMediatorFor(
                entity.getClass(), target.getClass());
                if (mediator == null) {
                    throw new NoMediatorDefinedException("No mediator defined for (" +
                    entity.getClass().getName() + "," + target.getClass().getName() +")");
                }
                else {
                    this.addSimEventListener(mediator);
                }
            }
            if (eventList.isRunning()) {
                processSensor((Sensor) entity);
            }
        }
        else if (entity instanceof Mover) {
            targets.put(entity, null);
            //            entity.addSimEventListener(this);
            entity.addPropertyChangeListener("movementState", this);
            for (Iterator i = sensors.keySet().iterator(); i.hasNext(); ) {
                Object sensor = i.next();
                Mediator mediator = sensorTargetMediatorFactory.getMediatorFor(
                sensor.getClass(), entity.getClass() );
                if (mediator == null) {
                    throw new NoMediatorDefinedException("No mediator defined for (" +
                    sensor.getClass().getName() + "," + entity.getClass().getName() +")");
                }
                else {
                    this.addSimEventListener(mediator);
                }
            }
            if (eventList.isRunning()) {
                processTarget((Mover) entity);
            }
        }
        else {
            throw new IllegalArgumentException(entity.getClass().getName() +
            " not a Sensor or a Target");
        }
    }
    
/**
* Removes the given SimEntity from this referee.
**/
    public void unregister(SimEntity entity) {
        if (entity instanceof simkit.smdx.Sensor) {
            sensors.remove(entity);
        }
        else if (entity instanceof Mover) {
            targets.remove(entity);
        }
        entity.removeSimEventListener(this);
        entity.removePropertyChangeListener(this);
    }
    
/**
* Processes property changes for "movementState" of the registered SimEntities.
* Causes the referee to recalculate the detection windows for the entity
* that fired the change.
* @throws RuntimeException If the MovementState of the entity is <CODE>ACCELERATING</CODE>
* since this feature is not yet implemented.
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (!e.getPropertyName().equals("movementState")) { return; }
        if (e.getNewValue() == MovementState.CRUISING || e.getNewValue() == MovementState.STOPPED) {
            if (e.getSource() instanceof Mover) {
                processTarget((Mover) e.getSource());
            }
            else if (e.getSource() instanceof Sensor) {
                processSensor((Sensor) e.getSource());
            }
        }
        else if (e.getNewValue() == MovementState.ACCELERATING) {
            throw new RuntimeException("Acceleration not supported (yet)");
        }
    }
    
/**
* Recalculates the detection windows for the given Mover.
**/
    protected void processTarget(Mover target) {
        Point2D targetLocation = target.getLocation();
        Point2D targetVelocity = target.getVelocity();
        for (Iterator i = sensors.keySet().iterator(); i.hasNext(); ) {
            Sensor sensor = (Sensor) i.next();
            if (target == sensor.getMover()) { continue; }
            Object[] pair = new Object[] { sensor, target };
            double time = findIntersectionTime(sensor, target);
            if (sensor.getFootprint().contains(targetLocation)) {
                interrupt("ExitRange", pair);
                if (time < Double.POSITIVE_INFINITY) {
                    waitDelay("ExitRange", time, pair);
                }
            }
            else {
                interrupt("EnterRange", pair);
                if (time < Double.POSITIVE_INFINITY) {
                    waitDelay("EnterRange", time, pair);
                }
            }
        }
    }
    
/**
* Recalculates the detection windows for the given Sensor.
**/
    protected void processSensor(Sensor sensor) {
        Point2D sensorLocation = sensor.getLocation();
        Point2D sensorVelocity = sensor.getVelocity();
        for (Iterator i = targets.keySet().iterator(); i.hasNext(); ) {
            Mover target = (Mover) i.next();
            if (target == sensor.getMover()) { continue; }
            Object[] pair = new Object[] { sensor, target };
            double time = findIntersectionTime(sensor, target);
            if (sensor.getFootprint().contains(target.getLocation())) {
                interrupt("ExitRange", pair);
                if (time < Double.POSITIVE_INFINITY) {
                    waitDelay("ExitRange", time, pair);
                }
            }
            else {
                interrupt("EnterRange", pair);
                if (time < Double.POSITIVE_INFINITY) {
                    waitDelay("EnterRange", time, pair);
                }
            }
        }
    }
    
/**
* Finds the next time the Mover intersects the detection volume of
* the Sensor.
**/
    public double findIntersectionTime(Sensor sensor, Mover target) {
        /*
        double time = Math2D.smallestPositive(
        Math2D.findIntersectionTime(
        target.getLocation(),
        Math2D.subtract(target.getVelocity(), sensor.getVelocity()),
        sensor.getFootprint()
        )
        );
        return time;
         */
        
        Point2D targetLocation = target.getLocation();
        Point2D relativeVelocity = Math2D.subtract(target.getVelocity(), sensor.getVelocity());
        Shape footPrint = sensor.getFootprint();
        double[] times = Math2D.findIntersectionTime(targetLocation, relativeVelocity, footPrint);
        double time = Math2D.smallestPositive(times);
        if (isVerbose()) {
            Point2D[] intersect = Math2D.findIntersection(targetLocation, relativeVelocity, footPrint);
            for (int i = 0; i < intersect.length; i++) {
                System.out.println(i + ": " + intersect[i] + " -> " + times[i] + " - " +
                    Math2D.add(target.getLocation(), Math2D.scalarMultiply(times[i], target.getVelocity())) + " | " +
                    Math2D.add(sensor.getLocation(), Math2D.scalarMultiply(times[i], sensor.getVelocity())) );
            }
        }

        return time;
    }
    
/**
* Schedules an EnterRange event for time 0 for any Movers that are
* currently inside the detection volume of any Sensors.
**/
    public void doRun() {
        for (Iterator i = getSensors().iterator(); i.hasNext(); ) {
            Sensor sensor = (Sensor) i.next();
            for (Iterator j = getTargets().iterator(); j.hasNext(); ) {
                Mover target = (Mover) j.next();
                if (sensor.getMover() != target && sensor.isInRangeOf(target.getLocation())) {
                    waitDelay("EnterRange", 0.0, new Object[] { sensor, target });
                }
            }
        }
    }
    
/**
* Does nothing.
**/
    public void doStartMove(Mover target) {
    }
    
/**
* Does nothing.
**/
    public void doStartMove(Sensor sensor) {
    }
    
/**
* Does nothing.
**/
    public void doEndMove(Mover target) {
    }
    
/**
* Does nothing.
**/
    public void doEndMove(Sensor sensor) {
    }
    
/**
* Schedules ExitRange for when the Mover leaves the detection volume
* of the Sensor.
**/
    public void doEnterRange(Sensor sensor, Mover target) {
        double exitTime = findIntersectionTime(sensor, target);
        waitDelay("ExitRange", exitTime, new Object[] { sensor, target} );
    }
    
/**
* Does nothing.
**/
    public void doExitRange(Sensor sensor, Mover target) {
    }
    
/**
* Unregisters all Sensors from this referee. 
**/
    public void clearSensors() {
        sensors.clear();
    }
    
/**
* Unregisters all Movers (targets) from this referee.
**/
    public void clearTargets() {
        targets.clear();
    }
    
/**
* Unregisters all Movers (targets) and Sensors from this referee.
**/
    public void clearAll() {
        sensors.clear();
        targets.clear();
    }
    
/**
* If true, all entities will be unregistered if <CODE>reset<CODE/>
* is called.
* @see #reset()
**/
    public void setClearOnReset(boolean b) { clearOnReset = b; }
    
/**
* If true, all entities will be unregistered if <CODE>reset<CODE/>
* is called.
* @see #reset()
**/
    public boolean isClearOnReset() { return clearOnReset; }
    
/**
* Cancels all pending SimEvents and if clearOnReset is true,
* unregisters all Sensors and targets (Movers).
**/
    public void reset() {
        super.reset();
        if (isClearOnReset()) {
            clearAll();
        }
    }
    
/**
* Returns a list of the Sensors and Movers (targets) currently registered
* with this referee.
**/
    public String paramString() {
        return toString();
    }
        
/**
* Returns a list of the Sensors and Movers (targets) currently registered
* with this referee.
**/
    public String toString() {
        StringBuffer buf = new StringBuffer(this.getClass().getName());
        buf.append("\nSensors:");
        for (Iterator i = sensors.keySet().iterator(); i.hasNext(); ) {
            buf.append('\n');
            buf.append(i.next());
        }
        buf.append("\n\nTargets:");
        for (Iterator i = targets.keySet().iterator(); i.hasNext(); ) {
            buf.append('\n');
            buf.append(i.next());
        }
        
        return buf.toString();
    }
}

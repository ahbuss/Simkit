/*
 * SensorTargetReferee.java
 *
 * Created on March 6, 2002, 7:17 PM
 */

package simkit.smdx;
import simkit.*;
import java.util.*;
import java.beans.*;
import java.awt.geom.*;
import java.awt.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class SensorTargetReferee extends SimEntityBase implements PropertyChangeListener {
    
    protected Map sensors;
    protected Map targets;
    protected Map mediators;
    protected MediatorFactory sensorTargetMediatorFactory;
    
    private boolean clearOnReset;
    
    /** Creates new SensorTargetReferee */
    public SensorTargetReferee() {
        sensors = new WeakHashMap();
        targets = new WeakHashMap();
        mediators = new WeakHashMap();
        setClearOnReset(false);
        sensorTargetMediatorFactory = SensorTargetMediatorFactory.getInstance();
    }
    
    public Set getSensors() {
        return new HashSet(sensors.keySet());
    }
    
    public Set getTargets() {
        return new HashSet(targets.keySet());
    }
    
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
            if (Schedule.isRunning()) {
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
            if (Schedule.isRunning()) {
                processTarget((Mover) entity);
            }
        }
        else {
            throw new IllegalArgumentException(entity.getClass().getName() +
            " not a Sensor or a Target");
        }
    }
    
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
    
    public void doStartMove(Mover target) {
    }
    
    public void doStartMove(Sensor sensor) {
    }
    
    public void doEndMove(Mover target) {
    }
    
    public void doEndMove(Sensor sensor) {
    }
    
    public void doEnterRange(Sensor sensor, Mover target) {
        double exitTime = findIntersectionTime(sensor, target);
        waitDelay("ExitRange", exitTime, new Object[] { sensor, target} );
    }
    
    public void doExitRange(Sensor sensor, Mover target) {
    }
    
    public void clearSensors() {
        sensors.clear();
    }
    
    public void clearTargets() {
        targets.clear();
    }
    
    public void clearAll() {
        sensors.clear();
        targets.clear();
    }
    
    public void setClearOnReset(boolean b) { clearOnReset = b; }
    
    public boolean isClearOnReset() { return clearOnReset; }
    
    public void reset() {
        super.reset();
        if (isClearOnReset()) {
            clearAll();
        }
    }
    
    public String paramString() {
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

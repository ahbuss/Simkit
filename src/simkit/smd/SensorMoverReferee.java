package simkit.smd;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import simkit.smd.util.Math2D;
import simkit.Priority;
import simkit.SimEntityBase;

/**
 * Maintains a list of Sensors and Movers that can potentially be
 * detected by the Sensors. Listens for StartMove and Stop events
 * and schedules Enter/ExitRange events as dicated by the
 * solutions to the quadratic equation.
 *
 * @version $Id: SensorMoverReferee.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class SensorMoverReferee extends SimEntityBase {

    public static final Point2D ZERO = new Point2D.Double(0.0, 0.0);
    protected HashSet<Mover> movers;
    protected HashSet<Sensor> sensors;
    private SensorMoverMediator defaultMediator;
    private HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>> mediators;

    public SensorMoverReferee() {
        movers = new HashSet<Mover>();
        sensors = new HashSet<Sensor>();
        mediators = new HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>>();
        setDefaultMediator(new CookieCutterMediator());
    }

    /**
     * Clear movers and sensors.
     */
    @Override
    public void reset() {
        super.reset();
        movers.clear();
        sensors.clear();
    }

    /**
     * Add mover to movers and listen to it. For each sensor, schedule
     * EnterRange(mover, sensor) if mover is in range.
     * @param mover Mover to be added as potential target
     */
    public void doRegisterMover(Mover mover) {
        HashSet<Mover> oldMovers = getMovers();
        movers.add(mover);
        firePropertyChange("movers", oldMovers, getMovers());

        mover.addSimEventListener(this);

        for (Sensor sensor : sensors) {
            if (sensor.getMover() == mover) {
                continue;
            }
            if (Math2D.normSquared(Math2D.subtract(mover.getCurrentLocation(), sensor.getCurrentLocation())) <
                    sensor.getMaxRange() * sensor.getMaxRange()) {
                waitDelay("EnterRange", 0.0, mover, sensor);
            }
        }
    }

    /**
     * Add to sensors list. For each mover already registered, schedule
     * EnterRange(mover, sensor) if mover is in range of sensor.
     * @param sensor The Sensor that can potentially detect targets in
     * the movers list
     */
    public void doRegisterSensor(Sensor sensor) {
        HashSet<Sensor> oldSensors = getSensors();
        sensors.add(sensor);
        firePropertyChange("sensors", oldSensors, getSensors());

        sensor.addSimEventListener(this);

        for (Mover mover : movers) {
            if (sensor.getMover() == mover) {
                continue;
            }
            if (Math2D.normSquared(Math2D.subtract(mover.getCurrentLocation(), sensor.getCurrentLocation())) <
                    sensor.getMaxRange() * sensor.getMaxRange()) {
                waitDelay("EnterRange", 0.0, mover, sensor);
            }
        }
    }

    /**
     * Normally this is only done when a platform is killed.
     * @param mover The mover to be removed as potential target
     */
    public void doUnregisterMover(Mover mover) {
        movers.remove(mover);
        mover.removeSimEventListener(this);
    }

    /**
     * Normally this is only done when a platform is killed.
     * @param sensor The sensor to be removed
     */
    public void doUnregisterSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.removeSimEventListener(this);
    }

    /**
     * Schedule CheckSensor(mover, Iterator&lt;Sensor&gt;).
     * @param mover Heard from a Mover.
     */
    public void doStartMove(Mover mover) {
        Iterator<Sensor> iter = sensors.iterator();
        if (iter.hasNext()) {
            waitDelay("CheckMover", 0.0, Priority.HIGHER, mover, iter);
        }
    }

    /**
     * Schedule CheckMover(sensor, Iterator&lt;Sensor&gt;).
     * @param sensor Heard from a Sensor
     */
    public void doStartMove(Sensor sensor) {
        Iterator<Mover> iter = movers.iterator();

        if (iter.hasNext()) {
            waitDelay("CheckSensor", 0.0, Priority.HIGHER, sensor, iter);
        }
    }

    /**
     * Schedule CheckSensor(mover, Iterator&lt;Sensor&gt;).
     * @param mover Heard from a Mover.
     */
    public void doStop(Mover mover) {
        Iterator<Sensor> iter = sensors.iterator();
        if (iter.hasNext()) {
            waitDelay("CheckMover", 0.0, Priority.HIGHER, mover, iter);
        }
    }

    /**
     * Schedule CheckMover(sensor, Iterator&lt;Sensor&gt;).
     * @param sensor Heard from a Sensor
     */
    public void doStop(Sensor sensor) {
        Iterator<Mover> iter = movers.iterator();

        if (iter.hasNext()) {
            waitDelay("CheckSensor", 0.0, Priority.HIGHER, sensor, iter);
        }
    }

    /**
     * If Mover being pointed to is not the sensor's, compute enter/exit
     * times. If smaller root is positive, cancel previously schedule
     * EnterRange and schedule EnterRange(mover, sensor) with delay of the
     * smaller value. If the smaller root is negative and the larger is
     * positive, cancel pending ExitRange and reschedule
     * ExitRange(mover, sensor) with delay of the larger value.<br>
     * If there is another Mover to check, advance iterator and
     * schedule CheckSensor.
     * @param sensor The sensor to be checked
     * @param iter Iterator&lt;Mover&gt; pointing to next Mover in movers
     */
    public void doCheckSensor(Sensor sensor, Iterator<Mover> iter) {

        Mover mover = iter.next();

        if (mover != sensor.getMover()) {
            interrupt("EnterRange", mover, sensor);
            double[] times = Math2D.findIntersectionTimes(mover, sensor);
            if (times.length == 2 && times[0] > 0.0) {
                waitDelay("EnterRange", times[0], mover, sensor);
            }
            if (times.length == 2 && times[0] <= 0.0 && times[1] >= 0.0) {
                interrupt("ExitRange", mover, sensor);
                waitDelay("ExitRange", times[1], mover, sensor);
            }
        }

        if (iter.hasNext()) {
            waitDelay("CheckSensor", 0.0, Priority.HIGHER, sensor, iter);
        }
    }

    /**
     * If Mover is not the Sensor's that is pointed to, compute enter/exit
     * times. If smaller root is positive, cancel previously schedule
     * EnterRange and schedule EnterRange(mover, sensor) with delay of the
     * smaller value. If the smaller root is negative and the larger is
     * positive, cancel pending ExitRange and reschedule ExitRange(mover, sensor)
     * with delay of the larger value.<br>
     * If there is another Sensor to check, advance iterator and
     * schedule CheckSensor.     *
     * @param mover
     * @param iter
     */
    public void doCheckMover(Mover mover, Iterator<Sensor> iter) {
        Sensor sensor = iter.next();

        if (mover != sensor.getMover()) {
            double[] times = Math2D.findIntersectionTimes(mover, sensor);
            interrupt("EnterRange", mover, sensor);
            if (times.length == 2 && times[0] > 0.0) {
                waitDelay("EnterRange", times[0], mover, sensor);
            }
            if (times.length == 2 && times[0] <= 0.0 && times[1] >= 0.0) {
                interrupt("ExitRange", mover, sensor);
                waitDelay("ExitRange", times[1], mover, sensor);
            }
        }

        if (iter.hasNext()) {
            waitDelay("CheckMover", 0.0, Priority.HIGHER, mover, iter);
        }
    }

    /**
     * The mover has just entered the maximum range of the sensor.
     * Schedule an EnterRange(mover, sensor) imediately on the appropriate
     * Mediator. If no Mediator defined, use the default Mediator.
     * Compute the solutons to the quadratic equation. If 
     * @param mover Mover that entered the range
     * @param sensor Sensor whose range was entered.
     */
    public void doEnterRange(Mover mover, Sensor sensor) {
        HashMap<Class<? extends Mover>, SensorMoverMediator> moverMap =
                mediators.get(sensor.getClass());
        SensorMoverMediator mediator =
                moverMap != null ? mediators.get(sensor.getClass()).get(mover.getClass()) : null;
        mediator = mediator != null ? mediator : getDefaultMediator();

        double[] times = Math2D.findIntersectionTimes(mover, sensor);

        mediator.waitDelay("EnterRange", 0.0, mover, sensor);

        if (times.length == 2 && times[1] > 0.0) {
            waitDelay("ExitRange", times[1], mover, sensor);
        }
    }

    /**
     * Schedule ExitRange(mover, sensor) on appropriate Mediator.
     * @param mover The Movere that exited the range of the sensor
     * @param sensor The Sensor whose range was exited
     */
    public void doExitRange(Mover mover, Sensor sensor) {
        HashMap<Class<? extends Mover>, SensorMoverMediator> moverMap =
                mediators.get(sensor.getClass());
        SensorMoverMediator mediator =
                moverMap != null ? mediators.get(sensor.getClass()).get(mover.getClass()) : null;
        mediator = mediator != null ? mediator : getDefaultMediator();

        mediator.waitDelay("ExitRange", 0.0, mover, sensor);
    }

    /**
     *
     * @param sensorClass The Class object for the Sensor
     * @param moverClass The Class object for the Mover
     * @param mediator The Mediator instance to be used to adjudicate between
     * Sensors of the given calss and Movers of the given class.
     */
    public void addMediator(Class<? extends Sensor> sensorClass,
            Class<? extends Mover> moverClass,
            SensorMoverMediator mediator) {
        HashMap<Class<? extends Mover>, SensorMoverMediator> moverMap =
                mediators.get(sensorClass);
        if (moverMap == null) {
            moverMap = new HashMap<Class<? extends Mover>, SensorMoverMediator>();
            mediators.put(sensorClass, moverMap);
        }
        moverMap.put(moverClass, mediator);
    }

    @Override
    public String toString() {
        String toString = "SensorMoverReferee\nMovers:";
        for (Mover mover : movers) {
            toString += "\n" + mover;
        }
        toString += "\n\nSensors:";
        for (Sensor sensor : sensors) {
            toString += "\n" + sensor;
        }

        return toString;
    }

    /**
     * @return the moversc
     */
    public HashSet<Mover> getMovers() {
        return new HashSet<Mover>(movers);
    }

    /**
     * @return the sensors(shallow copy)
     */
    public HashSet<Sensor> getSensors() {
        return new HashSet<Sensor>(sensors);
    }

    /**
     * @return the mediators
     */
    public HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>> getMediators() {
        return new HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>>(mediators);
    }

    /**
     * @param mediators the mediators to set
     */
    public void setMediators(HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>> mediators) {
        this.mediators = new HashMap<Class<? extends Sensor>, HashMap<Class<? extends Mover>, SensorMoverMediator>>(mediators);
    }

    /**
     * @return the defaultMediator
     */
    public SensorMoverMediator getDefaultMediator() {
        return defaultMediator;
    }

    /**
     * @param defaultMediator the defaultMediator to set
     */
    public void setDefaultMediator(SensorMoverMediator defaultMediator) {
        this.defaultMediator = defaultMediator;
    }
}
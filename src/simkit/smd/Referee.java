package simkit.smd;
/**
 *  <P> The latest version of Referee.  This one has been specifically developed
 *  to overcomes problems uncovered by MAJ Tom Turner, USMC in his thesis.
 *  <P> Several improvements to the design have been implemented, as well as
 *  the non-EnterRange problem being fixed.
 *  <UL>
 *      <LI> The mediator is not fetched from the MediatorFactory until EnterRange
 *           has actually happened.
 *      <LI> The MediatorFactory is now assumed to return the same mediator for each
 *           sensor/target pair, and therefore can be used to remove the Referee
 *           as a SimEventListener at the ExitRange event.  This will help with
 *           memory problems.
 *      <LI> There are fewer attempts to be "smart", which all had been stupid...
 *      <LI> Registries are now <CODE>protected</CODE> for subclasses to enjoy
 *      <LI> Just one StartMove and EndMove event (each) with a test to see if it
 *           is a Mover or a Sensor.
 *      <LI> There is now a reasonable reset() method. 
 *  </UL>
 *
 *  @version 21 Febraury 2001
 *  @author Arnold Buss
**/
import java.util.Iterator;
import java.util.Vector;

import simkit.Schedule;
import simkit.SimEntity;
import simkit.SimEntityBase;

public class Referee extends SimEntityBase {
/**
 *  For those too lazy to instantiate.
**/
    public static Referee DEFAULT_REFEREE ;
    static {
        DEFAULT_REFEREE = new Referee();
    }
    protected Vector sensors;
    protected Vector targets;

    public Referee() {
        sensors = new Vector();
        targets = new Vector();
    }
/**
 *  Event heard from a Mover (target) or Sensor.
 *  @param entity the Mover or Sensor that has started a move. 
**/
    public void doStartMove(SimEntity entity) {
        if (entity instanceof Mover) {
            this.scheduleEntries((Mover) entity);
        }
        else if (entity instanceof Sensor) {
            this.scheduleEntries((Sensor) entity);
        }
    }
/**
 *  Event heard from a Mover (target) or Sensor.
 *  @param entity the Mover or Sensor that has ended a move. 
**/
    public void doEndMove(SimEntity entity) {
        if (entity instanceof Mover) {
            this.scheduleEntries((Mover) entity);
        }
        else if (entity instanceof Sensor) {
            this.scheduleEntries((Sensor) entity);
        }
    }
/**
 *  A Target has entered the range of a sensor.
 *  @param rp a "struct" containing the target, the sensor, and the exit time.
**/
    public void doEnterRange(RangeParameters rp) {
        Mediator mediator = MediatorFactory.getMediator(rp.getTarget(), rp.getSensor());
        this.addSimEventListener(mediator);
        if (rp.getExitRangeTime() >= 0.0) {
            waitDelay("ExitRange", rp.getExitRangeTime(), rp);
        }
    }
/**
 *  A Target has exited the range of a sensor.
 *  @param rp a "struct" containing the target, the sensor, and the exit time.
**/
    public void doExitRange(RangeParameters rp) {
        Mediator mediator = MediatorFactory.getMediator(rp.getTarget(), rp.getSensor());
    }
/**
 *  For each sensor registered, compute the entries.
 *  @param target the Mover that has (presumably) changed its movement state
**/
    public void scheduleEntries(Mover target) {

        if (this.isVerbose()) {
            System.out.println("Interrupting EnterRange(" + target + ")");
        }
        interruptAll("EnterRange", target);
        if (this.isVerbose()) {
            System.out.println("Interrupting ExitRange(" + target + ")");
        }
        interruptAll("ExitRange", target);
        for (Iterator i = sensors.iterator();i.hasNext();) {
            Sensor sensor = (Sensor) i.next();
            if (sensor.getMoverDelegate() == target) { continue; } // skip sensor's mover
            double[] times =  EnterRange.getRangeTimes(target, sensor);
            if (times.length == 2) {
                if (times[1] < 0.0) { continue; }  // moving away from sensor
                if (times[0] < 0.0) {  // inside range already, changing direction
                    waitDelay("EnterRange", 0.0, new RangeParameters(target, sensor, times[1]));
                }
                else {
                    waitDelay("EnterRange", times[0], new RangeParameters(target, sensor, times[1] - times[0]));
                    continue;
                }
            } // if (times.length == 2)
        } // for

   } // scheduleEntries
/**
 *  For each target registered, compute the entries.
 *  @param sensor the Sensor that has (presumably) changed its movement state
**/
    public void scheduleEntries(Sensor sensor) {
        if (this.isVerbose()) {
            System.out.println("Interrupting EnterRange(" + sensor + ")");
        }
        interruptAll("EnterRange", sensor);
        if (this.isVerbose()) {
            System.out.println("Interrupting ExitRange(" + sensor + ")");
        }
        interruptAll("ExitRange", sensor);
        for (Iterator i = targets.iterator();i.hasNext(); ) {
            Mover target = (Mover) i.next();
            if (sensor.getMoverDelegate() == target) { continue; } // skip sensor's mover
            double[] times =  EnterRange.getRangeTimes(target, sensor);
            if (times.length == 2) {
                if (times[1] < 0.0) { continue; } // moving away from target
                if (times[0] < 0.0) { // target inside range
                    waitDelay("EnterRange", 0.0, new RangeParameters(target, sensor, times[1]));
                }
                else {
                    waitDelay("EnterRange", times[0], new RangeParameters(target, sensor, times[1] - times[0]));
                    continue;
                }
            } // if (times.length == 2)
        }
    } // scheduleEntries
/**
 *  Upon registry, the list of targets is checked to avoid duplicate entries.
 *  The target is added to the registry, the Referee is added as a SimEventListener, and
 *  the entries for this target are all scheduled.
 *  @param target the Mover to be registered as a potential target
**/
    public void registerTarget(Mover target) {
        if (!targets.contains(target)) {
            targets.add(target);
            target.addSimEventListener(this);
            if (Schedule.isRunning()) {
                scheduleEntries(target);
            }
        }
    }
/**
 *  Upon registry, the list of sensors is checked to avoid duplicate entries.
 *  The sensor is added to the registry, the Referee is added as a SimEventListener, and
 *  the entries for this sensor are all scheduled.
 *  @param sensor the Sensor to be registered
**/
    public void registerSensor(Sensor sensor) {
        if (!sensors.contains(sensor)) {
            sensors.add(sensor);
            sensor.addSimEventListener(this);
            if (Schedule.isRunning()) {
                scheduleEntries(sensor);
            }
        }
    }
/**
 *  Upon unregistry, the target is removed from the registry and Referee is removed
 *  as a SimEventListener.  All EnterRange and ExitRange events are cancelled.
 *  @param target the Mover to be unregistered as a potential target
**/
    public void unregisterTarget(Mover target) {
        targets.remove(target);
        interruptAll("EnterRange", target);
        interruptAll("ExitRange", target);
        target.removeSimEventListener(this);
    }
/**
 *  Upon unregistry, the sensor is removed from the registry and Referee is removed
 *  as a SimEventListener.  All EnterRange and ExitRange events are cancelled.
 *  @param sensor the Sensor to be unregistered as a potential target
**/
    public void unregisterSensor(Sensor sensor) {
        sensors.remove(sensor);
        interruptAll("EnterRange", sensor);
        interruptAll("ExitRange", sensor);
        sensor.removeSimEventListener(this);
    }
/**
 * @return The sensors listed in a String
**/
    public String listSensors() {
        StringBuffer buf = new StringBuffer("Sensors:\n");
        for (Iterator i = sensors.iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append("\n");
        }
        return buf.toString();
    }
/**
 * @return The targets listed in a String
**/
    public String listTargets() {
        StringBuffer buf = new StringBuffer("Targets:\n");
        for (Iterator i = targets.iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append("\n");
        }
        return buf.toString();
    }
/**
 * @return The sensors and targets listed in a String
**/
    public String paramString() {
        return this.getName() + "\n" + listSensors() + "\n" + listTargets();
    }
/**
 * @return The sensors and targets listed in a String
**/
    public String toString() { return paramString(); }
/**
 *  @return whether the target is in range of the given sensor.
 *  @param s the sensor to be tested.
 *  @param m the target to be tested.
**/
    public static boolean isInRange(Sensor s, Mover m) {
        return m.getCurrentLocation().distanceFrom(s.getCurrentLocation()) <= s.getMaxRange();
    }
}

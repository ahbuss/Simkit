package simkit.smdx;

import java.util.Map;
import java.util.LinkedHashMap;

import simkit.SimEntityBase;

/** Mediator for CookieCutter detection. The Sensor detects the Mover
 * as soon as the Mover enters the coverage volume.
 * @author Arnold Buss
 * @version $Id: CookieCutterMediator.java 1074 2008-06-03 21:50:17Z kastork $
 */
public abstract class CookieCutterMediator extends SimEntityBase implements SensorTargetMediator {
    
/**
* A cache of Contact objects for each Mover processed by this mediator.
**/
    protected Map<Mover, Contact> contacts;
    
    /** Creates new CookieCutterMediator */
    public CookieCutterMediator() {
        contacts = new LinkedHashMap<Mover, Contact>();
    }
    
    /**
     * This hook method must be implemented by subclasses to return a
     * {@code Contact} object for the given sensor-target pairing.  It is called
     * upon hearing an {@code enterRange} event with the same arguments.  The
     * contact returned by this method is the one that will be passed as a 
     * parameter when scheduling the detection.
     * <p>
     * If the subclass implementation returns null, no detection is scheduled.
     * @param sensor
     * @param target
     * @return
     */
    protected abstract Contact getContactForEnterRangeEvent(Sensor sensor, Mover target);
    
    /**
     * Optional hook method that is invoked upon hearing a {@code EnterRange}
     * event.  This method is called prior to any subsequent event scheduling.
     * <p>
     * Default behavior is to do nothing.
     * 
     * @param sensor
     * @param target
     */
    protected void targetIsEnteringSensorRange(Sensor sensor, Mover target){}
    
    /** When this event is heard, schedules a Detection event for the Sensor
     * immediately using a {@code Contact} object supplied by the subclass.
     * <p>
     * Subclasses may do arbitrary processing prior to the scheduling of the
     * detection event by implementing the optional hook method {@code 
     * targetIsEnteringSensorRange()}.
     * <p>
     * The sensor is passed the {@code Contact} returned by the subclass's
     * implementation of {@code getContactForEnterRangeEvent()}.  That object
     * is a doppleganger for the actual target.  Note that the waitDelay()
     * is invoked directly on the sensor.  This makes the sensor the
     * "source" of the SimEvent, and allows listeners to the sensor to be
     * able to hear it.
     * 
     * @see getContactForEnterRangeEvent()
     * @param sensor The Sensor whose range was entered
     * @param target The Mover (target) that entered the sensor's range
     */    
    public final void doEnterRange(Sensor sensor, Mover target) {
        //Be sure this is for us
        if (this == SensorTargetMediatorFactory.getInstance().getMediatorFor(
                sensor.getClass(), target.getClass())) {
        
            targetIsEnteringSensorRange(sensor, target);
            Contact contact = getContactForEnterRangeEvent(sensor, target);
            sensor.waitDelay("Detection", 0.0, new Object[] { contact });
        }
    }
    
    /** When the range is exited schedules the Undetection event for the 
     * Sensor immediately.
     * Like the Detection event, the Undetection event is scheduled directly
     * on the sensor, for the same reason.
     * @param sensor The sensor whose range was exited
     * @param target The target that exited the sensor's range
     */    
    public final void doExitRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getMediator(
                sensor.getClass(), target.getClass())) {
            targetIsExitingSensorRange(sensor, target);
            Object contact = contacts.get(target);
            sensor.waitDelay("Undetection", 0.0, new Object[] { contact });
        }
    }
    
   /**
     * Optional hook method that is invoked upon hearing a {@code ExitRange}
     * event.  This method is called prior to any subsequent event scheduling.
     * <p>
     * Default behavior is to do nothing.
     * 
     * @param sensor
     * @param target
     */
    protected void targetIsExitingSensorRange(Sensor sensor, Mover target){}
    /**
     * Does nothing - implements SensorTargetMediator interface
     */    
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {}

/**
* Clears the list of contacts and calls super.
**/
    public void reset() {
        super.reset();
        contacts.clear();
    }
}

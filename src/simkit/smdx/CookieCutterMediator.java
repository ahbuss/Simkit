package simkit.smdx;

import java.beans.*;
import simkit.*;
import java.util.*;

/** Mediator for CookieCutter detection.
 * @author Arnold Buss
 */
public class CookieCutterMediator extends SimEntityBase implements SensorTargetMediator {
    
    protected Map contacts;
    
    /** Creates new CookieCutterMediator */
    public CookieCutterMediator() {
        contacts = new WeakHashMap();
    }
    
    /** When this event is heard, schedule the detection for the sensor
     * based on the detection algorithm - in this case, immediately.
     * The sensor is passed an instance of a Contact, which is a 
     * doppleganger for the actual target.  Note that the waitDelay()
     * is invoked directly on the sensor.  This makes the sensor the
     * "source" of the SimEvent, and allows listeners to the sensor to be
     * able to hear it.
     * @param sensor The sensor whose range was entered
     * @param target The target that entered the sensor's range
     */    
    public void doEnterRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getInstance().getMediatorFor(
                sensor.getClass(), target.getClass())) {
            Object contact = contacts.get(target);
            if (contact == null) {
                contact = new Contact(target);
                contacts.put(target, contact);
            }
            sensor.waitDelay("Detection", 0.0, new Object[] { contact } );
        }
    }
    
    /** When the range is exited, what happens in general depends on
     * the undetection rule.  In this case, simply schedule an undetection.
     * Like the Detection event, the Undetection event is scheduled directly
     * on the sensor, for the same reason.
     * @param sensor The sensor whose range was exited
     * @param target The target that exited the sensor's range
     */    
    public void doExitRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getMediator(
                sensor.getClass(), target.getClass())) {
            Object contact = contacts.get(target);
            sensor.waitDelay("Undetection", 0.0, new Object[] { contact });
        }
    }
    
    /** Do nothing
     * @param propertyChangeEvent The heard PropertyChangeEvent
     */    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
    
}

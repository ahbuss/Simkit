/*
 * CookieCutterMediator.java
 *
 * Created on March 6, 2002, 8:51 PM
 */

package simkit.smdx;

import java.beans.*;
import simkit.*;
import java.util.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class CookieCutterMediator extends SimEntityBase implements SensorTargetMediator {
    
    protected Map contacts;
    
    /** Creates new CookieCutterMediator */
    public CookieCutterMediator() {
        contacts = new WeakHashMap();
    }
    
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
    
    public void doExitRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getMediator(
                sensor.getClass(), target.getClass())) {
            Object contact = contacts.get(target);
            sensor.waitDelay("Undetection", 0.0, new Object[] { contact });
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
    
}

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
    
    protected static final int[] INIT_COUNT = new int[] { 1 };
    
    protected Map contacts;
    protected Map targetCount;
    protected Map sensorCount;
    
    /** Creates new CookieCutterMediator */
    public CookieCutterMediator() {
        contacts = new WeakHashMap();
        targetCount = new WeakHashMap();
        sensorCount = new WeakHashMap();
    }
    
    public void doEnterRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getInstance().getMediatorFor(
                sensor.getClass(), target.getClass())) {
            Object contact = contacts.get(target);
            if (contact == null) {
                contact = new Contact(target);
                contacts.put(target, sensor);
            }
            sensor.waitDelay("Detection", 0.0, new Object[] { contact } );
        }
    }
    
    public void doExitRange(Sensor sensor, Mover target) {
        if (this == SensorTargetMediatorFactory.getInstance().getMediatorFor(
                sensor.getClass(), target.getClass())) {
            Object contact = contacts.get(target);
            sensor.waitDelay("Undetection", 0.0, new Object[] { contact });
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
    
}

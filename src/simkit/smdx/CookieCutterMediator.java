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
public class CookieCutterMediator extends SimEntityBase implements SensorTargetMediator, PropertyChangeListener {
    
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
        target.addPropertyChangeListener(this);
        sensor.addPropertyChangeListener(this);
        
        int[] count = (int[]) targetCount.get(target);
        if (count == null) {
            targetCount.put(target, INIT_COUNT.clone());
        }
        else {
            count[0]++;
        }
        count = (int[]) sensorCount.get(sensor);
        if (count == null) {
            sensorCount.put(target, INIT_COUNT.clone());
        }
        
        Object contact = contacts.get(target);
        if (contact == null) {
            contact = new SimpleContact(target);
            contacts.put(target, contact);
        }
        else {
            count[0]++;
        }
        sensor.waitDelay("Detection", 0.0, new Object[] { contact });
    }
    
    public void doExitRange(Sensor sensor, Mover target) {
        Object contact = contacts.get(target);
        int count = ((int[]) sensorCount.get(sensor))[0]--;
        if (count == 0) {
            sensor.removePropertyChangeListener(this);
        }
        count = ((int[]) targetCount.get(target))[0]--;
        if (count == 0) {
            target.removePropertyChangeListener(this);
        }
        sensor.waitDelay("UnDetection", 0.0, new Object[] { contact });
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
}

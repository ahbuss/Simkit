/*
 * SensorTargetReferee.java
 *
 * Created on March 6, 2002, 7:17 PM
 */

package simkit.smdx;
import simkit.*;
import java.util.*;
import java.beans.*;
import simkit.smdx.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class SensorTargetReferee extends SimEntityBase implements PropertyChangeListener {

    protected WeakHashMap sensors;
    protected WeakHashMap targets;
    
    private boolean clearOnReset;
    
    /** Creates new SensorTargetReferee */
    public SensorTargetReferee() {
        sensors = new WeakHashMap();
        targets = new WeakHashMap();
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
        }
        else if (entity instanceof Mover) {
            targets.put(entity, null);
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
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
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

/*
 * SensorTargetMediator.java
 *
 * Created on March 6, 2002, 6:29 PM
 */

package simkit.smdx;
import simkit.*;
import java.beans.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface SensorTargetMediator extends SimEntity, PropertyChangeListener {
    
    public void doEnterRange(Sensor sensor, Mover target);
    
    public void doExitRange(Sensor sensor, Mover target);

}


/*
 * Mediator.java
 *
 * Created on February 28, 2002, 11:43 AM
 */

package simkit.smdx;
import simkit.SimEventListener;import simkit.SimEventListener;
import simkit.smd.Mover;
import simkit.smd.Sensor;
import java.beans.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Mediator extends SimEventListener, SimEventSource, PropertyChangeListener {
    
    public void doEnterRange(Mover mover, Sensor sensor);
    
    public void doExitRange(Mover mover, Sensor sensor);
    
    public void doStartMove(Mover mover);
    
    public void doEndMove(Mover mover);
    
    public void doStartMove(Sensor mover);
    
    public void doEndMove(Sensor mover);

}


/*
 * Sensor.java
 *
 * Created on March 6, 2002, 6:26 PM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Sensor extends Moveable {

    public void doDetection(Moveable contact);
    
    public void doUndetection(Moveable contact);
    
}


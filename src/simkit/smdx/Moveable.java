/*
 * Moveable.java
 *
 * Created on February 20, 2002, 12:57 AM
 */

package simkit.smdx;
import simkit.smd.Coordinate;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Moveable {
    
    public Coordinate getCurrentLocation();
    
    public Coordinate getVelocity();
    
    public Coordinate getAcceleration();

}


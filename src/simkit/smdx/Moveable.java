/*
 * Moveable.java
 *
 * Created on February 20, 2002, 12:57 AM
 */

package simkit.smdx;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Moveable {
    
    public Point2D getLocation();
    
    public Point2D getVelocity();
    
    public Point2D getAcceleration();

}


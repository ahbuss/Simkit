/*
 * Moveable.java
 *
 * Created on February 20, 2002, 12:57 AM
 */

package simkit.smdx;
import java.awt.geom.*;
/**
 * An Object that can move in 2 dimensions.
 * @author  Arnold Buss
 * @version $Id$
 */
public interface Moveable {
    
/**
* Gets the current location of this Object.
**/
    public Point2D getLocation();
    
/**
* Gets the current velocity of this Object.
**/
    public Point2D getVelocity();
    
/**
* Gets the current acceleration of this Object.
**/
    public Point2D getAcceleration();

}


/*
 * SimpleTarget.java
 *
 * Created on July 29, 2002, 12:43 PM
 */

package simkit.smdx;
import java.awt.geom.Point2D;
/**
 * A Target that is only subject to being killed, not damaged.
 * @author  Arnold Buss
 * @version $Id$
 */
public class SimpleTarget extends UniformLinearMover implements Target{
    
/**
* True if this SimpleTarget is alive.
**/
    private boolean alive;
    
/** 
* Creates a new instance of SimpleTarget starting at the 
* given location with the given maximum speed.
**/
    public SimpleTarget(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
        alive = true;
    }
    
/** 
* Creates a new instance of SimpleTarget starting at the 
* given location with the given maximum speed and the given name.
**/
    public SimpleTarget(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        setName(name);
    }
    
/**
* Cancels all pending event for this Target, returns this to its
* original location, and makes it alive.
**/
    public void reset() {
        super.reset();
        alive = true;
    }
    
/**
* Fires a propertyChange for "alive"
**/
    public void doRun() {
        firePropertyChange("alive", Boolean.TRUE);
    }
    
/**
* Does nothing.
**/
    public void hit(Damage damage) {
    }
    
/**
* Returns true if this Target is alive.
**/
    public boolean isAlive() { return alive; }
    
/**
* Kills this Target. (Fires a property change event.)
**/
    public void kill() {
        alive = false;
        firePropertyChange("alive", Boolean.TRUE, Boolean.FALSE);
    }
    
/**
* Returns a String containing the name, current location, current
* velocity, and whether alive or not.
**/
    public String toString() {
        return super.toString() + (isAlive() ? " Alive" : " Dead");
    }
    
}

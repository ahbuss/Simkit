/*
 * GenericPlatform.java
 *
 * Created on June 28, 2002, 4:54 PM
 */

package simkit.smdx;

import simkit.SimEntityBase;
import java.awt.geom.Point2D;

/**
 *
 * @author  ahbuss
 */
public class GenericPlatform extends SimEntityBase implements Platform {
    
    protected Mover mover;
    protected boolean alive;
    
    /** Creates a new instance of GenericPlatform */
    public GenericPlatform(Mover m) {
        mover = m;
    }
    
    public void reset() {
        super.reset();
        alive = true;
    }
    
    public Point2D getAcceleration() {
        return mover.getAcceleration();
    }
    
    public Point2D getLocation() {
        return mover.getLocation();
    }
    
    public Point2D getVelocity() {
        return mover.getVelocity();
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void kill() {
        alive = false;
        firePropertyChange("alive", Boolean.FALSE, Boolean.TRUE);
    }
    
}

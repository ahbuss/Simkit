/*
 * Contact.java
 *
 * Created on May 31, 2002, 9:53 AM
 */

package simkit.smdx;
import java.awt.geom.Point2D;
/**
 *  A doppleganger for a mover - to be passed by a SensorTargetMediator in lieu
 *  of the actual target.
 *  @author  Arnold Buss
 */
public class Contact implements Moveable {
    
    private Mover mover;
    
    /** Creates a new instance of Contact */
    public Contact(Mover mover) {
        this.mover = mover;
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
    
}

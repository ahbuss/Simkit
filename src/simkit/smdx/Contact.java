/*
 * Contact.java
 *
 * Created on May 31, 2002, 9:53 AM
 */

package simkit.smdx;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
/**
 *  A doppleganger for a Moveable - to be passed by a SensorTargetMediator in lieu
 *  of the actual target.
 *  @author  Arnold Buss
 */
public class Contact implements Moveable {
    
    protected DecimalFormat form;
    protected Moveable mover;
    
    /** Creates a new instance of Contact */
    public Contact(Moveable mover) {
        this.mover = mover;
        form = new DecimalFormat("0.000;-0.000");
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
    
    public String toString() { 
        Point2D loc = mover.getLocation();        
        return "Contact: [" + form.format(loc.getX()) +
        ", " + form.format(loc.getY()) + "]";
    }
}

/*
 * SimpleTarget.java
 *
 * Created on July 29, 2002, 12:43 PM
 */

package simkit.smdx;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 */
public class SimpleTarget extends UniformLinearMover implements Target{
    
    private boolean alive;
    
    /** Creates a new instance of SimpleTarget */
    public SimpleTarget(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
        alive = true;
    }
    
    public SimpleTarget(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        setName(name);
    }
    
    public void reset() {
        super.reset();
        alive = true;
    }
    
    public void doRun() {
        firePropertyChange("alive", Boolean.TRUE);
    }
    
    public void hit(Damage damage) {
    }
    
    public boolean isAlive() { return alive; }
    
    public void kill() {
        alive = false;
        firePropertyChange("alive", Boolean.TRUE, Boolean.FALSE);
    }
    
    public String toString() {
        return super.toString() + (isAlive() ? " Alive" : " Dead");
    }
    
}

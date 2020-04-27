package simkit.smdx;

/*
 * SimpleContact.java
 *
 * Created on March 19, 2002, 4:14 PM
 */
import java.awt.geom.Point2D;

/**
 * Contains detection information about a Moveable. When a detection
 * is made, a SimpleContact should be passed to the Sensor to avoid
 * passing the Sensor a reference to the actual entity that was detected.
 * A SimpleContact reports ground truth information about the target.
 * @author  Arnold Buss
 * 
 */
public class SimpleContact implements Moveable {

    /** 
     * The Moveable for which this SimpleContact maintains information.
     */    
    protected Moveable target;
    
    /** 
     * Creates a new SimpleContact with information about the given
     * Moveable.
     * @param mover Given actual Moveable instance
     */
    public SimpleContact(Moveable mover) {
        target = mover;
    }
    
    /**
     * Returns the location of the underlying Moveable.
     */    
    public Point2D getLocation() { return target.getLocation(); }    

    /**
     * Returns the acceleration vector of the underlying Moveable.
     */    
    public Point2D getAcceleration() { return target.getAcceleration(); }
    
    /**
     * Returns the velocity vector of the underlying Moveable.
     */    
    public Point2D getVelocity() { return target.getVelocity(); }
    
}

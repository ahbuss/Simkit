package simkit.smdx;

/*
 * SimpleContact.java
 *
 * Created on March 19, 2002, 4:14 PM
 */
import simkit.*;
import simkit.smdx.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 */
public class SimpleContact implements Moveable {

    /** A delegate that actually maintains the state of location,
     * velocity, and acceleration.
     */    
    protected Moveable target;
    
    /** 
     * Creates new SimpleContact
     */
    public SimpleContact(Moveable mover) {
        target = mover;
    }
    
    /**
     * @return Location of the delegate
     */    
    public Point2D getLocation() { return target.getLocation(); }    

    /**
     * @return Acceleration of the delegate
     */    
    public Point2D getAcceleration() { return target.getAcceleration(); }
    
    /**
     * @return Velocity of the delegate
     */    
    public Point2D getVelocity() { return target.getVelocity(); }
    
}

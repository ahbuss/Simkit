/*
 * AcceleratedMover.java
 *
 * Created on March 15, 2002, 11:56 PM
 */

package simkit.smdx;
import simkit.*;
import java.awt.geom.*;

/**
 *
 * @author  ahbuss
 * @version
 */
public class AcceleratedMover extends SimEntityBase implements Mover {
    
    protected static final Point2D ORIGIN = new Point2D.Double(0.0, 0.0);
    
    protected double maxSpeed;
    protected double maxAcceleration;
    protected Point2D originalLocation;
    
    protected boolean cruising;
    protected boolean accelerating;
    protected Point2D initialLocation;
    protected double startMoveTime;
    protected Point2D destination;
    protected Point2D acceleration;
    protected Point2D velocity;
    protected boolean paused;
    
    /** Creates new AcceleratedMover */
    public AcceleratedMover() {
    }
    
    public String paramString() {
        return getName() +  " (" + initialLocation.getX() + ", " + initialLocation.getY() +
        ") " + maxSpeed + " " + maxAcceleration;
    }
    
    public Point2D getLocation() {
        return null;
    }
    
    public void pause() {
    }
    
    public void moveTo(Point2D destination) {
        
    }
    
    public void doStartMove(Moveable mover) {
        if (accelerating) {      // scedule endMove
            
        }
        
        else {
        }
    }
    
    public Point2D getAcceleration() {  return acceleration; }
    
    public void moveTo(Point2D destination, double cruisingSpeed) {
        if (!isMoving()) {  }        
        accelerating = true;
        cruising = false;
        double distance = getLocation().distance(destination);
        
    }
    
    public boolean isMoving() { return !Double.isNaN(startMoveTime); }
    
    public void doEndMove(Moveable mover) {
        Point2D here = getLocation();
        Point2D velocity = getVelocity();
        if (accelerating) {
            accelerating = !accelerating;
            acceleration = new Point2D.Double(0.0, 0.0);
            
        }
    }
    
    public void stop() {
    }
    
    public Point2D getVelocity() {
        if (accelerating) {
            return null;
        }
        else {
            return (Point2D) velocity.clone();
        }
    }
    
    public void reset() {
        initialLocation = (Point2D) originalLocation.clone();
        velocity = (Point2D) ORIGIN.clone();
        destination = null;
        accelerating = false;
        startMoveTime = Double.NaN;
        acceleration = (Point2D) ORIGIN.clone();
        paused = false;
        cruising = false;
    }
    
}
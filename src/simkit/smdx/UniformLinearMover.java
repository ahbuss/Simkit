/*
 * UniformLinearMover.java
 *
 * Created on March 6, 2002, 8:53 PM
 */

package simkit.smdx;
import simkit.*;
import java.awt.geom.*;
import java.text.DecimalFormat;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class UniformLinearMover extends SimEntityBase implements Mover {

    protected static final Point2D ORIGIN = new Point2D.Double();
    protected static final DecimalFormat df = new DecimalFormat("0.000");
    
    protected double maxSpeed;
    protected Point2D originalLocation;
    
    protected Point2D lastStopLocation;
    protected double startMoveTime;
    protected Point2D velocity;
    
    protected Point2D destination;
    protected double moveTime;
    
    protected MovementState movementState;
    
    /** Creates new UniformLinearMover */
    public UniformLinearMover(String name, Point2D location, double maxSpeed) {
        this.setName(name);
        originalLocation = (Point2D) location.clone();
        this.maxSpeed = maxSpeed;
        lastStopLocation = (Point2D) originalLocation.clone();
        velocity = (Point2D) ORIGIN.clone();
        setMovementState(MovementState.STOPPED);
    }

    public Point2D getVelocity() {
        return (Point2D) velocity.clone();
    }
    
    public Point2D getLocation() {
        if (isMoving()) {
            return new Point2D.Double(
                lastStopLocation.getX() + (Schedule.getSimTime() - startMoveTime) * getVelocity().getX(),
                lastStopLocation.getY() + (Schedule.getSimTime() - startMoveTime) * getVelocity().getY()
            );
        }
        else {
            return (Point2D) lastStopLocation.clone();
        }
    }
   
    public void stop() {
        setMovementState(MovementState.STOPPED);
        interrupt("EndMove", new Object[] { this });
        waitDelay("EndMove", 0.0, new Object[] { this });
    }
    
    public void doEndMove(Moveable mover) {
        setMovementState(MovementState.PAUSED);
        lastStopLocation = getLocation();
        velocity.setLocation(ORIGIN);
        startMoveTime = Schedule.getSimTime();
        firePropertyChange("stop", getLocation());
    }
    
    public Point2D getAcceleration() {
        return new Point2D.Double();
    }
    
    public void doStartMove(Moveable mover) {
        firePropertyChange("start", getLocation(), getVelocity());
        if (destination != null) {
            waitDelay("EndMove", moveTime, new Object[] { this });
        }
    }
    
    public void moveTo(Point2D destination) {
        moveTo(destination, maxSpeed);
    }
    
    public String toString() {
        Point2D loc = getLocation();
        return this.getName() + " (" + df.format(loc.getX()) + "," +
            df.format(loc.getX()) +") [" + df.format(this.getVelocity().getX()) +
            "," + df.format(this.getVelocity().getY()) + "]";
    }
    
    public String paramString() {
        return this.getName() + " " + originalLocation + " " + maxSpeed;
    }
    
    public void reset() {
        super.reset();
        setMovementState(MovementState.STOPPED);
        lastStopLocation = (Point2D) originalLocation.clone();
        velocity  = new Point2D.Double();
        startMoveTime = Schedule.getSimTime();
        destination = null;
    }
    
    public boolean isMoving() { return velocity != null || Math.abs(velocity.getX()) > 0.0 || Math.abs(velocity.getY()) > 0.0; }
    
    public void moveTo(Point2D destination, double cruisingSpeed) {
        if (destination == null || cruisingSpeed <= 0.0) { return; }
        if (isMoving()){
            pause();
        }
        setMovementState(MovementState.CRUISING);
        cruisingSpeed = Math.min(cruisingSpeed, maxSpeed);
        this.destination = destination;
        double distance = destination.distance(this.getLocation());
        moveTime = distance / cruisingSpeed;
        velocity.setLocation((destination.getX() - lastStopLocation.getX()) / moveTime,
                        (destination.getY() - lastStopLocation.getY())/moveTime);
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    public void pause() {
        setMovementState(MovementState.PAUSED);
        lastStopLocation = getLocation();
        if (velocity == null) {
            velocity = new Point2D.Double();
        } 
        else {
            velocity.setLocation(ORIGIN);
        }
        startMoveTime = Schedule.getSimTime();
        interrupt("EndMove", new Object[] { this });
    }
    
    public void move(Point2D desiredVelocity) {
        double desiredSpeed = desiredVelocity.distance(ORIGIN);
        if (desiredSpeed <= 0.0) { return; }
        setMovementState(MovementState.CRUISING);
        if ( desiredSpeed > maxSpeed) {
            desiredVelocity = Math2D.scalarMultiply(maxSpeed / desiredSpeed, desiredVelocity);
        }
        destination = null;
        lastStopLocation = getLocation();
        startMoveTime = Schedule.getSimTime();
        velocity = desiredVelocity;       
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    public void magicMove(Point2D location) throws MagicMoveException {
        if (MoverMaster.allowsMagicMove()) {
            this.originalLocation = (Point2D) location.clone();
            reset();
        }
        else {
            throw new MagicMoveException("Attempted illegal magic move: " + this);
        }
    }
    
    protected void setMovementState(MovementState state) {
        MovementState oldState = getMovementState();
        movementState = state;
        firePropertyChange("movementState", oldState, movementState);
    }
    
    public MovementState getMovementState() { return movementState; }
    
    public void accelerate(Point2D acceleration) {
    }
    
    public void accelerate(Point2D acceleration, double speed) {
    }
    
}

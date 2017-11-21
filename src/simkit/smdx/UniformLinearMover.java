package simkit.smdx;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import simkit.Priority;

import simkit.SimEntityBase;

/**
 * Implementation of Mover with uniform linear motion.  Note that the
 * parameters are protected with getters (like state variables) rather
 * than private with setters/getters.  This is because all parameters
 * (like max speed, etc) should not be changed after the object is instantiated.
 * 
 * <p>Modifications made per recommendations of Alistair Dickie.
 * 
 * <p>TODO Re-evaluate the above idea.  The real goal is to ensure that 
 * parameters are not set while the simulation is in progress, not to ensure
 * parameters are not set after the entity is instantiated.  There are two
 * reasons to reconsider:
 * <p>
 * 1. It impossible to construct objects using the Builder Pattern.
 * This pattern typically instantiates objects with a zero argument constructor,
 * and then uses setters to set up the object in whatever particular way that
 * builder builds things.  As we have seen, simkit models get complex enough
 * that the Builder Pattern is useful.
 * 
 * <p> 2.  In a multiple iteration scenario, if
 * we stick with the exisiting paradigm of re-cycling entities and using the
 * reset functionality, it is probable that you would want parameters to be 
 * settable so that a main program
 * trying to execute a design of experiments can change the parameters for a new
 * design point.
 * 
 * @author  Arnold Buss
 * @version $Id$
 */
public class UniformLinearMover extends SimEntityBase implements Mover {
    
    /** The (0,0) point */
    protected static final Point2D ORIGIN = new Point2D.Double();
    /** Default format */
    protected static final DecimalFormat df = new DecimalFormat("0.000;-0.000");
    
    /** The Mover should never travel faster than this speed,
     *  even when asked to do so.
     */
    protected double maxSpeed;
    /** reset() will return Mover to this location */
    protected Point2D originalLocation;
    
/**
* The point where this Mover last stopped. The origin of the 
* current motion.
**/
    protected Point2D lastStopLocation;

/**
* The time that the current movement started. When this Mover
* left the lastStopLocation.
**/
    protected double startMoveTime;

/**
* The current velocity of this Mover.
**/
    protected Point2D velocity;
   
    // the velocity to take upon the next StartMove event.
    protected Point2D nextVelocity;
/**
* The current location that this Mover is moving towards.
**/
    protected Point2D destination;

/**
* The length of time it will take this Mover to move from 
* the lastStopLocation to the destination at the current
* velocity.
**/
    protected double moveTime;
    
/** 
* Possible values are PAUSED, STOPPED, and CRUISING
**/
    protected MovementState movementState;

    /** Convenience array. <!-- End. Of. Sentence-->
     * doStartMove() and doEndMove() have identical
     * signatures, so the same Object array can be used.
     */
    protected final Object[] param;
    
    /**
     * Constructs a new UniformLinearMover.
     * @param name Name of the Mover
     * @param location starting location of the Mover
     * @param maxSpeed maximum possible speed of the Mover
     */
    public UniformLinearMover(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        setName(name);
    }
    
    /**
     * Constructs a new UniformLinearMover.
     * @param location starting location of the Mover
     * @param maxSpeed maximum possible speed of the Mover
     */
    public UniformLinearMover(Point2D location, double maxSpeed) {
        originalLocation = (Point2D) location.clone();
        this.maxSpeed = maxSpeed;
        lastStopLocation = (Point2D) originalLocation.clone();
        velocity = (Point2D) ORIGIN.clone();
        movementState = MovementState.STOPPED;
        param = new Object[] { this };
    }
    
    /**
     * Creates a Mover that can't move.  (Sometimes necessary for stationary
     * sensors.)
     * @param location Location of Mover
     */
    public UniformLinearMover(Point2D location) {
        this(location, 0.0);
    }
    
    /**
     * Creates a Mover that can't move.  (Sometimes necessary for stationary
     * sensors.)
     * @param name Name of the Mover
     * @param location Starting location of the Mover
     */
    public UniformLinearMover(String name, Point2D location) {
        this(name, location, 0.0);
    }
    
    /**
     * The current velocity of this Mover
     * @return current velocity of this Mover
     */
    public Point2D getVelocity() {
        return (Point2D) velocity.clone();
    }
    
    /**
     * If moving, determine actual location by use of the equation of
     * motion.  If stopped, return lastStopLocation, if not null.  Otherwise, 
     * null will be returned.
     * @return Current location of this Mover
     */
    public Point2D getLocation() {
        if (isMoving()) {
            return new Point2D.Double(
            lastStopLocation.getX() + (eventList.getSimTime() - startMoveTime) * getVelocity().getX(),
            lastStopLocation.getY() + (eventList.getSimTime() - startMoveTime) * getVelocity().getY()
            );
        }
        else if (lastStopLocation != null) {
            return (Point2D) lastStopLocation.clone();
        }
        else {
            return null;
        }
    }
// Javadoc inherited    
    public void stop() {
        stopHere();
        setMovementState(MovementState.STOPPED);
    }
// Javadoc inherited    
    public void doEndMove(Moveable mover) {
        if (mover == this) {
            Point2D oldVelocity = velocity;
            stopHere();
            firePropertyChange("velocity", oldVelocity, velocity);
            setMovementState(MovementState.PAUSED);
        }
    }

/**
* Always returns (0,0) for a UniformLinearMover.
**/
    public Point2D getAcceleration() {
        return new Point2D.Double();
    }
    
    /**
     * Event that signals the start of a move to previously set destination
     * @param mover This Mover
     */
    public void doStartMove(Moveable mover) {
        if (mover == this) {
            Point2D oldVelocity = velocity;
            velocity = nextVelocity;
            firePropertyChange("velocity", oldVelocity, velocity);

            if (destination != null) {
                waitDelay("EndMove", moveTime, Priority.HIGH, param);
            }
            setMovementState(MovementState.CRUISING);
        }
    }
    
    /**
     * Command issued to move to given destination at the maximum speed.
     */
    public void moveTo(Point2D destination) {
        moveTo(destination, maxSpeed);
    }
    
    /**
     * Returns a String containing the Name, current location, and current
     * velocity of this Mover, if initialized.
     */
    @Override
    public String toString() {
        Point2D loc = getLocation();
        if (loc != null) {
            return this.getName() + " (" + df.format(loc.getX()) + "," +
                df.format(loc.getY()) +") [" + df.format(this.getVelocity().getX()) +
                "," + df.format(this.getVelocity().getY()) + "]";
        }
        else {
            return getName() + " - Not Initialized";
        }
    }
    
    /**
     * Returns a String containing the Name, original location, and the 
     * maximum speed of this Mover.
     */
    public String paramString() {
        return this.getName() + " " + originalLocation + " " + maxSpeed;
    }
  
    protected Point2D getResetLocation() {
        return (Point2D)originalLocation.clone();
    }
/**
* Cancels all pending SimEvents for this Mover and returns it to its
* original location stopped.
**/
    @Override
    public void reset() {
        super.reset();
        setMovementState(MovementState.STOPPED);
        lastStopLocation = getResetLocation();
//        lastStopLocation = (Point2D) originalLocation.clone();
        velocity  = new Point2D.Double();
        startMoveTime = eventList.getSimTime();
        destination = null;
    }
// Javadoc inherited.
//    Returns <code>false</code> if velocity is <code>null</code>,
//    per Alistair Dickie's suggestion.
    public boolean isMoving() {
        if (velocity != null) {
            return Math.abs(velocity.getX()) > 0.0 || Math.abs(velocity.getY()) > 0.0; 
        }
        else {
            return false;
        }
    }
    
    /**
     * Move to the desired destination at the desired speed.  If the
     * requested speed is larger than maxSpeed for the Mover, it will
     * move to the destination at the maxSpeed.  If the destination
     * is null or the speed is &le; 0.0, it returns without comment or
     * error.
     * @param destination given destination
     * @param cruisingSpeed given cruising speed or maxSpeed, whichever is smaller
     */
    public void moveTo(Point2D destination, double cruisingSpeed) {
        if (destination == null || cruisingSpeed <= 0.0) { return; }
        if (isMoving()){
            pause();
        }
        cruisingSpeed = Math.min(cruisingSpeed, maxSpeed);
        Point2D oldDestination = this.destination;
        this.destination = destination;
        firePropertyChange("destination", oldDestination, 
            new Point2D.Double(destination.getX(), destination.getY()));
        double distance = destination.distance(this.getLocation());
        if ( distance <= 1E-12) {
            // If the distance is very small, set zero movement time, since
            // the distance/speed could return infinity or very large numbers
            // for low or zero cruising speeds
            moveTime = 0.0;
        } else {
            moveTime = distance / cruisingSpeed;
        }
        
        if (moveTime <= 0.0) {
            nextVelocity = new Point2D.Double(0.0, 0.0);
        } else {
            nextVelocity = new Point2D.Double((destination.getX() - lastStopLocation.getX()) / moveTime,
            (destination.getY() - lastStopLocation.getY())/moveTime);
        }
        startMoveTime = eventList.getSimTime();
        interrupt("StartMove", new Object[] {this});
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
/**
* Pauses this Mover at its current location.
**/    
    public void pause() {
        stopHere();
        setMovementState(MovementState.PAUSED);
    }
    
    /**
     * Stops Mover.  Updates its lastStopLocation to wherever it
     * happens to be.
     */
    protected void stopHere() {
        lastStopLocation = getLocation();
        velocity = new Point2D.Double();
        startMoveTime = eventList.getSimTime();
        interrupt("EndMove", param);
    }
    
    /**
     * Move with the desired velocity.  Scales to maximum speed if
     * desiredVelocity has greater magnitude.
     * @param desiredVelocity desired velocity
     */
    public void move(Point2D desiredVelocity) {
        double desiredSpeed = desiredVelocity.distance(ORIGIN);
        if (desiredSpeed <= 0.0) { return; }
        if ( desiredSpeed > maxSpeed) {
            desiredVelocity = Math2D.scalarMultiply(maxSpeed / desiredSpeed, desiredVelocity);
        }
        destination = null;
        lastStopLocation = getLocation();
        startMoveTime = eventList.getSimTime();
//        Point2D oldVelocity = getVelocity();
        nextVelocity = desiredVelocity;
//        firePropertyChange("velocity", oldVelocity, getVelocity());
        interrupt("StartMove", param); 
        waitDelay("StartMove", 0.0, param);
        setMovementState(MovementState.STARTING);
    }
    
    /**
     * Instantly moves this Mover to the given location.
     * @param location The new location of magic mover
     * @throws MagicMoveException if Magic Moves are not allowed. Note:
     * Magic moves are only allowed at the beginning of the simulation.
     */
    public void magicMove(Point2D location) throws MagicMoveException {
        if (MoverMaster.allowsMagicMove()) {
            this.originalLocation = (Point2D) location.clone();
            reset();
        }
        else {
            throw new MagicMoveException("Attempted illegal magic move: " + this);
        }
    }
    
    /**
     * Sets the MovementState and fires a property change.
     * @param state Given MovementState
     */
    protected void setMovementState(MovementState state) {
        MovementState oldState = getMovementState();
        movementState = state;
        firePropertyChange("movementState", oldState, movementState);
    }
    
    public MovementState getMovementState() { return movementState; }
    
    /**
     * Does nothing in this implementation.
     * @param acceleration given acceleration
     */
    public void accelerate(Point2D acceleration) {
    }
    
    /**
     * Does nothing in this implementation
     * @param acceleration given accelleration
     * @param speed given speed
     */
    public void accelerate(Point2D acceleration, double speed) {
    }
    
    /**
     * The speed that this Mover will never exceed.
     * @return maximum possible speed of Mover
     */
    public double getMaxSpeed() { return maxSpeed; }
    
    /**
     * Gives nicer view of a Point2D
     * @param point desired Point2D to format
     * @param form formatting String
     * @return formatted String of Point2D as "[x,y]"
     */
    public static String formatPoint(Point2D point, DecimalFormat form) {
        StringBuilder buf = new StringBuilder('[');
        buf.append(form.format(point.getX()));
        buf.append(',');
        buf.append(' ');
        buf.append(form.format(point.getY()));
        buf.append(']');
        return buf.toString();
    }
    
    /**
     * Formats Point2D with default format
     * @param point Point2D to format
     * @return formatted Point2D
     */
    public static String formatPoint(Point2D point) {
        return formatPoint(point, df);
    }
    
	public static void main(String[] args) {
		Mover thePlatform =
			new UniformLinearMover(
				"Sensor Platform",
				new Point2D.Double(0.0, 0.0),
				10.0);
	}

    public void setMaxSpeed(double max)  throws MagicMoveException{
        if(!MoverMaster.allowsMagicMove()){
            throw new MagicMoveException();
        }

        this.maxSpeed = max;
    }

    public void setLocation(Point2D location) throws MagicMoveException {
        magicMove(location);
    }

}

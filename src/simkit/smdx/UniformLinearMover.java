package simkit.smdx;
import simkit.*;
import java.awt.geom.*;
import java.text.DecimalFormat;

/**
 * Implementation of Mover with uniform linear motion.  Note that the
 * parameters are protected with getters (like state variables) rather
 * than private with setters/getters.  This is because all parameters
 * (like max speed, etc) should not be changed after the object is instantiated.
 * @author  Arnold Buss
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
    
    /** State variable when in motion. */
    protected Point2D lastStopLocation;
    /** State variable when in motion */
    protected double startMoveTime;
    /** State variable when in motion */
    protected Point2D velocity;
    
    /** Useful for certain tasks */
    protected Point2D destination;
    /** convenience value */
    protected double moveTime;
    
    /** Possible values are PAUSED, STOPPED, and CRUISING */
    protected MovementState movementState;
    /** Convenience array. doStartMove() and doEndMove() have identical
     * signatures, so the same Object array can be used.
     */
    protected final Object[] param;
    
    /**
     * @param name Name of the Mover
     * @param location starting location of the Mover
     * @param maxSpeed maximum possible speed of the Mover
     */
    public UniformLinearMover(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        setName(name);
    }
    
    /**
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
     * Create a Mover that can't move.  (Sometimes necessary for stationary
     * sensors.)
     * @param location Location of Mover
     */
    public UniformLinearMover(Point2D location) {
        this(location, 0.0);
    }
    
    /**
     * @param name Name of the Mover
     * @param location Starting location of the Mover
     */
    public UniformLinearMover(String name, Point2D location) {
        this(name, location, 0.0);
    }
    
    /**
     * @return current velocity of the Mover
     */
    public Point2D getVelocity() {
        return (Point2D) velocity.clone();
    }
    
    /**
     * If moving, determine actual location by use of the equation of
     * motion.
     * @return Current location of the Mover
     */
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
        stopHere();
        setMovementState(MovementState.STOPPED);
    }
    
    /**
     * @param mover
     */
    public void doEndMove(Moveable mover) {
        if (mover == this) {
            stopHere();
            setMovementState(MovementState.PAUSED);
        }
    }
    
    /**
     * @return
     */
    public Point2D getAcceleration() {
        return new Point2D.Double();
    }
    
    /**
     * Event that signals the start of a move to previously set destination
     * @param mover
     */
    public void doStartMove(Moveable mover) {
        if (mover == this) {
            if (destination != null) {
                waitDelay("EndMove", moveTime, param, 1.0);
            }
            setMovementState(MovementState.CRUISING);
        }
    }
    
    /**
     * Command issued to move to given destination at the maximum speed.
     * @param destination
     */
    public void moveTo(Point2D destination) {
        moveTo(destination, maxSpeed);
    }
    
    /**
     * @return
     */
    public String toString() {
        Point2D loc = getLocation();
        return this.getName() + " (" + df.format(loc.getX()) + "," +
        df.format(loc.getY()) +") [" + df.format(this.getVelocity().getX()) +
        "," + df.format(this.getVelocity().getY()) + "]";
    }
    
    /**
     * @return
     */
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
    
    /**
     * @return
     */
    public boolean isMoving() {
        return Math.abs(velocity.getX()) > 0.0 || Math.abs(velocity.getY()) > 0.0; 
    }
    
    /**
     * Move to the desired destination at the desired speed.  If the
     * requested speed is larger than maxSpeed for the Mover, it will
     * move to the destination at the maxSpeed.  If the destination
     * is null or the speed is <= 0.0, it returns without comment or
     * error.
     * @param destination
     * @param cruisingSpeed
     */
    public void moveTo(Point2D destination, double cruisingSpeed) {
        if (destination == null || cruisingSpeed <= 0.0) { return; }
        if (isMoving()){
            pause();
        }
        cruisingSpeed = Math.min(cruisingSpeed, maxSpeed);
        this.destination = destination;
        double distance = destination.distance(this.getLocation());
        moveTime = distance / cruisingSpeed;
        velocity.setLocation((destination.getX() - lastStopLocation.getX()) / moveTime,
        (destination.getY() - lastStopLocation.getY())/moveTime);
        startMoveTime = Schedule.getSimTime();
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    /**
     * Pauses Mover
     */
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
        if (velocity == null) {
            velocity = new Point2D.Double();
        }
        else {
            velocity.setLocation(ORIGIN);
        }
        startMoveTime = Schedule.getSimTime();
        
        interrupt("EndMove", param);
    }
    
    /**
     * Move with the desired velocity.  Scales to maximum speed if
     * desireVelocity has greater magnitude.
     * @param desiredVelocity
     */
    public void move(Point2D desiredVelocity) {
        double desiredSpeed = desiredVelocity.distance(ORIGIN);
        if (desiredSpeed <= 0.0) { return; }
        if ( desiredSpeed > maxSpeed) {
            desiredVelocity = Math2D.scalarMultiply(maxSpeed / desiredSpeed, desiredVelocity);
        }
        destination = null;
        lastStopLocation = getLocation();
        startMoveTime = Schedule.getSimTime();
        velocity = desiredVelocity;
        waitDelay("StartMove", 0.0, param);
        setMovementState(MovementState.STARTING);
    }
    
    /**
     * @param location Location of magic mover
     * @throws MagicMoveException if Magic Moves are not enabled.
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
    
    protected void setMovementState(MovementState state) {
        MovementState oldState = getMovementState();
        movementState = state;
        firePropertyChange("movementState", oldState, movementState);
    }
    
    public MovementState getMovementState() { return movementState; }
    
    /**
     * Does nothing in this implemntation.
     * @param acceleration
     */
    public void accelerate(Point2D acceleration) {
    }
    
    /**
     * Does nothing in this implemntation
     * @param acceleration
     * @param speed
     */
    public void accelerate(Point2D acceleration, double speed) {
    }
    
    /**
     * @return maximum possible speed of Mover
     */
    public double getMaxSpeed() { return maxSpeed; }
    
    /**
     * Gives nicer view of a Point2D
     * @param point desired Point2D to format
     * @param form fomatting String
     * @return formatted String of Point2D as "[x,y]"
     */
    public static String formatPoint(Point2D point, DecimalFormat form) {
        StringBuffer buf = new StringBuffer('[');
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
}
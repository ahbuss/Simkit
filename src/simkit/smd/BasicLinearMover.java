package simkit.smd;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import simkit.Priority;
import simkit.Schedule;
import simkit.SimEntityBase;

/**
 * Implements the simplest movement logic. Each maneuver is at a constant
 * velocity at no greater than the maximum possible speed.
 *
 * @author ahbuss
 */
public class BasicLinearMover extends SimEntityBase implements Mover {

    /**
     * For formatting numbers to enhance readability when debugging
     */
    public static final DecimalFormat FORM
            = new DecimalFormat("0.0000;-0.0000");

    /**
     * "Not a Point"
     */
    public static final Point2D NaP = new Point2D.Double(
            Double.NaN, Double.NaN);

//    Parameters
    private Point2D initialLocation;

    private double maxSpeed;

//    State variables
    protected double startMoveTime;

    protected Point2D lastStopLocation;

    protected Point2D velocity;

    protected Point2D destination;

    protected double currentSpeed;

    /**
     * Zero-parameter constructor for Javabeans support and for building from
     * input data by instantiating, then setting parameters.
     */
    public BasicLinearMover() {
        velocity = new Point2D.Double(0.0, 0.0);
    }

    /**
     * Instantiate a BasicLinearMover with the given name, initialLocation, and
     * maxSpeed.BasicLinearMover
     *
     * @param name Name for identification purposes
     * @param initialLocation initial location of Mover
     * @param maxSpeed maximum possible speed
     */
    public BasicLinearMover(String name, Point2D initialLocation, double maxSpeed) {
        this(initialLocation, maxSpeed);
        setName(name);
    }

    /**
     * Instantiate a BasicLinearMover with the given initialLocation maxSpeed.
     * Uses the default name of SimEntityBase.
     *
     * @param initialLocation initial location of Mover
     * @param maxSpeed maximum possible speed
     */
    public BasicLinearMover(Point2D initialLocation, double maxSpeed) {
        this();
        setInitialLocation(initialLocation);
        setMaxSpeed(maxSpeed);
        lastStopLocation = new Point2D.Double(initialLocation.getX(),
                initialLocation.getY());
    }

    /**
     * lastStopLocation set to initialLocation; speed and velocity set to 0,
     * destination set to "NaN" Point2D.
     */
    @Override
    public void reset() {
        super.reset();
        lastStopLocation = getInitialLocation();
        velocity.setLocation(0.0, 0.0);
        currentSpeed = 0.0;
        startMoveTime = getEventList().getSimTime();
        destination = NaP;
    }

    /**
     * Schedule RegisterMover(this) for listeners at start of run.
     */
    public void doRun() {
        firePropertyChange("lastStopLocation", getLastStopLocation());
        firePropertyChange("velocity", getVelocity());

        waitDelay("RegisterMover", 0.0, Priority.HIGH, this);
    }

    /**
     * Start moving at desiredSpeed to destination indicated. Set destination to
     * first parameter and currentSpeed to second. Schedule StartMove(this) with
     * 0.0 delay.
     *
     * @param dest destination
     * @param desiredSpeed speed to move
     */
    public void doMoveTo(Point2D dest, double desiredSpeed) {
        Point2D oldDestination = getDestination();
        destination = new Point2D.Double(dest.getX(), dest.getY());
        firePropertyChange("destination", oldDestination, getDestination());

        double oldCurrentSpeed = getCurrentSpeed();
        currentSpeed = Math.min(maxSpeed, desiredSpeed);
        firePropertyChange("currentSpeed", oldCurrentSpeed, getCurrentSpeed());

        waitDelay("StartMove", 0.0, this);
    }

    /**
     * Start moving at maximum speed to destination. Set destination to
     * parameter. Schedule StartMove(this) with 0.0 delay.
     *
     * @param dest destination
     */
    public void doMoveTo(Point2D dest) {
        Point2D oldDestination = getDestination();
        destination = new Point2D.Double(dest.getX(), dest.getY());
        firePropertyChange("destination", oldDestination, getDestination());

        double oldCurrentSpeed = getCurrentSpeed();
        currentSpeed = maxSpeed;
        firePropertyChange("currentSpeed", oldCurrentSpeed, getCurrentSpeed());

        waitDelay("StartMove", 0.0, this);
    }

    /**
     * Set startMoveTime to simTime, set velocity to match current speed and
     * destination. Compute moveTime and Schedule EndMove(this) with delay of
     * moveTime.
     *
     * @param me Reference to this Mover for listener purposes
     */
    @Override
    public void doStartMove(Mover me) {
        startMoveTime = getEventList().getSimTime();
        firePropertyChange("startMoveTime", getStartMoveTime());

        Point2D oldVelocity = getVelocity();
        double distance = lastStopLocation.distance(destination);
        double moveTime = distance / getCurrentSpeed();
        velocity = moveTime > 0.0 ? new Point2D.Double(
                (destination.getX() - lastStopLocation.getX()) / moveTime,
                (destination.getY() - lastStopLocation.getY()) / moveTime)
                : new Point2D.Double(0.0, 0.0);
        firePropertyChange("velocity", oldVelocity, getVelocity());

        waitDelay("EndMove", moveTime, this);
    }

    /**
     * After this event, the Mover may immediately be ordered to move again. Set
     * velocity and speed to 0. Set lastStopLocation to destination.
     *
     * @param me Reference to this Mover for listener purposes
     */
    public void doEndMove(Mover me) {
        startMoveTime = getEventList().getSimTime();
        firePropertyChange("startMoveTime", getStartMoveTime());

        Point2D oldVelocity = getVelocity();
        velocity.setLocation(0.0, 0.0);
        firePropertyChange("velocity", oldVelocity, getVelocity());

        double oldCurrentSpeed = getCurrentSpeed();
        currentSpeed = 0.0;
        firePropertyChange("currentSpeed", oldCurrentSpeed, getCurrentSpeed());

        Point2D oldLastStopLocation = getLastStopLocation();
        lastStopLocation = destination;
        firePropertyChange("lastStopLocation", oldLastStopLocation,
                getLastStopLocation());
    }

    /**
     * Event scheduled by another component that orders this Mover to actually
     * stop. Schedule Stop(this).
     *
     * @param mover This Mover
     */
    public void doOrderStop(Mover mover) {
        waitDelay("Stop", 0.0, this);
    }

    /**
     * Full stop. If actually moving, interrupt EndMove(this).
     *
     * @param mover This Mover.
     */
    @Override
    public void doStop(Mover mover) {

        boolean moving = velocity.distanceSq(0.0, 0.0) > 0.0;

        Point2D oldLastStopLocation = getLastStopLocation();
        lastStopLocation = getCurrentLocation();
        firePropertyChange("lastStopLocation", oldLastStopLocation,
                getLastStopLocation());

        startMoveTime = getEventList().getSimTime();
        firePropertyChange("startMoveTime", getStartMoveTime());

        Point2D oldVelocity = getVelocity();
        velocity.setLocation(0.0, 0.0);
        firePropertyChange("velocity", oldVelocity, getVelocity());

        double oldCurrentSpeed = getCurrentSpeed();
        currentSpeed = 0.0;
        firePropertyChange("currentSpeed", oldCurrentSpeed, getCurrentSpeed());

        if (moving) {
            interrupt("EndMove", this);
        }
    }

    /**
     * Derived state from initial conditions of equation of motion: x(t) = x0 +
     * (t - t0) v
     *
     * @return Current location
     */
    @Override
    public Point2D getCurrentLocation() {
        if (!isMoving()) {
            return getLastStopLocation();
        } else {
            double timeMoving = Schedule.getSimTime() - getStartMoveTime();
            return new Point2D.Double(
                    lastStopLocation.getX() + timeMoving * velocity.getX(),
                    lastStopLocation.getY() + timeMoving * velocity.getY());
        }
    }

    /**
     * @return the initialLocation
     */
    public Point2D getInitialLocation() {
        return initialLocation;
    }

    /**
     * @param initialLocation the initialLocation to set
     */
    public void setInitialLocation(Point2D initialLocation) {
        this.initialLocation = initialLocation;
    }

    /**
     * @return the maxSpeed
     */
    @Override
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed the maxSpeed to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return the startMoveTime
     */
    public double getStartMoveTime() {
        return startMoveTime;
    }

    /**
     * @return the lastStopLocation or NaP if not initialized
     */
    public Point2D getLastStopLocation() {
        return lastStopLocation != null
                ? new Point2D.Double(lastStopLocation.getX(), lastStopLocation.getY())
                : NaP;
    }

    /**
     * @return the velocity
     */
    @Override
    public Point2D getVelocity() {
        return new Point2D.Double(velocity.getX(), velocity.getY());
    }

    /**
     * @return the destination
     */
    public Point2D getDestination() {
        return new Point2D.Double(destination.getX(), destination.getY());
    }

    public boolean isMoving() {
        return velocity.distanceSq(0.0, 0.0) > 0.0;
    }

    @Override
    public String toString() {
        Point2D currentLocation = getCurrentLocation();
        Point2D vel = getVelocity();
        return getName() + " [" + FORM.format(currentLocation.getX())
                + ", " + FORM.format(currentLocation.getY()) + "] ["
                + FORM.format(vel.getX()) + ", " + FORM.format(vel.getY()) + "]";
    }

    public String paramString() {
        return super.toString();
    }

    /**
     * @return the currentSpeed
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    }

}

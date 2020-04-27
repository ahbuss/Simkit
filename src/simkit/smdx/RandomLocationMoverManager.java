package simkit.smdx;

import java.awt.geom.Point2D;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * Causes the controlled Mover to move to random points at random speeds. The
 * locations and speeds are controlled by the selection of the RandomVariates
 * for the location and speed.
 *
 * @see MoverManagerFactory
 * @author Arnold Buss
 * @version $Id: RandomLocationMoverManager.java 1170 2009-01-22 15:01:34Z
 * kastork $
 */
public class RandomLocationMoverManager extends SimEntityBase implements MoverManager {

    /**
     * The Mover that this MoverManager manages.
*
     */
    private Mover mover;

    /**
     * A 2-dimensional array that contains a RandomVariate for the x coordinate
     * and the y coordinate.
*
     */
    private RandomVariate[] location;

    /**
     * The RandomVariate for the speedGen of movement.
*
     */
    private RandomVariate speed;

    /**
     * If true, then after a reset, this MoverManager will re-start the motion
     * of the controlled Mover.
*
     */
    private boolean startOnReset;

    public RandomLocationMoverManager() {
        super();
    }

    /**
     * Creates a new instance of RandomLocationMoverManager.
     *
     * @param mover The Mover that this MoverManager controls.
     * @param location A 2-dimensional array containing a RandomVariate for the
     * x coordinate and one for the y coordinate.
     * @param speed The RandomVariate used to determine the speedGen of the moves.
     */
    public RandomLocationMoverManager(Mover mover, RandomVariate[] location, RandomVariate speed) {
        this(mover, location);
        setSpeedGenerator(speed);
    }

    /**
     * Creates a new instance of RandomLocationMoverManager that moves the Mover
 at its maximum speedGen.
     *
     * @param mover The Mover that this MoverManager controls.
     * @param location A 2-dimensional array containing a RandomVariate for the
     * x coordinate and one for the y coordinate.
*
     */
    public RandomLocationMoverManager(Mover mover, RandomVariate[] location) {
        if (null != location) {
            setLocationGenerator(location);
        } else {
            this.location = null;
        }
        if (null != mover) {
            setMover(mover);
            RandomVariate speedGen = RandomVariateFactory.getInstance("simkit.random.ConstantVariate",
                    mover.getMaxSpeed());
            setSpeedGenerator(speedGen);
        }
    }

    /**
     * Cancels all pending events for this MoverManager and if startOnReset is
     * true, starts the Mover's motion.
*
     */
    public void reset() {
        super.reset();
        if (isStartOnReset()) {
            waitDelay("Start", 0.0);
        }
    }

    /**
     * Causes the Mover to start moving to a random point.
*
     */

    public void doStart() {
        start();
    }

    /**
     * Notifies this MoverManager that the Mover has completed the current move
     * causing the Mover to move to another random point.
*
     */
    public void doEndMove(Mover m) {
        if (m == mover) {
            mover.moveTo(getLocation(), speed.generate());
        }
    }

    /**
     * The Mover that this MoverManager manages.
*
     */
    public Mover getMover() {
        return mover;
    }

    /**
     * True if the Mover controlled by this MoverManager is moving.
*
     */
    public boolean isMoving() {
        return mover.isMoving();
    }

    /**
     * If true, then after a reset, this MoverManager re-starts the motion of
     * the controlled Mover.
*
     */
    public boolean isStartOnReset() {
        return startOnReset;
    }

    /**
     * If true, then after a reset, this MoverManager re-starts the motion of
     * the controlled Mover.
*
     */
    public void setStartOnReset(boolean b) {
        startOnReset = b;
    }

    /**
     * Starts the controlled Mover moving to a random location.
*
     */
    public void start() {
        mover.moveTo(getLocation(), speed.generate());
    }

    /**
     * Stops the Mover at its current location.
*
     */
    public void stop() {
        mover.stop();
    }

    /**
     * Sets the RandomVariates used to pick the next x and y coordinates. The
     * array must be at least 2-dimensions, any extra elements are ignored.
     *
     * @param rv A 2-dimensional array containing the RandomVariate for the x
     * and y coordinates.
     * @throws NullPointerException If the array or either of its contents is
     * null.
     * @throws IllegalArgumentException If the array does not contain at least 2
     * elements.
     */
    public void setLocationGenerator(RandomVariate[] rv) {
        if (rv == null) {
            throw new NullPointerException("RandomVariate array is null");
        } else if (rv.length < 2) {
            throw new IllegalArgumentException("need array of at least length 2: " + rv.length);
        } else if (rv[0] == null || rv[1] == null) {
            throw new NullPointerException("Null RandomVariate in array");
        }
        location = (RandomVariate[]) rv.clone();
    }

    /**
     * 
     * @return a shallow copy of the location generator array.
     */
    public RandomVariate[] getLocationGenerator() {
        return (RandomVariate[]) location.clone();
    }

    /**
     * Sets the RandomVariate used to pick the speedGen for the next leg.
     * @param rv Given RandomVariate to generate speeds
     */
    public void setSpeedGenerator(RandomVariate rv) {
        speed = rv;
    }

    /**
     * 
     * @return the RandomVariate used to pick the speedGen for the next leg.
     */
    public RandomVariate getSpeedGenerator() {
        return speed;
    }

    /**
     *
     * @return the next random location to move to
     */
    protected Point2D getLocation() {
        return new Point2D.Double(location[0].generate(), location[1].generate());
    }

    /**
     * Sets the Mover that this MoverManager controls. Takes care of ensuring
     * that this MoverManager is a SimEventListener for the Mover.
*
     */
    public void setMover(Mover newMover) {
        if (mover != null) {
            mover.removeSimEventListener(this);
        }
        mover = newMover;
        if (null != newMover) {
            mover.addSimEventListener(this);
        }
    }

    /**
     * Returns a String containing the mover, the next position, and the transit
 speedGen.
*
     */
    public String toString() {
        if (null == mover || null == location || null == speed) {
            return "Unconfigured RandomLocationMoverManager";
        }
        return getMover() + " " + location[0] + " - " + location[1] + " ["
                + speed + "]";
    }

}

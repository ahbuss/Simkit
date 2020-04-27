package simkit.smd;

import java.awt.geom.Point2D;
import simkit.Priority;

/**
 * A Target that is only subject to being killed, not damaged.
 *
 * @author Arnold Buss
 * 
 */
public class SimpleTarget extends BasicLinearMover implements Target {

    /**
     * True if this SimpleTarget is alive.
     *
     */
    protected boolean alive;
    
    public SimpleTarget() { }

    /**
     * Creates a new instance of SimpleTarget starting at the given location
     * with the given maximum speed.
     *
     * @param location given location
     * @param maxSpeed given maximum speed
     */
    public SimpleTarget(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
        this.alive = true;
    }

    /**
     * Creates a new instance of SimpleTarget starting at the given location
     * with the given maximum speed and the given name.
     *
     * @param name given name
     * @param location given location
     * @param maxSpeed given maximum speed
     */
    public SimpleTarget(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        this.setName(name);
    }

    /**
     * Returns this to its original location, and makes it alive.
     */
    @Override
    public void reset() {
        super.reset();
        alive = true;
    }

    /**
     * Fires a propertyChange for "alive"
     */
    @Override
    public void doRun() {
        super.doRun();
        firePropertyChange("alive", isAlive());
    }

    /**
     * Does nothing.
     */
    @Override
    public void doHit(Damage damage) {
        waitDelay("Kill", 0.0, Priority.HIGH);
    }

    /**
     * Returns true if this Target is alive.
     *
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kills this Target. (Fires a property change event.)
     */
    @Override
    public void doKill() {
        boolean oldAlive = isAlive();
        alive = false;
        firePropertyChange("alive", oldAlive, isAlive());
    }

    /**
     * @return a String containing the name, current location, current velocity,
     * and whether alive or not.
     */
    @Override
    public String toString() {
        return super.toString() + (isAlive() ? " Alive" : " Dead");
    }

}

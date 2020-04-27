/*
 * SimpleTarget.java
 *
 * Created on July 29, 2002, 12:43 PM
 */
package simkit.smdx;

import java.awt.geom.Point2D;

/**
 * A Target that is only subject to being killed, not damaged.
 *
 * @author Arnold Buss
 * 
 */
public class SimpleTarget extends UniformLinearMover implements Target {

    /**
     * True if this SimpleTarget is alive.
     */
    private boolean alive;

    /**
     * Creates a new instance of SimpleTarget starting at the given location
     * with the given maximum speed.
     *
     * @param location Given starting location of this SimpleTarget
     * @param maxSpeed Given maximum speed of this SimpleTarget
     */
    public SimpleTarget(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
        alive = true;
    }

    /**
     * Creates a new instance of SimpleTarget starting at the given location
     * with the given maximum speed and the given name.
     * 
     * @param name Given name of this SimpleTarget
     * @param location Given starting location of this SimpleTarget
     * @param maxSpeed Given maximum speed of this SimpleTarget
     */
    public SimpleTarget(String name, Point2D location, double maxSpeed) {
        this(location, maxSpeed);
        setName(name);
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
    public void doRun() {
        firePropertyChange("alive", Boolean.TRUE);
    }

    /**
     * Does nothing.
     */
    @Override
    public void hit(Damage damage) {
    }

    /**
     * Returns true if this Target is alive.
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kills this Target (sets alive to false). (Fires a property change event.)
     */
    @Override
    public void kill() {
        alive = false;
        firePropertyChange("alive", Boolean.TRUE, Boolean.FALSE);
    }

    /**
     * Returns a String containing the name, current location, current velocity,
     * and whether alive or not.
     */
    @Override
    public String toString() {
        return super.toString() + (isAlive() ? " Alive" : " Dead");
    }

}

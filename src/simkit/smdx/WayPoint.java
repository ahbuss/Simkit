package simkit.smdx;

import java.awt.geom.Point2D;

/**
 * A 2-dimensional point with the speed that an entity should transit to the
 * point.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class WayPoint implements Cloneable {

    /**
     * Currently positive infinity.
     */
    public static final double DEFAULT_SPEED = Double.POSITIVE_INFINITY;

    /**
     * The 2-dimensional location of the WayPoint.
     */
    private Point2D point;

    /**
     * The speed at which to transit to the WayPoint.
     */
    private double speed;

    /**
     * Constructs a new WayPoint with the given location and speed.
     *
     * @param point given location
     * @param speed given speed
     * @throws IllegalArgumentException If the speed is negative.
     */
    public WayPoint(Point2D point, double speed) {
        this.point = (Point2D) point.clone();
        setSpeed(speed);
    }

    /**
     * Constructs a new WayPoint with the given location and infinite speed.
     *
     * @param point given location
     */
    public WayPoint(Point2D point) {
        this(point, DEFAULT_SPEED);
    }

    /**
     * Copy Constructor
     *
     * @param wayPoint The WayPoint instance to copy
     */
    public WayPoint(WayPoint wayPoint) {
        this(wayPoint.getPoint(), wayPoint.getSpeed());
    }

    public Point2D getPoint() {
        return this.point;
    }

    /**
     * @return the 2-dimensional point associated with this WayPoint.
     */
    public Point2D getWayPoint() {
        return point;
    }

    /**
     *
     * @param speed the speed at which to transit to the WayPoint.
     * @throws IllegalArgumentException If the speed is negative.
     */
    public void setSpeed(double speed) {
        if (speed >= 0.0) {
            this.speed = speed;
        } else {
            throw new IllegalArgumentException("WayPoint speed must be >= 0.0: " + speed);
        }
    }

    /**
     *
     * @return The speed at which to transit to the WayPoint.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return a String containing the coordinates of this WayPoint and the
     * speed.
     *
     */
    @Override
    public String toString() {
        return "WayPoint (" + point.getX()
                + ", " + point.getY() + ") " + speed;
    }

    /**
     *
     * @return a shallow copy of this WayPoint.
     * @throws java.lang.CloneNotSupportedException if clone not supported
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw (e);
        }
    }

}

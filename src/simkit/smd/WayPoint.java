package simkit.smd;
import java.awt.geom.Point2D;
/**
 * A 2-dimensional point with the speed that an entity should transit to the point.
 *
 * @author  Arnold Buss
 * @version $Id: WayPoint.java 1083 2008-06-11 20:41:21Z kastork $
 */
@SuppressWarnings("javadoc")
public class WayPoint implements Cloneable {
    
/**
* Currently positive infinity.
**/
    public static final double DEFAULT_SPEED = Double.POSITIVE_INFINITY;
    
/**
* The 2-dimensional location of the WayPoint.
**/
    private Point2D point;

/**
* The speed at which to transit to the WayPoint.
**/
    private double speed;
    
/**
* 
**/
    /**
     * Constructs a new WayPoint with the given location and speed.
     * @param point WayPoint destination
     * @param speed speed to this destination
     * @throws IllegalArgumentException If the speed is negative.
     */
    public WayPoint(Point2D point, double speed) {
        this.point = (Point2D) point.clone();
        this.setSpeed(speed);
    }
    
    /**
     * Constructs a new WayPoint with the given location and DEFAULT_SPEED.
     * @param point WayPoint to be copied
     */
    public WayPoint(Point2D point) {
        this(point, DEFAULT_SPEED);
    }

    /**
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param speed speed to this WayPoint
     */
    public WayPoint(double x, double y, double speed) {
        this(new Point2D.Double(x, y), speed);
    }
    
    /**
     * Uses DEFAULT_SPEED
     * @param x X coordinate
     * @param y Y coordinate
     */
    public WayPoint(double x, double y) {
        this(x, y, DEFAULT_SPEED);
    }
    
    /**
     * Copy Constructor
     * @param wayPoint The WayPoint instance to copy
     */
    public WayPoint(WayPoint wayPoint) {
        this(wayPoint.getPoint(), wayPoint.getSpeed());
    }
    
    public Point2D getPoint() {
        return point;
    }

/**
* Returns the 2-dimensional point associated with this WayPoint.
**/
    public Point2D getWayPoint() { return point; }
    
/**
* Sets the speed at which to transit to the WayPoint.
* @throws IllegalArgumentException If the speed is negative.
**/
    public void setSpeed(double speed) { 
        if (speed >= 0.0) {
          this.speed = speed; 
        }
        else {
            throw new IllegalArgumentException("WayPoint speed must be >= 0.0: " + speed);
        }
    }
    
/**
* The speed at which to transit to the WayPoint.
**/
    public double getSpeed() { return speed; }
    
/**
* Returns a String containing the coordinates of this WayPoint and the speed.
**/
    @Override
    public String toString() {
        if (Double.isInfinite(speed)) {
            return String.format("WayPoint(%.3f, %.3f)", point.getX(), point.getY());
        } else {
            return String.format("WayPoint(%.3f, %.3f) %.3f", point.getX(), point.getY(), speed);
        }
   }
    
/**
* Creates and returns a shallow copy of this WayPoint.
**/
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

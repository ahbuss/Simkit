package simkit.smdx;
import java.awt.geom.Point2D;
/**
 * A 2-dimensional point with the speed that an entity should transit to the point.
 *
 * @author  Arnold Buss
 * @version $Id$
 */

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
* Constructs a new WayPoint with the given location and speed.
* @throws IllegalArgumentException If the speed is negative.
**/
    public WayPoint(Point2D point, double speed) {
        this.point = (Point2D) point.clone();
        setSpeed(speed);
    }
    
/**
* Constructs a new WayPoint with the given location and infinite speed.
**/
    public WayPoint(Point2D point) {
        this(point, DEFAULT_SPEED);
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
    public String toString() {
        return "WayPoint (" + point.getX() + ", " + point.getY() + ") " + speed;
    }
    
/**
* Creates and returns a shallow copy of this WayPoint.
**/
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
/**
* Unit level test.
**/
    public static void main(String[] args) {
        WayPoint wp = new WayPoint(new Point2D.Double(20.0, 30.0));
        System.out.println(wp);
        System.out.println(wp.clone());
        wp.setSpeed(10.0);
        System.out.println(wp);
        
    }
    
}

package simkit.smdx;
import java.awt.geom.Point2D;
/**
 *
 * @author  Arnold Buss
 */

public class WayPoint implements Cloneable {
    
    private static final double DEFAULT_SPEED = Double.POSITIVE_INFINITY;
    
    private Point2D point;
    private double speed;
    
    public WayPoint(Point2D point, double speed) {
        this.point = (Point2D) point.clone();
        setSpeed(speed);
    }
    
    public WayPoint(Point2D point) {
        this(point, DEFAULT_SPEED);
    }
    
    public Point2D getWayPoint() { return point; }
    
    public void setSpeed(double speed) { 
        if (speed >= 0.0) {
          this.speed = speed; 
        }
        else {
            throw new IllegalArgumentException("WayPoint speed must be >= 0.0: " + speed);
        }
    }
    
    public double getSpeed() { return speed; }
    
    public String toString() {
        return "WayPoint (" + point.getX() + ", " + point.getY() + ") " + speed;
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public static void main(String[] args) {
        WayPoint wp = new WayPoint(new Point2D.Double(20.0, 30.0));
        System.out.println(wp);
        System.out.println(wp.clone());
        wp.setSpeed(10.0);
        System.out.println(wp);
        
    }
    
}

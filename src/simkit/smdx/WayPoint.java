package simkit.smdx;
import java.awt.geom.Point2D;
/**
 *
 * @author  Arnold Buss
 */

public class WayPoint {
    private Point2D point;
    private double speed;
    
    public WayPoint(Point2D point, double speed) {
        this.point = point;
        this.speed = speed;
    }
    
    public Point2D getWayPoint() { return point; }
    
    public double getSpeed() { return speed; }
    
}

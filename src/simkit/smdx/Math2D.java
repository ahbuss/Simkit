/*
 * Math2D.java
 *
 * Created on March 16, 2002, 12:23 AM
 */

package simkit.smdx;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author  ahbuss
 * @version 
 */
public class Math2D {

    public Point2D add(Point2D first, Point2D second) {
        return new Point2D.Double(
            first.getX() + second.getX(), first.getY() + second.getY()  );
    }
    
    public Point2D subtract(Point2D first, Point2D second) {
        return new Point2D.Double(
            first.getX() - second.getX(), first.getY() - second.getY()  );
    }
    
    public Point2D scalarMultiply(double value, Point2D point) {
        return new Point2D.Double( value * point.getX(), value * point.getY());
    }
    
    public double innerProduct(Point2D first, Point2D second) {
        return first.getX() * second.getX() + first.getY() * second.getY();
    }
    
}

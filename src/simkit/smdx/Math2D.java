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

    public static Point2D add(Point2D first, Point2D second) {
        return new Point2D.Double(
            first.getX() + second.getX(), first.getY() + second.getY()  );
    }
    
    public static Point2D subtract(Point2D first, Point2D second) {
        return new Point2D.Double(
            first.getX() - second.getX(), first.getY() - second.getY()  );
    }
    
    public static Point2D scalarMultiply(double value, Point2D point) {
        return new Point2D.Double( value * point.getX(), value * point.getY());
    }
    
    public static double innerProduct(Point2D first, Point2D second) {
        return first.getX() * second.getX() + first.getY() * second.getY();
    }
    
    public static Point2D findIntersection(Point2D start, Point2D velocity, Line2D line) {
        AffineTransform trans = getTransform(velocity, subtract(line.getP1(), line.getP2()));
        if (trans.getDeterminant() != 0.0) {
            try {
                Point2D result = trans.inverseTransform(subtract(line.getP1(), start), null);
                if (result.getX() >= 0.0 && result.getY() >= 0.0 && result.getY() <= 1.0) {
                    return add( start, scalarMultiply(result.getX(), velocity));
                }
            } catch (NoninvertibleTransformException e) {}
        }
        return null;
    }
    
    public static AffineTransform getTransform(Point2D p1, Point2D p2) {
        return new AffineTransform(new double[] { p1.getX(), p1.getY(), p2.getX(), p2.getY()});
    }
    
}

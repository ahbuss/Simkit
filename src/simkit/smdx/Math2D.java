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
    
    public static final Point2D ZERO = new Point2D.Double(0.0, 0.0);
    
    public static Point2D add(Point2D first, Point2D second) {
        return new Point2D.Double(
        first.getX() + second.getX(), first.getY() + second.getY()  );
    }
    
    public static Point2D add(Point2D[] points) {
        switch (points.length) {
            case 0:
                return (Point2D) ZERO.clone();
            case 1:
                return (Point2D) points[0].clone();
            case 2:
                return add(points[0], points[1]);
            default:
                Point2D sum = add(points[0], points[1]);
                for (int i = 2; i < points.length; i++) {
                    sum = add(sum, points[i]);
                }
                return sum;
        }
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
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, QuadCurve2D curve) {
        
        Point2D v = new Point2D.Double(velocity.getY(), -velocity.getX());
        double d = innerProduct(v, start);
        double a = innerProduct(v, curve.getP1());
        double b = innerProduct(v, curve.getCtrlPt());
        double c = innerProduct(v, curve.getP2());
        
        double[] coeff = new double[] {a - d, 2.0 * (b - a), a - 2.0 * b + c};
        int numberSol = QuadCurve2D.solveQuadratic(coeff);
        
        switch(numberSol) {
            case 0:
                return new Point2D[0];
            case 2:
                if (coeff[1] >= 0.0 && coeff[1] <= 1.0) {
                    if (coeff[0] >= 0.0 && coeff[0] <= 1.0) {
                        Point2D[] intersect = new Point2D[] {
                            getPoint(curve, coeff[0]),
                            getPoint(curve, coeff[1])
                        };
                    }
                    else {
                        coeff[0] = coeff[1];  // fall through
                    }
                }  // fall through
            case 1:
                if (coeff[0] >= 0.0 && coeff[0] <= 1.0) {
                    return new Point2D[] { getPoint(curve, coeff[0]) };
                }
                else {
                    return new Point2D[0];
                }
            default:
                return null;
        }
    }
    
    public static Point2D getPoint(QuadCurve2D curve, double lambda) {
        Point2D[] pts = new Point2D[3];
        pts[0] = scalarMultiply(Math.pow((1.0 - lambda), 2), curve.getP1());
        pts[1] = scalarMultiply(2.0 * lambda * (1.0 - lambda), curve.getCtrlPt());
        pts[2] = scalarMultiply(Math.pow(lambda, 2), curve.getP2());
        return add(pts);
    }
    
}

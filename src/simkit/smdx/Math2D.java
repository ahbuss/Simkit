/*
 * Math2D.java
 *
 * Created on March 16, 2002, 12:23 AM
 */

package simkit.smdx;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author  ahbuss
 * @version
 */
public class Math2D {
    
    public static final Point2D ZERO = new Point2D.Double(0.0, 0.0);
    
    public static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
    
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
    
    public static double[] innerProduct(Point2D[] points, Point2D by) {
        double[] ip = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            ip[i] = innerProduct(points[i], by);
        }
        return ip;
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
    
    public static Point2D orthogonal(Point2D point) {
        return new Point2D.Double(-point.getY(), point.getX());
    }
    
    public static double norm(Point2D point) { return Math.sqrt(normSq(point)); }
    
    public static double normSq(Point2D point) { return innerProduct(point, point); }
    
    public static double normSquared(Point2D point) { return point.distanceSq(ZERO); }
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, Line2D line) {
        AffineTransform trans = getTransform(velocity, subtract(line.getP1(), line.getP2()));
        if (trans.getDeterminant() != 0.0) {
            try {
                Point2D result = trans.inverseTransform(subtract(line.getP1(), start), null);
                if (result.getX() >= 0.0 && result.getY() >= 0.0 && result.getY() <= 1.0) {
                    return new Point2D[] { add( start, scalarMultiply(result.getX(), velocity)) };
                }
            } catch (NoninvertibleTransformException e) {}
        }
        return new Point2D[0];
    }
    
    public static double[] findIntersectionTime(Point2D start, Point2D velocity, Shape shape) {
        Point2D[] intersect = findIntersection(start, velocity, shape, IDENTITY_TRANSFORM);
        double[] times = new double[intersect.length];
        for (int i = 0; i < times.length; i++) {
            times[i] = innerProduct(subtract(intersect[i], start), velocity) / normSq(velocity);
        }
        return times;
    }
    
    public static AffineTransform getTransform(Point2D p1, Point2D p2) {
        return new AffineTransform(new double[] { p1.getX(), p1.getY(), p2.getX(), p2.getY()});
    }
    
    public static Point2D getCenter(RectangularShape shape) {
        return new Point2D.Double(shape.getX() + 0.5 * shape.getWidth(), shape.getY() + 0.5 * shape.getHeight());
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
                        return intersect;
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
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, Shape shape) {
        return findIntersection(start, velocity, shape, IDENTITY_TRANSFORM);
    }
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, Shape shape, AffineTransform trans) {
        ArrayList intersections = new ArrayList();
        Shape[] segment = getSegments(shape, trans);
        for (int i = 0; i < segment.length; i++) {
            if (segment[i] instanceof Line2D) {
                Point2D[] intersect = findIntersection(start, velocity, (Line2D) segment[i]);
                if (intersect != null && intersect.length == 1) {
                    intersections.add(intersect[0]);
                }
            }
            else if (segment[i] instanceof QuadCurve2D) {
                Point2D[] intersect = findIntersection(start, velocity, (QuadCurve2D) segment[i]);
                for (int j = 0; j < intersect.length; j++) {
                    intersections.add(intersect[i]);
                }
            }
            else if (segment[i] instanceof CubicCurve2D) {
                Point2D[] intersect = findIntersection(start, velocity, (CubicCurve2D) segment[i]);
                for (int j = 0; j < intersect.length; j++) {
                    intersections.add(intersect[j]);
                }
            }
        }
        return (Point2D[]) intersections.toArray(new Point2D[intersections.size()]);
    }
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, Ellipse2D ellipse) {
        Point2D adjustedStart = subtract(start, getCenter(ellipse));
        adjustedStart.setLocation(0.5 * ellipse.getHeight() * adjustedStart.getX(),
        0.5 * ellipse.getWidth() * adjustedStart.getY());
        Point2D adjustedVelocity = new Point2D.Double();
        adjustedVelocity.setLocation(0.5 * ellipse.getHeight() * velocity.getX(),
        0.5 * ellipse.getWidth() * velocity.getY());
        double[] coeff = new double[3];
        coeff[0] = normSq(adjustedStart) - Math.pow(0.25 * ellipse.getWidth() * ellipse.getHeight(), 2);
        coeff[1] = 2.0 * innerProduct(adjustedStart, adjustedVelocity);
        coeff[2] = normSq(adjustedVelocity);
        double[] sol = new double[2];
        int numberSolutions = QuadCurve2D.solveQuadratic(coeff, sol);
        Arrays.sort(sol);
        int numberPositiveSolutions = 0;
        for (int i = 0; i < numberSolutions; i++) {
            if (sol[i] >= 0.0) { numberPositiveSolutions++; }
        }
        double[] positiveSol = new double[numberPositiveSolutions];
        System.arraycopy(sol, sol.length - numberPositiveSolutions, positiveSol, 0, numberPositiveSolutions);
        Point2D[] intersection = new Point2D[numberPositiveSolutions];
        
        switch (numberPositiveSolutions) {
            case 2:
                intersection[1] = add(start, scalarMultiply(positiveSol[1], velocity));
            case 1:
                intersection[0] = add(start, scalarMultiply(positiveSol[0], velocity));
                break;
            case 0:
            default:
        }
        return intersection;
    }
    
    public static Point2D[] findIntersection(Point2D start, Point2D velocity, CubicCurve2D curve) {
        Point2D orthoVel = orthogonal(velocity);
        double[] coeff = new double[4];
        Point2D[] pts = new Point2D[4];
        pts[0] = subtract(curve.getP1(), start);
        pts[1] = subtract(curve.getCtrlP1(), curve.getP1());
        pts[1] = scalarMultiply(3.0, pts[1]);
        pts[2] = scalarMultiply(-2.0, curve.getCtrlP1());
        pts[2] = add(curve.getP1(), pts[2]);
        pts[2] = add(curve.getCtrlP2(), pts[2]);
        pts[2] = scalarMultiply(3.0, pts[2]);
        pts[3] = subtract(curve.getCtrlP1(), curve.getCtrlP2());
        pts[3] = scalarMultiply(3.0, pts[3]);
        pts[3] = subtract(pts[3], curve.getP1());
        pts[3] = add(curve.getP2(), pts[3]);

        for (int i = 0; i < coeff.length; i++) {
            coeff[i] = innerProduct(orthoVel, pts[i]);
        }
        double[] sol = new double[3];
        int numberSolutions = CubicCurve2D.solveCubic(coeff, sol);
        switch(numberSolutions) {
            case 1:
            case 3:
                ArrayList list = new ArrayList();
                for (int i = 0; i < sol.length; i++) {
                    if (Math.abs(sol[i] - 0.5) < 0.5) {
                        list.add(getPoint(curve, sol[i]));
                    }                    
                }
                return (Point2D[]) list.toArray(new Point2D[list.size()]);
            case -1:
            default:
        }
        return new Point2D[0];
    }
    
    public static Point2D getPoint(QuadCurve2D curve, double lambda) {
        Point2D[] pts = new Point2D[3];
        pts[0] = scalarMultiply(Math.pow((1.0 - lambda), 2), curve.getP1());
        pts[1] = scalarMultiply(2.0 * lambda * (1.0 - lambda), curve.getCtrlPt());
        pts[2] = scalarMultiply(Math.pow(lambda, 2), curve.getP2());
        return add(pts);
    }
    
    public static Point2D getPoint(CubicCurve2D curve, double lambda) {
        Point2D[] pts = new Point2D[4];
        pts[0] = scalarMultiply(Math.pow(1.0 - lambda, 3), curve.getP1());
        pts[1] = scalarMultiply(3.0 * lambda * Math.pow(1.0 - lambda, 2), curve.getCtrlP1());
        pts[2] = scalarMultiply(3.0 * (1.0 - lambda) * Math.pow(lambda, 2), curve.getCtrlP2());
        pts[3] = scalarMultiply(Math.pow(lambda, 3), curve.getP2());
        return add(pts);
    }
    
    public static Shape[] getSegments(Shape shape, AffineTransform transform) {
        double[] coords = new double[6];
        ArrayList segments = new ArrayList();
        Point2D firstPoint = null;
        Point2D lastPoint = null;
        for (PathIterator path = shape.getPathIterator(transform); !path.isDone(); path.next()) {
            switch(path.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    firstPoint = new Point2D.Double(coords[0], coords[1]);
                    lastPoint = (Point2D) firstPoint.clone();
                    break;
                    
                case PathIterator.SEG_QUADTO:
                    segments.add(new QuadCurve2D.Double(lastPoint.getX(), lastPoint.getY(),
                    coords[0], coords[1], coords[2], coords[3]));
                    lastPoint.setLocation(coords[2], coords[3]);
                    break;
                    
                case PathIterator.SEG_CUBICTO:
                    segments.add(new CubicCurve2D.Double(lastPoint.getX(), lastPoint.getY(),
                    coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]));
                    lastPoint.setLocation(coords[4], coords[5]);
                    break;
                    
                case PathIterator.SEG_LINETO:
                    segments.add(new Line2D.Double(lastPoint.getX(), lastPoint.getY(), coords[0], coords[1]));
                    lastPoint.setLocation(coords[1], coords[1]);                    
                    break;
                    
                case PathIterator.SEG_CLOSE:
                    if (norm(subtract(lastPoint, firstPoint)) > 0.0 ) {
                        segments.add(new Line2D.Double(lastPoint, firstPoint));
                    }
                    break;
                    
                default:
                    break;
            }
        }
        return (Shape[]) segments.toArray(new Shape[segments.size()]);
    }
    
    public static String arrayToString(Object[] array) {
        StringBuffer buf = new StringBuffer();
        buf.append('{');
        for (int i = 0; i < array.length; i++) {
            buf.append(array[i]);
            if (i < array.length - 1) {
                buf.append(',');
                buf.append(' ');
            }
        }
        buf.append('}');
        return buf.toString();
    }
    
    public static String arrayToString(double[] array) {
        StringBuffer buf = new StringBuffer();
        buf.append('{');
        for (int i = 0; i < array.length; i++) {
            buf.append(array[i]);
            if (i < array.length - 1) {
                buf.append(',');
                buf.append(' ');
            }
        }
        buf.append('}');
        return buf.toString();
    }
    
    public static double smallestPositive(double[] data, int num) {
        double smallest = Double.POSITIVE_INFINITY;
        if (num > data.length || num < 0) { return smallest; }
        for (int i = 0; i < num; i++) {
            if (data[i] >= 0.0 && data[i] < smallest){
                smallest = data[i];
            }
        }
        return smallest;
    }
    
    public static double smallestPositive (double[] data) {
        return smallestPositive(data, data.length);
    }
    
    public Point2D[] getCoefficients(Line2D line) {
        Point2D[] coeff = new Point2D[2];
        coeff[0] = line.getP1();
        coeff[1] = subtract(line.getP2(), line.getP1());
        return coeff;
    }
    
    public Point2D[] getCoefficients(QuadCurve2D quadCurve) {
        Point2D[] coeff = new Point2D[3];
        coeff[0] = quadCurve.getP1();
        coeff[1] = scalarMultiply(2.0, subtract(quadCurve.getCtrlPt(), quadCurve.getP1()));
        coeff[2] = add(quadCurve.getP2(), subtract(quadCurve.getP1(), scalarMultiply(2.0, quadCurve.getCtrlPt())));
        return coeff;
    }
    
    public Point2D[] getCoefficients(CubicCurve2D cubicCurve) {
        Point2D[] coeff = new Point2D[4];
        coeff[0] = cubicCurve.getP1();
        coeff[1] = scalarMultiply(3.0, add(cubicCurve.getP1(), cubicCurve.getCtrlP1()));
        coeff[2] = scalarMultiply(-3.0, add(cubicCurve.getP1(), add(scalarMultiply(2.0, cubicCurve.getCtrlP1()), cubicCurve.getCtrlP2())));
        coeff[3] = add(add(cubicCurve.getP1(), scalarMultiply(3.0, subtract(cubicCurve.getCtrlP1(), cubicCurve.getCtrlP2()))), cubicCurve.getP2());
        return coeff;
    }
    
}
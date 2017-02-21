/*
 * Math2D.java
 *
 * Created on March 16, 2002, 12:23 AM
 */
package simkit.smdx;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains a library of useful calculations in 2-dimensional space.
 * @author  ahbuss
 * @version $Id$
 */
public class Math2D {

    /**
     * The origin (0.0, 0.0).
     **/
    public static final Point2D ZERO = new Point2D.Double(0.0, 0.0);
    /**
     * A very small number, currently 1.0E-10.
     **/
    public static final double TINY = 1.0E-10;
    /**
     * Holds an instance of an Identity AffineTransform.
     **/
    public static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    public static Point2D getZero() {
        return (Point2D) ZERO.clone();
    }

    /**
     * Performs a vector addition of the two points.
     *
     * @param first First Point2D
     * @param second Second Point2D
     * @return Point2D containing the coordinate-wise sum of the given Point2Ds
     */
    public static Point2D add(Point2D first, Point2D second) {
        return new Point2D.Double(
                first.getX() + second.getX(), first.getY() + second.getY());
    }

    /**
     * Computes the vector total of all of the points in the given array.
     * 
     * @param points Given points to add
     * @return Point2D containing the coordinate-wise sum of the given Point2Ds
     */
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

    /**
     * Computes the inner product of each of the points in the array with the 
     * given point.
     * @return An array containing the inner product of each member of 
     * <B><CODE>points</CODE></B> with 
     * <B><CODE>by</CODE></B>.
     **/
    public static double[] innerProduct(Point2D[] points, Point2D by) {
        double[] ip = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            ip[i] = innerProduct(points[i], by);
        }
        return ip;
    }

    /**
     * Conputes the vector difference between the two points.
     **/
    public static Point2D subtract(Point2D first, Point2D second) {
        return new Point2D.Double(
                first.getX() - second.getX(), first.getY() - second.getY());
    }

    /**
     * Computes the scalar multiplication of the point by the given value.
     **/
    public static Point2D scalarMultiply(double value, Point2D point) {
        return new Point2D.Double(value * point.getX(), value * point.getY());
    }

    /**
     * Computes a unit vector in the direction of the argument.
     */
    public static Point2D unitVector(Point2D vec){
        double scale = 1.0 / norm(vec);
        return scalarMultiply(scale, vec);
    }

    /**
     * Computes the inner product of the two points.
     **/
    public static double innerProduct(Point2D first, Point2D second) {
        return first.getX() * second.getX() + first.getY() * second.getY();
    }

    /**
     * Returns a vector that is orthogonal to the given vector.
     **/
    public static Point2D orthogonal(Point2D point) {
        return new Point2D.Double(-point.getY(), point.getX());
    }

    /**
     * Computes the norm (distance from the origin) of the given point.
     **/
    public static double norm(Point2D point) {
        return Math.sqrt(normSq(point));
    }

    /**
     * Computes the norm squared (the inner product of the point with itself)
     * of the given point.
     **/
    public static double normSq(Point2D point) {
        return innerProduct(point, point);
    }

    /**
     * Computes the norm squared (the inner product of the point with itself)
     * of the given point.
     **/
    public static double normSquared(Point2D point) {
        return point.distanceSq(ZERO);
    }

    public static Point2D[] findIntersection(Point2D start, Point2D velocity, Line2D line) {
        AffineTransform trans = getTransform(velocity, subtract(line.getP1(), line.getP2()));
        if (trans.getDeterminant() != 0.0) {
            try {
                Point2D result = trans.inverseTransform(subtract(line.getP1(), start), null);
                if (result.getX() >= 0.0 && result.getY() >= 0.0 && result.getY() <= 1.0) {
                    return new Point2D[]{add(start, scalarMultiply(result.getX(), velocity))};
                }
            } catch (NoninvertibleTransformException e) {
                throw (new RuntimeException(e));
            }
        }
        return new Point2D[0];
    }

    /**
     * This is the main way that sensors determine when the next time
     * a contact will enter or exit the sensor's footprint.
     *
     * @param start normally the targetLocation
     * @param velocity normally the relative velocity between the target and the sensor
     * @param shape the footprint of the sensor
     * @return all intersections of the linear relative motion vector with the shape boundaries
     */

    public static double[] findIntersectionTime(Point2D start, Point2D velocity, Shape shape) {
        Point2D[] intersect = findIntersection(start, velocity, shape, IDENTITY_TRANSFORM);
        double[] times = new double[intersect.length];
        for (int i = 0; i < times.length; i++) {
            times[i] = innerProduct(subtract(intersect[i], start), velocity) / normSq(velocity);
        }
        return times;
    }

    public static AffineTransform getTransform(Point2D p1, Point2D p2) {
        return new AffineTransform(new double[]{p1.getX(), p1.getY(), p2.getX(), p2.getY()});
    }

    /**
     * Returns the center of the given rectangle.
     **/
    public static Point2D getCenter(RectangularShape shape) {
        return new Point2D.Double(shape.getX() + 0.5 * shape.getWidth(), shape.getY() + 0.5 * shape.getHeight());
    }

    public static Point2D[] findIntersection(Point2D start, Point2D velocity, QuadCurve2D curve) {

        Point2D v = new Point2D.Double(velocity.getY(), -velocity.getX());
        double d = innerProduct(v, start);
        double a = innerProduct(v, curve.getP1());
        double b = innerProduct(v, curve.getCtrlPt());
        double c = innerProduct(v, curve.getP2());

        double[] coeff = new double[]{a - d, 2.0 * (b - a), a - 2.0 * b + c};
        int numberSol = QuadCurve2D.solveQuadratic(coeff);

        switch (numberSol) {
            case 0:
                return new Point2D[0];
            case 2:
                if (coeff[1] >= 0.0 && coeff[1] <= 1.0) {
                    if (coeff[0] >= 0.0 && coeff[0] <= 1.0) {
                        Point2D[] intersect = new Point2D[]{
                            getPoint(curve, coeff[0]),
                            getPoint(curve, coeff[1])
                        };
                        return intersect;
                    } else {
                        coeff[0] = coeff[1];  // fall through
                    }
                }  // fall through
            case 1:
                if (coeff[0] >= 0.0 && coeff[0] <= 1.0) {
                    return new Point2D[]{getPoint(curve, coeff[0])};
                } else {
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
        if (shape instanceof Ellipse2D) {
            return findIntersection(start, velocity, (Ellipse2D) shape);
        }
        List<Point2D> intersections = new ArrayList<Point2D>();
        Shape[] segment = getSegments(shape, trans);
        for (int i = 0; i < segment.length; i++) {
            if (segment[i] instanceof Line2D) {
                Point2D[] intersect = findIntersection(start, velocity, (Line2D) segment[i]);
                if (intersect != null && intersect.length == 1) {
                    intersections.add(intersect[0]);
                }
            } else if (segment[i] instanceof QuadCurve2D) {
                Point2D[] intersect = findIntersection(start, velocity, (QuadCurve2D) segment[i]);
                for (int j = 0; j < intersect.length; j++) {
                    intersections.add(intersect[i]);
                }
            } else if (segment[i] instanceof CubicCurve2D) {
                Point2D[] intersect = findIntersection(start, velocity, (CubicCurve2D) segment[i]);
                for (int j = 0; j < intersect.length; j++) {
                    intersections.add(intersect[j]);
                }
            }
        }
        
//        return intersections.toArray(new Point2D[intersections.size()]);
        Set<Point2D> uniques =  eliminateDuplicates(intersections, 1.0E-12);

//        System.out.println("Returning intersection set, the line being analyzed starts at \n" +
//                start + " and goes in the " + velocity + " direction");
        return uniques.toArray(new Point2D[uniques.size()]);
    }

    public static Set<Point2D> eliminateDuplicates(List<Point2D> intersections, double tolerance) {
        Set<Point2D> s = new LinkedHashSet<Point2D>();
        double roundFactor = 1.0 / tolerance;
        double x, y;

        for(Point2D p : intersections){
            x = Math.round(p.getX() * roundFactor) * tolerance;
            y = Math.round(p.getY() * roundFactor) * tolerance;
            s.add(new Point2D.Double(x,y));
        }

        return s; //.toArray(new Point2D[s.size()]);
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
            if (sol[i] >= 0.0) {
                numberPositiveSolutions++;
            }
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
        switch (numberSolutions) {
            case 1:
            case 3:
                List<Point2D> list = new ArrayList<Point2D>();
                // Bug 1413.  Previously this iterated over the entire
                // sol array even if numberSolutions was only 1.
                for (int i = 0; i < numberSolutions; i++) {
                    Point2D p = getPoint(curve, sol[i]);
                    // bug 1413
                    // we round the lambda value only for the purposes of
                    // determining if we want the solution, the point is
                    // still calculated using the unmanipulated lambda.
                    StrictMath.ulp(TINY);
                    
                    double z = Math.round(sol[i] * 1.0E18) / 1.0E18;
                    // Bug [1413]  This test screens out valid solutions
//                    if (Math.abs(sol[i] - 0.5) <= 0.5) {
                    if (Math.abs(z - 0.5) <= 0.5) {
                        
                        list.add(getPoint(curve, sol[i]));
                    }
                }
                return list.toArray(new Point2D[list.size()]);
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

    /**
     * Takes the given Shape and breaks it up into the individual segments that
     * make up the Shape. 
     * @param shape The Shape to break up into segments.
     * @param transform An optional AffineTransform to apply to the segments of the Shape.
     * If <CODE>null</CODE> then no transform is applied.
     * @return An array containing each segment of the original Shape.
     **/
    public static Shape[] getSegments(Shape shape, AffineTransform transform) {
        double[] coords = new double[6];
        List<Shape> segments = new ArrayList<Shape>();
        Point2D firstPoint = null;
        Point2D lastPoint = null;
        for (PathIterator path = shape.getPathIterator(transform); !path.isDone(); path.next()) {
            switch (path.currentSegment(coords)) {
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
                    lastPoint.setLocation(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_CLOSE:
                    if (norm(subtract(lastPoint, firstPoint)) > 0.0) {
                        segments.add(new Line2D.Double(lastPoint, firstPoint));
                    }
                    break;

                default:
                    break;
            }
        }
        return segments.toArray(new Shape[segments.size()]);
    }

    /**
     * Returns the smallest positive value of the first num members of the given array.
     **/
    public static double smallestPositive(double[] data, int num) {
        return smallestPositive(data, num, TINY);
    }

    /**
     * Returns the smallest positive value of the first num members of the given array, 
     * where any number less than the tolerance is concidered to be non-positive.
     **/
    public static double smallestPositive(double[] data, int num, double tolerance) {
        double smallest = Double.POSITIVE_INFINITY;
        if (num <= data.length && num > 0) {
            for (int i = 0; i < num; i++) {
                if (data[i] > tolerance && data[i] < smallest) {
                    smallest = data[i];
                }
            }
        }
        return smallest;
    }

    /**
     * Returns the smallest positive value of the members of the given array, 
     * where any number less than the tolerance is concidered to be non-positive.
     **/
    public static double smallestPositive(double[] data, double tolerance) {
        return smallestPositive(data, data.length, tolerance);
    }

    /**
     * Returns the smallest positive value of the members of the given array.
     **/
    public static double smallestPositive(double[] data) {
        return smallestPositive(data, data.length, TINY);
    }

    /**
     * Returns the index of the member of the given array with the largest value.
     **/
    public static int getMaxIndex(double[] data) {
        int index = -1;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max) {
                index = i;
                max = data[i];
            }
        }
        return index;
    }

    public static Point2D[] getCoefficients(Line2D line) {
        Point2D[] coeff = new Point2D[2];
        coeff[0] = line.getP1();
        coeff[1] = subtract(line.getP2(), line.getP1());
        return coeff;
    }

    public static Point2D[] getCoefficients(QuadCurve2D quadCurve) {
        Point2D[] coeff = new Point2D[3];
        coeff[0] = quadCurve.getP1();
        coeff[1] = scalarMultiply(2.0, subtract(quadCurve.getCtrlPt(), quadCurve.getP1()));
        coeff[2] = add(quadCurve.getP2(), subtract(quadCurve.getP1(), scalarMultiply(2.0, quadCurve.getCtrlPt())));
        return coeff;
    }

    public static Point2D[] getCoefficients(CubicCurve2D cubicCurve) {
        Point2D[] coeff = new Point2D[4];
        coeff[0] = cubicCurve.getP1();
        coeff[1] = scalarMultiply(3.0, add(cubicCurve.getP1(), cubicCurve.getCtrlP1()));
        coeff[2] = scalarMultiply(-3.0, add(cubicCurve.getP1(), add(scalarMultiply(2.0, cubicCurve.getCtrlP1()), cubicCurve.getCtrlP2())));
        coeff[3] = add(add(cubicCurve.getP1(), scalarMultiply(3.0, subtract(cubicCurve.getCtrlP1(), cubicCurve.getCtrlP2()))), cubicCurve.getP2());
        return coeff;
    }

    /**
     * Returns a Point2D whose vector length is equal to scale.
     */
    public static Point2D scaleTo(Point2D point, double scale) {
        return Math2D.scalarMultiply(scale / Math2D.norm(point), point);
    }

    public static Point2D getIntercept(Point2D startingLocation, double speed,
            Point2D targetLocation, Point2D targetVelocity) {
        Point2D intercept = null;

        Point2D relativeLocation = subtract(targetLocation, startingLocation);
        if (Math2D.norm(relativeLocation) <= 1.0E-9) {
            // bug 1285.  Previously if the target and pursuer were
            // co-located, this procedure returned null

            intercept = targetLocation;
            return intercept;
        }

        double speed2 = speed * speed;

        double normt2 = Math2D.normSquared(targetVelocity);
        double xv = Math2D.innerProduct(relativeLocation, targetVelocity);


        if (speed2 != normt2) {
            double[] coeff = new double[3];

            coeff[0] = normt2 - speed2;
            coeff[1] = 2.0 * xv;
            coeff[2] = Math2D.normSquared(relativeLocation);

            double[] sol = new double[2];
            double oneOverTime = Double.NEGATIVE_INFINITY;
            int numberSolutions = QuadCurve2D.solveQuadratic(coeff, sol);
            switch (numberSolutions) {
                case 2:
                    double tmp = Math.max(sol[0], sol[1]);
                    if (tmp > 0.0) {
                        oneOverTime = tmp;
                    }
                    break;
                case 1:
                    if (sol[0] > 0.0) {
                        oneOverTime = sol[0];
                        break;
                    }
                default:
            }
            if (oneOverTime >= 0.0) {
                Point2D pursuerVelocity =
                        Math2D.add(Math2D.scalarMultiply(oneOverTime, relativeLocation),
                        targetVelocity);
                intercept = Math2D.add(
                        Math2D.scalarMultiply(1.0 / oneOverTime, pursuerVelocity),
                        startingLocation);
            }
        } else if (xv != 0) {
            // this case was added to resolve simkit bug 1394, and covers
            // the case where pursuer speed squared == target speed squared
            // AND relative position dotted with target velocity is
            // non-zero AND the solution results in a positive time value

            double normx2 = Math2D.normSquared(relativeLocation);
            double time = -normx2 / (2.0 * xv);
            if (time >= 0.0) {
                Point2D pursuerVelocity =
                        Math2D.add(Math2D.scalarMultiply(1.0 / time, relativeLocation),
                        targetVelocity);
                intercept = Math2D.add(
                        Math2D.scalarMultiply(time, pursuerVelocity), startingLocation);
            }
        }
        return intercept;

    }
    /**
     *  Computes the Point2D at which the pursuer will intersect the 
     *  target when the pursuer moves at the given speed.
     *  (Assumes Linear Mover).
     */
    public static Point2D getIntercept(Mover pursuer, double speed, Mover target) {
        if (speed > pursuer.getMaxSpeed()) {
            speed = pursuer.getMaxSpeed();
        }
        return getIntercept(pursuer.getLocation(), speed, target.getLocation(), target.getVelocity());

    }

    /**
     * Computes the intersection point when the pursuer goes at 
     * maximum speed.
     */
    public static Point2D getIntercept(Mover pursuer, Mover target) {
        return getIntercept(pursuer, pursuer.getMaxSpeed(), target);
    }

    public static Point2D getInterceptVelocity(Mover pursuer, double speed, Mover target) {
        Point2D intercept = getIntercept(pursuer, speed, target);
        if (intercept != null) {
            return Math2D.scaleTo(subtract(intercept, pursuer.getLocation()), speed);
        } else {
            return null;
        }
    }

    public static Point2D getInterceptVelocity(Point2D startingLocation,
            double speed, Point2D targetLocation, Point2D targetVelocity) {
        
        Point2D intercept = getIntercept(startingLocation, speed,
                targetLocation, targetVelocity);

        if (intercept != null) {
            return Math2D.scaleTo(subtract(intercept, startingLocation), speed);
        } else {
            return null;
        }
    }

    public static Point2D getInterceptVelocity(Mover pursuer, Mover target) {
        return getInterceptVelocity(pursuer, pursuer.getMaxSpeed(), target);
    }

    /**
     * Computes the point at which the pursuer is within the given range of 
     * the target.  If the pursuer is already within range, the pursuer's
     * current position is returned.
     **/
    public static Point2D getIntercept(Mover pursuer, double speed, double range, Mover target) {
        Point2D relativePursuerLocation = Math2D.subtract(target.getLocation(), pursuer.getLocation());
//        If in range already, the "intercept" is the current location
        if (Math2D.norm(relativePursuerLocation) <= range) {
            return pursuer.getLocation();
        }
        Point2D interceptVelocity = getInterceptVelocity(pursuer, speed, target);
        if (interceptVelocity == null) {
            return null;
        }
        Point2D targetVelocity = target.getVelocity();
        Point2D relativeInterceptVelocity = subtract(targetVelocity, interceptVelocity);
        double[] coeff = new double[3];
        coeff[0] = Math2D.normSquared(relativePursuerLocation) - range * range;
        coeff[1] = 2.0 * Math2D.innerProduct(relativePursuerLocation, relativeInterceptVelocity);
        coeff[2] = Math2D.normSquared(relativeInterceptVelocity);

        double[] sol = new double[2];
        int numSol = QuadCurve2D.solveQuadratic(coeff, sol);
        if (numSol <= 0) {
            return null;
        }
        double time = Math2D.smallestPositive(sol);
        return add(pursuer.getLocation(), scalarMultiply(time, interceptVelocity));
    }

    public static Point2D getIntercept(Point2D startingLocation, double speed, double range, Point2D targetLocation, Point2D targetVelocity) {
        Point2D relativePursuerLocation = Math2D.subtract(targetLocation, startingLocation);
//        If in range already, the "intercept" is the current location
        if (Math2D.norm(relativePursuerLocation) <= range) {
            return startingLocation;
        }
        Point2D interceptVelocity = getInterceptVelocity(startingLocation, speed,
                targetLocation, targetVelocity);
                //getInterceptVelocity(pursuer, speed, target);
        if (interceptVelocity == null) {
            return null;
        }
        Point2D relativeInterceptVelocity = subtract(targetVelocity, interceptVelocity);
        double[] coeff = new double[3];
        coeff[0] = Math2D.normSquared(relativePursuerLocation) - range * range;
        coeff[1] = 2.0 * Math2D.innerProduct(relativePursuerLocation, relativeInterceptVelocity);
        coeff[2] = Math2D.normSquared(relativeInterceptVelocity);

        double[] sol = new double[2];
        int numSol = QuadCurve2D.solveQuadratic(coeff, sol);
        if (numSol <= 0) {
            return null;
        }
        double time = Math2D.smallestPositive(sol);
        return add(startingLocation, scalarMultiply(time, interceptVelocity));
    }

    /**
     * Returns the angle in degrees to go from point to toPoint.  The
     * orientation is as for compass headings (clockwise) with north
     * taken as 0.0, being the y-axis, and east taken as 90.0, the x-axis
     *
     * @param point (from)
     * @param toPoint (to)
     * @return compass heading from point to toPoint
     */
    public static double bearingFrom(Point2D point, Point2D toPoint) {
        Point2D translated = Math2D.subtract(toPoint, point);
        double angle = StrictMath.atan2(translated.getX(), translated.getY());
        while(0 > angle) {
            angle += 2.0 * Math.PI;
        }
        return angle * 180.0 / Math.PI ;
    }
}

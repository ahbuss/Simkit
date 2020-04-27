package simkit.smd.util;

import java.awt.geom.Point2D;
import simkit.smd.Mover;
import simkit.smd.Sensor;


/**
 * 
 * @author ahbuss
 */
public class Math2D {

    /**
     *
     * @param first First point
     * @param second Second point
     * @return sum of the two points
     */
    public static Point2D add(Point2D first, Point2D second) {
        return new Point2D.Double(first.getX() + second.getX(), first.getY() + second.getY());
    }

    /**
     *
     * @param first First point
     * @param second Second point
     * @return difference of the two points
     */
    public static Point2D subtract(Point2D first, Point2D second) {
        return new Point2D.Double(first.getX() - second.getX(), first.getY() - second.getY());
    }

    /**
     *
     * @param alpha Given scalar
     * @param point given point
     * @return result of scalar multiplying the coordinates
     */
    public static Point2D scalarMultiply(double alpha, Point2D point) {
        return new Point2D.Double(alpha * point.getX(), alpha * point.getY());
    }

    /**
     * @param first First point
     * @param second second point
     * @return inner product of parameters
     */
    public static double innerProduct(Point2D first, Point2D second) {
        return first.getX() * second.getX() + first.getY() * second.getY();
    }

    /**
     *
     * @param point Point2D to have norm<sup>2</sup> computed
     * @return norm<sup>2</sup> of point
     */
    public static double normSquared(Point2D point) {
        return innerProduct(point, point);
    }

    /**
     * @param point Point2D to have norm<sup>2</sup> computed
     * @return norm of point
     */
    public double norm(Point2D point) {
        return Math.sqrt(normSquared(point));
    }    

    /**
     * <ol><li>If no real roots or both negative roots, return empty array.
     * <li>If one negative and one positive root, return the positive root.
     * <li>If 2 positive roots, return (smaller, larger).</ol>
     * @param location Relative location of target
     * @param velocity Relative location of target
     * @param range Maximum range of sensor
     * @return intersection times, or empty array if none.
     */
    public static double[] findIntersectionTimes(Point2D location, 
            Point2D velocity, double range) {
        double[] times = null;
        double velocityNormSq = normSquared(velocity);
        double innerProduct0 = innerProduct(velocity, location);
        double determinant =  velocityNormSq * (range * range - normSquared(location)) +
                innerProduct0 * innerProduct0;
        if (determinant < 0.0) {
            times = new double[0];
        } else {
            double firstTerm = -innerProduct0 / velocityNormSq;
            double secondTerm = Math.sqrt(determinant)/ velocityNormSq;
            times = new double[] { firstTerm - secondTerm, firstTerm + secondTerm };
        }
        return times;
    }

    /**
     * Convenience method to include relative calculations.
     * @param mover Potential target
     * @param sensor Sensor to possibly detect target
     * @return intersection times
     */
    public static double[] findIntersectionTimes(Mover mover, Sensor sensor) {
        return findIntersectionTimes(subtract(mover.getCurrentLocation(),
                sensor.getCurrentLocation()), subtract(mover.getVelocity(), sensor.getVelocity()),
                sensor.getMaxRange());
    }
    
}

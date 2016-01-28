package simkit.smd.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import static simkit.smd.Math2D.subtract;
import static simkit.smd.Math2D.innerProduct;
import static simkit.smd.Math2D.orthogonal;
import simkit.smd.Mover;
import simkit.smdx.Math2D;

/**
 * Computations associated with avoiding barriers. Here &lambda; is the
 * coefficient of where a line segment barrier might be intersected. If 0 &lt; &lambda; &lt; 1,
 * then the trajectory intersects the line segment; otherwise it misses. t is
 * the time it would take the Mover to reach the intersection point should
 * it move in that direction (possibly at unit speed). t &gt; 0 means that there will indeed be a collision. 
 * @version $Id$
 * @author ahbuss
 */
public class BarrierMath {

    /**
     * Compute &lambda; and t for a Mover at xLocation traveling with
     * velocity xVelocity with respect to a line barrier [y0,y1].
     * <p>Note that the trajectory intersects the barrier if 0 &lt; &lambda; &lt; 1
     * and that the intersection will occur in the future if t &gt; 0.
     * @param xLocation location of Mover
     * @param xVelocity velocity of Mover
     * @param y0 One endpoint of line barrier
     * @param y1 Other endpoint of line barrier
     * @return array containing computed &lambda; and t
     */
    public static double[] findLambdaAndT(Point2D xLocation, Point2D xVelocity,
            Point2D y0, Point2D y1) {
        Point2D x0minusy0 = subtract(xLocation, y0);
        Point2D y1minusy0 = subtract(y1, y0);
        Point2D vT = orthogonal(xVelocity);
        Point2D y1minusy0T = orthogonal(y1minusy0);
        
        double lambda = innerProduct(x0minusy0, vT) / innerProduct(y1minusy0, vT);
        
        double t = - innerProduct(x0minusy0, y1minusy0T) / innerProduct(xVelocity, y1minusy0T);
        double[] lambdaAndT = new double[] {lambda, t};
        
        return lambdaAndT;
    }
    
    /**
     * Computes &lambda; and t for a Mover considering movement to destination. 
     * Note that for this version, a unit vector in the direction of the 
     * velocity is used rather than an actual velocity. The important thing
     * is the sign of t.
     * @param mover Given Mover
     * @param destination Given destination
     * @param line Line barrier
     * @return array containing computed &lambda; and t
     */
    public static double[] findLambdaAndT(Mover mover, Point2D destination, Line2D line) {
        Point2D velocity = Math2D.subtract(destination, mover.getCurrentLocation());
        velocity = Math2D.unitVector(velocity);
        return findLambdaAndT(mover.getCurrentLocation(), velocity,
                    line.getP1(), line.getP2());
    }
    
    /**
     * Finds &lambda; and t for a Mover considering moving to destination and
     * possibly encountering a barrier between points y0 and y1
     * @param mover Given Mover
     * @param destination Given destination
     * @param y0 Given endpoint of line barrier
     * @param y1 Given endpoint of line barrier
     * @return array containing computed &lambda; and t
     */
    public static double[] findLambdaAndT(Mover mover, Point2D destination, Point2D y0, Point2D y1) {
        Point2D velocity = Math2D.subtract(destination, mover.getCurrentLocation());
        velocity = Math2D.unitVector(velocity);
        return findLambdaAndT(mover.getCurrentLocation(), velocity, y0, y1);
    }
    
    /**
     * 
     * @param lambdaAndT Given {&lambda;, t}
     * @return true if the given values indicate a collision
     */
    public static boolean isCollision(double[] lambdaAndT) {
        return lambdaAndT[0] < 1.0 && lambdaAndT[0] > 0.0 && lambdaAndT[1] >= 0.0;
    }
        
}

/*
 * TestIntersectionTime.java
 *
 * Created on May 29, 2002, 1:14 PM
 */

package simkit.smdx.test;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import simkit.Schedule;
import simkit.smdx.Math2D;
import simkit.smdx.Mover;
import simkit.smdx.PathMoverManager;
import simkit.smdx.UniformLinearMover;
/**
 *
 * @author  Arnold Buss
 */
public class TestIntersectionTime {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Point2D start = new Point2D.Double(20.0, 30.0);
        Point2D velocity = new Point2D.Double(40.0, 50.0);
        Line2D line = new Line2D.Double(0.0, 100.0, 80.0, 0.0);
        double maxSpeed = 100.0;
        
        Point2D[] intersect = Math2D.findIntersection(start, velocity, line);
        double[] time = Math2D.findIntersectionTime(start, velocity, line);
        
        for (int i = 0; i < time.length; i++) {
            System.out.println(i + ": " + intersect[i] + " -> " + time[i]);
        }
        
        Mover mover = new UniformLinearMover("Barney", start, maxSpeed);
        PathMoverManager pmm = new PathMoverManager(mover);
        
        pmm.addWayPoint(intersect[0], Math2D.norm(velocity));
        
        Schedule.reset();
        pmm.start();
        Schedule.setVerbose(true);
        Schedule.startSimulation();
        
        Schedule.reset();
        Point2D destination = new Point2D.Double(-20.0, -50.0);
        
        Point2D velTemp = Math2D.subtract(destination, start);
        velocity = Math2D.scalarMultiply( maxSpeed / Math2D.norm(velTemp), velTemp );
        
        intersect = Math2D.findIntersection(start, velocity, line);
        time = Math2D.findIntersectionTime(start, velocity, line);
        
        for (int i = 0; i < time.length; i++) {
            System.out.println(i + ": " + intersect[i] + " -> " + time[i]);
        }

        pmm.clearPath();
        pmm.addWayPoint(destination);
        Schedule.reset();
        pmm.start();
        
        Schedule.startSimulation();
        
        Shape cookie = new Ellipse2D.Double(0.0, -20.0, 40.0, 40.0);

        intersect = Math2D.findIntersection(start, velocity, cookie);
        time = Math2D.findIntersectionTime(start, velocity, cookie);
        
        for (int i = 0; i < time.length; i++) {
            System.out.println(i + ": " + intersect[i] + " -> " + time[i]);
        }
        
        pmm.clearPath();
        pmm.addWayPoint(intersect[0], Math2D.norm(velocity));
        Schedule.reset();
        pmm.start();
        Schedule.startSimulation();
        
    }
    
}

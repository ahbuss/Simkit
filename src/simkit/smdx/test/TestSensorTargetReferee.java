/*
 * TestSensorTargetReferee.java
 *
 * Created on May 30, 2002, 10:09 AM
 */

package simkit.smdx.test;
import simkit.smdx.*;
import simkit.*;
import simkit.util.*;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestSensorTargetReferee {
    
    static {
        SensorTargetMediatorFactory.addMediator(
            simkit.smdx.CookieCutterSensor.class,
            simkit.smdx.UniformLinearMover.class,
            simkit.smdx.CookieCutterMediator.class
        );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
/**/        
        Shape ellipse2 = new Ellipse2D.Double(100.0, 100.0, 300.0, 300.0);
        
        Point2D center = new Point2D.Double(250, 250);
        Point2D corner = new Point2D.Double(center.getX() + 150, center.getY() + 150);
        
        Ellipse2D ellipse = new Ellipse2D.Double();
        ellipse.setFrameFromCenter(center, corner);
        
        System.out.println(ellipse.getFrame());
        System.out.println("(" + ellipse.getCenterX() + ", " + ellipse.getCenterY() + ")");
        Point2D start = new Point2D.Double(0.0, 250.0);
        Point2D end = new Point2D.Double(450.0, 250.0);
        Point2D velocity = Math2D.subtract(end, start);
        velocity = Math2D.scaleTo(velocity, 20.0);
        
        System.out.println("start = " + start + " velocity = " + velocity);
        
        Point2D[] intersect = Math2D.findIntersection(start, velocity, ellipse);
        System.out.println(Math2D.arrayToString(intersect));
        
        double[] times = Math2D.findIntersectionTime(start, velocity, ellipse);
        System.out.println(Math2D.arrayToString(times));
        
        intersect = Math2D.findIntersection(start, velocity, ellipse2);
        System.out.println(Math2D.arrayToString(intersect));
        
        times = Math2D.findIntersectionTime(start, velocity, ellipse2);
        System.out.println(Math2D.arrayToString(times));
        
        Mover target = new UniformLinearMover("Target", start, 20.0);
        PathMoverManager pmm = new PathMoverManager(target);
        pmm.addWayPoint(end);
        pmm.setStartOnReset(true);
        
        Mover targetMover = new UniformLinearMover("Sensor", center, 0.0);
        Sensor sensor = new CookieCutterSensor(150, targetMover);
        
        System.out.println(sensor.getFootprint().getBounds2D());
        
        SensorTargetReferee ref = new SensorTargetReferee();
        ref.register(target);
        ref.register(sensor);
        System.out.println(ref.paramString());
        ref.setVerbose(true);
        
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
        
 /**/     
        /*
        Point2D targetStart = new Point2D.Double(-40.0, 30.0);
        double targetMaxSpeed = 20.0;
        Mover target = new UniformLinearMover("Fred", targetStart, targetMaxSpeed);
        PathMoverManager pmm = new PathMoverManager(target);
        
        Point2D dest = new Point2D.Double(80.0, 30.0);
        pmm.addWayPoint(dest);
        pmm.setStartOnReset(true);
        
        Mover sensorMover = new UniformLinearMover("Barney", new Point2D.Double(0.0, 0.0), 0.0);
        Sensor sensor = new CookieCutterSensor(30.0, sensorMover);
        
        SensorTargetReferee ref = new SensorTargetReferee();
        ref.setVerbose(true);
        
        ref.register(target);
        ref.register(sensor);
        ref.register(sensorMover);
        
        System.out.println(ref.paramString());
        
        System.out.println(sensor.getMaxRange());
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        target.addPropertyChangeListener(pcf);
        sensor.addPropertyChangeListener(pcf);
        pcf.setVisible(true);
        
        Schedule.reset();
        
        Schedule.setVerbose(true);
        Schedule.startSimulation();
         */
        
    }
    
}
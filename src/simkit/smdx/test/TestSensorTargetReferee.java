/*
 * TestSensorTargetReferee.java
 *
 * Created on May 30, 2002, 10:09 AM
 */

package simkit.smdx.test;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import simkit.Schedule;
import simkit.smdx.*;

/**
 * @version $Id$
 * @author  Arnold Buss
 */
public class TestSensorTargetReferee {
    
    static class TestMediator extends CookieCutterMediator {

        @Override
        protected void targetIsEnteringSensorRange(Sensor sensor, Mover target){
            System.out.println(target.toString() + " is entering range of " + sensor.toString());
            System.out.println("TestMediator does no special processing before scheduling detections");
        }
        
        @Override
        protected void targetIsExitingSensorRange(Sensor sensor, Mover target){
            System.out.println(target.toString() + " is exitng range of " + sensor.toString());
            System.out.println("TestMediator does no special processing before scheduling undetections");
        }
        
        @Override
        protected Contact getContactForEnterRangeEvent(Sensor sensor, Mover target) {
            Contact contact = contacts.get(target);
            if (contact == null) {
                contact = new Contact((Mover)target);
                contacts.put(target, contact);
            }
            System.out.println("TestMediator providing contact " + 
                    contact.toString() + " for target " + target.toString());
            return contact;
        }
    }
    
    static {
        SensorTargetMediatorFactory.addMediator(
            CookieCutterSensor.class,
            UniformLinearMover.class,
            new TestMediator()
        );
    }
    
    public static void main(String[] args) {

        Shape ellipse2 = new java.awt.geom.Ellipse2D.Double(100.0, 100.0, 300.0, 300.0);
        
        Point2D center = new Double(250, 250);
        Point2D corner = new Double(center.getX() + 150, center.getY() + 150);
        
        Ellipse2D ellipse = new java.awt.geom.Ellipse2D.Double();
        ellipse.setFrameFromCenter(center, corner);
        
        System.out.println(ellipse.getFrame());
        System.out.println("(" + ellipse.getCenterX() + ", " + ellipse.getCenterY() + ")");
        Point2D start = new Double(0.0, 250.0);
        Point2D end = new Double(450.0, 250.0);
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

/*
 * TestSensorTargetReferee.java
 *
 * Created on May 30, 2002, 10:09 AM
 */

package simkit.smdx.test;
import simkit.smdx.*;
import simkit.*;
import simkit.util.*;

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
/*        
        Ellipse2D ellipse = new Ellipse2D.Double();
        ellipse.setFrameFromCenter(0.0, 0.0, 30.0, 30.0);
        System.out.println(ellipse.getFrame());
        System.out.println("Center: (" + ellipse.getCenterX() + ", " + ellipse.getCenterY() + ")");
        System.out.println("Width/height: " + ellipse.getWidth() + " " + ellipse.getHeight());
        System.out.println("MinX/MaxX: " + ellipse.getMinX() + " " + ellipse.getMaxX());
        System.out.println("MinY/MaxY: " + ellipse.getMinY() + " " + ellipse.getMaxY());
        if (true) { return; }
 */     
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
        
    }
    
}

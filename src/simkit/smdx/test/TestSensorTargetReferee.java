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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Point2D targetStart = new Point2D.Double(-40.0, -50.0);
        double targetMaxSpeed = 20.0;
        Mover target = new UniformLinearMover("Fred", targetStart, targetMaxSpeed);
        PathMoverManager pmm = new PathMoverManager(target);
        
        Point2D dest = new Point2D.Double(50.0, 50.0);
        pmm.addWayPoint(dest);
        
        Mover sensorMover = new UniformLinearMover("Barney", new Point2D.Double(0.0, 0.0), 0.0);
        Sensor sensor = new CookieCutterSensor(30, sensorMover);
        
        SensorTargetReferee ref = new SensorTargetReferee();
        ref.register(target);
        ref.register(sensor);
        
        System.out.println(ref.paramString());
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        target.addPropertyChangeListener("movementState", pcf);
        pcf.setVisible(true);
        
        Schedule.reset();
        
        pmm.start();
        
        Schedule.setVerbose(true);
        Schedule.startSimulation();
        
    }
    
}

/*
 * TestNewMediator.java
 *
 * Created on March 6, 2002, 8:49 PM
 */

package simkit.test;
import simkit.*;
import simkit.smdx.*;
import java.awt.geom.*;
import java.util.*;
/**
 *
 * @author  Arnold Buss
 * @version
 */
public class TestNewMediator {
    
    /** Creates new TestNewMediator */
    public TestNewMediator() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MediatorFactory mediatorFactory = SensorTargetMediatorFactory.getInstance();
        mediatorFactory.addMediatorFor(simkit.smdx.CookieCutterSensor.class,
        simkit.smdx.UniformLinearMover.class, simkit.smdx.CookieCutterMediator.class);
        System.out.println(mediatorFactory.getMediators());
        
        Mover target = new UniformLinearMover("Fred", new Point2D.Double(0.0, 0.0), 10.0);
        Sensor sensor = new CookieCutterSensor();
        
        Mediator mediator = mediatorFactory.getMediatorFor(sensor.getClass(), target.getClass());
        System.out.println(mediator);
        
        SensorTargetReferee referee = new SensorTargetReferee();
        referee.register(target);
        referee.register(sensor);
        System.out.println(referee);
        
        mediatorFactory.clear();
        System.out.println(mediatorFactory.getMediators());
        try {
            mediatorFactory.addMediatorFor("simkit.smdx.CookieCutterSensor",
            "simkit.smdx.UniformLinearMover", "simkit.smdx.CookieCutterMediator");
        }
        catch (ClassNotFoundException e) {System.err.println(e);}
        System.out.println(mediatorFactory.getMediators());
        
        PathMoverManager pmm = new PathMoverManager(target);
        List wayPoints = new ArrayList();
        wayPoints.add(new Point2D.Double(20, 30));
        wayPoints.add(new Point2D.Double(30, 50));
        wayPoints.add(new Point2D.Double(50, 50));
        pmm.setWayPoints(wayPoints);
        
        Mover target2 = new UniformLinearMover("Barney", new Point2D.Double(100.0, 100.0), 15.0);
        wayPoints.clear();
        wayPoints.add(new Point2D.Double(80.0, 70.0));
        wayPoints.add(new Point2D.Double(40.0, 70.0));
        wayPoints.add(new Point2D.Double(40.0, 30.0));
        wayPoints.add(new Point2D.Double(45.0, 50.0));
        PathMoverManager pmm2 = new PathMoverManager(target2);
        pmm2.setWayPoints(wayPoints);
        
        Schedule.reset();
        Schedule.setSingleStep(true);
        pmm.start();
        pmm2.start();
        
        Schedule.startSimulation();
    }
    
}

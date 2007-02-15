/*
 * TestNewMediator.java
 *
 * Created on March 6, 2002, 8:49 PM
 */

package simkit.test;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import simkit.Schedule;
import simkit.smdx.CookieCutterSensor;
import simkit.smdx.Mediator;
import simkit.smdx.MediatorFactory;
import simkit.smdx.Mover;
import simkit.smdx.PathMoverManager;
import simkit.smdx.Sensor;
import simkit.smdx.SensorTargetMediatorFactory;
import simkit.smdx.SensorTargetReferee;
import simkit.smdx.UniformLinearMover;
import simkit.smdx.WayPoint;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
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
        
        Mover[] mover =
        new Mover[] {
            new UniformLinearMover("Fred", new Point2D.Double(0.0, 0.0), 10.0),
            new UniformLinearMover("Barney", new Point2D.Double(100.0, 100.0), 15.0)
        };
        Sensor[] sensor = new Sensor[] {
            new CookieCutterSensor(10.0, mover[0]),
            new CookieCutterSensor(20.0, mover[1])
        };
        
//        for (int i = 0; i < sensor.length; i++) {
//            ((SimEntityBase)sensor[i]).setVerbose(true);
//        }
        Mediator mediator = mediatorFactory.getMediatorFor(sensor[0].getClass(), mover[0].getClass());
        System.out.println(mediator);
        
        SensorTargetReferee referee = new SensorTargetReferee();
        for (int i = 0; i < mover.length; i++) {
            referee.register(mover[i]);
            referee.register(sensor[i]);
        }
        referee.setVerbose(true);
        System.out.println(referee);        
        
        mediatorFactory.clear();
        System.out.println(mediatorFactory.getMediators());
        try {
            mediatorFactory.addMediatorFor("simkit.smdx.CookieCutterSensor",
            "simkit.smdx.UniformLinearMover", "simkit.smdx.CookieCutterMediator");
        }
        catch (ClassNotFoundException e) {System.err.println(e);}
        System.out.println(mediatorFactory.getMediators());
        
        PathMoverManager[] pmm = new PathMoverManager[] {
            new PathMoverManager(mover[0]),
            new PathMoverManager(mover[1])
        };
        
        List<List<WayPoint>> wayPoints = new ArrayList<List<WayPoint>>();
        for (int i = 0; i < pmm.length; ++i) {
            wayPoints.add(new ArrayList<WayPoint>());
        }
        
        wayPoints.get(0).add(new WayPoint(new Point2D.Double(20, 30)));
        wayPoints.get(0).add(new WayPoint(new Point2D.Double(30, 50)));
        wayPoints.get(0).add(new WayPoint(new Point2D.Double(50, 50)));
        
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(80.0, 70.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(40.0, 70.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(40.0, 30.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(45.0, 50.0)));
        
        for (int i = 0; i < pmm.length; ++i) {
            pmm[i].setWayPoints(wayPoints.get(i));
        }
        
        Schedule.reset();
        Schedule.setSingleStep(false);
        Schedule.setVerbose(true);
        for (int i = 0; i < pmm.length; i++) {
            pmm[i].start();
        }
        
        Schedule.startSimulation();
    }
    
}

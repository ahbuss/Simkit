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
        
        List[] wayPoints = new List[2];
        for (int i = 0; i < wayPoints.length; i++) {
            wayPoints[i] = new ArrayList();
        }
        wayPoints[0].add(new Point2D.Double(20, 30));
        wayPoints[0].add(new Point2D.Double(30, 50));
        wayPoints[0].add(new Point2D.Double(50, 50));
        
        wayPoints[1].add(new Point2D.Double(80.0, 70.0));
        wayPoints[1].add(new Point2D.Double(40.0, 70.0));
        wayPoints[1].add(new Point2D.Double(40.0, 30.0));
        wayPoints[1].add(new Point2D.Double(45.0, 50.0));
        
        for (int i = 0; i < wayPoints.length; i++) {
            pmm[i].setWayPoints(wayPoints[i]);
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

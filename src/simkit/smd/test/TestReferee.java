package simkit.smd.test;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import simkit.smd.BasicLinearMover;
import simkit.smd.BasicSensor;
import simkit.smd.CookieCutterMediator;
import simkit.smd.CookieCutterSensor;
import simkit.smd.PathMoverManager;
import simkit.smd.SensorMoverReferee;
import simkit.Schedule;
import simkit.smdx.WayPoint;
import simkit.util.SimplePropertyDumper;

/**
 * Simple test of Referee and CookieCutterMediator
 * @version $Id: TestReferee.java 75 2009-11-10 22:24:26Z ahbuss $
 * @author ahbuss
 */
public class TestReferee {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicLinearMover fred = new BasicLinearMover("Fred",
                new Point2D.Double(30.0, 0.0), 40.0);
        LinkedList<WayPoint> waypoint = new LinkedList<WayPoint>();
        waypoint.add(new WayPoint(new Point2D.Double(0.0, 0.0), 40.0));
        waypoint.add(new WayPoint(new Point2D.Double(0.0, 50.0), 20.0));
        PathMoverManager fredMM = new PathMoverManager(fred, waypoint, true);

        BasicSensor fredEye = new CookieCutterSensor(fred, 20.0);

        BasicLinearMover barney = new BasicLinearMover("Barney",
                new Point2D.Double(0.0, 0.0), 50.0);

        BasicSensor barneyEye = new CookieCutterSensor(barney, 10.0);

        System.out.println(fred);
        System.out.println(fredMM);
        System.out.println(fredEye);

        System.out.println(barney);
        System.out.println(barneyEye);

        SensorMoverReferee referee = new SensorMoverReferee();
        fred.addSimEventListener(referee);
        fredEye.addSimEventListener(referee);
        barney.addSimEventListener(referee);
        barneyEye.addSimEventListener(referee);

        CookieCutterMediator cookieCutterMediator = new CookieCutterMediator();
        referee.addMediator(CookieCutterSensor.class, BasicLinearMover.class,
                cookieCutterMediator);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
        referee.addPropertyChangeListener(simplePropertyDumper);

        Schedule.setEventSourceVerbose(true);

        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();

    }

}

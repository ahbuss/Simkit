/*
 * TestNewMediator.java
 *
 * Created on March 6, 2002, 8:49 PM
 */
package simkit.test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import simkit.Schedule;
import simkit.smd.CookieCutterSensor;
import simkit.smd.Mover;
import simkit.smd.PathMoverManager;
import simkit.smd.Sensor;
import simkit.smd.SensorMoverReferee;
import simkit.smd.BasicLinearMover;
import simkit.smd.CookieCutterMediator;
import simkit.smd.WayPoint;

/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TestNewMediator {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SensorMoverReferee referee = new SensorMoverReferee();
        referee.addMediator(
                CookieCutterSensor.class,
                BasicLinearMover.class,
                new CookieCutterMediator());

        Mover[] mover =
                new Mover[]{
            new BasicLinearMover("Fred", new Point2D.Double(0.0, 0.0), 10.0),
            new BasicLinearMover("Barney", new Point2D.Double(100.0, 100.0), 15.0)
        };
        Sensor[] sensors = new Sensor[]{
            new CookieCutterSensor(mover[0], 10.0),
            new CookieCutterSensor(mover[1], 20.0)
        };


        for (Sensor sensor : sensors) {
            sensor.addSimEventListener(referee);
            sensor.getMover().addSimEventListener(referee);
        }

        PathMoverManager[] pmm = new PathMoverManager[]{
            new PathMoverManager(mover[0], new LinkedList<WayPoint>(), true),
            new PathMoverManager(mover[1], new LinkedList<WayPoint>(), true)
        };

        List<LinkedList<WayPoint>> wayPoints = new ArrayList<LinkedList<WayPoint>>();
        for (int i = 0; i < pmm.length; ++i) {
            wayPoints.add(new LinkedList<WayPoint>());
        }

        wayPoints.get(0).add(new WayPoint(new Point2D.Double(20, 30)));
        wayPoints.get(0).add(new WayPoint(new Point2D.Double(30, 50)));
        wayPoints.get(0).add(new WayPoint(new Point2D.Double(50, 50)));

        wayPoints.get(1).add(new WayPoint(new Point2D.Double(80.0, 70.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(40.0, 70.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(40.0, 30.0)));
        wayPoints.get(1).add(new WayPoint(new Point2D.Double(45.0, 50.0)));

        for (int i = 0; i < pmm.length; ++i) {
            pmm[i].setWaypoint(wayPoints.get(i));
        }

        Schedule.reset();
        Schedule.setSingleStep(false);
        Schedule.setVerbose(true);
        Schedule.setEventSourceVerbose(true);

        Schedule.startSimulation();

        System.out.println(referee);
    }
}

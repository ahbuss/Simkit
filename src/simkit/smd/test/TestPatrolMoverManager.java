package simkit.smd.test;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import simkit.smd.BasicLinearMover;
import simkit.smd.PatrolMoverManager;
import simkit.Schedule;
import simkit.smdx.WayPoint;
import simkit.util.SimplePropertyDumper;

/**
 * Simple test of PatrolMoverManager.
 * Output:<pre>
oa3302.smd.PatrolMoverManager.2
        waypoint = [WayPoint (0.0, 0.0) 10.0, WayPoint (30.0, 40.0) 30.0, WayPoint (-30.0, 40.0) 10.0, WayPoint (50.0, 60.0) 50.0]
        startOnRun = true
        mover = Fred [0.0000, 0.0000] [0.0000, 0.0000]
** Event List 0 -- Starting Simulation **
0.000        Run
0.000        Run
20.000        Stop
 ** End of Event List -- Starting Simulation **

lastStopLocation: null => Point2D.Double[0.0, 0.0]
velocity: null => Point2D.Double[0.0, 0.0]
Time: 0.0000        CurrentEvent: Run [1]
** Event List 0 --  **
0.000        Run
0.000        RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Run [2]
** Event List 0 --  **
0.000        RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
0.000        Start
20.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        Start
20.000        Stop
 ** End of Event List --  **

nextWayPoint: null => WayPoint (0.0, 0.0) 10.0
Time: 0.0000        CurrentEvent: Start [1]
** Event List 0 --  **
0.000        MoveTo         {Point2D.Double[0.0, 0.0], 10.0}
20.000        Stop
 ** End of Event List --  **

destination: Point2D.Double[NaN, NaN] => Point2D.Double[0.0, 0.0]
currentSpeed: 0.0 => 10.0
Time: 0.0000        CurrentEvent: MoveTo         {Point2D.Double[0.0, 0.0], 10.0} [1]
** Event List 0 --  **
0.000        StartMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 0.0
Time: 0.0000        CurrentEvent: StartMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        EndMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 0.0
currentSpeed: 10.0 => 0.0
nextWayPoint: null => WayPoint (30.0, 40.0) 30.0
Time: 0.0000        CurrentEvent: EndMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        MoveTo         {Point2D.Double[30.0, 40.0], 30.0}
20.000        Stop
 ** End of Event List --  **

destination: Point2D.Double[0.0, 0.0] => Point2D.Double[30.0, 40.0]
currentSpeed: 0.0 => 20.0
Time: 0.0000        CurrentEvent: MoveTo         {Point2D.Double[30.0, 40.0], 30.0} [2]
** Event List 0 --  **
0.000        StartMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 0.0
velocity: Point2D.Double[0.0, 0.0] => Point2D.Double[12.0, 16.0]
Time: 0.0000        CurrentEvent: StartMove         {Fred [0.0000, 0.0000] [12.0000, 16.0000]} [2]
** Event List 0 --  **
2.500        EndMove         {Fred [0.0000, 0.0000] [12.0000, 16.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 2.5
velocity: Point2D.Double[12.0, 16.0] => Point2D.Double[0.0, 0.0]
currentSpeed: 20.0 => 0.0
lastStopLocation: Point2D.Double[0.0, 0.0] => Point2D.Double[30.0, 40.0]
nextWayPoint: null => WayPoint (-30.0, 40.0) 10.0
Time: 2.5000        CurrentEvent: EndMove         {Fred [30.0000, 40.0000] [0.0000, 0.0000]} [2]
** Event List 0 --  **
2.500        MoveTo         {Point2D.Double[-30.0, 40.0], 10.0}
20.000        Stop
 ** End of Event List --  **

destination: Point2D.Double[30.0, 40.0] => Point2D.Double[-30.0, 40.0]
currentSpeed: 0.0 => 10.0
Time: 2.5000        CurrentEvent: MoveTo         {Point2D.Double[-30.0, 40.0], 10.0} [3]
** Event List 0 --  **
2.500        StartMove         {Fred [30.0000, 40.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 2.5
velocity: Point2D.Double[0.0, 0.0] => Point2D.Double[-10.0, 0.0]
Time: 2.5000        CurrentEvent: StartMove         {Fred [30.0000, 40.0000] [-10.0000, 0.0000]} [3]
** Event List 0 --  **
8.500        EndMove         {Fred [30.0000, 40.0000] [-10.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 8.5
velocity: Point2D.Double[-10.0, 0.0] => Point2D.Double[0.0, 0.0]
currentSpeed: 10.0 => 0.0
lastStopLocation: Point2D.Double[30.0, 40.0] => Point2D.Double[-30.0, 40.0]
nextWayPoint: null => WayPoint (50.0, 60.0) 50.0
Time: 8.5000        CurrentEvent: EndMove         {Fred [-30.0000, 40.0000] [0.0000, 0.0000]} [3]
** Event List 0 --  **
8.500        MoveTo         {Point2D.Double[50.0, 60.0], 50.0}
20.000        Stop
 ** End of Event List --  **

destination: Point2D.Double[-30.0, 40.0] => Point2D.Double[50.0, 60.0]
currentSpeed: 0.0 => 20.0
Time: 8.5000        CurrentEvent: MoveTo         {Point2D.Double[50.0, 60.0], 50.0} [4]
** Event List 0 --  **
8.500        StartMove         {Fred [-30.0000, 40.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 8.5
velocity: Point2D.Double[0.0, 0.0] => Point2D.Double[19.40285000290664, 4.85071250072666]
Time: 8.5000        CurrentEvent: StartMove         {Fred [-30.0000, 40.0000] [19.4029, 4.8507]} [4]
** Event List 0 --  **
12.623        EndMove         {Fred [-30.0000, 40.0000] [19.4029, 4.8507]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 12.623105625617661
velocity: Point2D.Double[19.40285000290664, 4.85071250072666] => Point2D.Double[0.0, 0.0]
currentSpeed: 20.0 => 0.0
lastStopLocation: Point2D.Double[-30.0, 40.0] => Point2D.Double[50.0, 60.0]
nextWayPoint: null => null
Time: 12.6231        CurrentEvent: EndMove         {Fred [50.0000, 60.0000] [0.0000, 0.0000]} [4]
** Event List 0 --  **
12.623        Start
20.000        Stop
 ** End of Event List --  **

nextWayPoint: null => WayPoint (0.0, 0.0) 10.0
Time: 12.6231        CurrentEvent: Start [2]
** Event List 0 --  **
12.623        MoveTo         {Point2D.Double[0.0, 0.0], 10.0}
20.000        Stop
 ** End of Event List --  **

destination: Point2D.Double[50.0, 60.0] => Point2D.Double[0.0, 0.0]
currentSpeed: 0.0 => 10.0
Time: 12.6231        CurrentEvent: MoveTo         {Point2D.Double[0.0, 0.0], 10.0} [5]
** Event List 0 --  **
12.623        StartMove         {Fred [50.0000, 60.0000] [0.0000, 0.0000]}
20.000        Stop
 ** End of Event List --  **

startMoveTime: null => 12.623105625617661
velocity: Point2D.Double[0.0, 0.0] => Point2D.Double[-6.401843996644799, -7.682212795973759]
Time: 12.6231        CurrentEvent: StartMove         {Fred [50.0000, 60.0000] [-6.4018, -7.6822]} [5]
** Event List 0 --  **
20.000        Stop
20.433        EndMove         {Fred [50.0000, 60.0000] [-6.4018, -7.6822]}
 ** End of Event List --  **

Time: 20.0000        CurrentEvent: Stop [1]
** Event List 0 --  **
20.433        EndMove         {Fred [2.7743, 3.3291] [-6.4018, -7.6822]}
 ** End of Event List --  **</pre>
 * @version $Id: TestPatrolMoverManager.java 82 2009-11-20 19:27:18Z ahbuss $
 * @author ahbuss
 */
public class TestPatrolMoverManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicLinearMover fred = new BasicLinearMover(
                "Fred", new Point2D.Double(), 20.0);

        LinkedList<WayPoint> wayPoint = new LinkedList<WayPoint>();
        wayPoint.add(new WayPoint(new Point2D.Double(0.0, 0.0), 10.0));
        wayPoint.add(new WayPoint(new Point2D.Double(30.0, 40.0), 30.0));
        wayPoint.add(new WayPoint(new Point2D.Double(-30.0, 40.0), 10.0));
        wayPoint.add(new WayPoint(new Point2D.Double(50.0, 60.0), 50.0));

        PatrolMoverManager fredMM = new PatrolMoverManager(fred, wayPoint, true);
        
        System.out.println(fredMM);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
        fred.addPropertyChangeListener(simplePropertyDumper);
        fredMM.addPropertyChangeListener(simplePropertyDumper);

        Schedule.setVerbose(true);
        Schedule.setEventSourceVerbose(true);

        Schedule.stopAtTime(20.0);

        Schedule.reset();

        Schedule.startSimulation();
    }

}

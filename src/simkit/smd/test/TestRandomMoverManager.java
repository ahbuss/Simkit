package simkit.smd.test;

import java.awt.geom.Point2D;
import simkit.smd.BasicLinearMover;
import simkit.smd.RandomMoverManager;
import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * Simple test of RandomMoverManager.
 * Output:<pre>
RandomMoverManager Fred [0.0000, 0.0000] [0.0000, 0.0000] [Uniform (-100.0, 100.0), Normal (0.0, 5.0)]
** Event List 0 -- Starting Simulation **
0.000        Run
0.000        Run
10.000        Stop
 ** End of Event List -- Starting Simulation **

Time: 0.0000        CurrentEvent: Run [1]
** Event List 0 --  **
0.000        Run
0.000        RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Run [2]
** Event List 0 --  **
0.000        RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
0.000        Start
10.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: RegisterMover         {Fred [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        Start
10.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Start [1]
** Event List 0 --  **
0.000        MoveTo         {Point2D.Double[63.46601196564734, 0.4079293132028803]}
10.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: MoveTo         {Point2D.Double[63.46601196564734, 0.4079293132028803]} [1]
** Event List 0 --  **
0.000        StartMove         {Fred [0.0000, 0.0000] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: StartMove         {Fred [0.0000, 0.0000] [29.9994, 0.1928]} [1]
** Event List 0 --  **
2.116        EndMove         {Fred [0.0000, 0.0000] [29.9994, 0.1928]}
10.000        Stop
 ** End of Event List --  **

Time: 2.1156        CurrentEvent: EndMove         {Fred [63.4660, 0.4079] [0.0000, 0.0000]} [1]
** Event List 0 --  **
2.116        MoveTo         {Point2D.Double[-73.69341803714633, 0.847129474179178]}
10.000        Stop
 ** End of Event List --  **

Time: 2.1156        CurrentEvent: MoveTo         {Point2D.Double[-73.69341803714633, 0.847129474179178]} [2]
** Event List 0 --  **
2.116        StartMove         {Fred [63.4660, 0.4079] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 2.1156        CurrentEvent: StartMove         {Fred [63.4660, 0.4079] [-29.9998, 0.0961]} [2]
** Event List 0 --  **
6.688        EndMove         {Fred [63.4660, 0.4079] [-29.9998, 0.0961]}
10.000        Stop
 ** End of Event List --  **

Time: 6.6876        CurrentEvent: EndMove         {Fred [-73.6934, 0.8471] [0.0000, 0.0000]} [2]
** Event List 0 --  **
6.688        MoveTo         {Point2D.Double[-17.857897793874145, -7.371207988731184]}
10.000        Stop
 ** End of Event List --  **

Time: 6.6876        CurrentEvent: MoveTo         {Point2D.Double[-17.857897793874145, -7.371207988731184]} [3]
** Event List 0 --  **
6.688        StartMove         {Fred [-73.6934, 0.8471] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 6.6876        CurrentEvent: StartMove         {Fred [-73.6934, 0.8471] [29.6802, -4.3686]} [3]
** Event List 0 --  **
8.569        EndMove         {Fred [-73.6934, 0.8471] [29.6802, -4.3686]}
10.000        Stop
 ** End of Event List --  **

Time: 8.5688        CurrentEvent: EndMove         {Fred [-17.8579, -7.3712] [0.0000, 0.0000]} [3]
** Event List 0 --  **
8.569        MoveTo         {Point2D.Double[-32.38573623821139, -9.406355789235368]}
10.000        Stop
 ** End of Event List --  **

Time: 8.5688        CurrentEvent: MoveTo         {Point2D.Double[-32.38573623821139, -9.406355789235368]} [4]
** Event List 0 --  **
8.569        StartMove         {Fred [-17.8579, -7.3712] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 8.5688        CurrentEvent: StartMove         {Fred [-17.8579, -7.3712] [-29.7099, -4.1619]} [4]
** Event List 0 --  **
9.058        EndMove         {Fred [-17.8579, -7.3712] [-29.7099, -4.1619]}
10.000        Stop
 ** End of Event List --  **

Time: 9.0578        CurrentEvent: EndMove         {Fred [-32.3857, -9.4064] [0.0000, 0.0000]} [4]
** Event List 0 --  **
9.058        MoveTo         {Point2D.Double[17.83553697168827, -1.4616999254002925]}
10.000        Stop
 ** End of Event List --  **

Time: 9.0578        CurrentEvent: MoveTo         {Point2D.Double[17.83553697168827, -1.4616999254002925]} [5]
** Event List 0 --  **
9.058        StartMove         {Fred [-32.3857, -9.4064] [0.0000, 0.0000]}
10.000        Stop
 ** End of Event List --  **

Time: 9.0578        CurrentEvent: StartMove         {Fred [-32.3857, -9.4064] [29.6315, 4.6875]} [5]
** Event List 0 --  **
10.000        Stop
10.753        EndMove         {Fred [-32.3857, -9.4064] [29.6315, 4.6875]}
 ** End of Event List --  **

Time: 10.0000        CurrentEvent: Stop [1]
** Event List 0 --  **
10.753        EndMove         {Fred [-4.4672, -4.9898] [29.6315, 4.6875]}
 ** End of Event List --  **</pre>
 *
 * @version $Id: TestRandomMoverManager.java 82 2009-11-20 19:27:18Z ahbuss $
 * @author ahbuss
 */
public class TestRandomMoverManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        BasicLinearMover fred = new BasicLinearMover("Fred",
                new Point2D.Double(), 30.0);

        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance("Uniform", -100.0, 100.0);
        rv[1] = RandomVariateFactory.getInstance("Normal", 0.0, 5.0);

        RandomMoverManager randomMoverManager = 
                new RandomMoverManager(fred, rv, true);

        System.out.println(randomMoverManager);

        Schedule.stopAtTime(10.0);
        Schedule.setVerbose(true);
        Schedule.setEventSourceVerbose(true);

        Schedule.reset();
        Schedule.startSimulation();
    }

}

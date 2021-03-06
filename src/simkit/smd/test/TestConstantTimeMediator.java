package simkit.smd.test;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import simkit.smd.BasicLinearMover;
import simkit.smd.BasicSensor;
import simkit.smd.ConstantTimeMediator;
import simkit.smd.ConstantTimeSensor;
import simkit.smd.CookieCutterMediator;
import simkit.smd.CookieCutterSensor;
import simkit.smd.PathMoverManager;
import simkit.smd.SensorMoverReferee;
import simkit.Schedule;
import simkit.smd.WayPoint;
import simkit.util.SimplePropertyDumper;

/**
 * Simple test of ConstantTimeMediator, also showing how it can co-exist
 * with a CookieCutterMediator.&lt;br&gt;
 * Output:&lt;pre&gt;
Fred [30.0000, 0.0000] [0.0000, 0.0000]
oa3302.smd.PathMoverManager.2
        mover = Fred [30.0000, 0.0000] [0.0000, 0.0000]
        waypoint = [WayPoint (-5.0, 0.0) 40.0, WayPoint (-5.0, 50.0) 20.0]
        startOnRun = true
CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]
Barney [0.0000, 0.0000] [0.0000, 0.0000]
ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0
** Event List 0 -- Starting Simulation **
0.000        Run         &lt;Fred&gt;
0.000        Run         &lt;oa3302.smd.PathMoverManager.2&gt;
0.000        Run         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        Run         &lt;Barney&gt;
0.000        Run         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
 ** End of Event List -- Starting Simulation **

Time: 0.0000        CurrentEvent: Run [1]
** Event List 0 --  **
0.000        Run         &lt;oa3302.smd.PathMoverManager.2&gt;
0.000        Run         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        Run         &lt;Barney&gt;
0.000        Run         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Run [2]
** Event List 0 --  **
0.000        Run         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        Run         &lt;Barney&gt;
0.000        Run         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

contacts: null =&gt; []
Time: 0.0000        CurrentEvent: Run [3]
** Event List 0 --  **
0.000        Run         &lt;Barney&gt;
0.000        Run         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
0.000        RegisterSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Run [4]
** Event List 0 --  **
0.000        Run         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
0.000        RegisterSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        RegisterMover         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;Barney&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

contacts: null =&gt; []
Time: 0.0000        CurrentEvent: Run [5]
** Event List 0 --  **
0.000        RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
0.000        RegisterSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        RegisterMover         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;Barney&gt;
0.000        RegisterSensor         {ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

movers: [] =&gt; [Fred [30.0000, 0.0000] [0.0000, 0.0000]]
Time: 0.0000        CurrentEvent: RegisterMover         {Fred [30.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        RegisterSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.000        RegisterMover         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;Barney&gt;
0.000        RegisterSensor         {ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

sensors: [] =&gt; [CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]]
Time: 0.0000        CurrentEvent: RegisterSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        RegisterMover         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;Barney&gt;
0.000        RegisterSensor         {ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

movers: [Fred [30.0000, 0.0000] [0.0000, 0.0000]] =&gt; [Fred [30.0000, 0.0000] [0.0000, 0.0000], Barney [0.0000, 0.0000] [0.0000, 0.0000]]
Time: 0.0000        CurrentEvent: RegisterMover         {Barney [0.0000, 0.0000] [0.0000, 0.0000]} [2]
** Event List 0 --  **
0.000        RegisterSensor         {ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

sensors: [CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]] =&gt; [ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0, CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [0.0000, 0.0000]]
Time: 0.0000        CurrentEvent: RegisterSensor         {ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0} [1]
** Event List 0 --  **
0.000        Start         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: Start [1]
** Event List 0 --  **
0.000        MoveTo         {Point2D.Double[-5.0, 0.0], 40.0}         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: MoveTo         {Point2D.Double[-5.0, 0.0], 40.0} [1]
** Event List 0 --  **
0.000        StartMove         {Fred [30.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: StartMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        CheckMover         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@665753}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.000        StartMove         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: CheckMover         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@665753} [1]
** Event List 0 --  **
0.000        CheckMover         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@665753}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.000        StartMove         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.500        EnterRange         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: CheckMover         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@665753} [2]
** Event List 0 --  **
0.000        StartMove         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.500        EnterRange         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: StartMove         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000]} [1]
** Event List 0 --  **
0.000        CheckSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@ef22f8}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.500        EnterRange         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@ef22f8} [1]
** Event List 0 --  **
0.000        CheckSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@ef22f8}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.500        EnterRange         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.0000        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000], java.util.HashMap$KeyIterator@ef22f8} [2]
** Event List 0 --  **
0.250        EnterRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.500        EnterRange         {Fred [30.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [30.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.2500        CurrentEvent: EnterRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]} [1]
** Event List 0 --  **
0.250        EnterRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterMediator.8&gt;
0.500        EnterRange         {Fred [20.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

Time: 0.2500        CurrentEvent: EnterRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]} [2]
** Event List 0 --  **
0.250        Detection         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.500        EnterRange         {Fred [20.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

contacts: [] =&gt; [Barney [0.0000, 0.0000] [0.0000, 0.0000]]
detection: null =&gt; Barney [0.0000, 0.0000] [0.0000, 0.0000]
Time: 0.2500        CurrentEvent: Detection         {Barney [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.500        EnterRange         {Fred [20.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        EndMove         {Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [20.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

Time: 0.5000        CurrentEvent: EnterRange         {Fred [10.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0} [1]
** Event List 0 --  **
0.500        EnterRange         {Fred [10.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeMediator.9&gt;
0.875        EndMove         {Fred [10.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
1.000        ExitRange         {Fred [10.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [10.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

Time: 0.5000        CurrentEvent: EnterRange         {Fred [10.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0} [2]
** Event List 0 --  **
0.875        EndMove         {Fred [10.0000, 0.0000] [-40.0000, 0.0000]}         &lt;Fred&gt;
1.000        ExitRange         {Fred [10.0000, 0.0000] [-40.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [10.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [10.0000, 0.0000] [-40.0000, 0.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: EndMove         {Fred [-5.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.875        EndMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
0.875        MoveTo         {Point2D.Double[-5.0, 50.0], 20.0}         &lt;oa3302.smd.PathMoverManager.2&gt;
1.000        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: EndMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
0.875        MoveTo         {Point2D.Double[-5.0, 50.0], 20.0}         &lt;oa3302.smd.PathMoverManager.2&gt;
1.000        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: MoveTo         {Point2D.Double[-5.0, 50.0], 20.0} [2]
** Event List 0 --  **
0.875        StartMove         {Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
1.000        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 0.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: StartMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]} [2]
** Event List 0 --  **
0.875        CheckMover         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@1e0cf70}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        StartMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
1.000        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: CheckMover         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@1e0cf70} [3]
** Event List 0 --  **
0.875        CheckMover         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@1e0cf70}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
0.875        StartMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.308        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: CheckMover         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@1e0cf70} [4]
** Event List 0 --  **
0.875        StartMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.308        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: StartMove         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]} [2]
** Event List 0 --  **
0.875        CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@c40c80}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.308        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@c40c80} [3]
** Event List 0 --  **
0.875        CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@c40c80}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.250        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.308        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 0.8750        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000], java.util.HashMap$KeyIterator@c40c80} [4]
** Event List 0 --  **
1.308        ExitRange         {Fred [-5.0000, 0.0000] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
1.500        Detection         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
1.843        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
3.375        EndMove         {Fred [-5.0000, 0.0000] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 1.3080        CurrentEvent: ExitRange         {Fred [-5.0000, 8.6603] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0} [1]
** Event List 0 --  **
1.308        ExitRange         {Fred [-5.0000, 8.6603] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0}         &lt;oa3302.smd.ConstantTimeMediator.9&gt;
1.500        Detection         {Fred [-5.0000, 8.6603] [0.0000, 20.0000]}         &lt;oa3302.smd.ConstantTimeSensor.5&gt;
1.843        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 8.6603] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
3.375        EndMove         {Fred [-5.0000, 8.6603] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 1.3080        CurrentEvent: ExitRange         {Fred [-5.0000, 8.6603] [0.0000, 20.0000], ConstantTimeSensor 10.0 Barney [0.0000, 0.0000] [0.0000, 0.0000] 1.0} [2]
** Event List 0 --  **
1.843        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 8.6603] [0.0000, 20.0000]}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
3.375        EndMove         {Fred [-5.0000, 8.6603] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 1.8432        CurrentEvent: ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 19.3649] [0.0000, 20.0000]} [1]
** Event List 0 --  **
1.843        ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 19.3649] [0.0000, 20.0000]}         &lt;oa3302.smd.CookieCutterMediator.8&gt;
3.375        EndMove         {Fred [-5.0000, 19.3649] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 1.8432        CurrentEvent: ExitRange         {Barney [0.0000, 0.0000] [0.0000, 0.0000], CookieCutterSensor 20.0 Fred [-5.0000, 19.3649] [0.0000, 20.0000]} [2]
** Event List 0 --  **
1.843        Undetection         {Barney [0.0000, 0.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
3.375        EndMove         {Fred [-5.0000, 19.3649] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

contacts: [Barney [0.0000, 0.0000] [0.0000, 0.0000]] =&gt; []
undetection: null =&gt; Barney [0.0000, 0.0000] [0.0000, 0.0000]
Time: 1.8432        CurrentEvent: Undetection         {Barney [0.0000, 0.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
3.375        EndMove         {Fred [-5.0000, 19.3649] [0.0000, 20.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: EndMove         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]} [2]
** Event List 0 --  **
3.375        EndMove         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
3.375        OrderStop         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: EndMove         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]} [2]
** Event List 0 --  **
3.375        OrderStop         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.PathMoverManager.2&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: OrderStop         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
3.375        Stop         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;Fred&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: Stop         {Fred [-5.0000, 50.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
3.375        CheckMover         {Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@110d81b}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
3.375        Stop         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: CheckMover         {Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@110d81b} [5]
** Event List 0 --  **
3.375        CheckMover         {Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@110d81b}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
3.375        Stop         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: CheckMover         {Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@110d81b} [6]
** Event List 0 --  **
3.375        Stop         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]}         &lt;oa3302.smd.CookieCutterSensor.3&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: Stop         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000]} [1]
** Event List 0 --  **
3.375        CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@dbe178}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@dbe178} [5]
** Event List 0 --  **
3.375        CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@dbe178}         &lt;oa3302.smd.SensorMoverReferee.6&gt;
 ** End of Event List --  **

Time: 3.3750        CurrentEvent: CheckSensor         {CookieCutterSensor 20.0 Fred [-5.0000, 50.0000] [0.0000, 0.0000], java.util.HashMap$KeyIterator@dbe178} [6]
** Event List 0 --  **
            &lt;&lt; empty &gt;&gt;
 ** End of Event List --  **
&lt;/pre&gt;
 * @version $Id: TestConstantTimeMediator.java 75 2009-11-10 22:24:26Z ahbuss $
 * @author ahbuss
 */
public class TestConstantTimeMediator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicLinearMover fred = new BasicLinearMover("Fred",
                new Point2D.Double(30.0, 0.0), 40.0);
        LinkedList<WayPoint> waypoint = new LinkedList<>();
        waypoint.add(new WayPoint(new Point2D.Double(-5.0, 0.0), 40.0));
        waypoint.add(new WayPoint(new Point2D.Double(-5.0, 50.0), 20.0));
        PathMoverManager fredMM = new PathMoverManager(fred, waypoint, true);

        BasicSensor fredEye = new CookieCutterSensor(fred, 20.0);

        BasicLinearMover barney = new BasicLinearMover("Barney",
                new Point2D.Double(0.0, 0.0), 50.0);

        BasicSensor barneyEye = new ConstantTimeSensor(barney, 10.0, 1.0);

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

        ConstantTimeMediator constantTimeMediator = new ConstantTimeMediator();
        referee.addMediator(ConstantTimeSensor.class, BasicLinearMover.class,
                constantTimeMediator);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
        referee.addPropertyChangeListener(simplePropertyDumper);

        fredEye.addPropertyChangeListener(simplePropertyDumper);
        barneyEye.addPropertyChangeListener(simplePropertyDumper);

        Schedule.setEventSourceVerbose(true);

        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
}
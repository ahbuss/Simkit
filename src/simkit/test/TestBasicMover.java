package simkit.test;

import simkit.Schedule;
import simkit.smd.BasicMover;
import simkit.smd.Coordinate;

/**
 *
 * @author  Arnold Buss
 * 
 *  This test verifies that the BasicMover,moveTo(Coordinate, double) method
 *  works properly.
 */
public class TestBasicMover {

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        BasicMover bm = new BasicMover("Test (10, 20) 50");
        Coordinate destination = new Coordinate("40 60");
        
        Schedule.setVerbose(true);
        Schedule.reset();
        bm.moveTo(destination);
        Schedule.startSimulation();
        
        Schedule.reset();
        bm.moveTo(destination, 20);
        Schedule.startSimulation();
        
        Schedule.reset();
        bm.moveTo(destination, 100);
        Schedule.startSimulation();
        
    }
/*
 ** Event List -- Starting Simulation **
0.000        StartMove         {Test (10.00, 20.00) 50.0}        
 ** End  of Event List -- Starting Simulation **
 
Time: 0.000         Current Event: StartMove         {Test (10.00, 20.00) 50.0}        [1]
 ** Event List --  **
1.000        EndMove         {Test (10.00, 20.00) 50.0}        
 ** End  of Event List --  **
 
Time: 1.000         Current Event: EndMove         {Test (40.00, 60.00) 50.0}        [1]
 ** Event List --  **
               << empty >>
 ** End  of Event List --  **
 
 ** Event List -- Starting Simulation **
0.000        StartMove         {Test (10.00, 20.00) 20.0}        
 ** End  of Event List -- Starting Simulation **
 
Time: 0.000         Current Event: StartMove         {Test (10.00, 20.00) 20.0}        [1]
 ** Event List --  **
2.500        EndMove         {Test (10.00, 20.00) 20.0}        
 ** End  of Event List --  **
 
Time: 2.500         Current Event: EndMove         {Test (40.00, 60.00) 20.0}        [1]
 ** Event List --  **
               << empty >>
 ** End  of Event List --  **
 
 ** Event List -- Starting Simulation **
0.000        StartMove         {Test (10.00, 20.00) 50.0}        
 ** End  of Event List -- Starting Simulation **
 
Time: 0.000         Current Event: StartMove         {Test (10.00, 20.00) 50.0}        [1]
 ** Event List --  **
1.000        EndMove         {Test (10.00, 20.00) 50.0}        
 ** End  of Event List --  **
 
Time: 1.000         Current Event: EndMove         {Test (40.00, 60.00) 50.0}        [1]
 ** Event List --  **
               << empty >>
 ** End  of Event List --  **
 
 */    

}

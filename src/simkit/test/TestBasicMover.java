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
        VariableSpeedMover bm = new VariableSpeedMover("Test (10, 20) 50");
        Coordinate destination = new Coordinate("40 60");
        
        Schedule.setVerbose(true);
        
//  Move to destination at maximum speed (should take 1.0)
        Schedule.reset();
        bm.moveTo(destination);
        Schedule.startSimulation();
        
//  Move to destination at speed less than maximum speed (should take 2.5)
        Schedule.reset();
        bm.moveTo(destination, 20);
        Schedule.startSimulation();
        
//  Attempt to move to destination at speed greater than max speed (should take 1.0)
        Schedule.reset();
        bm.moveTo(destination, 100);
        Schedule.startSimulation();
        
//  Attempt to move to destination at speed less than max speed 
//  by first setting the speed then using as a second argument to
//  moveTo(Coordinate, double) (should take 2.0)
        Schedule.reset();
        bm.setSpeed(25);
        bm.moveTo(destination, bm.getSpeed());
        Schedule.startSimulation();
    }
}

/**
 *  Class to illustrate subclassing BasicMover so setSpeed() can be made public.
 *  Note that you cannot simply setSpeed() then moveTo(), but must use
 *  moveTo(Coordinate, VariableSpeedMover.getSpeed());
 */
    class VariableSpeedMover extends BasicMover {
        
        public VariableSpeedMover(String name, Coordinate location, double maxSpeed) {
            super(name, location, maxSpeed);
        }
        
        public VariableSpeedMover(Coordinate location, double speed) {
            super(location, speed);
        }
        
        public VariableSpeedMover(String params) {
            super(params);
        }
        
        public void setSpeed(double speed) { super.setSpeed(speed); }
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
 
 ** Event List -- Starting Simulation **
0.000        StartMove         {Test (10.00, 20.00) 25.0}        
 ** End  of Event List -- Starting Simulation **
 
Time: 0.000         Current Event: StartMove         {Test (10.00, 20.00) 25.0}        [1]
 ** Event List --  **
2.000        EndMove         {Test (10.00, 20.00) 25.0}        
 ** End  of Event List --  **
 
Time: 2.000         Current Event: EndMove         {Test (40.00, 60.00) 25.0}        [1]
 ** Event List --  **
               << empty >>
 ** End  of Event List --  **
 
 */    
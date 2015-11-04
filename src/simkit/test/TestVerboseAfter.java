package simkit.test;

import simkit.Schedule;
import simkit.VerboseAfter;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class TestVerboseAfter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrivalProcess arrivalProcess = 
                new ArrivalProcess(RandomVariateFactory.getInstance("Exponential", 1.7));
        VerboseAfter verboseAfter = new VerboseAfter(3.0, 8.0);
//        verboseAfter.setStartVerboseTime(-1.0);
//        verboseAfter.setEndVerboseTime(1.0);

        
        System.out.println(arrivalProcess);
        System.out.println(verboseAfter);
        
        Schedule.stopAtTime(10.0);
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.printf("Simulation ended at %.3f%n", Schedule.getSimTime());
        
    }

}

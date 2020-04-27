package simkit.test;

import simkit.Schedule;
import simkit.VerboseInterval;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;

/**
 * 
 * @author ahbuss
 */
public class TestVerboseInterval {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrivalProcess arrivalProcess = 
                new ArrivalProcess(RandomVariateFactory.getInstance("Exponential", 1.7));
        VerboseInterval verboseInterval = new VerboseInterval(3.0, 8.0);
//        verboseInterval.setStartVerboseTime(-1.0);
//        verboseInterval.setEndVerboseTime(1.0);

        
        System.out.println(arrivalProcess);
        System.out.println(verboseInterval);
        
        Schedule.stopAtTime(10.0);
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.printf("Simulation ended at %.3f%n", Schedule.getSimTime());
        
    }

}

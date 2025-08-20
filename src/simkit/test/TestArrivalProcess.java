package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class TestArrivalProcess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate interarrivalTimeGenerator =
                RandomVariateFactory.getInstance("Exponential", 1.2);
        ArrivalProcess arrivalProcess = new ArrivalProcess(interarrivalTimeGenerator);
        System.out.println(arrivalProcess);
        
        Schedule.setVerbose(true);
        Schedule.stopOnEvent(5, "Arrival");
        
        Schedule.reset();
        Schedule.startSimulation();
        
        Schedule.setVerbose(false);
        
        System.out.println("Restarting silently...");
        Schedule.reset();
        Schedule.startSimulation();
        
        
    }
    
}

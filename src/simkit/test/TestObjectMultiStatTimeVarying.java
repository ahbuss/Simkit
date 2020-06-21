package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.ObjectMultiTimeVaryingStat;

/**
 *
 * @author ahbuss
 */
public class TestObjectMultiStatTimeVarying {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int number = 5;
        ObjectMultiTimeVaryingStat stat = new ObjectMultiTimeVaryingStat("numberArrivals");
        for (int i = 0; i < number; ++i) {
            RandomVariate rv = RandomVariateFactory.getInstance("Exponential", 1.7 + i);
            ArrivalProcess arrivalProcess = new ArrivalProcess(rv);
            arrivalProcess.addPropertyChangeListener(stat);
        }
        
        Schedule.stopAtTime(1000.0);
        
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println(stat);
    }

}

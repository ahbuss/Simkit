package simkit.test;

import java.util.Set;

import simkit.ReRunnable;
import simkit.Schedule;
import simkit.SimExec;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author  ahbuss
 */
public class TestSimExec {

    public static void main(String[] args) {
        
        ArrivalProcess arrivalProcess = new ArrivalProcess(
            RandomVariateFactory.getInstance("Exponential", 3.2));
        
        SimExec simExec = new SimExec();
        simExec.setSingleStep(false);
        simExec.setStopTime(50.0);
        simExec.setVerbose(true);
        simExec.setNumberRuns(2);
        System.out.println(simExec);
        
        System.out.println(Schedule.getReruns());
        
        new Thread(simExec).start();
    }
    
}

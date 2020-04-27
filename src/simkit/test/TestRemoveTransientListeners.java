package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;

/**
 * 
 * @author ahbuss
 */
public class TestRemoveTransientListeners extends SimEntityBase {

    public void doArrival() {
        System.out.println(this + " in doArrival");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrivalProcess arrivalProcess = new ArrivalProcess(
                RandomVariateFactory.getInstance(
                    "Constant", 1.1) );
        TestRemoveTransientListeners[] testRemoveTransientListeners =
                new TestRemoveTransientListeners[] {
            new TestRemoveTransientListeners(),
            new TestRemoveTransientListeners(),
            new TestRemoveTransientListeners()
        };
        
        for (TestRemoveTransientListeners trtl : testRemoveTransientListeners) {
            arrivalProcess.addSimEventListener(trtl);
        }
        testRemoveTransientListeners[1].setPersistant(false);
        
        Schedule.setVerbose(true);
        Schedule.stopOnEvent(2, "Arrival");
        Schedule.reset();
        arrivalProcess.addSimEventListener(testRemoveTransientListeners[1]);
        Schedule.startSimulation();
        
        Schedule.reset();
        Schedule.startSimulation();
    }
}
package simkit;

import simkit.Schedule;
import simkit.SimEntityBase;

/**
 * Execute all events scheduled at time 0.0 and then pause.  
 * This is done by the PauseAfterZero's Run
 * event scheduling a Pause event with the lowest priority.  The pause event
 * pauses the simulation by calling pause() on its EventList instance.  By
 * default the default EventList is used.
 *
 * <p>Care must be used if there are more than one EventLists in the JVM.
 * @version $ID$
 * @author ahbuss
 */
public class PauseAfterZero extends SimEntityBase {
    
    public void doRun() {
        waitDelay("Pause", 0.0, Double.NEGATIVE_INFINITY);
    }
    
    public void doPause() {
        Schedule.pause();
    }
    
}
package simkit;

/**
 * Execute all events scheduled at time 0.0 and then pause. This is done by the
 * PauseAfterZero's Run event scheduling a Pause event with the lowest priority.
 * The pause event pauses the simulation by calling pause() on its EventList
 * instance. 
 *
 * <p>
 * Care must be used if there are more than one EventLists in the JVM.
 *
 * @author ahbuss
 */
public class PauseAfterZero extends SimEntityBase {

    public void doRun() {
        waitDelay("Pause", 0.0, Priority.LOWEST);
    }

    public void doPause() {
        getEventList().pause();
    }

}

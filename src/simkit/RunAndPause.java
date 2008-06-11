package simkit;
/**
 * <p>If a RunAndPause is instantiated, the simulation will execute all the
 * Run events and then pause.  This is done by the RunAndPause's Run
 * event scheduling a Pause event with the highest priority.  The pause event
 * pauses the simulation by calling pause() on its EventList instance.  By
 * default the default EventList is used.
 *
 * <p>Care must be used if there are more than one EventLists in the JVM.
 *
 * @version $Id: RunAndPause.java 1000 2007-02-15 19:43:11Z ahbuss $
 * @author ahbuss
 */
public class RunAndPause extends SimEntityBase {
    
    public void doRun() {
        waitDelay("Pause", 0.0, Priority.HIGHEST);
    }
    
    public void doPause() {
        Schedule.pause();
    }
}
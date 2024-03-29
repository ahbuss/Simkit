package simkit;

/**
 * Interface for objects who wish to participate in the re-runnable scheme
 * implemented by {@code EventList} and {@code Schedule}.
 * <p>
 * If this interface is implemented, the object is presumed to manage its own
 * state from simulation run to simulation run, persisting as an entity across
 * those runs, but resetting between runs.
 * <p>
 * The simkit idiom is to reset only <em>simulation state variables</em>
 * to some initial value governed by other properties or settings for each run.
 * This should occur in the object's {@code reset} method. Further, if
 * {@code isReRunnable} returns true, then the object must have a
 * {@code doRun()} method. It is in this method that initial scheduling activity
 * should take place
 * <p>
 * Refactored out of the interface {@code SimEntity}
 *
 * @author Arnie Buss, The MOVES Insititute, NPS
 */
public interface ReRunnable extends SimEventScheduler {

    /**
     *
     * @return true if given ReRunnable persists from one Schedule.reset() to
     * the next; false otherwise
     */
    boolean isPersistant();

    /**
     *
     * @return true if given ReRunnable should have its Run event scheduled at
     * the start of the simulation
     */
    boolean isReRunnable();

    /**
     * 
     * @param persist Given new value of persistent 
     */
    void setPersistant(boolean persist);

    /**
     * Implementing classes with state variables should initialize them in 
     * their overridden reset() method.
     */
    void reset();
}

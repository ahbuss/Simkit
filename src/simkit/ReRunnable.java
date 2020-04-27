package simkit;

/**
 * Interfacet for objects who wish to participate in the
 * re-runnable scheme implemented by {@code EventList} and
 * {@code Schedule}.
 * <p>
 * If this interface is implemented, the object is presumed
 * to manage its own state from simulation run to simulation
 * run, persisiting as an entity across those runs, but resetting
 * between runs.
 * <p>
 * The simkit idiom is to reset only <em>simulation state variables</em>
 * to some initial value governed by other properties or settings for
 * each run.  This should occur in the object's {@code reset} method.
 * Further, if {@code isReRunnable} returns true, then the object must
 * have a {@code doRun()} method.  It is in this method that initial scheduling
 * activity should take place
 * <p>
 * Refactored out of the interface {@code SimEntity}
 *
 * @author Arnie Buss, The MOVES Insititute, NPS
 */

public interface ReRunnable extends SimEventScheduler {
    boolean isPersistant();
    boolean isReRunnable();
    void setPersistant(boolean persist);
    void reset();
}

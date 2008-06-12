package simkit;

import java.io.PrintStream;
import java.util.Set;

/**
 *
 * @author kirk
 */
public interface BasicEventList {

    /**
     * @return Current SimEvent being processed
     */
    SimEvent getCurrentSimEvent();

    /**
     * For debugging purposes - returns a String depicting the
     * current event and state of the event list.
     * 
     * @param reason User message to be appended to event list
     * @return String version of current event and event list
     */
    String getEventListAsString(String reason);

    /**
     * @return The identifying number for this
     * <CODE>EventList</CODE> instance
     */
    int getID();

    /**
     * @return The current simulation time
     */
    double getSimTime();

    /**
     * @return Name of stop event
     */
    String getStopEventName();

    /**
     * @return Time simulation will end, if <CODE>stopAtTime</CODE>
     * is set
     */
    double getStopTime();

    /**
     * Cancel next event of given name (regardless of
     * signature) owned by the given SimEntity

     * @param SimEventScheduler SimEntity to have event cancelled
     * @param eventName Name of event to cancel
     */
    void interrupt(SimEventScheduler simEntity, String eventName);

    /**
     * Cancel next event of given name matching the
     * parameter list owned by the given SimEntity
     * 
     * @param SimEventScheduler SimEventScheduler to have event cancelled
     * @param eventName Name of event to cancel
     * @param parameters edge parameters of cancelled event
     */
    void interrupt(SimEventScheduler simEntity, String eventName, Object... parameters);

    /**
     * Cancel the all events scheduled by the given scheduler
     * 
     * @param SimEventScheduler Entity whos events will be interrupted.
     */
    void interruptAll(SimEventScheduler scheduler);

    /**
     * Cancel all events with the given name which were scheduled by the given
     * SimEventScheduler regardless of signature.
     * 
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event
     */
    void interruptAll(SimEventScheduler simEntity, String eventName);

    /**
     * Cancel all events with the given name which were scheduled by the given
     * SimEventScheduler and whos parameters exactly match the given array.
     * 
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event to cancel
     * @param parameters edge parameters that must match
     */
    void interruptAll(SimEventScheduler simEntity, String eventName, Object... parameters);

    /**
     * Returns true if there are no Events scheduled.
     */
    boolean isFinished();

    /**
     * @return Whether simulation is currently running
     */
    boolean isRunning();

    /**
     * Cause pending event processing to stop after completing processing
     * of the current event.
     * <p>
     * It is legal to subsequently call {@code step} or {@code startSimulation}.
     * {@code isRunning} will return false after this is called.
     */
    void pause();

    /**
     * Stop processing pending events.
     * <p>
     * Unlike {@code pause} resumption of the simulation is not supported.
     * After this call there  is no guarantee that previously pending events
     * or any other external references will remain accessable.
     * It is not legal to subsequently call {@code startSimulation} or
     * {@code step}
     * <p>
     */
    void stopSimulation();

    /**
     * Performs (at least) the following:
     * <UL>
     * <LI>Empty the pending event list</LI>
     * <LI>Sets simulation time to 0.0</LI>
     * </UL>
     * 
     */
    void reset();

    /**
     * Place an event on the event list.
     * 
     * @param event Event to be scheduled
     * @throws InvalidSchedulingException If scheduled time is less than current simTime.
     */
    void scheduleEvent(SimEvent event) throws InvalidSchedulingException;


    void startSimulation();

    /**
     * Process one event
     * <p>
     * Advance time to next event time and handle it.
     * <p>
     * An event is "handled" by making a callback to the scheduler of the event,
     * invoking its <CODE>handleSimEvent</CODE> method.
     * <p>
     * Then have the owner of the event notify its listeners of the event.
     * <p>
     * {@code isRunning()} will return true after this is called.
     * <p>
     * Note:  Initiating event notifications from this method is a legacy
     * imlementation detail.  Newer implementations of 
     * this interface will likely not include this action since it should be the
     * responsibility of the listened-to object to notify its listeners.
     * That is, an object should not have to be a SimEventListener in order
     * to schedule and respond to its own events.  Conversely, a {@link SimEventListener}
     * should not have to be a {@link SimEventScheduler}.
     */
    void step();

    /**
     * Cause the schedule to stop processing pending events when simulation
     * time reaches the given value.
     * <p>
     * Calling this method should cause any other stop method previosly called
     * to be ineffective.  That is, the last stop_xxx method invoked should
     * be the difinitive one.
     * 
     * @deprecated Use a Stop entity
     * @param time Time to stop simulation
     */
    void stopAtTime(double time);

    /**
     * Cause event processing to stop when the given number of
     * events with this signature have occurred.
     * <p>
     * Calling this method should cause any other stop method previosly called
     * to be ineffective.  That is, the last stop_xxx method invoked should
     * be the difinitive one.
     * 
     * @deprecated Use a Stop entity
     * @param numberEvents Number of events to occur
     * @param eventName Name of stop event
     * @param signature Signature of stop event
     */
    void stopOnEvent(int numberEvents, String eventName, Class... signature);


}

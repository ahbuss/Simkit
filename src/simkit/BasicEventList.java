package simkit;

import java.io.PrintStream;
import java.util.Set;

/**
 * A complete interface extraction of the public API provided by
 * simkit.EventList
 *
 * @author kirk
 */
public interface BasicEventList {

    /**
     * @return Current SimEvent being processed
     */
    SimEvent getCurrentSimEvent();

    /**
     * For debugging purposes - returns a String depicting the current event and
     * state of the event list.
     *
     * @param reason User message to be appended to event list
     * @return String version of current event and event list
     */
    String getEventListAsString(String reason);

    /**
     * @return The identifying number for this instance.
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
     * @return Time simulation will end, if
     * <CODE>stopAtTime</CODE> is set
     */
    double getStopTime();

    /**
     * Cancel next event of given name (regardless of signature) owned by the
     * given SimEntity
     *
     * @param simEntity SimEventScheduler to have event cancelled
     * @param eventName Name of event to cancel
     */
    void interrupt(SimEventScheduler simEntity, String eventName);

    /**
     * Cancel next event of given name matching the parameter list owned by the
     * given SimEntity
     *
     * @param simEntity SimEventScheduler to have event cancelled
     * @param eventName Name of event to cancel
     * @param parameters edge parameters of cancelled event
     */
    void interrupt(SimEventScheduler simEntity, String eventName, Object... parameters);

    /**
     * Cancel the all events scheduled by the given scheduler
     *
     * @param scheduler SimEventScheduler whose events will be interrupted.
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
     * Interrupt all events scheduled by this simEntity having the given
     * eventName and containing at least one of the given parameter in its
     * arguments.
     *
     * @param simEntity Scheduling SimEntity
     * @param eventName Name of events to be canceled
     * @param parameter argument to be matched
     */
    void interruptAllWithArg(SimEventScheduler simEntity, String eventName, Object parameter);

    /**
     * Interrupt all events scheduled by this simEntity containing at least one 
     * of the given parameter in its arguments.
     *
     * @param simEntity Scheduling SimEntity
     * @param parameter argument to be matched
     */
    void interruptAllWithArg(SimEventScheduler simEntity, Object parameter);

    /**
     * Interrupt all events of the given eventName and having an instance of
     * the parameter in its arguments regardless of who scheduled it.
     * 
     * @param eventName Name of events to be canceled
     * @param parameter argument to be matched
     */
    void interruptAllWithArg(String eventName, Object parameter);

    /**
     * Interrupt all events having this parameter in its argument list,
     * regardless of event name and who scheduled it.
     * 
     * @param parameter argument to be matched
     */
    void interruptAllWithArg(Object parameter);

    /**
     * @return true if there are no Events scheduled.
     */
    boolean isFinished();

    /**
     * @return Whether simulation is currently running
     */
    boolean isRunning();

    /**
     * Cause pending event processing to stop after completing processing of the
     * current event. <p> It is legal to subsequently call {@code step} or {@code startSimulation}.
     * {@code isRunning} will return false after this is called.
     */
    void pause();

    /**
     * Stop processing pending events. <p> Unlike {@code pause} resumption of
     * the simulation is not supported. After this call there is no guarantee
     * that previously pending events or any other external references will
     * remain accessable. It is not legal to subsequently call {@code startSimulation}
     * or
     * {@code step}
     *
     */
    void stopSimulation();

    /**
     * Performs (at least) the following: <UL> <LI>Empty the pending event
     * list</LI> <LI>Sets simulation time to 0.0</LI> </UL>
     *
     */
    void reset();

    /**
     * Place an event on the event list.
     *
     * @param event Event to be scheduled
     * @throws InvalidSchedulingException If scheduled time is less than current
     * simTime.
     */
    void scheduleEvent(SimEvent event) throws InvalidSchedulingException;

    /**
     * Starts event list algorithm. While the event list is not empty, advance
     * time to next event and handle that event. An event is "handled" by making
     * a callback to its
     * <CODE>handleSimEvent</CODE> method. Then have the owner of the event
     * notify its listeners of the event.
     */
    void startSimulation();

    /**
     * Process one event <p> Advance time to next event time and handle it. <p>
     * An event is "handled" by making a callback to the scheduler of the event,
     * invoking its
     * <CODE>handleSimEvent</CODE> method. <p> Then have the owner of the event
     * notify its listeners of the event. <p>
     * {@code isRunning()} should return true after this is called. <p> Note:
     * Initiating event notifications from this method is a legacy implementation
     * detail. Newer implementations of this interface will likely not include
     * this action since it should be the responsibility of the listened-to
     * object to notify its listeners. That is, an object should not have to be
     * a SimEventListener in order to schedule and respond to its own events.
     * Conversely, a {@link SimEventListener} should not have to be a {@link SimEventScheduler}.
     */
    void step();

    /**
     * Cause the schedule to stop processing pending events when simulation time
     * reaches the given value. <p> Calling this method should cause any other
     * stop method previously called to be ineffective. That is, the last
     * stop_xxx method invoked should be the definitive one.
     *
     * @param time Time to stop simulation
     */
    void stopAtTime(double time);

    /**
     * Cause event processing to stop when the given number of events with this
     * signature have occurred. <p> Calling this method should cause any other
     * stop method previously called to be ineffective. That is, the last
     * stop_xxx method invoked should be the definitive one.
     *
     * @param numberEvents Number of events to occur
     * @param eventName Name of stop event
     * @param signature Signature of stop event
     */
    void stopOnEvent(int numberEvents, String eventName, Class<?>... signature);

    /**
     * If true, then contents of the event list are printed after each event is
     * processed.
     *
     * @param b whether verbose mode is on
     */
    public void setVerbose(boolean b);

    /**
     * @return Whether verbose mode is on
     */
    public boolean isVerbose();

    /**
     * @param b Whether single step mode is on
     */
    public void setSingleStep(boolean b);

    /**
     * @return Whether singleStep mode is on
     */
    public boolean isSingleStep();

    public void setUserDefinedStop();

    /**
     * @param b Whether the event source is printed on a dump()
     */
    public void setPrintEventSources(boolean b);

    /**
     *
     * @return true if event sources are printed on a dump()
     */
    public boolean isPrintEventSources();

    /**
     * Add the SimEntity to the reRun list. On
     * <CODE>Schedule.reset</CODE> the SimEntity's
     * <CODE>reset()</CODE> method is invoked and its Run event (if it has one)
     * is scheduled at time 0.0. This happens only if the SimEntity is
     * persistant.
     *
     * @param simEntity SimEntity to be added as a reRun
     */
    public void addRerun(ReRunnable simEntity);

    /**
     * Remove the given SimEntity from the reRun list
     *
     * @param simEntity SimEntity to be removed from reRun list
     */
    public void removeRerun(ReRunnable simEntity);

    /**
     * Empty the reRun list
     */
    public void clearRerun();

    /**
     * For debugging purposes - gets a copy of the current reRun list.
     *
     * @return Copy of reRun list
     */
    public Set<ReRunnable> getRerun();

    /**
     * Events of this name will not be printed in verbose mode.
     *
     * @param eventName Name of event to be ignored
     */
    public void addIgnoreOnDump(String eventName);

    /**
     * Events of this name now <I>will</I> be printed in verbose mode.
     *
     * @param eventName Event Name
     */
    public void removeIgnoreOnDump(String eventName);

    /**
     * For debugging purposes - returns a copy of the ignored events
     *
     * @return Copy of ignored events
     */
    public Set<String> getIgnoredEvents();

    /**
     * For debugging, gives more detailed output
     *
     * @param b Whether reallyVerbose is on
     */
    public void setReallyVerbose(boolean b);

    public boolean isReallyVerbose();

    /**
     * If true, then simulation will pause after each event and resume only on
     * another call to
     * {@code startSimulation()}
     *
     * @param b Whether this mode is on
     */
    public void setPauseAfterEachEvent(boolean b);

    public boolean isPauseAfterEachEvent();

    /**
     * Resets instance to pristine condition, as if it were freshly
     * instantiated. All containers are emptied, and various booleans are set to
     * their default values (typically
     * <CODE>false</CODE>).
     */
    public void coldReset();

    public void setOutputStream(PrintStream outputStream);

    public PrintStream getOutputStream();

    /**
     * Dumps current event list to the stream set by
     * {@code setOutputStream()}.
     */
    public void dump();

    /**
     * Dumps current event list to the stream set by
     * {@code setOutputStream()}.
     *
     * @param reason Short message to add to dump
     */
    public void dump(String reason);

    /**
     * If true, then the SimEntity toString() is dumped with verbose mode for
     * each event.
     *
     * @param b Whether this mode is on
     */
    public void setDumpEventSources(boolean b);

    /**
     * @return Whether this mode is on
     */
    public boolean isDumpEventSources();

    /**
     * @return Whether this mode is on
     */
    public boolean isStopOnEvent();

    /**
     * If true, then pending SimEvents will be stored in a secondary hash table
     * to make them easier to find when interrupting (defaults to true). For
     * simulations that do not interrupt events, the added overhead from storing
     * and removing events in the secondary table could add to run time. If
     * going from false to true, add any pending events to the secondary hash
     * tables. If going from true to false, clear the secondary hash tables.
     *
     * @param value Given value
     */
    public void setFastInterrupts(boolean value);

    /**
     * @param format String for {@code DecimalFormat} of time strings
     */
    public void setFormat(String format);


    /**
     * @return Number of events before simulation ends
     */
    public int getNumberStopEvents();

    /**
     * @return Whether this mode is on
     */
    public boolean isStopAtTime();
}

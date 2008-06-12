package simkit;

/**
 * Defines the contract for interacting with the scheduling
 * mechanisms in simkit.
 *
 * @author Kirk Stork (The MOVES Institute)
 * @version $Id$
 */
public interface SimEventScheduler extends Named {
    public static final String DEFAULT_ENTITY_NAME = "SimEntity";
    public static final String DEFAULT_EVENT_NAME = "SimEvent";
    public static final Priority DEFAULT_PRIORITY = Priority.DEFAULT;
    public static final String EVENT_METHOD_PREFIX = "do";
    public static final String NL = System.getProperty("line.separator");

    /**
     * If two events occur at the same time with the same event priority,
     * the one with the highest entity priority will be processed first.
     */
    Priority getPriority();

    /**
     * A unique number assigned to this entity when it is constructed.
     */
    int getSerial();

    /**
     * Typically an Event is handled (as opposed to processed, as in SimEventListener)
     * by actually executing a method.
     * 
     * @param event The SimEvent to be handled.
     */
    void handleSimEvent(SimEvent event);

    /**
     * Interrupt (cancel) the next pending event with name eventName
     * and interruption parameter array "parameters"
     * belonging to this object.
     */
    void interrupt(String eventName, Object... parameters);

    /**
     * Interrupt (cancel) all pending events for this entity.
     */
    void interruptAll();

    /**
     * Interrupt (cancel) all pending events with name eventName
     * and interruption parameter array "parameters"
     * belonging to this object.
     */
    void interruptAll(String eventName, Object... parameters);

    /**
     * If two events occur at the same time with the same event priority,
     * the one with the highest entity priority will be processed first.
     */
    void setPriority(Priority d);

    /**
     * Schedule an event with no parameters and a default priority after a delay from
     * the current simulation time.
     * 
     * @param eventName The name of the scheduled event (prefixed by "do" for method name).
     * @param delay The amount of time before the event is scheduled
     */
    SimEvent waitDelay(String eventName, double delay, Object... parameters);

    /**
     * Schedule an event after a delay from the current
     * simulation time.
     *
     * @see simkit.SimEvent
     * @param eventName The name of the scheduled event (prefixed by "do" for method name).
     * @param delay The amount of time before the event is scheduled
     * @param priority The priority of this event (higher is better).
     * @param parameters The parameters passed to the scheduled event.
     */
    SimEvent waitDelay(String eventName, double delay, Priority priority, Object... parameters);

    /**
     * Return the particular {@link EventList} on which this scheduler will
     * schedule its events (and receive {@code handleSimEvent()} calls from).
     */
    BasicEventList getEventList();
}

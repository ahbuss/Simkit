package simkit;

/**
 * Defines the contract for interacting with the scheduling
 * mechanisms in simkit.
 *
 * @author Kirk Stork (The MOVES Institute)
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
     * Note: this is the reverse of common practice.
     * @return Priority of this object
     */
    public Priority getPriority();

    /**
     * 
     * @return A unique number assigned to this entity when it is constructed.
     */
    public int getSerial();

    /**
     * Typically an Event is handled (as opposed to processed, as in SimEventListener)
     * by actually executing a method.
     * 
     * @param event The SimEvent to be handled.
     */
    public void handleSimEvent(SimEvent event);

    /**
     * Interrupt (cancel) the next pending event with name eventName
     * and interruption parameter array "parameters"
     * belonging to this object.
     * @param eventName Name of event to cancel
     * @param parameters Parameters of the event to cancel (must be matched)
     */
    public void interrupt(String eventName, Object... parameters);

    /**
     * Cancels the next event for this entity that matches the event name.
     * @param eventName Given event name
     */
    public void interrupt(String eventName);

    /**
     * Interrupt (cancel) all pending events for this entity.
     */
    public void interruptAll();

    /**
     * Cancels all events for this entity that match the event name.
     * @param eventName Given event name
     */
    public void interruptAll(String eventName);
    
    /**
     * Interrupt (cancel) all pending events with name eventName
     * and interruption parameter array "parameters"
     * belonging to this object.
     * @param eventName Given event name
     * @param parameters Given parameters to be matched
     */
    void interruptAll(String eventName, Object... parameters);
    
    /**
     * Interrupt (cancel) all events scheduled by this component having
     * the given eventName and an argument matching the parameter.
     * @param eventName name of event
     * @param parameter parameter to match
     */
    public void interruptAllWithArgs(String eventName, Object parameter);
    
    /**
     * 
     * Interrupt (cancel) all events scheduled by this component having
     * the given eventName and an argument matching the parameter.
     * @param parameter parameter to match
     */
    public void interruptAllWithArgs(Object parameter);

    /**
     * If two events occur at the same time with the same event priority,
     * the one with the highest entity priority will be processed first.
     * Note that this is the reverse of common practice.
     * @param priority Priority of this object
     */
    public void setPriority(Priority priority);

    /**
     * Schedule an event with (optional) given parameters and a default priority after a delay from
     * the current simulation time.
     * 
     * @param eventName The name of the scheduled event (prefixed by "do" for method name).
     * @param delay The amount of time before the event is scheduled
     * @param parameters parameters, possibly empty
     * @return Generated SimEvent
     */
    public SimEvent waitDelay(String eventName, double delay, Object... parameters);

    /**
     * Schedule an event after a delay from the current
     * simulation time.
     *
     * @see simkit.SimEvent
     * @param eventName The name of the scheduled event (prefixed by "do" for method name).
     * @param delay The amount of time before the event is scheduled
     * @param priority The priority of this event (higher is better).
     * @param parameters The parameters passed to the scheduled event.
     * @return Generated SimEvent
     */
    public SimEvent waitDelay(String eventName, double delay, Priority priority, Object... parameters);

    /**
     * 
     * @return the particular {@link EventList} on which this scheduler will
     * schedule its events (and receive {@code handleSimEvent()} calls from).
     */
    public BasicEventList getEventList();
}

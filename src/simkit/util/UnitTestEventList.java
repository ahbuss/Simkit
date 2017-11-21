package simkit.util;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import simkit.EventList;
import simkit.InvalidSchedulingException;
import simkit.SimEvent;

/**
 * <p>This EventList implementation is for Unit Testing purposes only.
 * Specifically, it only stores scheduled events and returns a copy of
 * them, the first scheduled SimEvent of a given name, or an array of
 * SimEvents of a given name.  Since no SimEvents are passed to the
 * superclass scheduleEvent() method, nothing will ever be run.
 * <p>Instantiation should be left to Schedule, and all SimEntities to
 * be tested should have their simEventListID set to the id that is
 * given it by Schedule.
 * <p>Usage:
 * <pre>
 *          int id = Schedule.addNewEventList(UnitTestEventList.class);
 *          ...
 *          SimEntity aSimEntity = new ASimEntity(...);
 *          aSimEntity.setEventListID(id);
 * </pre>
 * <p>After each test, a call to coldReset() on the UnitTestEventList instance
 * should be made.  Note that Schedule.coldReset() only resets its default
 * EventList instance.
 *
 * @version $Id$
 * @author ahbuss
 */
public class UnitTestEventList extends EventList {
    
    /**
     * Instantiate a UnitTestEventList with the given id number.
     * @param id the id of this UnitTestEventList instance
     */
    public UnitTestEventList(int id) {
        super(id);
    }
    
    /**
     * Gets the SimEvent of the given name that would occur first in the
     * current event list's state.
     * @param eventName Name of desired SimEvent
     * @return SimEvent of given name that would happen first or null if
     * no such event
     */
    public SimEvent getScheduledEvent(String eventName) {
        SimEvent simEvent = null;
        SortedSet<SimEvent> eventListCopy = getEventList();
        for (SimEvent nextEvent : eventListCopy) {
            if (nextEvent.getEventName().equals(eventName)) {
                simEvent = nextEvent;
                break;
            }
        }
        return simEvent;
    }
    
    /**
     * Gets the SimEvent of the given name and parameter list that would occur
     * first in the current event list's state.
     * @param eventName Name of desired SimEvent
     * @param params parameters passed to event when scheduled
     * @return SimEvent of given name that would happen first or null if
     * no such event
     */
    public SimEvent getScheduledEvent(String eventName, Object[] params) {
        SimEvent simEvent = null;
        SortedSet eventListCopy = getEventList();
        for (Iterator i = eventListCopy.iterator(); i.hasNext(); ) {
            SimEvent nextEvent = (SimEvent) i.next();
            if (nextEvent.getEventName().equals(eventName) &&
                    nextEvent.interruptParametersMatch(params)) {
                simEvent = nextEvent;
                break;
            }
        }
        return simEvent;
    }
    
    /**
     * Returns all scheduled events of the given name in increasing scheduled
     * order.
     * @param eventName Name of the desired SimEvent
     * @return Array of all scheduled SimEvents of the given name
     */
    public SortedSet<SimEvent> getScheduledEvents(String eventName) {
        SortedSet<SimEvent> simEvents = new TreeSet<SimEvent>();
        SortedSet<SimEvent> eventListCopy = getEventList();
        for (SimEvent nextEvent : eventListCopy ) {
            if (nextEvent.getEventName().equals(eventName)) {
                simEvents.add(nextEvent);
            }
        }
        return simEvents;
    }
    
    public SortedSet<SimEvent> getScheduledEvents() {
        return getEventList();
    }
    
    /**
     * Returns all scheduled events of the given name and parameter list in
     * increasing scheduled order.
     * @param eventName Name of the desired SimEvent
     * @param params Given parameter list
     * @return Array of all scheduled SimEvents of the given name
     */
    public SortedSet getScheduledEvents(String eventName, Object[] params) {
        SortedSet<SimEvent> simEvents = new TreeSet<SimEvent>();
        SortedSet<SimEvent> eventListCopy = getEventList();
        for (SimEvent nextEvent : eventListCopy) {
            if (nextEvent.getEventName().equals(eventName) &&
                    nextEvent.interruptParametersMatch(params)) {
                simEvents.add(nextEvent);
            }
        }
        return simEvents;
    }
    
    /**
     * @throws InvalidSchedulingException if argument &lt; current simTime
     * @throws InvalidSchedulingException if argument &gt; time of last scheduled event
     * @param simTime the desired simTime
     */
    public synchronized void setSimTime(double simTime) {
        if (simTime < this.simTime) {
            throw new InvalidSchedulingException("Attempt to reverse time!");
        }
        if (!eventList.isEmpty()) {
            SimEvent lastEvent = (SimEvent) eventList.last();
            if (simTime > lastEvent.getScheduledTime()) {
                throw new InvalidSchedulingException("Attempt to move time past a " +
                        "scheduled event: " + simTime + " > " + lastEvent.getScheduledTime());
            }
        }
        this.simTime = simTime;
    }
}
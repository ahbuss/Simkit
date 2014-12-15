package simkit;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/** <P>This class filters events by name.  A list of the names of
 * filtered events is kept, and selected events dispatched directly
 * to listeners to an instance of this class.
 * <P>If <CODE>allButThese</CODE> is false (default),
 * then any event whose name is on the list will be passed
 * through.  If <CODE>allButThese</CODE> is true, then every
 * event <I>except</I> the ones on the list is passed through.
 *
 * @version $Id$
 * @author ahbuss
 */
public class SimEventFilter extends simkit.BasicSimEntity {
    
    private boolean allButTheseEvents;
    
    /** List of event names to either be passed through
     * or not passed through.
     */    
    private Set<String> events;
    
    /** Construct an instance with the Collection's events filtered
     * @param ev Collection of event names
     */    
    public SimEventFilter(Collection<String> ev) {
        events = new LinkedHashSet<String>(ev);
        setAllButTheseEvents(false);
    }
    
    /** Construct an instance with the given array of events to
     * be filtered
     * @param names Array of event names to be filtered
     */    
    public SimEventFilter(String[] names) {
        this();
        for (int i = 0; i < names.length; ++i) {
            addFilteredEvent(names[i]);
        }
    }
    
    /** Construct an instance with the single event to be filtered
     * @param name A single event to be filtered
     */    
    public SimEventFilter(String name) {
        this();
        addFilteredEvent(name);
    }
    
    /** A convenience constructor to easily create a filter that
     * passes all events except for one.  Do this by invoking
     * the constructor with <CODE>true</CODE> as the second argument:
     * <PRE>new SimEventFilter("Foo", true)</PRE>
     * @param name Single Event name
     * @param allBut Whether events are passed or filtered
     */    
    public SimEventFilter(String name, boolean allBut) {
        this(name);
        setAllButTheseEvents(allBut);
    }
    
    public SimEventFilter() {
        this(new LinkedHashSet<String>());
    }
    
    /** Add an event to be filtered
     * @param eventName The name of the event to be filtered
     */    
    public void addFilteredEvent(String eventName) {
        events.add(eventName);
    }
    
    /** Removes a filtered event
     * @param eventName The name of the venet to be removed from the filter
     */    
    public void removeFilteredEvent(String eventName) {
        events.remove(eventName);
    }
    
    /**
     * @param b whether the events will be passed or all but the events
     * will be passed
     */    
    public void setAllButTheseEvents(boolean b) { allButTheseEvents = b; }
    
    /**
     * @return Whether events are passed or all but events are passed.
     */    
    public boolean isAllButTheseEvents() { return allButTheseEvents; }
    
    /** Does nothing, since no events will be scheduled
     * @param simEvent SimEvent from Event List
     */    
    public void handleSimEvent(simkit.SimEvent simEvent) {
    }
    
    /** <p>If passing through events and event is on list, send to listeners.
     * <p>Otherwise, if not on list, sent to listeners
     * @param simEvent Heard SimEvent
     */    
    public void processSimEvent(simkit.SimEvent simEvent) {
        if (!isAllButTheseEvents() && events.contains(simEvent.getEventName())) {
            notifyListeners(simEvent);
        }
        else if (isAllButTheseEvents() && !events.contains(simEvent.getEventName())) {
            notifyListeners(simEvent);
        }
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        if (!events.isEmpty()) {
            buf.append(System.getProperty("line.separator"));
            buf.append('\t');
            buf.append("[Filtered Events]");
        }
        for (Iterator i = events.iterator(); i.hasNext(); ) {
            Object next = i.next();
            if (next instanceof java.lang.String) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(next);
            }
        }
        return buf.toString();
    }    
}

package simkit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>A generic counter for SimEvents that occur during a simulation run.
 * As events are heard, a Map is built up that keeps a count for each
 * type of event by name.  Only the name matters - events with the same
 * name but different signatures are treated as the same event.  Events of
 * the same name from different objects are also treaded identically.
 * Internally, the Map stores the key as the name of the event and the value 
 * as an <CODE>int[]</CODE> array of length 1.  This avoids the immutability 
 * problem with Java's primitive wrapper classes, in which the value must be 
 * extracted and a new object created.</i>
 * <p>The counts are built up on the fly.  When a new event is heard, a new
 * entry is created in the Map.  If an old event is heard, the <CODE>int[]</CODE>
 * array is retreived from the Map and the the first (and only) value incremented.
 *
 * @version $Id$
 * @author  ahbuss
 */
public class EventCounter extends BasicSimEntity {
    
    protected Map eventCounts;
    
    /**
     * Instantiate the Map of event names and counts
     */
    public EventCounter() {
        eventCounts = new LinkedHashMap();
    }
    
    /**
     * Clear the entire map of all names and counts
     */    
    public void reset() {
        super.reset();
        eventCounts.clear();
    }
    
    /**
     * Does nothing, since this will never schedule any events
     */    
    public void handleSimEvent(simkit.SimEvent simEvent) {
    }
    
    /**
     * If a non-Run event is heard that has been heard before,
     * increment its count.  If the first time an event is
     * heard, create an entry for it.
     */    
    public void processSimEvent(simkit.SimEvent simEvent) {
        String eventName = simEvent.getEventName();
        if (!eventName.equals("Run")) {
            
            int[] count = (int[]) eventCounts.get(eventName);
            if (count == null) {
                count = new int[1];
                eventCounts.put(eventName, count);
            }
            count[0] = count[0] + 1;
            firePropertyChange("number" + eventName, count[0] - 1, count[0]);
        }
    }
    
    /**
     *
     * @return Copy of the entire Map of event names and counts
     */    
    public Map getEventCounts() { return new LinkedHashMap(eventCounts); }
    
    /**
     *
     * @return Array of all events that have been heard so far
     */    
    public String[] getHeardEvents() {
        return (String[]) eventCounts.keySet().toArray(new String[0]);
    }
    
    /**
     * If this event has not been heard, returns -1.
     * @param eventName Name of event count is desired for
     * @return event count for name, if exists.
     */    
    public int getEventCount(String eventName) {
        int[] count = (int[]) eventCounts.get(eventName);
        return count != null ? count[0] : -1;
    }
}

package simkit;

import simkit.BasicEventList;

/**
 * Objects that need a reference to the event list can implement
 * this interface to indicate that fact to clients that test for
 * this requirement/capability.
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public interface EventListAware {
    public void setEventListID(int id);
    public void setEventList(BasicEventList eventList);
    public int getEventListID();
    public BasicEventList getEventList();
}

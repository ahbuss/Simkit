
package simkit;

import java.util.SortedSet;
import java.util.Arrays;
import java.util.List;

/**
* Used to gain access to protected things for testing.
*
* Renamed and moved from JDAFS project at Id: simkit.Helper.java 1544
* 
* @version Copied from DS at version 1.1
* @version $Id$
* @author John Ruck (Rolands and Associates Corporation 11/3/05)
**/
public class SimkitTestingHelper {

    public static void setSimTime(double time) {

        // this will fail if the event list is not a simkit.EventList
        // but this helper class is only intended to work with EventList
        // so an exception here is a flaw in the testin code.
        EventList el = (EventList)Schedule.getDefaultEventList();
        el.simTime = time;
    }

    public static SortedSet<SimEvent> getEventSet() {
        // this will fail if the event list is not a simkit.EventList
        // but this helper class is only intended to work with EventList
        // so an exception here is a flaw in the testin code.
        EventList el = (EventList)Schedule.getDefaultEventList();
        return el.eventList;
    }

    public static List<SimEventListener> getSimEventListenersAsList(SimEventSource source) {
        return Arrays.asList(source.getSimEventListeners());
    }
}

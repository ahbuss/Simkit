
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
* @version $Id: Helper.java 1544 2007-07-11 19:20:32Z jlruck $
* @author John Ruck (Rolands and Associates Corporation 11/3/05)
**/
public class SimkitTestingHelper {

    public static void setSimTime(double time) {
        Schedule.getDefaultEventList().simTime = time;
    }

    public static SortedSet<SimEvent> getEventSet() {
        return Schedule.getDefaultEventList().eventList;
    }

    public static List<SimEventListener> getSimEventListenersAsList(SimEventSource source) {
        return Arrays.asList(source.getSimEventListeners());
    }
}

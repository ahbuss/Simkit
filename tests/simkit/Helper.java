
package simkit;

import java.util.SortedSet;
/**
* Used to access protected things for testing.
*
* @author John Ruck (Rolands and Associates Corporation)
* @version $Id$
**/
public class Helper {

/**
* Sets the simulation time.
**/
    public static void setSimTime(double time) {
        Schedule.getDefaultEventList().simTime = time;
    }

    public static SortedSet getEventSet() {
        return Schedule.getDefaultEventList().eventList;
    }
}

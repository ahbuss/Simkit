package simkit;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Schedule maintains the simulation schedule and
 * manages SimEvents.
 *
 * @author K. A. Stork
 * @author Arnold Buss
 * @version $Id$
 *
 **/
public class Schedule  {
    
    private static int nextEventListID;
    
    private static EventList defaultEventList;
    
    private static Map allEventLists;
    
    private static DecimalFormat form;
    
    static {
        nextEventListID = 0;
        allEventLists = new HashMap();
        defaultEventList = getEventList(0);
        setDecimalFormat("0.0000");
    }
    
    /**
     * Only subclass Schedule by augmenting the simkit package.
     **/
    Schedule() {}

/**
* Causes a list of the pending events (and other 
* information) to be dumped to the output stream prior
* to processing each event when set to true.
**/    
    public static void setVerbose(boolean v) { defaultEventList.setVerbose(v); }

/**
* Causes a list of the pending events (and other 
* information) to be dumped to the output stream prior
* to processing each event when set to true.
**/    
    public static boolean isVerbose() { return defaultEventList.isVerbose(); }
    
/**
* Sets single step mode.  After each event is executed, the user must
* enter something at the console.<BR>
* 's' will stop the simulation.<BR>
* 'g' will take the simulation out of single-step and continue.<BR>
* 'f' will take the simulation out of single-step, set verbose to false, and continue.<BR>
* Return will cause the simulation to step to the next event.
* @param step true if single-step mode, false otherwise.
**/
    public static final void setSingleStep(boolean step) {
        defaultEventList.setSingleStep(step);
    }

/**
* If true, then Schedule is running in single-step mode and will
* pause prior to processing each event.
*/
    public static boolean isSingleStep() { return defaultEventList.isSingleStep(); }
    
	public static boolean isPauseAfterEachEvent() { return defaultEventList.isPauseAfterEachEvent(); }

	public static void setPauseAfterEachEvent(boolean b) { defaultEventList.setPauseAfterEachEvent(b);
	}
/**
* True when a simulation is currently running.
**/
    public static boolean isRunning() {return defaultEventList.isRunning();}
    
    /**
     * Clears the event list and starts time at 0.0.
     **/
    public static void reset() {
        defaultEventList.reset();
    }
    
    /**
     *  Starts or resumes simulation.  This method implements the fundamental discrete
     *  event simulation algorithm.  While the Event List is not empty, simTime is
     *  advanced to the time of the soonest event, which is then processed.  If the
     *  Event List becomes empty, then the simulation stops and the method returns.
     **/
    public static void startSimulation() {
        defaultEventList.startSimulation();
    }
    
/**
* Pause the simulation, which can be resumed with either resumeSimulation()
* or startSimulation()
**/
    public static void pause() {
        defaultEventList.pause();
    }
    
/**
* Wait for user input before continuing. <P>
* 's' will stop the simulation.<BR>
* 'g' will take the simulation out of single-step and continue.<BR>
* 'f' will take the simulation out of single-step, set verbose to false, and continue.<BR>
* Return will cause the simulation to step to the next event.
**/
    private static void step() {
        defaultEventList.step();
    }
  
/**
* Will cause the simulation to stop when it reaches the given
* simulation time. Any previously set stop time or stop on event is cleared.
**/  
    public static void stopAtTime(double atTime) {
        defaultEventList.stopAtTime(atTime);
    }
    
/**
* Disables both stop at time and stop on event.
**/
    public static void setUserDefinedStop() {
        defaultEventList.setUserDefinedStop();
    }
    
/**
* Causes the simulation to stop after the given event (which takes no arguments) has been processed the
* given number of times. Cancels any previous stopOnEvent or stopAtTime.
* @throws IllegalArgumentException if the number of events is negative.
**/
    public static void stopOnEvent(String eventName, int number) {
        defaultEventList.stopOnEvent(eventName, new Class[0], number);
    }
    
/**
* Causes the simulation to stop after the given event with the given signature
* has been processed the
* given number of times. Cancels any previous stopOnEvent, but does not cancel stopAtTime.
* @throws IllegalArgumentException if the number of events is negative.
**/
    public static void stopOnEvent(String eventName, Class[] eventSignature, int number) {
        defaultEventList.stopOnEvent(eventName, eventSignature, number);
    }
    
/**
* Stops the simulation, which cannot be resumed.
**/
    public static synchronized void stopSimulation() {
        defaultEventList.stopSimulation();
    }
    
    /**
     *  Returns currently executing event; null if simulation is not currently running.
     **/
    public static synchronized SimEvent getCurrentEvent() {
        return defaultEventList.getCurrentSimEvent();
    }
    
    public static double getSimTime() {
        return defaultEventList.getSimTime();
    }
    
    public static String getSimTimeStr() {
        return form.format(getSimTime());
    }

/**
* If set to true causes the owner of the event to be included
* whenever the event list is printed. Used by dump() and getEventListAsString()
*/
//TODO
    public static void setEventSourceVerbose(boolean v) {
    }
    
/**
* Adds the entity to the list of entities with "Run" events.
 * Note: addRerun() no longer schedules the Run event - that is
 * supposed to happen when Schedule.reset() is invoked, as it <i>should</i>
 * be, before each simulation run.
 * schedules the Run event at the current time.
**/
    public static void addRerun(SimEntity se) {
        defaultEventList.addRerun(se);
    }

/**
* Removes the SimEntity from the list of entities with Run events.
* Note it does not interrupt its Run event.
**/
    public static void removeRerun(SimEntity se) {defaultEventList.removeRerun(se);}

/**
* Clears the list of SimEntities with Run events.
**/
    public static void clearRerun() {defaultEventList.clearRerun();}

/**
* Returns a copy of the list of SimEntities with Run events.
**/
    public static Set getReruns() {
        return defaultEventList.getRerun();
    }
    
    /**
     *  When dumping event list, ignore this event.
     *  @param ignoredEventName The name of the event to be ignored
     **/
    public static void addIgnoreOnDump(String ignoredEventName) {
        defaultEventList.addIgnoreOnDump(ignoredEventName);
    }
    
    /**
     *  Stop ignoring this event on dump().
     *  @param ignoredEventName The event that was previously ignored but now is not.
     **/
    public static void removeIgnoreOnDump(String ignoredEventName) {
        defaultEventList.removeIgnoreOnDump(ignoredEventName);
    }
    
/**
* It true, causes Schedule to print additional debug/trace information.
**/
//TODO
    public static void setReallyVerbose(boolean rv) {
    }

/**
* It true, causes Schedule to print additional debug/trace information.
**/
//TODO
    public static boolean isReallyVerbose() {return false;}

    public static void setDecimalFormat(String format) {
        form = new DecimalFormat(format);
    }
    
    public static int addNewEventList() {
        Integer key = null;
        do {
            key = new Integer(++nextEventListID);
        } while (allEventLists.containsKey(key));
        EventList newEventList = new EventList(nextEventListID);
        allEventLists.put(key, newEventList);
        return key.intValue();
    }
    
    public static EventList getEventList(int index) {
        EventList eventList = (EventList) allEventLists.get(new Integer(index));
        if (eventList == null) {
            eventList = new EventList(index);
            allEventLists.put(new Integer(index), eventList);
        }
        return eventList;
    }
    
    public static EventList getDefaultEventList() {
        return defaultEventList;
    }
    
    public static void setDefaultEventList(EventList eventList) {
        defaultEventList = eventList;
    }
    
    public static void resetToOriginalEventList() {
        setDefaultEventList(getEventList(0));
    }
    
    
    
} // class Schedule

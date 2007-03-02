package simkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Schedule maintains the various <CODE>EventList</CODE> instances
 * and provides a universally available way to interact
 * with a single default instance of an <CODE>EventList</CODE>.
 *
 * @author K. A. Stork
 * @author Arnold Buss
 * @version $Id$
 *
 **/
public class Schedule  {
    
    private static int nextEventListID;
    
    private static EventList defaultEventList;
    
    private static Map<Integer, EventList> allEventLists;
    
    private static DecimalFormat form;
    
    static {
        nextEventListID = 0;
        allEventLists = new LinkedHashMap<Integer, EventList>();
        defaultEventList = getEventList(0);
        setDecimalFormat("0.0000");
    }
    
    /**
     * Only subclass Schedule by augmenting the simkit package.
     **/
    Schedule() {}

    /** Causes a list of the pending events (and other
     * information) to be dumped to the output stream prior
     * to processing each event when set to true.
     * @param v Whether this instance is verbose
     */    
    public static void setVerbose(boolean v) { defaultEventList.setVerbose(v); }

    /** Causes a list of the pending events (and other
     * information) to be dumped to the output stream prior
     * to processing each event when set to true.
     * @return Whether this instance is verbose
     */    
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

    /** If true, then Schedule is running in single-step mode and will
     * pause prior to processing each event.
     * @return Whether this instance is in single-step mode
     */
    public static boolean isSingleStep() { return defaultEventList.isSingleStep(); }
    
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
 * Same as pause()
 * @deprecated use Schedule.pause() instead
 */
    public static void pauseSimulation() {
        pause();
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
  
    /** Will cause the simulation to stop when it reaches the given
     * simulation time. Any previously set stop time or stop on event is cleared.
     * @param atTime Time to stop simulation run
     */  
    public static void stopAtTime(double atTime) {
        defaultEventList.stopAtTime(atTime);
    }
    
/**
* Disables both stop at time and stop on event.
**/
    public static void setUserDefinedStop() {
        defaultEventList.setUserDefinedStop();
    }
    
    /** Causes the simulation to stop after the given event with the given 
     * signature has been processed the given number of times. Overrides any 
     * previous stopOnEvent, but does not cancel stopAtTime.
     * @param numberEvents Number of events until stopping
     * @param eventName Name of event to stop on
     * @param eventSignature Signature of stopping event
     */
    public static void stopOnEvent(int numberEvents, String eventName,
            Class... eventSignature) {
        defaultEventList.stopOnEvent(numberEvents, eventName, eventSignature);
    }
    
    /**
     * Causes the simulation to stop after the given event (which takes no arguments) has been processed the
     * given numberEvents of times. Cancels any previous stopOnEvent or stopAtTime.
     * 
     * @param eventName Name of stopping event
     * @param numberEvents Number of events until stop
     * @deprecated Use stopOnEvent(int, String, Class...) instead
     */
    public static void stopOnEvent(String eventName, int numberEvents) {
        defaultEventList.stopOnEvent(numberEvents, eventName);
    }
    
    /**
     * @deprecated Use stopOnEvent(int, String, Class...) instead
    */
    public static void stopOnEvent(String eventName, Class[] eventSignature, int numberEvents) {
        defaultEventList.stopOnEvent(numberEvents, eventName, eventSignature);
    }
    
    /**
     * Stops the simulation and clears the event list.  The current replication
     * therefore cannot be resumed, but calling reset() followed by 
     * startSimulation() will start a new run to start with everything 
     * re-initialized.
     **/
    public static synchronized void stopSimulation() {
        defaultEventList.stopSimulation();
    }
    
    /**  
     * Returns currently executing event; null if simulation is not currently 
     * running.
     * @return SimEvent currently being processed
     */
    public static synchronized SimEvent getCurrentEvent() {
        return defaultEventList.getCurrentSimEvent();
    }
    
    /** For default event list
     * @return Current simulation time
     */    
    public static double getSimTime() {
        return defaultEventList.getSimTime();
    }
    
    /** For default event list
     * @return simTime as a String
     */    
    public static String getSimTimeStr() {
        return form.format(getSimTime());
    }

    /** If set to true causes the owner of the event to be included
     * whenever the event list is printed. Used by dump() and getEventListAsString()
     * @param b whether event sources will be in dump
     */
    public static void setEventSourceVerbose(boolean b) {
        defaultEventList.setPrintEventSources(b);
    }
    
    /** Adds the entity to the list of entities with "Run" events.
     * Note: addRerun() no longer schedules the Run event - that is
     * supposed to happen when Schedule.reset() is invoked, as it <i>should</i>
     * be, before each simulation run.
     * schedules the Run event at the current time.
     * (For default event list)
     * @param se SimEntity to be added to reRun list
     */
    public static void addRerun(SimEntity se) {
        defaultEventList.addRerun(se);
    }

    /** Removes the SimEntity from the list of entities with Run events.
     * Note it does not interrupt its Run event.
     * @param se SimEntity to be removed
     */
    public static void removeRerun(SimEntity se) {defaultEventList.removeRerun(se);}

/**
* Clears the list of SimEntities with Run events.
**/
    public static void clearRerun() {defaultEventList.clearRerun();}

    /** Returns a copy of the list of SimEntities with Run events.
     * @return copy of reRuns for default event list
     */
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
    
    /** It true, causes Schedule to print additional debug/trace information.
     * @param b whether reallyVerbose is to be set
     */
    public static void setReallyVerbose(boolean b) {
        defaultEventList.setReallyVerbose(b);
    }

    /** It true, causes Schedule to print additional debug/trace information.
     * @return whether reallyVerbose is set
     */
    public static boolean isReallyVerbose() {return defaultEventList.isReallyVerbose();}

    /**
     * @param format DecimalFormat string for simTimes
     */    
    public static void setDecimalFormat(String format) {
        form = new DecimalFormat(format);
    }
    
    /** Instantiate a new Event list and return its id.
     * @return id of new event list
     */    
    public static int addNewEventList() {
//        Integer key = getNextAvailableID();
//        EventList newEventList = new EventList(nextEventListID);
//        allEventLists.put(key, newEventList);
//        return key.intValue();
        return addNewEventList(simkit.EventList.class);
    }
    
    /** Instantiates a new EventList subclass.  Assumes that the
     * constructor has signature <CODE>(int)</CODE>
     * @param clazz The class of the EventList to be added
     * @return The id number of the new EventList instance
     */    
    public static int addNewEventList(Class<?> clazz) {
        Integer key = getNextAvailableID();
        try {
            Constructor construct = clazz.getConstructor( new Class<?>[] { int.class } );
            EventList newEventList = (EventList) construct.newInstance(new Object[] { key });
            allEventLists.put(key, newEventList);
        } 
        catch (NoSuchMethodException e) { 
            throw new RuntimeException(e.getMessage()); }
        catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage()); }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage()); }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException().getMessage()); }
        return key.intValue();
    }
    
    /** Search event lists for an unused id.
     * @return Next available id as Integer
     */    
    protected static Integer getNextAvailableID() {
        Integer key = null;
        do {
            key = new Integer(++nextEventListID);
        } while (allEventLists.containsKey(key));    
        return key;
    }
    
    /** Will return an existing EventList, if one with that
     * id exists.  Otherwise, will create a new one and
     * return a reference to it.
     * @param index id of desired EventList instance
     * @return EventList instance
     */    
    public static EventList getEventList(int index) {
        EventList eventList = allEventLists.get(index);
        if (eventList == null) {
            eventList = new EventList(index);
            allEventLists.put(new Integer(index), eventList);
        }
        return eventList;
    }
    
    /**
     * @return Referenmce to current default event list
     */    
    public static EventList getDefaultEventList() {
        return defaultEventList;
    }
    
    /** If not on master list, will add it with the
     * next available id.
     * @param eventList Desired EventList instance for default
     */    
    public static void setDefaultEventList(EventList eventList) {
        defaultEventList = eventList;
        if (!allEventLists.containsValue(eventList)) {
            allEventLists.put(getNextAvailableID(), eventList);
        }
    }
    
    /** Resets defaultEvent list to the original one. */    
    public static void resetToOriginalEventList() {
        setDefaultEventList(getEventList(0));
    }
    
    /** If true, then only one event will be processed.
     * A call to <CODE>startSimulation()</CODE> is required
     * to get going again.
     * @param b Whether default simulation will pause after each event
     */    
    public static void setPauseAfterEachEvent(boolean b ) {
        defaultEventList.setPauseAfterEachEvent(b);
    }
    
    /**
     * @return Whether default EventList is in this mode
     */    
    public static boolean getPauseAfterEachEvent() {
        return defaultEventList.isPauseAfterEachEvent();
    }
    
    /** defaultEventList is now completely pristine. */    
    public static void coldReset() {
        defaultEventList.coldReset();
    }
    
    /**
     * @deprecated Use Schedule.stopAtTime(double) instead
     * @param stopTime time to stop simulation
     */    
    public static void stopOnTime(double stopTime) {
        stopAtTime(stopTime);
    }
} // class Schedule

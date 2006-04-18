package simkit;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/** <P>Replacement for original static <CODE>Schedule</CODE>
 * implementation of event list.  Now multiple instances
 * of <CODE>EventList</CODE> may exist so that the
 * same virtual machine can be running multiple
 * independent simulations simultaneously.</P>
 *
 * <P>The motivation for switching from the single
 * event list (static) is twofold: (1) Web services
 * typically run in a single virtual machine, and
 * a simulation web service needs to be able to
 * run multiple simulations; (2) The simulation launcher
 * application also could be capable of running multiple
 * simulations in the same virtual machine.  This approach
 * also helps protect from rogue models that do bad things
 * to the event list, affecting other running models.</P>
 *
 * <P>Unlike the previous <CODE>Schedule</CODE>, separate
 * references to input and output objects are not maintained.
 * Instead, "dumping" and input is vis <CODE>System.out</CODE>
 * and <CODE>System.in</CODE>.  If it is desired that
 * i/o comes from different sources, then change the
 * default streams via <CODE>System.setOut()</CODE> and
 * <CODE>System.setIn()</CODE>.
 * @version $Id$
 * @author Arnold Buss
 */

public class EventList {
    
/**
 * Holds the pending events.
 */
    private SortedSet eventList;
    
/**
 * The current simulation time.
 */
    protected double simTime;
    
/**
 * The event currently being handled.
 */
    private SimEvent currentSimEvent;
    
/**
 * Holds the number of times each type of event has been processed.
 */
    private HashMap eventCounts;
    
/**
 * If true causes the event list to be printed prior
 * to processing each event.
 */
    private boolean verbose;
    
/**
 * Causes additional debug/trace information to be printed.
**/
    private boolean reallyVerbose;

/**
 * If true the simulation will stop prior to each event and 
 * wait for user input.
 */
    private boolean singleStep;
    
/**
* Causes the owner of the event to be included in 
* printouts of the event list.
**/
    private boolean dumpEventSources;
    
/**
 * Holds a list of entities with Run events.
 * Note: It is up to the SimEntity to add itself. (This is
 * implemented in BasicSimEntity.)
 */
    private SortedSet reRun;
    
/**
 * The name of the event to stop on after it
 * has been processed numberEvents times.
 */
    private String stopEventName;
    
/**
 * The number of times to process the stop event
 * prior to stopping the simulation.
 */
    private int numberStopEvents;
    
/**
* If true, stop the simulation after the stop event
* has been processed numberEvents times.
**/
    private boolean stopOnEvent;
    
/**
 * If true, stop the simulation at endingTime.
 */
    private boolean stopAtTime;
    
/**
* The time to stop the simulation if stopOnTime is true.
**/
    private double stopTime;
    
/**
* True if the simulation is currently running.
**/
    private boolean running;

/**
 * Instance of <CODE>Stop</CODE> if <CODE>stopAtTime</CODE> is set.
 */
    private SimEntity stopInstance;
    
/**
 * List of events that are not dumped in Event List
 */
    private HashSet ignoreOnDump;
    
/**
 * The identifying number of this <CODE>EventList</CODE> instance.  It is 
 * the responsibility of <CODE>Schedule</CODE> to ensure uniqueness across
 * <CODE>EventList</CODE> instances.
 */
    private int id;
    
/**
 * Holds the default format for outputting times.
 */
    private DecimalFormat form;
    
    private boolean printEventSources;
    
    private double precision;
    
/**
 * If true, will process one event and wait for another call to 
 * <code>startSimulation()</CODE>
 */
    private boolean pauseAfterEachEvent;
    
/**
 * Instantiate an <CODE>EventList</CODE> with given id.
 * @param id The id number for this <CODE>EventGraph</CODE> instance
 */
    public EventList(int id) {
        eventList = Collections.synchronizedSortedSet(new TreeSet(new SimEventComp()));
        simTime = 0.0;
        running = false;
        eventCounts = new LinkedHashMap();
        reRun = Collections.synchronizedSortedSet(new TreeSet(new SimEntityComparator()));
        ignoreOnDump = new LinkedHashSet();
        this.id = id;
        setFormat("0.0000");
        setSimEventPrecision(0.0);
    }
    
    /**
     * @return The identifying number for this
     * <CODE>EventGraph</CODE> instance
     */    
    public int getID() { return id; }
    
    /**
     * @return The current simulation time
     */    
    public double getSimTime() { return simTime; }
    
    /** If true, then contents of the event list are
     * printed after each event is processed.
     * @param b whether verbose mode is on
     */    
    public void setVerbose(boolean b) { verbose = b; }
    
    /**
     * @return Whether verbose mode is on
     */    
    public boolean isVerbose() { return verbose; }
    
    /** For debugging, gives more detailed output
     * @param b Whether reallyVerbose is on
     */    
    public void setReallyVerbose(boolean b) { reallyVerbose = b; }
    
    /**
     * @return Whether reallyVerbose is true
     */    
    public boolean isReallyVerbose() { return reallyVerbose; }
    
    /**
     * @param b Whether the event source is printed on a dump()
     */    
    public void setPrintEventSources(boolean b) { printEventSources = b; }
    
    /**
     * @return true if event sources are printed on a dump()
     */    
    public boolean isPrintEventSources() { return printEventSources; }
    
    /**
     * @param b Whether single step mode is on
     */    
    public void setSingleStep(boolean b) {
        singleStep = b;
        setVerbose(singleStep);
        if (isVerbose()) {
            setPauseAfterEachEvent(false);
        }
    }
    
    /**
     * @return Whether singleStep mode is on
     */    
    public boolean isSingleStep() { return singleStep; }
    
    /** If true, then the SimEntity toString() is dumped
     * with verbose mode for each event.
     * @param b Whether this mode is on
     */    
    public void setDumpEventSources(boolean b) { dumpEventSources = b; }
    
    /**
     * @return Whether this mode is on
     */    
    public boolean isDumpEventSources() { return dumpEventSources; }
    
    /**
     * @return Whether this mode is on
     */    
    public boolean isStopOnEvent() { return stopOnEvent; }
    
    /**
     * @param format String for <CODE>DecimalFormat</CODE> of times
     */    
    public void setFormat(String format) {
        form = new DecimalFormat(format);
    }
    
    /**
     * @return Number of events before simulation ends
     */    
    public int getNumberStopEvents() { return numberStopEvents; }
    
    /**
     * @return Whether this mode is on
     */    
    public boolean isStopAtTime() { return stopAtTime; }
    
    /**
     * @return Time simulation will end, if <CODE>stopAtTime</CODE>
     * is set
     */    
    public double getStopTime() { return stopTime; }
    
    /**
     * @return Whether simulation is currently running
     */    
    public boolean isRunning() { return running; }
    
    /**
     * @return Name of stop event
     */    
    public String getStopEventName() { return stopEventName; }

    /**
    * Returns true if there are no Events scheduled.
    **/
    public boolean isFinished() {return eventList.isEmpty();}
    
    /**
     * @return Current SimEvent being processed
     */    
    public synchronized SimEvent getCurrentSimEvent() { return currentSimEvent; }
    
    /** If true, then  simulation will pausde after each
     * event and resume only on another call to
     * <CODE>startSimulation()</CODE>
     * @param b Whether this mode is on
     */    
    public void setPauseAfterEachEvent(boolean b) { 
        pauseAfterEachEvent = b;
        if (isPauseAfterEachEvent()) {
            setSingleStep(false);
        }
    }
    
    /**
     * @return Whether this mode is on
     */    
    public boolean isPauseAfterEachEvent() { return pauseAfterEachEvent; }
    
    /** Performs the following:
     * <UL>
     *    <LI>Empties the event list</LI>
     *    <LI>Sets simulation time to 0.0</LI>
     *    <LI>Invokes <CODE>reset()</CODE> on each persistent
     *        <CODE>SimEntity</CODE> in <CODE>reRun</CODE></LI>
     *    <LI>Removes all listeners for every transient
     *        <CODE>SimEntity</CODE> in reRun.
     * </UL>
     */    
    public void reset() {
        if (isReallyVerbose()) {
            System.out.println(getSimTime() + ": reset() called");
        }
        clearEventList();
        currentSimEvent = null;
        simTime = 0.0;
        SimEvent.resetID();
        synchronized(reRun) {
            for (Iterator i = reRun.iterator(); i.hasNext(); ) {
                SimEntity simEntity = (SimEntity) i.next();
                if (isReallyVerbose()) {
                    System.out.println(getSimTime() + ": Checking rerun " + 
                        simEntity + " [rerunnable?] " + 
                        simEntity.isReRunnable());
                }
                if (simEntity.isPersistant()) {
                    simEntity.reset();
                    if (simEntity.isReRunnable()) {
                        simEntity.waitDelay("Run", 0.0, null,
                        Double.POSITIVE_INFINITY);
                    }
                }
                else {
                    removeListeners(simEntity);
                    i.remove();
                }
            }
        }
        if (isStopAtTime()) {
            stopAtTime(getStopTime());
        }
    }
    
    /** Removes all SimEventListeners and PropertyChangelisteners
     * from the given <CODE>SimEntity</CODE>
     * @param se The <CODE>SimEntity</CODE> for which listeners are
     * to be removed
     */    
    protected void removeListeners(SimEntity se) {
        SimEventListener[] seListener = se.getSimEventListeners();
        for (int i = 0; i < seListener.length; ++i) {
            se.removeSimEventListener(seListener[i]);
        }
        PropertyChangeListener[] pcListener = se.getPropertyChangeListeners();
        for (int i = 0; i < pcListener.length; ++i) {
            se.removePropertyChangeListener(pcListener[i]);
        }
    }
    
    /** Empties event list and returns each <CODE>SimEvent</CODE>
     * to the pool.
     */    
    protected void clearEventList() {
        eventList.clear();
        eventCounts.clear();
    }
    
    /** Removes all cancelled events from the front of the
     * Event List.  This should not be as necessary
     * as it once was.
     */    
    protected void clearDeadEvents() {
        while ( !eventList.isEmpty() ) {
            SimEvent simEvent = (SimEvent) eventList.first();
            if (!simEvent.isPending()) {
                eventList.remove(simEvent);
            }
            else {
                break;
            }
        }
    }
    
    /** Sets the stopAtTime mode to true, setting others to false.
     * @param time Time to stop simulation
     */    
    public void stopAtTime(double time) {
        stopAtTime = true;
        stopOnEvent = false;
        stopTime = time;
        if (stopInstance == null) {
            stopInstance = new Stop();
            stopInstance.setEventListID(getID());
        }
        else {
            stopInstance.interruptAll("Stop", new Object[0]);
        }
        stopInstance.waitDelay("Stop", getStopTime() - getSimTime(), null, Double.NEGATIVE_INFINITY);
    }
    
    /** Sets stopOnEvent to true and other modes false.
     * The simulation will end after the given number of
     * events have occurred.
     * @param eventName Name of event to top at
     * @param numberEvents Number of times stop event will occur
     */    
    public void stopOnEvent(String eventName, int numberEvents) {
        stopOnEvent(eventName, new Class[0], numberEvents);
    }
    
    /** Sets stopOnEvent to true and other modes false.
     * The simulation will end after the given number of
     * events with this signature have occurred.
     * @param eventName Name of stop event
     * @param signature Signature of stop event
     * @param numberEvents Number of events to occur
     */    
    public void stopOnEvent(String eventName, Class[] signature, int numberEvents) {
        if (numberEvents > 0) {
            stopOnEvent = true;
            stopAtTime = false;
            StringBuffer fullName = new StringBuffer("do" + eventName +"(");
            for (int i = 0; i < signature.length; ++i) {
                fullName.append(signature[i].getName());
                if (i < signature.length - 1) { fullName.append(','); }
            }
            fullName.append(')');
            stopEventName = fullName.toString();
            numberStopEvents = numberEvents;
        }
        else {
            throw new IllegalArgumentException("Need positive number of events: " 
                + numberEvents);
        }
    }
    
    /** Sets all stop modes to false.  This is used
     * if the user will terminate the run based on
     * other criteria.
     */    
    public void setUserDefinedStop() {
        stopAtTime = false;
        stopTime = Double.NaN;
        stopOnEvent = false;
        stopEventName = null;
        numberStopEvents = 0;
    }
    
    /** Place an event on the event list.
     * @param event Event to be scheduled
     * @throws InvalidSchedulingException If scheduled time is less than current simTime.
     */    
    public void scheduleEvent(SimEvent event) throws InvalidSchedulingException {
        if (event.getScheduledTime()  + precision < getSimTime() ) {
            throw new InvalidSchedulingException("Attempt to reverse time!: " +
            event.getScheduledTime() + " < " + getSimTime() );
        }
        else if (!Double.isNaN(event.getScheduledTime())) {
            boolean success = eventList.add(event);
            if (!success) {
                throw new InvalidSchedulingException("Problem adding event to " +
                "Event List: " + event);
            }
        }
        if (isReallyVerbose()) {
            System.out.println("\n" + getSimTime() + ": Event " + event + " Scheduled by " + 
                event.getSource());
            //dump();
        }
    }
    
    /** Starts event list algorithm.  While the event
     * list is not empty, advance time to next event
     * and handle that event.  An event is "handled"
     * by making a callback to its <CODE>handleSimEvent</CODE>
     * method.  Then have the owner of the event
     * notify its listeners of the event.
     */    
    public void startSimulation() {
        if (isRunning()) {
            throw new RuntimeException("Simulation already running!");
        }
        running = true;
        
        if (isVerbose()) { dump("Starting Simulation"); }
        
        if (isSingleStep()) { 
            System.err.println("Press [Enter] for next step; (s)top, (g)o or (f)inish");
        }
        
        while (!eventList.isEmpty() && isRunning()) {
            currentSimEvent = (SimEvent) eventList.first();
            eventList.remove(currentSimEvent);
            simTime = currentSimEvent.getScheduledTime();
            if (reallyVerbose) {
                System.out.println(simTime + ": Processing " + currentSimEvent + 
                    "  source=" + currentSimEvent.getSource());
            }
            
            if (currentSimEvent.isPending()) {
                updateEventCounts(currentSimEvent);
                SimEntity simEntity = (SimEntity) currentSimEvent.getSource();
                simEntity.handleSimEvent(currentSimEvent);
                simEntity.notifyListeners(currentSimEvent);
                
                if (isStopOnEvent()) { checkStopEvent(); }
                if (isSingleStep()) { step(); }
                if (isVerbose()) { dump(""); }
                if (isPauseAfterEachEvent()) { pause(); }
            }
            currentSimEvent = null;
        }
        running = false;
    }
    
    /** Processed events one at a time based on
     * user input from the console.
     * <UL>
     *    <LI> &lt;Enter&gt; advances by one event</LI>
     *    <LI> s stops the run</LI>
     *    <LI> f finishes the run in silent mode</LI>
     *    <LI> g finishes the run in verbose mode</LI>
     * </UL>
     */    
    public void step() {
        while(true) {
            try {
                char response = (char) System.in.read();
                if (  response == 's') { stopSimulation(); }
                if (  response == 'g') { setSingleStep(false); setVerbose(true); }
                if (  response == 'f') { setSingleStep(false); setVerbose(false);}
                if (  response == '\n') { return;}
            }
            catch (IOException e) { System.err.println(); e.printStackTrace(System.err); }
        }    
    }

    /** Dumps current event list to the <CODE>System.out</CODE>
     * @param reason Short message to add to dump
     */    
    public void dump(String reason) {
        System.out.println(getEventListAsString(reason));
    }
    
    /** Dump without an additional user message */    
    public void dump() {
        dump("");
    }
    
    /** Adds one to the number of this event that have occured.
     * Used for <CODE>stopOnEvent</CODE>
     * @param event event to update counts
     */    
    protected void updateEventCounts(SimEvent event) {
        int[] serial = (int[]) eventCounts.get(event.getFullMethodName());
        if (serial == null) {
            serial = new int[] { 0 };
            eventCounts.put(event.getFullMethodName(), serial);
        }
        synchronized(serial) {
            serial[0]++;
            event.setSerial(serial[0]);
        }
    }

    /** Stops simulation if the number of stop events
     * have occurred.
     */    
    protected void checkStopEvent() {
        if (isStopOnEvent() && currentSimEvent.getFullMethodName().equals(stopEventName) &&
                currentSimEvent.getSerial() >= numberStopEvents) {
            stopSimulation();
        }
    }
    
    /** Sets running to false, causing the event list
     * algorithm to pause after the current event finishes
     * processing.
     */    
    public void pause() {
        running = false;
        if (isVerbose()) {
            System.out.println("Simulation is paused");
        }
    }
    
    /** Clear event list and set running to false. */    
    public void stopSimulation() {
        clearEventList();
        running = false;
    }
    
    /** Cancel next event of given name (regardless of
     * signature) owned by the given SimEntity
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event to cancel
     */    
    public void interrupt(SimEntity simEntity, String eventName) {
        clearDeadEvents();
        synchronized(eventList) {
            for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                SimEvent event = (SimEvent) i.next();
                if ((event.getSource() == simEntity) &&
                    (event.getEventName().equals(eventName)) &&
                    (event.isPending())) {
                        if (reallyVerbose) {
                            System.out.println("\n" + getSimTime() 
                                                + ": Cancelling " + event); 
                        }
                        i.remove();
                        break;
                }
            }
        }
    }
    
    /** Cancel next event of given name matching the
     *  parameter list owned by the given SimEntity
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event to cancel
     * @param parameters edge parameters of cancelled event
     */    
    public void interrupt(SimEntity simEntity, String eventName,
            Object[] parameters) {
        clearDeadEvents();
        synchronized(eventList) {
            for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                SimEvent event = (SimEvent) i.next();
                if ((event.getSource() == simEntity) &&
                    (event.getEventName().equals(eventName)) &&
                    (event.interruptParametersMatch(parameters)) &&
                    (event.isPending())) {
                        if (reallyVerbose) {
                            System.out.println("\n" + getSimTime() 
                                                + ": Cancelling " + event); 
                        }
                        i.remove();
                        break;
                }
            }
        }
    }
    
    /** Cancel the next event of this SimEntity.
     * (Note: this is probably not very useful).
     * @param simEntity SimEntity to have event cancelled
     */    
    public void interruptAll(SimEntity simEntity) {
        clearDeadEvents();
        synchronized(eventList) {
            for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                SimEvent simEvent = (SimEvent) i.next();
                if (simEvent.getSource() == simEntity) {
                    if (reallyVerbose) {
                        System.out.println("\n" + getSimTime() 
                                            + ": Cancelling " + simEvent); 
                    }
                    i.remove();
                }
            }
        }
    }
    
    /** Cancel all events owned by this SimEntity that
     * have the given name, regardless of signature.
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event
     */    
    public void interruptAll(SimEntity simEntity, String eventName) {
        clearDeadEvents();
        synchronized(eventList) {
            for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                SimEvent simEvent = (SimEvent) i.next();
                if ((simEvent.getSource() == simEntity) &&
                    (simEvent.getEventName().equals(eventName)) ){
                    if (reallyVerbose) {
                        System.out.println("\n" + getSimTime() 
                                            + ": Cancelling " + simEvent); 
                    }
                    i.remove();
                }
            }
        }
    }
    
    /** Cancel all events owned by this SimEntity
     * of the givene name whos parameters exactly
     * match the given array.
     * @param simEntity SimEntity to have event cancelled
     * @param eventName Name of event to cancel
     * @param parameters edge parameters that must match
     */    
    public void interruptAll(SimEntity simEntity, String eventName,
        Object[] parameters) {
        clearDeadEvents();
        synchronized(eventList) {
            for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                SimEvent simEvent = (SimEvent) i.next();
                if ((simEvent.getSource() == simEntity) &&
                    (simEvent.getEventName().equals(eventName)) &&
                    (simEvent.interruptParametersMatch(parameters)) ){
                        if (reallyVerbose) {
                            System.out.println("\n" + getSimTime() 
                                                + ": Cancelling " + simEvent); 
                        }
                    i.remove();
                }
            }
        }
    }
    
    /** Add the SimEntity to the reRun list.  On <CODE>Schedule.reset</CODE>
     * the SimEntity's <CODE>reset()</CODE> method is invoked and
     * its Run event (if it has one) is scheduled at time 0.0.  This
     * happens only if the SimEntity is persistant.
     * @param simEntity SimEntity to be added as a reRun
     */    
    public void addRerun(SimEntity simEntity) {
        reRun.add(simEntity);
    }
    
    /** Remove the given SimEntity from the reRun list
     * @param simEntity SimEntity to be removed from reRun list
     */    
    public void removeRerun(SimEntity simEntity) {
        reRun.remove(simEntity);
    }
    
    /** Empty the reRun list */    
    public void clearRerun() {
        reRun.clear();
    }

    /** For debugging purposes - gets a copy of the
     * current reRun list.
     * @return Copy of reRun list
     */    
    public Set getRerun() {
        return new LinkedHashSet(reRun);
    }
    
    /** Events of this name will not be printed in verbose mode.
     * @param eventName Name of event to be ignored
     */    
    public void addIgnoreOnDump(String eventName) {
        ignoreOnDump.add(eventName);
    }
    
    /** Events of this name now <I>will</I>i> be
     * printed in verbose mode.
     * @param eventName Event Name
     */    
    public void removeIgnoreOnDump(String eventName) {
        ignoreOnDump.remove(eventName);
    }
    
    /** For debugging purposes - returns a copy of the ignored events
     * @return Copy of ignored events
     */    
    public Set getIgnoredEvents() {
        return new LinkedHashSet(ignoreOnDump);
    }

    /** Resets instance to pristine condition, as if it were
     * freshly instantiated.  All containers are emptied,
     * and various booleans are set to their default
     * values (typically <CODE>false</CODE>).
     */    
    public void coldReset() {
        stopSimulation();
        clearRerun();
        simTime = 0.0;
        ignoreOnDump.clear();
        setDumpEventSources(false);
        setUserDefinedStop();
        setVerbose(false);
        setReallyVerbose(false);
        setSingleStep(false);
        setPauseAfterEachEvent(false);
    }
    
    /** For debugging purposes - returns a String depicting the
     * current event and state of the event list.
     * @param reason User message to be appended to event list
     * @return String version of current event and event list
     */    
    public String getEventListAsString(String reason) {
        clearDeadEvents();
        StringBuffer buf = new StringBuffer();
        if (currentSimEvent != null) {
            buf.append("Time: ");
            buf.append(form.format(getSimTime()));
            buf.append("\tCurrentEvent: ");
            buf.append(currentSimEvent.paramString());
            buf.append(' ');
            buf.append('[');
            buf.append(currentSimEvent.getSerial());
            buf.append(']');
            buf.append(SimEntity.NL);
        }
        buf.append("** Event List ");
        buf.append(getID());
        buf.append(" -- ");
        buf.append(reason);
        buf.append(" **");

        buf.append(SimEntity.NL);
        if (eventList.isEmpty()) {
            buf.append("            << empty >>");
            buf.append(SimEntity.NL);
        }
        else {
            synchronized(eventList) {
                for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                    SimEvent simEvent = (SimEvent) i.next();
                    if (ignoreOnDump.contains(simEvent.getEventName())) {
                        continue;
                    }
                    if (simEvent.isPending()) {
                        buf.append(simEvent);
                        if (isPrintEventSources()) {
                            buf.append(' ');
                            buf.append('<');
                            buf.append(simEvent.getSource().getName());
                            buf.append('>');
                        }
                        buf.append(SimEntity.NL);
                    }
                }
            }
        }
        buf.append(" ** End of Event List -- ");
        buf.append(reason);
        buf.append(" **");
        buf.append(SimEntity.NL);
        return buf.toString();
    }
    
    public void setSimEventPrecision(double precision) {
        this.precision = precision;
        SortedSet temp = Collections.synchronizedSortedSet(new TreeSet(new SimEventComp(precision)));
        temp.addAll(eventList);
        eventList = temp;
    }
    
    /**
     * @return shallow copy of actual events.  To be used by subclasses only
     * for debugging purposes.
     */
    protected SortedSet getEventList() {
        return new TreeSet(eventList);
    }
    
}

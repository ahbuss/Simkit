package simkit;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

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
 * @version $Id$
 * @author Arnold Buss
 */

public class EventList implements BasicEventList {

    public static final Logger log = Logger.getLogger("simkit");
    
    public static final String _VERSION_ = "$Id$";
    
/**
* The default setting for fastInterrupts (TRUE).
*/
    boolean DEFAULT_FAST_INTERRUPTS = true;

    
/**
 * Holds the pending events.
 */
    protected SortedSet<SimEvent> eventList;
    
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
    private final HashMap<String, int[]> eventCounts;
    
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
    private final SortedSet<ReRunnable> reRun;
    
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
    private final HashSet<String> ignoreOnDump;
    
/**
 * The identifying number of this <CODE>EventList</CODE> instance.  It is 
 * the responsibility of <CODE>Schedule</CODE> to ensure uniqueness across
 * <CODE>EventList</CODE> instances.
 */
    private final int id;
    
    /**
     * PrintStream that dumps will be directed to.  
     */
    private PrintStream outputStream;
    
    /**
    * Holds the default format for outputting times.
    */
    private DecimalFormat form;
    
    private boolean printEventSources;
    
    private final double precision;
    
/**
 * If true, will process one event and wait for another call to 
 * <code>startSimulation()</CODE>
 */
    private boolean pauseAfterEachEvent;

/**
* If true, then pending SimEvents will be stored in a secondary hash table
* to make them easier to find when interrupting. For simulations that
* do not interrupt events, the added overhead from storing and removing
* events in the secondary table could add to run time.
**/
    private boolean fastInterrupts = false;
//even though the default is true, this needs to be set to false to 
//force setFastInterrupts to initialize correctly.

/**
* A Map a SimEntity to a SortedSet of its pending SimEvents.
**/
    protected Map<SimEventScheduler, SortedSet<SimEvent>> entityEventMap;

/**
* A Map from a SimEvent.getEventHash() (an Integer) to a SortedSet of pending events.
**/
    protected Map<Integer, SortedSet<SimEvent>> hashEventMap;

/**
* A count of the number of threads in startSimulation, reset(), and coldReset().
* The total number of Threads in these methods should be &le; 1. 
**/
    protected int entryCounter = 0;

/**
* An Object used to synchronize access to entryCounter.
**/
    protected Object entryCounterMutex = new Object();

/**
* If true, the event list will be cleared at the start of the next iteration
* of the simulation loop in startSimulation().
**/
    protected boolean stoppingSimulation = false;
    
/**
 * Instantiate an <CODE>EventList</CODE> with given id.
 * @param id The id number for this <CODE>EventGraph</CODE> instance
 */
    public EventList(int id) {
        eventList = Collections.synchronizedSortedSet(
                new TreeSet<SimEvent>());
        simTime = 0.0;
        running = false;
        eventCounts = new LinkedHashMap<>();
        reRun = Collections.synchronizedSortedSet(new TreeSet<ReRunnable>());
        ignoreOnDump = new LinkedHashSet<>();
        this.id = id;
        setFormat("0.0000");
        this.precision = 0.0; 
        setFastInterrupts(true);
        setOutputStream(System.out);
    }

    /**
     * Returns the number of events on the event list.
     *
     * @return the number of events on the event list.
     */
    public int getPendingEventCount() {
        return eventList.size();
    }

    @Override
    public int getID() { return id; }
    
    @Override
    public double getSimTime() { return simTime; }
    
    @Override
    public void setVerbose(boolean b) { verbose = b; }
    
    @Override
    public boolean isVerbose() { return verbose; }
    
    @Override
    public void setReallyVerbose(boolean b) { reallyVerbose = b; }
    
    @Override
    public boolean isReallyVerbose() { return reallyVerbose; }
    
    @Override
    public void setPrintEventSources(boolean b) { printEventSources = b; }

    @Override
    public boolean isPrintEventSources() { return printEventSources; }

    @Override
    public void setSingleStep(boolean b) {
        singleStep = b;
        setVerbose(singleStep);
        if (isVerbose()) {
            setPauseAfterEachEvent(false);
        }
    }
    
    @Override
    public boolean isSingleStep() { return singleStep; }

    @Override
    public void setDumpEventSources(boolean b) { dumpEventSources = b; }

    @Override
    public boolean isDumpEventSources() { return dumpEventSources; }

    @Override
    public boolean isStopOnEvent() { return stopOnEvent; }
    
    @Override
    public void setFormat(String format) {
        form = new DecimalFormat(format);
    }
    
    @Override
    public int getNumberStopEvents() { return numberStopEvents; }
    
    @Override
    public boolean isStopAtTime() { return stopAtTime; }
    
    @Override
    public double getStopTime() { return stopTime; }
    
    @Override
    public boolean isRunning() { return running; }
    
    @Override
    public String getStopEventName() { return stopEventName; }

    @Override
    public boolean isFinished() {return eventList.isEmpty();}
    
    @Override
    public SimEvent getCurrentSimEvent() { return currentSimEvent; }
    
    @Override
    public void setPauseAfterEachEvent(boolean b) { 
        pauseAfterEachEvent = b;
        if (isPauseAfterEachEvent()) {
            setSingleStep(false);
        }
    }
    
    /**
     * @return Whether this mode is on
     */    
    @Override
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
    @Override
    public void reset() {
        synchronized(entryCounterMutex) {
            if (entryCounter > 0) {
                entryCounter--;
                throw new SimkitConcurrencyException();
            }
            entryCounter++;
        }
        if (isReallyVerbose()) {
            log.log(Level.INFO, "{0}: reset() called", getSimTime());
        }
        clearEventList();
        running = false;
        currentSimEvent = null;
        simTime = 0.0;
        SimEvent.resetID();
        synchronized(reRun) {
            for (Iterator i = reRun.iterator(); i.hasNext(); ) {
                ReRunnable simEntity = (ReRunnable) i.next();
                if (isReallyVerbose()) {
                    log.log(Level.INFO, "{0}: Checking rerun {1} [rerunnable?] {2}", 
                            new Object[]{getSimTime(), simEntity, simEntity.isReRunnable()});
                }
                if (simEntity.isPersistant()) {
                    simEntity.reset();
                    if (simEntity.isReRunnable()) {
                        simEntity.waitDelay("Run", 0.0,
                        Priority.HIGHEST);
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
        synchronized(entryCounterMutex) {
            entryCounter--;
            if (entryCounter < 0) {
                entryCounter = 0;
            }
        }
    }
    
    /** Removes all SimEventListeners and PropertyChangelisteners
     * from the given <CODE>SimEntity</CODE>
     * @param se The <CODE>SimEntity</CODE> for which listeners are
     * to be removed
     */    
    protected void removeListeners(Object se) {
        // The listener patterns are not really part of the scheduling
        // contract, but were historically used here.  This is a stop-gap before
        // removing this concept from the scheduling mechanism altogether.
        
        if (se instanceof SimEventSource) {
            SimEventListener[] seListener = ((SimEventSource)se).getSimEventListeners();
            for (int i = 0; i < seListener.length; ++i) {
                ((SimEventSource)se).removeSimEventListener(seListener[i]);
            }
        }
        if(se instanceof PropertyChangeSource) {
            PropertyChangeListener[] pcListener = ((PropertyChangeSource)se).getPropertyChangeListeners();
            for (int i = 0; i < pcListener.length; ++i) {
                ((PropertyChangeSource)se).removePropertyChangeListener(pcListener[i]);
            }
        }
    }
    
    /** 
     * Empties event list.
     */    
    protected void clearEventList() {
        eventList.clear();
        eventCounts.clear();
        if (entityEventMap != null) {
            entityEventMap.clear();
        }
        if (hashEventMap != null) {
            hashEventMap.clear();
        }
    }
    
    /** Removes all cancelled events from the front of the
     * Event List.  This should not be as necessary
     * as it once was.
     * Should only be called from inside a block synchronized on "eventList"
     */    
    protected void clearDeadEvents() {
        while (!eventList.isEmpty()) {
            SimEvent simEvent = (SimEvent) eventList.first();
            if (!simEvent.isPending()) {
                eventList.remove(simEvent);
                if (fastInterrupts) {
                    removeFromEntityEventMap(simEvent);
                    removeFromHashEventMap(simEvent);
                }
            }
            else {
                break;
            }
        }
    }
    
    /** Sets the stopAtTime mode to true, setting others to false.
     * @param time Time to stop simulation
     */    
    @Override
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
        stopInstance.waitDelay("Stop", getStopTime() - getSimTime(), Priority.LOWEST);
    }
    
    @Override
    public void stopOnEvent(int numberEvents, String eventName, Class... signature) {
        if (numberEvents > 0) {
            stopOnEvent = true;
            stopAtTime = false;
            StringBuilder fullName = new StringBuilder("do" + eventName +"(");
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
    @Override
    public void setUserDefinedStop() {
        stopAtTime = false;
        stopTime = Double.NaN;
        stopOnEvent = false;
        stopEventName = null;
        numberStopEvents = 0;
    }

    @Override
    public void scheduleEvent(SimEvent event) throws InvalidSchedulingException {
        if (event.getScheduledTime()  + precision < getSimTime() ) {
            throw new InvalidSchedulingException("Attempt to reverse time!: " +
            event.getScheduledTime() + " < " + getSimTime() );
        }
        else if (!Double.isNaN(event.getScheduledTime()) && !Double.isInfinite(event.getScheduledTime())) {
            boolean success = eventList.add(event);
            if (!success) {
                throw new InvalidSchedulingException("Problem adding event to " +
                "Event List: " + event);
            }
            if (fastInterrupts) {
                addToEntityEventMap(event);
                addToHashEventMap(event);
            }
        }
        if (isReallyVerbose()) {
            log.info("\n" + getSimTime() + ": Event " + event + " Scheduled by " + 
                event.getSource());
            //dump();
        }
    }
    
    @Override
    public void startSimulation() {
        synchronized(entryCounterMutex) {
            if (entryCounter > 0) {
                entryCounter--;
                throw new SimkitConcurrencyException();
            }
            entryCounter++;
            if (isRunning()) {
                if (entryCounter > 0) {
                    entryCounter--;
                }
                throw new RuntimeException("Simulation already running!");
            }
        }
        running = true;
        
        if (isVerbose()) { dump("Starting Simulation"); }
        
        if (isSingleStep()) { 
            System.err.println("Press [Enter] for next step; (s)top, (g)o or (f)inish");
        }
        
        while (isRunning()) {
            synchronized(eventList) {
                if (stoppingSimulation) {
                    eventList.clear();
                    stoppingSimulation = false;
                }
                if (eventList.isEmpty()) {
                    break;
                } 
                currentSimEvent = (SimEvent) eventList.first();
                eventList.remove(currentSimEvent);
            }
            if (fastInterrupts) {
                removeFromEntityEventMap(currentSimEvent);
                removeFromHashEventMap(currentSimEvent);
            }
            simTime = currentSimEvent.getScheduledTime();
            if (reallyVerbose) {
                log.log(Level.INFO, "{0}: Processing {1}  source={2}", 
                        new Object[]{simTime, currentSimEvent, currentSimEvent.getSource()});
            }
            
            if (currentSimEvent.isPending()) {
                updateEventCounts(currentSimEvent);
                SimEventScheduler simEntity = (SimEventScheduler) currentSimEvent.getSource();
                simEntity.handleSimEvent(currentSimEvent);
                
                // TODO: Consider moving this into the handleSimEvent
                // method of the entity since listening is a different
                // contract than scheduling.
                
                if (simEntity instanceof SimEventSource){
                    ((SimEventSource)simEntity).notifyListeners(currentSimEvent);
                }
                if (isStopOnEvent()) { checkStopEvent(); }
                if (isSingleStep()) { step(); }
                if (isVerbose()) { dump(""); }
                if (isPauseAfterEachEvent()) { pause(); }
            }
            currentSimEvent = null;
        }
        running = false;
        synchronized(entryCounterMutex) {
            entryCounter--;
            if (entryCounter < 0) {
                entryCounter = 0;
            }
        }
    }
    
    /**
     * Processed events one at a time based on user input from the console.
     * <UL>
     *    <LI> &lt;Enter&gt; advances by one event</LI>
     *    <LI> s stops the run</LI>
     *    <LI> f finishes the run in silent mode</LI>
     *    <LI> g finishes the run in verbose mode</LI>
     * </UL>
     */    
    @Override
    public void step() {
        while(true) {
            try {
                char response = (char) System.in.read();
                if (  response == 's') { stopSimulation(); }
                if (  response == 'g') { setSingleStep(false); setVerbose(true); }
                if (  response == 'f') { setSingleStep(false); setVerbose(false);}
                if (  response == '\n') { return;}
            }
            catch (IOException e) {
                System.err.println();
                e.printStackTrace(System.err);
                throw(new RuntimeException(e));
            }
        }    
    }

    @Override
    public void dump(String reason) {
        if (reason.equals("Starting Simulation") || !ignoreOnDump.contains(currentSimEvent.getEventName())) {
            outputStream.println(getEventListAsString(reason));
        }
    }

    @Override
    public void dump() {
        dump("");
    }
    
    /**
     * Adds one to the number of this event that have occured.
     * Used for <CODE>stopOnEvent</CODE>
     * This method is not synchronized since it is only called from startSimulation(),
     * which is protected from being entered multiple times.
     *
     * @param event event to update counts
     */    
    protected void updateEventCounts(SimEvent event) {
        int[] serial = eventCounts.get(event.getFullMethodName());
        if (serial == null) {
            serial = new int[] { 0 };
            eventCounts.put(event.getFullMethodName(), serial);
        }
        serial[0]++;
        event.setSerial(serial[0]);
    }

    /**
     * Stops simulation if the number of stop events
     * have occurred.
     */    
    protected void checkStopEvent() {
        if (isStopOnEvent() && currentSimEvent.getFullMethodName().equals(stopEventName) &&
                currentSimEvent.getSerial() >= numberStopEvents) {
            stopSimulation();
        }
    }

    @Override
    public void pause() {
        //    Sets running to false, causing the event list
        //    algorithm to pause after the current event finishes
        //    processing.
        running = false;
        synchronized (entryCounterMutex) {
            entryCounter--;
            if (entryCounter < 0) {
                entryCounter = 0;
            }
        }
        if (isVerbose()) {
            log.info("Simulation is paused");
        }
    }
    
    @Override
    public void stopSimulation() {
    //Set flag to cause the startSimulation() loop to clear the event list and exit.
        stoppingSimulation = true;
    }
    
    @Override
    public void interrupt(SimEventScheduler simEntity, String eventName) {
        synchronized(eventList) {
            clearDeadEvents();
            Set<SimEvent> events = null;
            if (fastInterrupts) {
                events = entityEventMap.get(simEntity);
            } else {
                events = eventList;
            }
            if (events == null) {
                return;
            }
            for (Iterator<SimEvent> i = events.iterator(); i.hasNext(); ) {
                SimEvent event = i.next();
                if ((event.getSource() == simEntity) &&
                    (event.getEventName().equals(eventName)) &&
                    (event.isPending())) {
                        if (reallyVerbose) {
                            log.info("\n" + getSimTime() 
                                                + ": Cancelling " + event); 
                        }
                        i.remove();
                        if (fastInterrupts) {
                            eventList.remove(event);
                            //removeFromEntityEventMap(event);
                            removeFromHashEventMap(event);
                        }
                        break;
                }//if matches
            }//next i
        }//synch
    }

    @Override
    public void interrupt(SimEventScheduler simEntity, String eventName,
            Object... parameters) {
        Integer hash = null;
        synchronized(eventList) {
            clearDeadEvents();
            Set<SimEvent> events = null;
            if (fastInterrupts) {
                hash = SimEvent.calculateEventHash(simEntity, eventName, parameters);
                events = hashEventMap.get(hash);
            } else {
                events = eventList;
            }
            if (events == null) {
                return;
            }
            for (Iterator<SimEvent> i = events.iterator(); i.hasNext(); ) {
                SimEvent event = i.next();
                if ((event.getSource() == simEntity) &&
                    (event.getEventName().equals(eventName)) &&
                    (event.interruptParametersMatch(parameters)) &&
                    (event.isPending())) {
                        if (reallyVerbose) {
                            log.info("\n" + getSimTime() 
                                                + ": Cancelling " + event); 
                        }
                        i.remove();
                        if (fastInterrupts) {
                            eventList.remove(event);
                            removeFromEntityEventMap(event);
                            //removeFromHashEventMap(event);
                        }
                        break;
                }
            }
            if (fastInterrupts) {
                cleanUpHashEventMap(hash);
            }
        }
    }
    
    @Override
    public void interruptAll(SimEventScheduler simEntity) {
        synchronized(eventList) {
            clearDeadEvents();
            if (fastInterrupts) {
                Set<SimEvent> events = entityEventMap.get(simEntity);
                if (events == null) {
                    return;
                }
                eventList.removeAll(events);
                for (SimEvent event : events) {
                    removeFromHashEventMap(event);
                }
                entityEventMap.put(simEntity, null);
            } else {
                for (Iterator<SimEvent> i = eventList.iterator(); i.hasNext(); ) {
                    SimEvent simEvent = i.next();
                    if (simEvent.getSource() == simEntity) {
                        if (reallyVerbose) {
                            log.info("\n" + getSimTime() 
                                                + ": Cancelling " + simEvent); 
                        }
                        i.remove();
                    }
                }
            }
        }
    }
    
    @Override
    public void interruptAll(SimEventScheduler simEntity, String eventName) {
        synchronized(eventList) {
            clearDeadEvents();
            Set<SimEvent> events = null;
            if (fastInterrupts) {
                events = entityEventMap.get(simEntity);
            } else {
                events = eventList;
            }
            if (events == null) {
                return;
            }
            for (Iterator<SimEvent> i = events.iterator(); i.hasNext(); ) {
                SimEvent simEvent = i.next();
                if ((simEvent.getSource() == simEntity) &&
                    (simEvent.getEventName().equals(eventName)) ){
                    if (reallyVerbose) {
                        log.info("\n" + getSimTime() 
                                            + ": Cancelling " + simEvent); 
                    }
                    i.remove();
                    if (fastInterrupts) {
                        eventList.remove(simEvent);
                        //removeFromEntityEventMap(simEvent);
                        removeFromHashEventMap(simEvent);
                    }
                }
            }
        }
    }
    
    @Override
    public void interruptAll(SimEventScheduler simEntity, String eventName,
        Object... parameters) {
        Integer hash = null;
        synchronized(eventList) {
            clearDeadEvents();
            Set<SimEvent> events = null;
            if (fastInterrupts) {
                hash = SimEvent.calculateEventHash(simEntity, eventName, parameters);
                events = hashEventMap.get(hash);
            } else {
                events = eventList;
            }
            if (events == null) {
                return;
            }
            for (Iterator<SimEvent> i = events.iterator(); i.hasNext(); ) {
                SimEvent simEvent = i.next();
                if ((simEvent.getSource() == simEntity) &&
                    (simEvent.getEventName().equals(eventName)) &&
                    (simEvent.interruptParametersMatch(parameters)) ){
                        if (reallyVerbose) {
                            log.info("\n" + getSimTime() 
                                                + ": Cancelling " + simEvent); 
                        }
                    i.remove();
                    if (fastInterrupts) {
                        eventList.remove(simEvent);
                        removeFromEntityEventMap(simEvent);
                        //removeFromHashEventMap(simEvent);
                    }
                }
            }
            if (fastInterrupts) {
                cleanUpHashEventMap(hash);
            }
        }
    }
    
    @Override
    public void addRerun(ReRunnable simEntity) {
        reRun.add(simEntity);
    }
    
    @Override
    public void removeRerun(ReRunnable simEntity) {
        reRun.remove(simEntity);

        // fix memory leak
        if (entityEventMap != null) {
            entityEventMap.remove(simEntity);
        }
    }
    
    @Override
    public void clearRerun() {
        reRun.clear();
    }

    @Override
    public Set<ReRunnable> getRerun() {
        return new LinkedHashSet<ReRunnable>(reRun);
    }
    
    @Override
    public void addIgnoreOnDump(String eventName) {
        ignoreOnDump.add(eventName);
    }
    
    @Override
    public void removeIgnoreOnDump(String eventName) {
        ignoreOnDump.remove(eventName);
    }
    
    @Override
    public Set<String> getIgnoredEvents() {
        return new LinkedHashSet<String>(ignoreOnDump);
    }

    @Override
    public void coldReset() {
        synchronized(entryCounterMutex) {
            if (entryCounter > 0) {
                entryCounter--;
                throw new SimkitConcurrencyException();
            }
            entryCounter++;
        }
        clearEventList();
        running = false;
        clearRerun();
        simTime = 0.0;
        SimEntityBase.coldReset();
        SimEntityBaseProtected.coldReset();
        ignoreOnDump.clear();
        setDumpEventSources(false);
        setUserDefinedStop();
        setVerbose(false);
        setReallyVerbose(false);
        setSingleStep(false);
        setPauseAfterEachEvent(false);
        setFastInterrupts(true);
        synchronized(entryCounterMutex) {
            entryCounter--;
            if (entryCounter < 0) {
                entryCounter = 0;
            }
        }
    }
    
    @Override
    public String getEventListAsString(String reason) {
        StringBuilder buf = new StringBuilder();
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
        synchronized(eventList) {
            clearDeadEvents();
            if (eventList.isEmpty()) {
                buf.append("            << empty >>");
                buf.append(SimEntity.NL);
            }
            else {
                for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                    SimEvent simEvent = (SimEvent) i.next();
                    if (ignoreOnDump.contains(simEvent.getEventName())) {
                        continue;
                    }
                    if (simEvent.isPending()) {
                        buf.append(simEvent);
//                        if (isPrintEventSources()) {
                            buf.append(' ');
                            buf.append('<');
                            buf.append(simEvent.getSource().getName());
                            buf.append('>');
//                        }
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
    
    /**
     * @return shallow copy of actual events.  To be used by subclasses only
     * for debugging purposes.
     */
    protected SortedSet<SimEvent> getEventList() {
        return Collections.synchronizedSortedSet(new TreeSet<SimEvent>(eventList));
    }

/**
* If true, then pending SimEvents will be stored in a secondary hash table
* to make them easier to find when interrupting. For simulations that
* do not interrupt events, the added overhead from storing and removing
* events in the secondary table could add to run time.
**/
    public boolean isFastInterrupts() {return fastInterrupts;}

/**
* If true, then pending SimEvents will be stored in a secondary hash table
* to make them easier to find when interrupting (defaults to true). For simulations that
* do not interrupt events, the added overhead from storing and removing
* events in the secondary table could add to run time.
* If going from false to true, add any pending events to the secondary hash tables.
* If going from true to false, clear the secondary hash tables.
**/
    public void setFastInterrupts(boolean value) {
        if (value == fastInterrupts) {
            return;
        }
        this.fastInterrupts = value;
        if (fastInterrupts) {
            entityEventMap = 
                    Collections.synchronizedMap(new LinkedHashMap<SimEventScheduler, SortedSet<SimEvent>>());
            hashEventMap = Collections.synchronizedMap(new LinkedHashMap<Integer, SortedSet<SimEvent>>());
            synchronized(eventList) {
                for (SimEvent event : eventList) {
                    addToEntityEventMap(event);
                    addToHashEventMap(event);
                }
            }
        } else {
            entityEventMap = null;
            hashEventMap = null;
        }
    }

/**
* Adds the given SimEvent to the entity event Map.
* @return True if the SimEvent was not already in the Map.
**/
    protected boolean addToEntityEventMap(SimEvent event) {
        if (!fastInterrupts) {
            throw new InvalidSchedulingException("addToEntityEventMap called when "
                + "fastInterrupts was false");
        }
        SimEventScheduler source = event.getSource();
        SortedSet<SimEvent> events = entityEventMap.get(source);
        if (events == null) {
            events = new TreeSet<SimEvent>();
            entityEventMap.put(source, events);
        }
        boolean temp = events.add(event);
        if (!temp) {
            log.warning(getSimTime() + ": The following SimEvent was already in the entity hash "
                + event);
        }
        return temp;
    }

/**
* Removes the given SimEvent from the entity event Map.
* @return True if the SimEvent was found in the Map.
**/
    protected boolean removeFromEntityEventMap(SimEvent event) {
        if (!fastInterrupts) {
            throw new InvalidSchedulingException("removeFromEntityEventMap called when "
                + "fastInterrupts was false");
        }
        SimEventScheduler source = event.getSource();
        SortedSet<SimEvent> events = entityEventMap.get(source);
        if (events == null) {
            log.warning(getSimTime() + ": There are no events for the owner of the SimEvent "
                + " in the entity hash. The event was " + event);
            return false;
        }
        boolean temp = events.remove(event);
        if (!temp) {
            log.warning(getSimTime() + ": The following SimEvent was not found in the entity hash "
                + event);
        }
        return temp;
    }

/**
* Adds the given SimEvent to the event hash event Map.
* @return True if the SimEvent was not already in the Map.
**/
    protected boolean addToHashEventMap(SimEvent event) {
        if (!fastInterrupts) {
            throw new InvalidSchedulingException("addToHashEventMap called when "
                + "fastInterrupts was false");
        }
        Integer hash = event.getEventHash();
        SortedSet<SimEvent> events = hashEventMap.get(hash);
        if (events == null) {
            events = new TreeSet<SimEvent>();
            hashEventMap.put(hash, events);
        }
        boolean temp = events.add(event);
        if (!temp) {
            log.warning(getSimTime() + ": The following SimEvent was already in the Integer hash "
                + event);
        }
        return temp;
    }

/**
* Removes the given SimEvent from the hash event Map.
* @return True if the SimEvent was found in the Map.
**/
    protected boolean removeFromHashEventMap(SimEvent event) {
        if (!fastInterrupts) {
            throw new InvalidSchedulingException("removeFromHashEventMap called when "
                + "fastInterrupts was false");
        }
        Integer hash = event.getEventHash();
        SortedSet<SimEvent> events = hashEventMap.get(hash);
        boolean temp = false;
        if (events != null) {
            temp = events.remove(event);
            if (events.isEmpty()) {
                hashEventMap.remove(hash);
            }
        }
        if (!temp) {
            log.warning(getSimTime() + ": The following SimEvent was not found in the "
                + " hash event Map." + event);
        }
        return temp;
    }

/**
* Removes the entry for the given hash code if there are no more
* events.
**/
    protected void cleanUpHashEventMap(Integer hash) {
        SortedSet events = (SortedSet)hashEventMap.get(hash);
        if (events != null && events.isEmpty()) {
            hashEventMap.remove(hash);
        }
    }

    @Override
    public PrintStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void interruptAllWithArg(SimEventScheduler simEntity, String eventName, Object parameter) {
        for (Iterator<SimEvent> iterator = eventList.iterator();
                iterator.hasNext();) {
            SimEvent event = iterator.next();
            if (event.getSource().equals(simEntity) && event.getEventName().equals(eventName)) {
                for (Object param : event.getParameters()) {
                    if (param.equals(parameter)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void interruptAllWithArg(SimEventScheduler simEntity, Object parameter) {
        for (Iterator<SimEvent> iterator = eventList.iterator();
                iterator.hasNext();) {
            SimEvent event = iterator.next();
            if (event.getSource().equals(simEntity)) {
                for (Object param : event.getParameters()) {
                    if (param.equals(parameter)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void interruptAllWithArg(String eventName, Object parameter) {
        for (Iterator<SimEvent> iterator = eventList.iterator();
                iterator.hasNext();) {
            SimEvent event = iterator.next();
            if (event.getEventName().equals(eventName)) {
                for (Object param : event.getParameters()) {
                    if (param.equals(parameter)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void interruptAllWithArg(Object parameter) {
        for (Iterator<SimEvent> iterator = eventList.iterator();
                iterator.hasNext();) {
            SimEvent event = iterator.next();
            for (Object param : event.getParameters()) {
                if (param.equals(parameter)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

}

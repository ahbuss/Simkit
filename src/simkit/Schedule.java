package simkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
/*
* Revision history:
* 
* 
* 3 April 1999 -- removed "synchronized" from scheduleEvent.
* 3 April 99 -- tried to add wait/notify to pause/resume simulation
* 28 May 1999 -- removed threading from Schedule
* 28 Sept 1999 -- Removed all "rewind" stuff.
*
*/

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
    
/**
* Holds the pending events.
**/
    private static SortedSet     agenda;

/**
* The container type for the event list.
**/
    private static Class         eventListClass;

/**
* The current simulation time.
**/
    private static double        time;

/**
* Unused.
**/
    private static long          eventSerializer;

/**
* Holds the default format for outputting times.
**/
    private static DecimalFormat tsf;

/**
* The event currently being handled.
**/
    private static SimEvent      currentSimEvent;
    
/**
* Holds the number of times each type of event has been processed.
**/
    private static Hashtable     eventCounts;
    
/**
* If true causes the event list to be printed prior
* to processing each event.
**/
    private static boolean verbose;

/**
* Causes additional debug/trace information to be printed.
**/
    private static boolean reallyVerbose;

/**
* Causes the owner of the event to be included in 
* printouts of the event list.
**/
    private static boolean printEventSources;

/**
* Holds a list of entities with Run events.
* Note: It is up to the SimEntity to add itself. (This is
* implemented in BasicSimEntity.)
**/
    private static Map reRun;
    
    // for single-step mode
/**
* If true the simulation will stop prior to each event and 
* wait for user input.
**/
    private static boolean singleStep;

/**
* A different way to do single step mode.  If true the simulation
* will stop after each event and wait for a <code>resume()</code>
* message.
* 
* 22 May 2004 - kas - added field
**/
	private static boolean pauseAfterEachEvent = false;
/**
* The InputStream where Schedule gets input.
**/
    private static InputStream input;

/**
* The PrintWriter where Schedule sends output.
**/
    private static PrintWriter output;
    
    // for stop on event

/**
* The name of the event to stop on after it
* has been processed numberEvents times.
**/
    private static String stopEventName;

/**
* The number of times to process the stop event
* prior to stopping the simulation.
**/
    private static long numberEvents;

/**
* If true, stop the simulation after the stop event
* has been processed numberEvents times.
**/
    private static boolean stopOnEvent;

/**
* If true, stop the simulation at endingTime.
**/
    private static boolean stopOnTime;

/**
* True if the simulation is currently running.
**/
    private static boolean running;
    
/**
* The time to stop the simulation if stopOnTime is true.
**/
    private static double endingTime;
    
/**
* Unused.
**/
    private static boolean cleanDump;
    
/**
* List of events that are not dumped in Event List
**/
    private static Vector ignoreOnDump;
    
    // Comparator for Event List
/**
* Determines the sort order of the SimEvents.
**/
    private static Comparator simEventComp;
    
    private static SimEntity stopInstance;
    
    static {
        simEventComp    = new SimEventComp();
        eventListClass  = java.util.TreeSet.class;
        agenda          = Collections.synchronizedSortedSet(
        new TreeSet( simEventComp ) );
        eventSerializer = 0;
        time            = 0.0;
        running         = false;
        tsf             = new DecimalFormat("0.000");
        eventCounts     = new Hashtable();
        reRun           = new WeakHashMap();
        
        setInputStream(System.in);
        setOutputStream(System.out);
        setCleanDump(false);
        ignoreOnDump = new Vector();
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
    public static void setVerbose(boolean v) {verbose = v;}

/**
* Causes a list of the pending events (and other 
* information) to be dumped to the output stream prior
* to processing each event when set to true.
**/    
    public static boolean isVerbose() {return verbose;}
    
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
        singleStep = step;
        if (singleStep) {setVerbose(singleStep);}
        if (input == null) {
            input = System.in;
        }
        if (singleStep) {
            System.err.println("Press [Enter] for next step; (s)top, (g)o or (f)inish");
        }
    }

/**
* If true, then Schedule is running in single-step mode and will
* pause prior to processing each event.
*/
    public static boolean isSingleStep() { return singleStep; }

/**
* True when a simulation is currently running.
**/
    public static boolean isRunning() {return running;}
    
// cleanDump appears unused.
/**
* @deprecated No replacement
**/
    public static void setCleanDump(boolean c) {cleanDump = c;}

/**
* @deprecated No replacement
**/
    public static boolean isCleanDump() {return cleanDump;}
    
    /**
     *  @deprecated - Use getSimTime() instead.
     **/
    public static  double simTime() { return time; }
    
    /**
     * Gets the current simulation time.
     * @return the current value of simulated time.
     **/
    public static double getSimTime() { return time; }
    
    /**
     *  @deprecated - use getSimTimeStr() or getSimTimeStr(String) instead.
     **/
    public static  String simTimeStr() { return getSimTimeStr();}
    
    /**
     * Get the current simulation time as a formatted String. The
     * default is in decimal form with 3 digits after the decimal point.
     * @return The current value of simulated time as a <CODE>String</CODE>
     **/
    public static String getSimTimeStr() { return tsf.format(simTime()); }
    
    /**
     * Get the current simulation time as a formatted String based on 
     * the user supplied format.
     *  @param format - The format to use.
     *  @return The current value of simulated time as a <CODE>String</CODE>
     * @see DecimalFormat
     **/
    public static String getSimTimeStr(String format) { return new DecimalFormat(format).format(simTime()); }
    
    /**
     * Clears the event list and starts time at 0.0.
     **/
    public static void reset() {
        clearEventList();
        currentSimEvent = null;
        time = 0.0;
        TreeSet sortedReruns = new TreeSet(new SimEntityComparator());
        synchronized(reRun) {
            sortedReruns.addAll(reRun.keySet());
        }
        ArrayList transients = new ArrayList();
        for (Iterator i = sortedReruns.iterator(); i.hasNext();) {
            SimEntity rerunSimEntity =  (SimEntity) i.next();
            if (isReallyVerbose()) {
                output.println("Checking rerun " + rerunSimEntity + "[rerunnable?] " + rerunSimEntity.isReRunnable());
            }
            if (!rerunSimEntity.isPersistant()) {
                transients.add(rerunSimEntity);
                SimEventListener[] listener = rerunSimEntity.getSimEventListeners();
                for (int j = 0; j < listener.length; ++j) {
                    if (listener[j] instanceof SimEntity) {
                        SimEntity se = (SimEntity) listener[j];
                        if ( !se.isPersistant() ) {
                            rerunSimEntity.removeSimEventListener(se);
                        }
                    }
                }
            }
            else {
                rerunSimEntity.reset();
                if (rerunSimEntity.isReRunnable()) {
                    rerunSimEntity.waitDelay("Run", 0.0, null, Double.POSITIVE_INFINITY);
                }
            }
        }
        synchronized(reRun) {
            for (int j = 0; j < transients.size(); ++j) {
                reRun.remove(transients.get(j));
            }
        }
        if (stopOnTime) { stopAtTime(endingTime); }
    }
    
    /**
     * Puts an event on the event list.
     * @param SimEvent event
     * @exception  InvalidSchedulingException If the scheduled time is < simTime()
     **/
    public static void scheduleEvent(SimEvent event)
    throws InvalidSchedulingException {
        if (event.getScheduledTime() < simTime()) {
            throw new InvalidSchedulingException("Attempt to reverse time!");
        }
        else if (!Double.isNaN(event.getScheduledTime())){
            if (!agenda.add(event)){
                throw new InvalidSchedulingException("Unknown Problem Scheduling event " +
                event + "," + event.getSource() + ">");
            }
        }
        if (reallyVerbose) {
            output.println("Event " + event + " Scheduled by " + event.getSource());
            dump();
        }
    }
    
    /**
     *  Starts or resumes simulation.  This method implements the fundamental discrete
     *  event simulation algorithm.  While the Event List is not empty, simTime is
     *  advanced to the time of the soonest event, which is then processed.  If the
     *  Event List becomes empty, then the simulation stops and the method returns.
     **/
    public static void startSimulation() {
        if (running) {
            throw new RuntimeException("Simulation already running");
        }
        running = true;
        
        if (verbose) { dump("Starting Simulation"); }
        if (singleStep) { step(); }
        while ( !agenda.isEmpty() && running ) {
            currentSimEvent = (SimEvent)(agenda.first());
            agenda.remove(currentSimEvent);
            if (reallyVerbose) {
                output.println("Processing " + currentSimEvent + " from Event List");
            }
            time = currentSimEvent.getScheduledTime();
            
            if (currentSimEvent.isPending()) {
                updateEventCounts(currentSimEvent);
                SimEntity entity = (SimEntity) currentSimEvent.getSource();
                entity.handleSimEvent(currentSimEvent);
                entity.notifyListeners(currentSimEvent);
                
                // 22 May 2004 - kas - addition
                if (pauseAfterEachEvent) { pauseSimulation(); }
                // end 22 May 2004 addition
                if (stopOnEvent) { checkStopEvent(); }
                if (verbose) { dump(""); }
                if (singleStep) { step(); }
            } //if
            SimEventFactory.returnSimEventToPool(currentSimEvent);
            clearDeadEvents();
        }
        running = false;
        currentSimEvent = null;
    }
    
/**
* If the current event is the designated stop event and the number
* of occurrences exceeds the number required, then stop the simulation.
**/
    private static void checkStopEvent() {
        if (currentSimEvent.getFullMethodName().equals(stopEventName) &&
        currentSimEvent.getSerial() >= numberEvents) {
            stopSimulation();
        }
    }
    
/**
* Increment the count for the event type corresponding to the given event.
**/
    private static void updateEventCounts(SimEvent currentSimEvent) {
        int[] serial = null;
        if (eventCounts.containsKey(currentSimEvent.getFullMethodName())) {
            serial = (int[]) eventCounts.get(currentSimEvent.getFullMethodName());
        }
        else {
            serial = new int[] {0};
            eventCounts.put(currentSimEvent.getFullMethodName(), serial);
        }
        synchronized (serial) {
            serial[0]++;
            currentSimEvent.setSerial(serial[0]);
        }
/*
      int serial = 1;
      if (eventCounts.containsKey(currentSimEvent.getFullMethodName())) {
         serial = ((Integer)
               eventCounts.get(currentSimEvent.getFullMethodName())).intValue() + 1;
      }
      currentSimEvent.setSerial(serial);
      eventCounts.put(currentSimEvent.getFullMethodName(), new Integer(serial));
 */
    }
    
/**
* Pause the simulation, which can be resumed with either resumeSimulation()
* or startSimulation()
**/
    public static void pause() {
        if (verbose) {
            System.out.println("Schedule is paused");
        }
        running = false;
    }
    
/**
* Pause the simulation, which can be resumed with either resumeSimulation()
* or startSimulation()
**/
    public static void pauseSimulation() {
        pause();
    }
    
/**
* Resume the simulation at the next event.
**/
    public static void resumeSimulation() {
        resume();
    }
    
/**
* Resume the simulation at the next event.
**/
    public static void resume() {
        startSimulation();
    }
    
/**
* Wait for user input before continuing. <P>
* 's' will stop the simulation.<BR>
* 'g' will take the simulation out of single-step and continue.<BR>
* 'f' will take the simulation out of single-step, set verbose to false, and continue.<BR>
* Return will cause the simulation to step to the next event.
**/
    private static void step() {
        while(true) {
            try {
                char response = (char) input.read();
                if (  response == 's') { stopSimulation(); }
                if (  response == 'g') { setSingleStep(false); }
                if (  response == 'f') { setSingleStep(false); setVerbose(false);}
                if (  response == '\n') { return;}
/*            switch (response) {
                case 's':
                    stopSimulation();
                    break;
                case 'f':
                    setSingleStep(false);
                case 'g':
                    setVerbose(false);
                    break;
                case '\n':
                    return;
                default:
                    return;
            }
 */
            }
            catch (IOException e) { System.err.println(); e.printStackTrace(System.err); }
        }
    }
  
/**
* @deprecated Use stopAtTime()
**/  
    public static void stopOnTime(double endingT) {
        stopAtTime(endingT);
    }
    
/**
* Will cause the simulation to stop when it reaches the given
* simulation time. Any previously set stop time or stop on event is cleared.
**/  
    public static void stopAtTime(double atTime) {
        interrupt("Stop");
        endingTime = atTime;
        if (stopInstance == null) {
            stopInstance = new Stop();
        }
        stopInstance.waitDelay("Stop", endingTime - getSimTime(), new Object[] {}, Double.NEGATIVE_INFINITY );
        stopOnEvent = false;
        stopOnTime = true;
    }
    
/**
* Disables both stop at time and stop on event.
**/
    public static void setUserDefinedStop() {
        stopOnTime = false;
        stopOnEvent = false;
    }
    
/**
* Causes the simulation to stop after the given event (which takes no arguments) has been processed the
* given number of times. Cancels any previous stopOnEvent or stopAtTime.
* @throws IllegalArgumentException if the number of events is negative.
**/
    public static void stopOnEvent(String eventName, int number) {
        Schedule.stopOnEvent(eventName, new Class[]{}, number);
        stopOnTime = false;
        stopOnEvent = true;
    }
    
/**
* Causes the simulation to stop after the given event with the given signature
* has been processed the
* given number of times. Cancels any previous stopOnEvent, but does not cancel stopAtTime.
* @throws IllegalArgumentException if the number of events is negative.
**/
    public static void stopOnEvent(String eventName, Class[] eventSignature, int number) {
        if (number >= 0) {
            stopOnEvent = true;
            StringBuffer fmn = new StringBuffer("do" + eventName + "(");
            for (int i = 0; i < eventSignature.length; i++) {
                fmn.append(eventSignature[i].getName());
                if (i < eventSignature.length - 1) {fmn.append(",");}
            }
            fmn.append(")");
            stopEventName = fmn.toString();
            numberEvents = (long) number;
        }
        else {
            throw new IllegalArgumentException("Negative number of events specified");
        }
    }
    
/**
* Stops the simulation, which cannot be resumed.
**/
    public static synchronized void stopSimulation() {
        clearEventList();
        running = false;
    }
    
    /**
     *  Returns current event.
     *  @deprecated - Use getCurrentEvent() instead
     **/
    public static synchronized SimEvent currentEvent() {return currentSimEvent;}
    
    /**
     *  Returns currently executing event; null if simulation is not currently running.
     **/
    public static synchronized SimEvent getCurrentEvent() {return currentSimEvent;}
    
/**
* Cancels the next event for the given SimEntity.
**/
    public static void interrupt(SimEntity se) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if (event.getSource() == se && event.isPending()) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
                break;
            }
        }
    }
    
/**
* Cancels the next event of the given name for the given SimEntity.
**/
    public static void interrupt(SimEntity se, String eventName) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ((event.getSource() == se) && event.getEventName().equals(eventName) &&
                event.isPending()) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
                break;
            }
        }
    }
    
/**
* Cancels the next event of the given name with the given parameters for the given SimEntity.
* The parameters must match in signature and value.
**/
    public static void interrupt(SimEntity se, String eventName, Object[] parameters) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ((event.getSource() == se) && event.getEventName().equals(eventName) &&
            (event.interruptParametersMatch(parameters)) && event.isPending()) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);                
                break;
            }
        }
    }
    
/**
* Cancels all events for the given SimEntity.
**/
    public static void interruptAll(SimEntity se) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if (event.getSource() == se) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
            }
        }
    }
    
    /**
     *  Interrupts all pending events; synonym for "stopSimulation()".
     **/
    public static void interruptAll() {
        stopSimulation();
    }
    
/**
* Cancels all events of the given name for the given SimEntity.
**/
    public static void interruptAll(SimEntity se, String eventName) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ((event.getSource() == se) && event.getEventName().equals(eventName)) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
            }
        }
    }
    
/**
* Cancels all events of the given name with the given parameters for the given SimEntity.
* The parameters must match in signature and value.
**/
    public static void interruptAll(SimEntity se, String eventName, Object[] parameters) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ((event.getSource() == se) && event.getEventName().equals(eventName) &&
            (event.interruptParametersMatch(parameters)) ) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
            }
        }
    }
    
/**
* Cancel the next event.
**/
    public static void interrupt(){
        clearDeadEvents();
//        ((SimEvent)agenda.first()).interrupt();
        SimEvent event = (SimEvent)agenda.first();
        agenda.remove(event);
        SimEventFactory.returnSimEventToPool(event);
    }
    
/**
* Cancel the next event of the given name.
**/
    public static void interrupt(String eventName) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ( (event.getWaitState() == SimEventState.WAITING) &&
            event.getEventName().equals(eventName)) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
                break;
            }
        }
    }
    
/**
* Cancels the next event of the given name with the given parameters.
* The parameters must match in signature and value.
**/
    public static void interrupt(String eventName, Object[] parameters) {
        clearDeadEvents();
        SimEvent event = null;
        for (Iterator i = agenda.iterator(); i.hasNext();) {
            event = (SimEvent) i.next();
            if ( (event.getWaitState() == SimEventState.WAITING) &&
            event.getEventName().equals(eventName) && event.interruptParametersMatch(parameters)) {
//                event.interrupt();
                i.remove();
                SimEventFactory.returnSimEventToPool(event);
                break;
            }
        }
    }
    
    /** 
     * Pops all events that are not waiting to execute from this
     *  object's agenda, ie., those that are interrupted
     **/
    private static void clearDeadEvents() {
        while ( !agenda.isEmpty() &&
        !((SimEvent)(agenda.first())).isPending() ) {
            agenda.remove(agenda.first());
        }
    }
    
/**
* Return a list of the currently pending events.
* @param reason Added to the last line of the list.
**/
    public static String getEventListAsString(String reason) {
        clearDeadEvents();
        StringBuffer buf = new StringBuffer();
        if (currentEvent() != null) {
            buf.append("Time: ");
            buf.append(simTimeStr());
            buf.append(" \tCurrent Event: " );
            buf.append(currentEvent().paramString());
            buf.append('\t');
            buf.append('[');
            buf.append(currentEvent().getSerial());
            buf.append(']');
            buf.append(SimEntity.NL);
        }
        buf.append(" ** Event List -- ");
        buf.append(reason);
        buf.append(" **");
        buf.append(SimEntity.NL);
        
        if ( agenda.isEmpty() ) {
            buf.append("               << empty >>");
            buf.append(SimEntity.NL);
        }
        else {
            synchronized(agenda) {
                for (Iterator i = agenda.iterator(); i.hasNext();) {
                    SimEvent nextEvent = (SimEvent) i.next();
                    if (ignoreOnDump.contains(nextEvent)) { continue; }
                    if (nextEvent.isPending()) {
                        buf.append( nextEvent);
                        if (printEventSources) {
                            buf.append('\t');
                            buf.append('<');
                            buf.append(((SimEvent)nextEvent).getSource());
                            buf.append('>');
                        }
                        buf.append(SimEntity.NL);
                    }
                }
            }
        }
        buf.append(" ** End  of Event List -- " + reason + " **");
        buf.append(SimEntity.NL);
        
        return buf.toString();
    }
    
/**
* Return a list of the currently pending events.
**/
    public static String getEventListAsString() {
        return getEventListAsString("");
    }
    
/**
* Prints the list of the currently pending events to the output stream.
* @param reason Added to the last line of the list.
**/
    public static void dump(String reason) {
        if (output != null) {
            output.println(getEventListAsString(reason));
            output.flush();
        }
    }
    
/**
* Prints the list of the currently pending events to the output stream.
**/
    public static void dump() {
        dump("");
    }
    
/**
* If set to true causes the owner of the event to be included
* whenever the event list is printed. Used by dump() and getEventListAsString()
*/
    public static void setEventSourceVerbose(boolean v) {printEventSources = v;}
    
/**
* Adds the entity to the list of entities with "Run" events.
 * Note: addRerun() no longer schedules the Run event - that is
 * supposed to happen when Schedule.reset() is invoked, as it <i>should</i>
 * be, before each simulation run.
 * schedules the Run event at the current time.
**/
    public static void addRerun(SimEntity se) {
        reRun.put(se, null);
    }

/**
* Removes the SimEntity from the list of entities with Run events.
* Note it does not interrupt its Run event.
**/
    public static void removeRerun(SimEntity se) {reRun.remove(se);}

/**
* Clears the list of SimEntities with Run events.
**/
    public static void clearRerun() {reRun.clear();}

/**
* Returns a copy of the list of SimEntities with Run events.
**/
    public static Map getReruns() {
        return new HashMap(reRun);
    }
    
    /**
     *  When dumping event list, ignore this event.
     *  @param ignoredEventName The name of the event to be ignored
     **/
    public static void addIgnoreOnDump(String ignoredEventName) {
        if (!ignoreOnDump.contains(ignoredEventName)) {
            ignoreOnDump.add(ignoredEventName);
        }
    }
    
    /**
     *  Stop ignoring this event on dump().
     *  @param ignoredEventName The event that was previously ignored but now is not.
     **/
    public static void removeIgnoreOnDump(String ignoredEventName) {
        ignoreOnDump.remove(ignoredEventName);
    }
    
    /**
     * Returns the list of events that are not included when the event list is output.
     *  @return Current list of events that are ignored on dump().
     **/
    public static Vector getIgnoreOnDumpEvents() { return (Vector)ignoreOnDump.clone(); }
    
/**
* Set the InputStream from which Scheduled will get its input.
**/
    public static void setInputStream(InputStream in) {input = in;}

/**
* Sets the OutputStream to which Scheduled will sent output.
* A new PrintWriter is constructed to handle the output.
**/
    public static void setOutputStream(OutputStream out) {
        output = new PrintWriter(out);
    }

/**
* Sets the output of Scheduled to a pre-existing Writer.
**/
    public static void setOutputStream(Writer out) { output = new PrintWriter(out); }
    
/**
* The InputStream from which Schedule will get its input.
**/
    public static InputStream getInputStream() {return input;}

/**
* The PrintWriter to which Schhedule will send output.
**/
    public static PrintWriter getOutputStream() {return output;}
    
/**
* It true, causes Schedule to print additional debug/trace information.
**/
    public static void setReallyVerbose(boolean rv) {reallyVerbose = rv;}

/**
* It true, causes Schedule to print additional debug/trace information.
**/
    public static boolean isReallyVerbose() {return reallyVerbose;}
    
/**
* Change the underlying container for the event list. (The default is a TreeSet.)<P>
* The following conditions must be true in order for the change to succeed: 
* <UL>
* <LI>The specified Class must implement the SortedSet interface.</LI>
* <LI>It must have a constructor that takes a java.util.Comparator as its argument.</LI>
* <LI>The simulation must not be running.</LI>
* </UL>
* @param The class to be used as the container.
**/
    public static synchronized void setEventListType(Class newClass) {
        if (java.util.SortedSet.class.isAssignableFrom(newClass)) {
            if (!Schedule.isRunning()) {
                try {
                    Comparator comp = new SimEventComp();
                    Constructor construct = newClass.getConstructor(new Class[] {java.util.Comparator.class});
                    synchronized(agenda) {
                        //                        SortedSet newEventList = (SortedSet) construct.newInstance(new Object[] {agenda.comparator()});
                        SortedSet newEventList = (SortedSet) construct.newInstance(new Object[] {comp});
                        for (Iterator i = agenda.iterator(); i.hasNext();) {
                            newEventList.add(i.next());
                        }
                        agenda.clear();
                        agenda = Collections.synchronizedSortedSet(newEventList);
                        eventListClass = newClass;
                        if (verbose) {
                            System.out.println("Comparator passed is " + comp);
                            System.out.println("New Comparator has " + newEventList.comparator());
                        }
                    }
                }
                catch (NoSuchMethodException e) {e.printStackTrace(System.err);}
                catch (InvocationTargetException e) {e.getTargetException().printStackTrace(System.err);}
                catch (IllegalAccessException e) {e.printStackTrace(System.err);}
                catch (InstantiationException e) {e.printStackTrace(System.err);}
                
            }
        }
    }
    
/**
* Change the underlying container for the event list. (The default is a TreeSet.)<P>
* The following conditions must be true in order for the change to succeed: 
* <UL>
* <LI>The specified Class must implement the SortedSet interface.</LI>
* <LI>It must have a constructor that takes a java.util.Comparator as its argument.</LI>
* <LI>The simulation must not be running.</LI>
* </UL>
* @param The name of the class to be used as the container.
**/
    public static synchronized void setEventListType(String newClassName) {
        try {
            setEventListType(Thread.currentThread().getContextClassLoader().loadClass(newClassName));
        }
        catch (ClassNotFoundException e) {e.printStackTrace(System.err);}
    }
    
/**
* Returns the Class that is the current container for the event list.
**/
    public static Class getEventListType() {return eventListClass;}
    
/**
* Remove all events from the event list. Causes the simulation to end.
**/
    private static void clearEventList() {
        while (!agenda.isEmpty()) {
            SimEvent event = (SimEvent) agenda.first();
            agenda.remove(agenda.first());
            SimEventFactory.returnSimEventToPool(event);
        }
        agenda.clear();
        eventCounts.clear();
    }
    
public static boolean getPauseAfterEachEvent() {
	return pauseAfterEachEvent;
}

/**
* A different way to do single step mode.  If true the simulation
* will stop after each event and wait for a <code>resume()</code>
* message.
* 
* 22 May 2004 - kas - added field
**/
public static void setPauseAfterEachEvent(boolean b) {
	pauseAfterEachEvent = b;
}

} // class Schedule

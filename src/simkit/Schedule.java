/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

/**
* Simulation scheduling.
*
* Schedule maintains the simulation schedule and
* manages SimEvents.
* <UL>
* <LI> 3 April 1999 -- removed "synchronized" from scheduleEvent.
* <LI> 3 April 99 -- tried to add wait/notify to pause/resume simulation
* <LI> 28 May 1999 -- removed threading from Schedule
* <LI> 28 Sept 1999 -- Removed all "rewind" stuff.
* </UL>
*
* @author K. A. Stork
* @author Arnold Buss
* @version 1.1.1
*
**/
package simkit;

import java.util.*;
import java.io.*;
import java.text.*;

import java.lang.reflect.*;

public class Schedule  {

    private static SortedSet     agenda;
    private static Class         eventListClass;
    private static double        time;
    private static long          eventSerializer;
    private static DecimalFormat tsf;
    private static SimEvent      currentSimEvent;

    private static Hashtable     eventCounts;

    private static boolean verbose;
    private static boolean reallyVerbose;
    private static boolean printEventSources;
//   private static Vector reRun;
    private static Map reRun;

   // for single-step mode
    private static boolean singleStep;
    private static InputStream input;
    private static PrintWriter output;

   // for stop on event
    private static String stopEventName;
    private static long numberEvents;
    private static boolean stopOnEvent;
    private static boolean stopOnTime;
    private static boolean running;

   // for stop at time
    private static double endingTime;

   // turns INTERRUPTED on & off on dump()
    private static boolean cleanDump;

   // List of events that are not dumped in Event List
    private static Vector ignoreOnDump;

    // Comparator for Event List
    private static Comparator simEventComp;

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

    public static void setVerbose(boolean v) {verbose = v;}
    public static boolean isVerbose() {return verbose;}

/**
  * Sets single step mode.  After each event is executed, the user must
  * enter something at the console.
  * @param boolean step true if seingle-step mode, false otherwise.
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
    public static boolean isSingleStep() { return singleStep; }
    public static boolean isRunning() {return running;}

    public static void setCleanDump(boolean c) {cleanDump = c;}
    public static boolean isCleanDump() {return cleanDump;} 
   
/**
 *  @deprecated - Use getSimTime() instead.
**/
    public static  double simTime() { return time; }

/**
  * @return the current value of simulated time.
**/
   public static double getSimTime() { return time; }
   
/**
 *  @deprecated - use getSimTimeStr() or getSimTimeStr(String) instead.
**/
    public static  String simTimeStr() { return getSimTimeStr();}

/**
  * @return The current value of simulated time as a <CODE>String</CODE>
**/
   public static String getSimTimeStr() { return tsf.format(simTime()); }

/**
 *  @param format - The
 *  @return The current value of simulated time as a <CODE>String</CODE>
**/
   public static String getSimTimeStr(String format) { return new DecimalFormat(format).format(simTime()); }

/**
  * Clears the event list and starts time at 0.0.
**/
   public static void reset() {
      agenda.clear();
      currentSimEvent = null;
      time = 0.0;
      eventCounts.clear();
      for (Iterator i = reRun.keySet().iterator(); i.hasNext();) {
         SimEntity rerunSimEntity =  (SimEntity) i.next();
if (isReallyVerbose()) {
   output.println("Checking rerun " + rerunSimEntity + "[rerunnable?] " + rerunSimEntity.isReRunnable());
}
         rerunSimEntity.reset();
         if (rerunSimEntity.isReRunnable()) {
           rerunSimEntity.waitDelay("Run", 0.0, null, rerunSimEntity.getPriority());
         }
      }
      if (stopOnTime) { stopOnTime(endingTime); }
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
                ((SimEntity) currentSimEvent.getSource()).handleSimEvent(currentSimEvent);

	            if (stopOnEvent) { checkStopEvent(); }
                if (verbose) { dump(""); }
                if (singleStep) { step(); }
             } //if
             clearDeadEvents();
        }
        running = false;
        currentSimEvent = null;
   }

   private static void checkStopEvent() {
      if (currentSimEvent.getFullMethodName().equals(stopEventName) &&
          currentSimEvent.getSerial() >= numberEvents) {
         stopSimulation();
      }
   }

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

    public static void pause() {
        if (verbose) {
            System.out.println("Schedule is paused");
        }
        running = false;
    }

   public static void pauseSimulation() {
       pause();
   }

   public static void resumeSimulation() {
       resume();
   }

   public static void resume() {
       startSimulation();
   }
   
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

   public static void stopOnTime(double endingT) {
       stopAtTime(endingT);
   }

    public static void stopAtTime(double atTime) {
        interrupt("Stop");
        endingTime = atTime;
        new Stop().waitDelay("Stop", endingTime - simTime() );
        stopOnEvent = false;
        stopOnTime = true;
    }

   public static void setUserDefinedStop() {
     stopOnTime = false;
     stopOnEvent = false;
   }

   public static void stopOnEvent(String eventName, int number) {
      Schedule.stopOnEvent(eventName, new Class[]{}, number);
      stopOnTime = false;
      stopOnEvent = true;
   }

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

   public static synchronized void stopSimulation() {
      agenda.clear();
      eventCounts.clear();
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

  public static void interrupt(SimEntity se) {
    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if (event.getSource() == se) {
          event.interrupt();
          break;
       }
    }
  }

  public static void interrupt(SimEntity se, String eventName) {
    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ((event.getSource() == se) && event.getEventName().equals(eventName)) {
          event.interrupt();
          break;
       }
    }
  }

  public static void interrupt(SimEntity se, String eventName, Object[] parameters) {
    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ((event.getSource() == se) && event.getEventName().equals(eventName) &&
            (event.interruptParametersMatch(parameters)) ) {
          event.interrupt();
          break;
       }
    }
  }

  public static void interruptAll(SimEntity se) {
    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if (event.getSource() == se) {
          event.interrupt();
       }
    }
  }

/**
 *  Interrupts all pending events; synonym for "stopSimulation()".
**/
    public static void interruptAll() {
        stopSimulation();
    }

  public static void interruptAll(SimEntity se, String eventName) {
    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ((event.getSource() == se) && event.getEventName().equals(eventName)) {
          event.interrupt();
       }
    }
  }

  public static void interruptAll(SimEntity se, String eventName, Object[] parameters) {

    clearDeadEvents();
    SimEvent event = null;
    for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ((event.getSource() == se) && event.getEventName().equals(eventName) &&
            (event.interruptParametersMatch(parameters)) ) {
          event.interrupt();
       }
    }
  }

   public static void interrupt(){
     clearDeadEvents();
     ((SimEvent)agenda.first()).interrupt();
   }

   public static void interrupt(String eventName) {
     clearDeadEvents();
     SimEvent event = null;
     for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ( (event.getWaitState() == SimEventState.WAITING) &&
            event.getEventName().equals(eventName)) {
         event.interrupt();
         break;
       }
     }
   }

   public static void interrupt(String eventName, Object[] parameters) {
      clearDeadEvents();
      SimEvent event = null;
     for (Iterator i = agenda.iterator(); i.hasNext();) {
       event = (SimEvent) i.next();
       if ( (event.getWaitState() == SimEventState.WAITING) &&
            event.getEventName().equals(eventName) && event.interruptParametersMatch(parameters)) {
         event.interrupt();
         break;
       }
     }      
   }
   
/** pops all events that are not waiting to execute from this
 *  object's agenda, ie., those that are interrupted 
**/
   private static void clearDeadEvents() {
      while ( !agenda.isEmpty() &&
              !((SimEvent)(agenda.first())).isPending() ) {
         agenda.remove(agenda.first());
      }
   }

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

   public static String getEventListAsString() {
       return getEventListAsString("");
   }

   public static void dump(String reason) {
      if (output != null) {
          output.println(getEventListAsString(reason));
          output.flush();
      }
   }

   public static void dump() {
      dump("");
   }

   public static void setEventSourceVerbose(boolean v) {printEventSources = v;}

    public static void addRerun(SimEntity se) {
        reRun.put(se, null);
        if (se.isReRunnable()) {
            se.waitDelay("Run", 0.0, null, SimEvent.DEFAULT_PRIORITY);
        }
    }
    public static void removeRerun(SimEntity se) {reRun.remove(se);}
    public static void clearRerun() {reRun.clear();}
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
 *  @return Current list of events that are ignored on dump().
**/
   public static Vector getIgnoreOnDumpEvents() { return (Vector)ignoreOnDump.clone(); }

   public static void setInputStream(InputStream in) {input = in;}
   public static void setOutputStream(OutputStream out) {
     output = new PrintWriter(out);
   }
   public static void setOutputStream(Writer out) { output = new PrintWriter(out); }

   public static InputStream getInputStream() {return input;}
   public static PrintWriter getOutputStream() {return output;}

   public static void setReallyVerbose(boolean rv) {reallyVerbose = rv;}
   public static boolean isReallyVerbose() {return reallyVerbose;}

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

    public static synchronized void setEventListType(String newClassName) {
        try {
            setEventListType(Class.forName(newClassName));
        }
        catch (ClassNotFoundException e) {e.printStackTrace(System.err);}
    }

    public static Class getEventListType() {return eventListClass;}

} // class Schedule

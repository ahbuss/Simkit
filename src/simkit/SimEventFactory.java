package simkit;
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
 *  This class has nothing but "factory" methods for creating SimEvents based
 *  on the type of data needed for the event.
 *  @version 1.1.2
 *  @author Arnold Buss
**/

import java.util.*;

public class SimEventFactory {

    private static Stack eventPool;
    private static int initialCapacity;
    private static int increment;

    private static final int DEFAULT_INITIAL_CAPACITY = 50;
    private static final int DEFAULT_INCREMENT = 50;

    private static boolean verbose;
    private static int eventSerializer;

    static {
        eventPool = new Stack();
        setInitialCapacity(DEFAULT_INITIAL_CAPACITY);
        setIncrement(DEFAULT_INCREMENT);
        augmentEventPool(initialCapacity);
        eventSerializer = 0;
        verbose = false;
    }

/*
    public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       Object[] params,
                       double schTime,
                       double priority,
                       String name) {
         return new SimEventImpl(source, theMethodName, params, schTime, priority, name);
    }

   public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       double schTime,
                       double priority,
                       String name) {
     return createSimEvent(source, theMethodName, new Object[]{}, schTime, priority, name);
   }

    public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       Object[] params,
                       double schTime,
                       String name) {
     return createSimEvent(source, theMethodName, params, schTime, SimEvent.DEFAULT_PRIORITY, name);
   }
*/   
    public static SimEvent createSimEvent(
                       SimEntity source,
                       String eventName,
                       double delay,
                       Object[] params,
                       double priority
                       ) {
        if (eventPool.size() <= 1) {
            augmentEventPool(increment);
        }

        SimEvent event = (SimEvent) eventPool.pop();
        event.reset();
        event.setSource(source);
        event.setEventName(eventName);
        event.setParameters(params);
        event.setScheduledTime(delay);
        event.setPriority(priority);
        event.setWaitState(SimEventState.WAITING);
        event.setSerial(++eventSerializer);
        event.setID(eventSerializer);
        event.setCreationTime(Schedule.getSimTime());

        if (verbose) {
            System.out.print("\tEvent fetched from event pool, ");
            System.out.print(" hashcode = " + event.hashCode());
            System.out.println(" -> Event [id = " + event.getID() + ", fullMethodName = " + event.getFullMethodName() + " ]:" +
                SimEntity.NL + event);
        }

        return event;
//     return createSimEvent(source, theMethodName, params, schTime, priority);
    }

   public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       double delay,
                       Object[] params
                       ) {
     return createSimEvent(source, theMethodName, delay, params, SimEvent.DEFAULT_PRIORITY);
   }

    public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       double delay
                       ) {
        return createSimEvent(source, theMethodName, delay, new Object[] {}, SimEvent.DEFAULT_PRIORITY);
    }

    private static String getEventNameFromMethod(String methodName) {
        return (methodName.startsWith("do") ) ? methodName.substring(2) : methodName;
    }

    public static synchronized void returnSimEventToPool(SimEvent event) {
        eventPool.push(event);
        if (verbose) {
            Schedule.getOutputStream().println("\tEvent " + event.hashCode() +
                " returned to Event pool");
        }
    }

    protected static synchronized void augmentEventPool(int byThisMany) {
        for (int i = 0; i < byThisMany; i++) {
            eventPool.push(new SimEventImpl());
        }
    }

    public static void setInitialCapacity(int cap) {initialCapacity = cap;}
    public static void setIncrement(int inc) {increment = inc;}
    public static void setVerbose(boolean v) {verbose = v;}
    public static boolean isVerbose() {return verbose;}

}

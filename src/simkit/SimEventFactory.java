package simkit;

import java.util.*;

/**
 *  A factory for creating SimEvents. 
 *  This class has nothing but "factory" methods for creating SimEvents based
 *  on the type of data needed for the event.
 *  @version 1.1.2
 *  @author Arnold Buss
**/
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

/**
* Creates a SimEvent with the given parameters. This is the full implementation with no default values.
* @param source The SimEntity that is scheduling this SimEvent. When the event occurs the source will
* first attempt to execute the event, then notify any registered SimEventListeners. 
* @param eventName The name of the event method. Event methods must start with "do", however
* the "do" is optional in the event name parameter. (i.e., A SimEvent to execute the "doRun"
* method can be scheduled as either "Run" or "doRun"
* @param delay The time the event will occur.
* @param params An Object array containing the parameters for the event method.
* Primatives should be wrapped in the appropriate Object.
* @param priority If two events are scheduled to occur at the same time, the one
* with the higher priority will be processed first.
**/ 
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
        if (event == null) {
            event = new SimEventImpl();
        }
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

/**
* Creates a SimEvent with a default priority.
* @param source The SimEntity that is scheduling this SimEvent. When the event occurs the source will
* first attempt to execute the event, then notify any registered SimEventListeners. 
* @param eventName The name of the event method. Event methods must start with "do", however
* the "do" is optional in the event name parameter. (i.e., A SimEvent to execute the "doRun"
* method can be scheduled as either "Run" or "doRun"
* @param delay The time the event will occur.
* @param params An Object array containing the parameters for the event method.
* Primatives should be wrapped in the appropriate Object.
**/ 
   public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       double delay,
                       Object[] params
                       ) {
     return createSimEvent(source, theMethodName, delay, params, SimEvent.DEFAULT_PRIORITY);
   }

/**
* Creates a SimEvent with a default priority and no parameters.
* @param source The SimEntity that is scheduling this SimEvent. When the event occurs the source will
* first attempt to execute the event, then notify any registered SimEventListeners. 
* @param eventName The name of the event method. Event methods must start with "do", however
* the "do" is optional in the event name parameter. (i.e., A SimEvent to execute the "doRun"
* method can be scheduled as either "Run" or "doRun"
* @param delay The time the event will occur.
**/ 
    public static SimEvent createSimEvent(
                       SimEntity source,
                       String theMethodName,
                       double delay
                       ) {
        return createSimEvent(source, theMethodName, delay, new Object[] {}, SimEvent.DEFAULT_PRIORITY);
    }

/**
* Gets the name of the event (without the "do") from the given method name (with or without the "do")
**/
    private static String getEventNameFromMethod(String methodName) {
        return (methodName.startsWith("do") ) ? methodName.substring(2) : methodName;
    }

/**
* Returns a SimEvent that is no longer active to the pool.
**/
    public static synchronized void returnSimEventToPool(SimEvent event) {
        eventPool.push(event);
        if (verbose) {
            Schedule.getOutputStream().println("\tEvent " + event.hashCode() +
                " returned to Event pool");
        }
    }
/**
* Increases the size of the event pool.
* @param byThisMany How many SimEvent's to add to the pool.
**/
    protected static synchronized void augmentEventPool(int byThisMany) {
        for (int i = 0; i < byThisMany; i++) {
            eventPool.push(new SimEventImpl());
        }
    }

/**
* The initial size of the event pool.
**/
    public static void setInitialCapacity(int cap) {initialCapacity = cap;}

/**
* The number of new SimEvents to add to the pool when the pool must be expanded.
**/
    public static void setIncrement(int inc) {increment = inc;}

    public static void setVerbose(boolean v) {verbose = v;}
    public static boolean isVerbose() {return verbose;}

}

package simkit;

import java.util.Stack;

/**
 *  A factory for creating SimEvents. 
 *  This class has nothing but "factory" methods for creating SimEvents based
 *  on the type of data needed for the event.
 * <P/>Keeps a pool of already created SimEvents for performance. This class handles 
 * replenishing the pool when needed. Users of this class should call one of the createSimEvent
 * methods instead of constructing SimEvents directly. When a SimEvent is no longer needed,
 * it should be returned to the pool for reuse by calling returnSimEventToPool.
 *
 *  @version $Id$
 *  @author Arnold Buss
**/
public class SimEventFactory {

/**
* Holds the pool of SimEvents.
**/
    private static Stack eventPool;

/**
* The initial size of the pool. Note: This method is called
* during static initialization to set the initial size of the pool. Subsequent
* calls have no effect.
**/
    private static int initialCapacity;

/**
* The number of SimEvents that will be added to the pool when
* it is augmented during a call to createSimEvent. The default is 50.
**/
    private static int increment;

    private static final int DEFAULT_INITIAL_CAPACITY = 50;
    private static final int DEFAULT_INCREMENT = 50;

/**
* If true, print debug/trace information.
**/
    private static boolean verbose;

/**
* Holds the serial number of the next event to be created.
* Each event returned by createSimEvent will have a unique 
* serial number.
**/
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
* No need to instantiate since all methods are static.
**/
   private SimEventFactory() {}

/**
* Creates a SimEvent with the given parameters. This is the full implementation with no default values.
* @param source The SimEntity that is scheduling this SimEvent. When the event occurs the source will
* first attempt to execute the event, then notify any registered SimEventListeners. 
* @param eventName The name of the event method. Event methods must start with "do", however
* the "do" is optional in the event name parameter. (i.e., A SimEvent to execute the "doRun"
* method can be scheduled as either "Run" or "doRun")
* @param delay The time the event will occur.
* @param params An Object array containing the parameters for the event method.
* Primitives should be wrapped in the appropriate Object.
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
        event.setCreationTime(source.getEventList().getSimTime());

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
* method can be scheduled as either "Run" or "doRun")
* @param delay The time the event will occur.
* @param params An Object array containing the parameters for the event method.
* Primitives should be wrapped in the appropriate Object.
**/ 
   public static SimEvent createSimEvent(
                       SimEntity source,
                       String eventName,
                       double delay,
                       Object[] params
                       ) {
     return createSimEvent(source, eventName, delay, params, SimEvent.DEFAULT_PRIORITY);
   }

/**
* Creates a SimEvent with a default priority and no parameters.
* @param source The SimEntity that is scheduling this SimEvent. When the event occurs the source will
* first attempt to execute the event, then notify any registered SimEventListeners. 
* @param eventName The name of the event method. Event methods must start with "do", however
* the "do" is optional in the event name parameter. (i.e., A SimEvent to execute the "doRun"
* method can be scheduled as either "Run" or "doRun")
* @param delay The time the event will occur.
**/ 
    public static SimEvent createSimEvent(
                       SimEntity source,
                       String eventName,
                       double delay
                       ) {
        return createSimEvent(source, eventName, delay, new Object[] {}, SimEvent.DEFAULT_PRIORITY);
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
            System.out.println("\tEvent " + event.hashCode() +
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

/**
* If true, print debug/trace information.
**/
    public static void setVerbose(boolean v) {verbose = v;}

/**
* If true, print debug/trace information.
**/
    public static boolean isVerbose() {return verbose;}

}

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
* Base interface for simulation entities.
*
* This interface defines the basic event scheduling
* behavior of simulation entities in the simkit
* structure.  The default implementation is in
* SimEntityBase.
*
* <P>Changes
* <UL>
* <LI> 22 Oct 1998 - SimEntity now extends SimEventSource and SimEventListener.
* This should not impact anything, but will make references to SimEntity
* full-featured.  That is, they will now be able to dispatch and listen to
* other SimEntitys' SimEvents. [AB]
* </UL>
*
* @author K. A. Stork
* @author Arnold Buss
* @version 1.0
*
**/

package simkit;

public interface SimEntity extends Named,
                                   SimEventSource,
                                   SimEventListener,
                                   java.io.Serializable
{

   public static final double DEFAULT_PRIORITY    = 0.0;
   public static final String DEFAULT_ENTITY_NAME = "SimEntity";
   public static final String DEFAULT_EVENT_NAME  = "SimEvent";
   public static final String EVENT_METHOD_PREFIX = "do";

   public static final String NL = System.getProperty("line.separator");
   
/**
 * Schedule an event after a delay of delay from the current
 * simulation time.
 *
 * @see simkit.SimEvent
 * @param eventName The name of the scheduled event (prefixed by "do" for method name).
 * @param delay The amount of time before the event is scheduled
 * @param parameters The parameters passed to the scheduled event.
 * @param eventPriority The priority of this event (higher is better).
 * @param name The name of the event, if different from the first argument.
**/
    public SimEvent waitDelay(
              String      eventName,
              double      delay,
              Object[]    parameters,
              double      eventPriority
            );
/**
 * The most common convenience waitDelay method.
 * @param eventName The name of the scheduled event (prefixed by "do" for method name).
 * @param delay The amount of time before the event is scheduled
**/
   public SimEvent waitDelay( String eventName, double delay  );

/**
 *  The second most common convenience waitDelay method.
 * @param eventName The name of the scheduled event (prefixed by "do" for method name).
 * @param delay The amount of time before the event is scheduled
 * @param param The (single) parameter passed.  Note that it is not an array.
**/
   public SimEvent waitDelay( String eventName, double delay, Object param  );

/**
 * Interrupt the next pending event with name eventName
 * and interruption parameter array "parameters"
 * belonging to this object.
**/   
   public void interrupt(String eventName, Object[] parameters);
   
/**
 * Interrupt the next pending event.
**/   
   public void interrupt();
   
/**
 * Interrupt all pending events with name eventName
 * and interruption parameter array "parameters"
 * belonging to this object.
**/   
   public void interruptAll(String eventName, Object[] parameters);
   
/**
 * Interrupt all pending events .
**/   
   public void interruptAll();

/**
 *  Typically an Event is handled (as opposed to processed, as in SimEventListener)
 *  by actually executing a method.
 * @param event The SimEvent to be handled.
**/
   public void handleSimEvent(SimEvent event);

/**
 * Return this Entity's scheduling priority.
 * Entity scheduling priorities are more important than
 * event priorities.
**/
   public double getPriority ();
   
/**
 * Set this Entity's scheduling priority.

 * Entity scheduling priorities are more important than
 * event priorities.
**/
   public void setPriority (double d);

/**
 * Reset SimEntity to its pristine state.
**/
  public void reset();

/**
 *  True if this SimEntity has the doRun() method.
**/

  public boolean isReRunnable();
  
  public int getSerial();

}

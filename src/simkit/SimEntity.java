package simkit;

/**
 * Base interface for simulation entities.
 *
 * This interface defines the basic event scheduling
 * behavior of simulation entities in the simkit
 * structure.  The reference implementations are BasicSimEntity and
 * SimEntityBase.
 *
 * @author K. A. Stork
 * @author Arnold Buss
 * @version $Id: SimEntity.java 1000 2007-02-15 19:43:11Z ahbuss $
 *
**/
public interface SimEntity extends Named,
                                   SimEventSource,
                                   SimEventListener,
                                   PropertyChangeSource,
                                   Comparable<SimEntity>
{

   public static final Priority DEFAULT_PRIORITY    = Priority.DEFAULT;
   public static final String DEFAULT_ENTITY_NAME = "SimEntity";
   public static final String DEFAULT_EVENT_NAME  = "SimEvent";
   public static final String EVENT_METHOD_PREFIX = "do";

   public static final String NL = System.getProperty("line.separator");
   
/**
 * Schedule an event after a delay from the current
 * simulation time.
 *
 * @see simkit.SimEvent
 * @param eventName The name of the scheduled event (prefixed by "do" for method name).
 * @param delay The amount of time before the event is scheduled
 * @param priority The priority of this event (higher is better).
 * @param parameters The parameters passed to the scheduled event.
**/
   public SimEvent waitDelay( String eventName, double delay, Priority priority, 
           Object... parameters  );
/**
 * Schedule an event with no parameters and a default priority after a delay from
 * the current simulation time.
 * @param eventName The name of the scheduled event (prefixed by "do" for method name).
 * @param delay The amount of time before the event is scheduled
**/
   public SimEvent waitDelay( String eventName, double delay, Object... parameters  );

/**
 * Interrupt (cancel) the next pending event with name eventName
 * and interruption parameter array "parameters"
 * belonging to this object.
**/   
   public void interrupt(String eventName, Object... parameters);  
/**
 * Interrupt (cancel) all pending events with name eventName
 * and interruption parameter array "parameters"
 * belonging to this object.
**/   
   public void interruptAll(String eventName, Object... parameters);
   
/**
 * Interrupt (cancel) all pending events for this entity.
**/   
   public void interruptAll();

/**
 *  Typically an Event is handled (as opposed to processed, as in SimEventListener)
 *  by actually executing a method.
 * @param event The SimEvent to be handled.
**/
   public void handleSimEvent(SimEvent event);

/**
 * If two events occur at the same time with the same event priority,
 * the one with the highest entity priority will be processed first.
**/
   public Priority getPriority ();
   
/**
 * If two events occur at the same time with the same event priority,
 * the one with the highest entity priority will be processed first.
**/
   public void setPriority (Priority d);

/**
 * Reset SimEntity to its pristine state.
**/
  public void reset();

/**
 *  True if this SimEntity has the doRun() method.
**/
  public boolean isReRunnable();
  
/**
* A unique number assigned to this entity when it is constructed.
**/
  public int getSerial();
  
/*
 * Sets the SimEntity to be persistent.  By default, SimEntites will be persistant.
 */
  public void setPersistant(boolean persist);
  
/*
 * Whether or not the SimEntity is persistent.  Non-persistant SimEntities will be
 * cleared by Schedule between runs.
 */
  public boolean isPersistant();
  
  public void setEventListID(int id);
  
  public int getEventListID();
  
  public EventList getEventList();

}

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
* An abstraction of an event in a descrete event simulation. 
* 
* @version $Id$
* @author Kirk A. Stork
* @author Arnold H. Buss
*
**/

public interface SimEvent {

   public static final double   DEFAULT_PRIORITY = 0.0;

/**
* The SimEntity that owns this event. This SimEntity will
* first process the event and then notify any other SimEntities
* who have registered as listeners.
**/
   public SimEntity getSource();

/**
* Gets the name and signature of the method associated with this event.
* Event methods must begin with "do"
**/
   public String getFullMethodName();

/**
* Returns an array containing the parameters for the event method.
* Primative arguments will be wrapped in the appropriate Object.
**/
   public Object[] getParameters();

/**
* Sets the SimEntity that owns this event. This SimEntity will
* first process the event and then notify any other SimEntities
* who have registered as listeners.
**/
    public void setSource(SimEntity source);

/**
* Determine if the parameters for this event have the same value 
* as the array of parameters. Note: Equality is 
* determined by the equals() methods of the Objects.
* @param parameters The parameters of this event are compared to this array of parameters
* @return true If the paramaters have the same values as the parameters
*/
    public boolean interruptParametersMatch(Object[] parameters);

/**
* Cancels this event. The SimEventState is set to INTERRUPTED,
* the event is not immediatly removed from the event list.
**/
    public void interrupt();

/**
* Sets the serial, which is the number of SimEvents that
* have been processed that match this SimEvent's method name
* and signature.
**/
    public void setSerial(int num);

/**
* Sets the ID, which is a unique identifier for a SimEvent.
**/
    public void setID(int num);

/**
* Indicates the state that this SimEvent is in.
* @see simkit.SimEventState
**/
    public void setWaitState(SimEventState ws);

/**
* Sets the parameters that will be passed to the event method
* associated with this event.
**/
    public void setParameters(Object[] params);

/**
* Sets the name of the event method. If the name
* does not start with "do", the implementation should add it.
**/
    public void setEventName(String eventName);

/**
* Set the simulation time at which this event will occure.
**/
    public void setScheduledTime(double time);

/**
* If two events occur at the same time, the one with the higher priority
* will be processed first.
**/
    public void setPriority(double priority);

/**
* Set the simulation time at which this event was created. 
**/
    public void setCreationTime(double time);

/**
* The name of the event method associated with this event.
* Must start with "do"
**/
    public String getMethodName();

/**
* Indicates the state that this SimEvent is in.
* @see simkit.SimEventState
**/
    public SimEventState getWaitState();

/**
* The simulation time at which this event will occur.
**/
    public double getScheduledTime();

/**
* The priority of this event. For two events scheduled at the
* same time, the one with the higher priority will be processed first.
**/
    public double getEventPriority();

/**
* The simulation time at which this event was created.
**/
    public double getCreationTime();

/**
* The default event priority of the owner of this SimEvent.
* If two events are scheduled at the same time and have the same
* priority, then the one with the highest ownerPriority will be
* processed first.
**/
    public double getOwnerPriority();

/**
* The number of SimEvents that
* have been processed that match this SimEvent's method name
* and signature.
**/
    public int getSerial();

/**
* Returns the name of the event method plus a list of the parameters.
**/
    public String paramString();

/**
* The name of the event. This same as the methodName without the "do"
**/
    public String getEventName();

/**
* A unique identifier for this event.
**/
    public int getID();

/**
* True if this event has been scheduled and not interupted (canceled)
**/
    public boolean isPending();

/**
* Reinitializes the SimEvent.
**/
    public void reset();

} 

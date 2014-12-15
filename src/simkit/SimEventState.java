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
 * A Java enumeration that indicates what state a SimEvent is in. <P>
 * The possible states are:
 * <OL>
 * <LI>WAITING: The event is on the event list waiting to be processed.</LI>
 * <LI>INTERRUPTED: The event may be on the event list, but has been
 * cancelled and will never be processed.</LI>
 * <LI>UNUSED: The event object is currently not is use.</LI>
 * </OL>
 *
 *  @version $Id$
 *  @author Arnold Buss
**/
public class SimEventState {

/**
* The event is on the event list waiting to be processed.
**/
    public static final SimEventState WAITING = new SimEventState("WAITING");
/**
* The event has been processed by its scheduling SimEntity.
**/
    public static final SimEventState PROCESSED = new SimEventState("PROCESSED");

/**
* The event has been cancelled and will never be processed.
**/
    public static final SimEventState INTERRUPTED = new SimEventState("INTERRUPTED");

/**
* The event object is not currently in use.
**/
    public static final SimEventState UNUSED = new SimEventState("UNUSED");

/**
* The name of this event state.
**/
    private String name;

/**
* Construct a new SimEventState with the given name. This constructor
* should only be called during static initialization of this class.
**/
    protected SimEventState(String name) {this.name = name;}

/**
* Returns the name of the SimEventState.
**/
    public String toString() {return name;}
  
}

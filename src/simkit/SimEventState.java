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
 *  This Java-style "Enumeration" is all that is left of the dozen or so
 *  states a SimEvent originally could be in.  Now it is either WAITING
 *  (which would be better named "PENDING"), menaing it is on the event list
 *  waiting to occur, or INTERRUPTED, meaning that it may still be on the event
 *  list but will never occur.
 *  @version 1.1.2
 *  @author Arnold Buss
**/

public class SimEventState {

    public static SimEventState WAITING = new SimEventState("WAITING");
    public static SimEventState INTERRUPTED = new SimEventState("INTERRUPTED");
    public static SimEventState UNUSED = new SimEventState("UNUSED");

    private String name;
    protected SimEventState(String name) {this.name = name;}
    public String toString() {return name;}
  
}
// FILE SimEventComp.java

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
* Binary predicate (comparator) for simkit SimEvents.
*
* This functor is used to prioritize events on the
* Schedule, and in the individual SimEntities active
* agendas.  This comparator should prevent ties from
* arrising.
*
* <P> Changes
* <UL>
* <LI> 9/30/98 - Changed from serial to ID per new Event counting.
* </UL>
* <br>
*
* The order of importance regarding scheduling is
* as follows:
*
* <br>
*
* 1. Scheduled time of the events <br>
* 2. Priorities of the SimEntities that own the SimEvents <br>
* 3. Priority of the SimEvents <br>
* 4. Time the SimEvents were instantiated <br>
* 5. Order in which the SimEvents were created <br>
*
* @author K. A. Stork
* @uathor Arnold H. Buss
* @version 1.0
*
**/
package simkit;

public class SimEventComp implements java.util.Comparator
{
   public int compare( Object fst, Object snd ) {
      if (fst.equals(snd)) {return 0;}
      if (snd.equals(fst)) {return 0;}

      SimEvent b = (SimEvent)fst;
      SimEvent a = (SimEvent)snd;

      if ( a.getScheduledTime() > b.getScheduledTime() ) return -1;
      if ( a.getScheduledTime() < b.getScheduledTime() ) return 1;     
      if ( a.getEventPriority() < b.getEventPriority() ) return -1;
      if ( a.getEventPriority() > b.getEventPriority() ) return 1;
      if ( a.getOwnerPriority() > b.getOwnerPriority() ) return -1;
      if ( a.getOwnerPriority() < b.getOwnerPriority() ) return 1;
      if ( a.getCreationTime()  > b.getCreationTime() )  return -1;
      if ( a.getCreationTime()  < b.getCreationTime() )  return 1;
      if ( a.getID()        > b.getID() )        return -1;
      if ( a.getID()        < b.getID() )        return 1;
      return 0;
   }
}

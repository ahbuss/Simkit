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
  * Interface for a listener of SimEvents.  Uses the standard java beans
  * listener model.  The one method is invoked when a SimEvent is fired by
  * its source.
  *
  * @see SimEvent
  * @see SimEventSource
  *
  * @author Arnold Buss 
  * @version Simkit 0.5.0
  *
**/

public interface SimEventListener {

/**
  * Process the fired SimEvent.  What is done is, of course, entirely up
  * to the listener.
  * <I>Note:</I> Should this be changed to "handleSimEvent" in accord with Modkit
  * conventions??
  *
  * @param SimEvent event
**/
   public void processSimEvent(SimEvent event);
}
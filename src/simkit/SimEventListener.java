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
  * @version $Id: SimEventListener.java 476 2003-12-09 00:27:33Z jlruck $
  *
**/
public interface SimEventListener {

/**
  * Process the fired SimEvent.  What is done is, of course, entirely up
  * to the listener. Listeners should not process other entity's doRun Events.
  * <I>Note:</I> Should this be changed to "handleSimEvent" in accord with Modkit
  * conventions??
  *
  * @param event The SimEvent to process.
**/
   public void processSimEvent(SimEvent event);
}

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
  * Interface for a source of SimEvents.  Uses the standard java beans
  * listener model.  Methods are available for SimEventListeners to
  * register and un-register.
  *
  * @see SimEvent
  * @see SimEventListener
  *
  * @author Arnold Buss 
  * @version Simkit 0.5.0
  *
**/

public interface SimEventSource  {

/**
  * register listener
  * @param SimEventListener s
**/
   public void addSimEventListener(SimEventListener s);
 /**
  * unregister listener
  * @param SimEventListener s
**/
  public void removeSimEventListener(SimEventListener s);
/**
  * notify listeners
  * @param SimEvent event
**/
   public void notifyListeners(SimEvent event);
/**
 *  @return Array of SimEventListeners
 */   
   public SimEventListener[] getSimEventListeners();
}
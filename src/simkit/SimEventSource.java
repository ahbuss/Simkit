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
  * @version $Id$
  *
**/
public interface SimEventSource  {

/**
  * Registers a listener.
  * @param listener 
**/
   public void addSimEventListener(SimEventListener listener);

 /**
  * Unregister a listener.
  * @param listener
**/
  public void removeSimEventListener(SimEventListener listener);

/**
  * Notify registered listeners by calling their processSimEvent method.
  * @param event the event to notify listeners of.
**/
   public void notifyListeners(SimEvent event);

/**
 * Get an array conatianing all registered listeners.
 *  @return Array of SimEventListeners
 */   
   public SimEventListener[] getSimEventListeners();
}

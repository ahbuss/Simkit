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
 * Interface for a source of SimEvents. Uses the standard java beans listener
 * model. Methods are available for SimEventListeners to register and
 * un-register.
 *
 * @see SimEvent
 * @see SimEventListener
 * @author Arnold Buss
 * 
*
 */
public interface SimEventSource {

    /**
     * Registers a listener.
     *
     * @param listener SimEventListener to add
     */
    public void addSimEventListener(SimEventListener listener);

    /**
     * Unregister a listener.
     *
     * @param listener SimEventListener to remove as listener
     */
    public void removeSimEventListener(SimEventListener listener);

    /**
     * Notify registered listeners by calling their processSimEvent method.
     * <p>
     * TODO: should be eliminated or moved: This is not really part of the
     * public contract for the listener pattern, but rather one way to implement
     * that contract. It should be the object's responsibility to figure out
     * when and how to notify its listeners, so no externally invokable trigger
     * should be provided.
     *
     * NOTE: Un-deprecating this method.
     *
     * @param event the event to notify listeners of.
*
     */
    public void notifyListeners(SimEvent event);

    /**
     * Get an array containing all registered listeners.
     *
     * @return Array of SimEventListeners
     */
    public SimEventListener[] getSimEventListeners();
}

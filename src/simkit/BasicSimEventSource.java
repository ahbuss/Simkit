package simkit;

import java.util.ArrayList;

/**
 *  A basic implementation of a SimEventSource that is potentially useful for
 *  either subclassing or as a delegate.  A basic implementation of the Listener
 *  pattern.  It does attempt to be somewhat thread safe.
 *  @author Arnold Buss
 *  @version $Id$
 **/
public class BasicSimEventSource implements SimEventSource {
    
   /**
   * The SimEventListeners who have registered.
   **/
    private ArrayList listeners;
    
   /**
   * Construct a new BasicSimEventSource. 
   **/
    public BasicSimEventSource() {
        listeners = new ArrayList();
    }
    
    /**
     * Registers the given SimEventListener to be notified of all SimEvents from 
     * this source.
     *  Note that the listener is added only if it is not already a listener.
     *  @param listener The SimEventListener that is the new listener.
     **/
    public void addSimEventListener(SimEventListener listener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }
    
    /**
     * Unregisters the given SimEventListener. Does nothing if the SimEventListener
     * wasn't registered.
     *  @param listener The SimEventListener to be removed as a listener.
     **/
    public void removeSimEventListener(SimEventListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Causes all registered SimEventListeners to be notified of the given SimEvent.
     * @param event The SimEvent that all SimEventListeners are notified has occured.
     **/
    public void notifyListeners(SimEvent event) {
        ArrayList listenersCopy = null;
        synchronized(listeners) {
            listenersCopy = (ArrayList) listeners.clone();
        }
        for (int i = 0; i < listenersCopy.size(); i++) {
            ((SimEventListener) listenersCopy.get(i)).processSimEvent(event);
        }
    }
    
    /**  
     * Returns a copy of the currently registered SimEventListeners.
     * @return Array of SimEventListeners
     */
    public SimEventListener[] getSimEventListeners() {
        SimEventListener[] listenerArray = new SimEventListener[0];
        synchronized (listeners) {
            listenerArray = (SimEventListener[]) listeners.toArray(listenerArray);
        }
        return listenerArray;
    }
    
}

package simkit;
/**
 *  A basic implementation of a SimEventSource that is potentially useful for
 *  either subclassing or as a delegate.  A basic implementation of the Listener
 *  pattern.  It does attempt to be somewhat thread safe.
 *  @author Arnold Buss
 *  @version 0.5
 **/

import java.util.*;
public class BasicSimEventSource implements SimEventSource {
    
    private ArrayList listeners;
    
    public BasicSimEventSource() {
        listeners = new ArrayList();
    }
    
    /**
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
     *  @param listener The SimEventListener to be removed as a listener.
     **/
    public void removeSimEventListener(SimEventListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
    
    /**
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
    
}

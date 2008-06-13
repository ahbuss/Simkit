package simkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *  A basic implementation of a SimEventSource that is potentially useful for
 *  either subclassing or as a delegate.  A basic implementation of the Listener
 *  pattern.  It does attempt to be somewhat thread safe.
 * 
 * 23 May 2004 - kas - added <code>isMyEvent()</code> method.
 * 27 October 2006 - ab - removed isMyEvent()
 * 
 *  @author Arnold Buss
 *  @author Kirk Stork
 *  @version $Id$
 **/
public class BasicSimEventSource implements SimEventSource {
    
    public static final Logger log = Logger.getLogger("simkit");
    
   /**
   * The SimEventListeners who have registered.
   **/
    private List<SimEventListener> listeners;
    
   /**
   * Construct a new BasicSimEventSource. 
   **/
    public BasicSimEventSource() {
        listeners = Collections.synchronizedList(new ArrayList<SimEventListener>());
    }
    
    /**
     * Registers the given SimEventListener to be notified of all SimEvents from 
     * this source.
     *  Note that the listener is added only if it is not already a listener.
     *  @param listener The SimEventListener that is the new listener.
     **/
    @Override
    public void addSimEventListener(SimEventListener listener) {
        if (listener == null) {
            log.fine("addSimEventListener called with null");
            return;
        }
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
    @Override
    public void removeSimEventListener(SimEventListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Causes all registered SimEventListeners to be notified of the given SimEvent.
     * @param event The SimEvent that all SimEventListeners are notified has occured.
     **/
    @Override
    public void notifyListeners(SimEvent event) {
        ArrayList<SimEventListener> listenersCopy = null;
        synchronized(listeners) {
            listenersCopy = new ArrayList<SimEventListener>(listeners);
        }
        for (int i = 0; i < listenersCopy.size(); i++) {
            listenersCopy.get(i).processSimEvent(event);
        }
    }
    
    /**  
     * Returns a copy of the currently registered SimEventListeners.
     * @return Array of SimEventListeners
     */
    @Override
    public SimEventListener[] getSimEventListeners() {
        SimEventListener[] listenerArray = new SimEventListener[0];
        synchronized (listeners) {
            listenerArray = listeners.toArray(listenerArray);
        }
        return listenerArray;
    }
}

package simkit;

import java.util.*;

public class SimEventSourceImpl
        implements SimEventSource {

    private Vector listeners;

    public SimEventSourceImpl() {
        listeners = new Vector();
    }

/**
 *  Add a SimEventListener
 *  @param s The listener to be added.
**/
    public void addSimEventListener(SimEventListener s) {
        synchronized(listeners) {
            if (!listeners.contains(s)) {
                listeners.addElement(s);
            }
        }
    }
   
/**
 *  Remove a SimEventListener
 *  @param s The listener to be removed.
**/
    public void removeSimEventListener(SimEventListener s) {
        synchronized (listeners) {
            listeners.removeElement(s);
        }
    }

/**
 *  Please comment this, Buss!
**/
    public void notifyListeners(SimEvent event) {
        SimEventListener nextListener;
        Vector listenersCopy = null;
        synchronized (listeners) {
            listenersCopy = (Vector) listeners.clone();
        }

        for (Iterator e = listenersCopy.iterator();e.hasNext(); ) {
            nextListener = ((SimEventListener) e.next());
            nextListener.processSimEvent(event);
        }
   }


}

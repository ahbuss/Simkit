package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listens for PropertyChangeEvents and remembers last one.  Useful for
 * Unit testing purposes.  Recommended usage: instantiate one of these
 * for each state variable in the class being tested and add them as
 * PropertyChangeListeners for those specific events.  To test whether
 * a state transition is being correctly fired, invoke a 'do' method
 * directly (contrary to "normal" operation) and check whether the 
 * lastEvent is non-null (if property is to be changed) or null (if 
 * property is not to be fired).  The newValue and oldValue of the lastEvent 
 * could also be checked.
 *
 * 
 * @author ahbuss
 */
public class PropertyChangeListenerHelper implements PropertyChangeListener {
    
    private PropertyChangeEvent lastEvent;
    
    /**
     * @param propertyChangeEvent heard PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        lastEvent = propertyChangeEvent;
    }

    /**
     * @return last heard PropertyChangeEvent (or null if none heard yet)
     */
    public PropertyChangeEvent getLastEvent() {
        return lastEvent;
    }
    
    /**
     * Sets lastEvent to null
     */
    public void reset() {
        lastEvent = null;
    }
    
}

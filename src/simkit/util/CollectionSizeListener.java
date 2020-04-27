package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import simkit.Schedule;
import simkit.BasicSimEntity;

/**
 * Listens for a PropertyChangeEvent where the propert value
 * is a Collection (Set, List, Map, etc).
 *
 * Fires a new PropertyChangeEvent of the same name but with
 * ".size" appended.   The value of the property fired is
 * the size of the collection.
 *
 * This is useful when containers are used for state and
 * statistics need to be collected on the size of the
 * containers.
 *
 * Note that it subclasses BasicSimEntity just for the
 * convenience of its PropertyChangeSource methods
 * (registering and unregistering PropertyChangeListeners
 * and firing PropertyChangeEvents to listeners)
 * 
 * @author ahbuss
 */
public class CollectionSizeListener extends BasicSimEntity implements PropertyChangeListener {
    
    /**
     *
     */    
    public CollectionSizeListener() {
        setPersistant(false);
    }
    
    /**
     * If the property is of type Collection, fire a
     * new PropertyChangeEvent of the same name with ".size"
     * appended, whose value is the size of the Collection.
     * @param e Heard event
     */    
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getNewValue() instanceof Collection) {
            firePropertyChange(e.getPropertyName() + ".size",
                ((Collection) e.getNewValue()).size());
        }
    }
    
    /**
     * Does nothing, since no SimEvents are scheduled by this class
     * @param event SimEvent from EventList
     */    
    public void handleSimEvent(simkit.SimEvent event) {
    }
    
    /**
     * Does nothing, since this class should not respond to
     * any SimEvents
     * @param event Heard SimEvent
     */    
    public void processSimEvent(simkit.SimEvent event) {
    }
    
}

package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import simkit.Schedule;
import simkit.BasicSimEntity;

/**
 * Listens for an IndexedPropertyChangeEvent where the propert value
 * is a Collection (Set, List, Map, etc).
 *
 * Fires a new IndexedPropertyChangeEvent of the same name but with
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
 * @version $Id$
 * @author ahbuss
 */
public class IndexedCollectionSizeListener extends BasicSimEntity implements PropertyChangeListener {
    
    /**
     *
     */    
    public IndexedCollectionSizeListener() {
        setPersistant(false);
    }
    
    /**
     * If the event is an IndexedPropertyChangeEvent and
     * the property is of type Collection, fire a
     * new IndexedPropertyChangeEvent of the same name and index with ".size"
     * appended, whose value is the size of the Collection.
     * @param e Heard event
     */    
    public void propertyChange(PropertyChangeEvent e) {
        if ( e instanceof IndexedPropertyChangeEvent && 
            e.getNewValue() instanceof Collection) {
            fireIndexedPropertyChange(
            ((IndexedPropertyChangeEvent) e).getIndex(),
            e.getPropertyName() + ".size",
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

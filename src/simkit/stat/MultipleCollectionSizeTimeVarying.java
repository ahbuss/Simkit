package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import simkit.util.IndexedPropertyChangeEvent;

/**
 * Listen for IndexedPropertyChangeEvent of type Collection.
 * The size() value is passed to the super class.  The
 * end result is that the size of a Collection property
 * has time-varying statistics collected.
 * @version $Id$
 * @author ahbuss
 */
public class MultipleCollectionSizeTimeVarying extends MultipleSimpleStatsTimeVarying {
    
    /**
     * Instantiate a MultipleCollectionSizeTimeVarying to listen to
     * a property of type Collection
     * @param name Name of Collection property
     */
    public MultipleCollectionSizeTimeVarying(String name) {
        super(name + ".size");
    }
    
    /**
     * If newValue is a Collection, and the PropertyChangeEvent is
     * an IndexedPropertyChangeEvent, pass a new IndexedPropertyChangeEvent
     * to the super class.  The property name is that of the
     * Collection's property with ".size" appended.
     * @param e Heard PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent e) {
        Object container = e.getNewValue();
        if (e instanceof IndexedPropertyChangeEvent) {
            if (container instanceof Collection) {
                super.propertyChange(new IndexedPropertyChangeEvent(
                    e.getSource(), e.getPropertyName() + ".size",
                null, new Integer(((Collection) container).size()),
                ((IndexedPropertyChangeEvent)e).getIndex()));
            }
        }
    }
    
}

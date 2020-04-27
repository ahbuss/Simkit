package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

/**
 * Listen for PropertyChangeEvent of type Collection.
 * The size() value is passed to the super class.  The
 * end result is that the size of a Collection property
 * has time-varying statistics collected.
 *
 * 
 * @author ahbuss
 */
public class CollectionSizeTimeVaryingStats extends SimpleStatsTimeVarying {
    
    /**
     * Instantiate a CollectionSizeTimeVarying to listen to
     * a property of type Collection
     * @param name Name of Collection property
     */    
    public CollectionSizeTimeVaryingStats(String name) {
        super(name + ".size");
    }
    
    /**
     * If this constructor is used, a separate call to setName(String) 
     * must be made to make it at all useful.
     */
    public CollectionSizeTimeVaryingStats() {
        super();
    }
    
    /**
     * If newValue is a Collection, pass a new PropertyChangeEvent
     * to the super class.  The property name is that of the
     * Collection's property with ".size" appended.
     * @param e Heard PropertyChangeEvent
     */    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Object container = e.getNewValue();
        if (container instanceof Collection) {
            super.propertyChange(new PropertyChangeEvent(e.getSource(), e.getPropertyName() + ".size", 
                null, ((Collection) container).size()));
        }
    }
    
}

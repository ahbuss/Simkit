package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import simkit.stat.SimpleStatsTimeVarying;

/**
 * Listen for PropertyChangeEvent of type Collection.
 * The size() value is passed to the super class.  The
 * end result is that the size of a Collection property
 * has time-varying statistics collected.
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
     * If newValue is a Collection, pass a new PropertyChangeEvent
     * to the super class.  The property name is that of the
     * Collection's property with ".size" appended.
     * @param e Heard PropertyChangeEvent
     */    
    public void propertyChange(PropertyChangeEvent e) {
        Object container = e.getNewValue();
        if (container instanceof Collection) {
            super.propertyChange(new PropertyChangeEvent(e.getSource(), e.getPropertyName() + ".size", 
                null, new Integer(((Collection) container).size())));
        }
    }
    
}

package simkit.examples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import simkit.Schedule;
import simkit.SimEntityBase;

/**
 *
 * @author  ahbuss
 */
public class CollectionSizeListener extends SimEntityBase implements PropertyChangeListener {
    
    public CollectionSizeListener() {
        Schedule.removeRerun(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getNewValue() instanceof Collection) {
            firePropertyChange(e.getPropertyName() + ".size",
                ((Collection) e.getNewValue()).size());
        }
    }
}

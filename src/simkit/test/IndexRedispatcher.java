package simkit.test;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class IndexRedispatcher extends PropertyChangeSupport implements PropertyChangeListener {

    private int lastIndex;
    private int increment;
    private int maxIndex;

    public IndexRedispatcher(Object bean, int start, int incr, int max) {
        super(bean);
        lastIndex = start;
        increment = incr;
        maxIndex = max;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        this.firePropertyChange(
            new IndexedPropertyChangeEvent(
                e.getSource(),
                e.getPropertyName(),
                e.getOldValue(),
                e.getNewValue(),
                lastIndex
            )
        );
        lastIndex += increment;
        lastIndex %= maxIndex;
    }
} 
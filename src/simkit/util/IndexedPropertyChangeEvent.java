package simkit.util;
import java.beans.PropertyChangeEvent;
/**
 *  Event to support indexed property change events.
 *  @see simkit.PropertyChangeDispatcher
 *  @author Arnold Buss
 *  @version $DATE$
**/
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {

    private int index;

    public IndexedPropertyChangeEvent(Object source, String propertyName,
        Object oldValue, Object newValue, int index) {
        super(source, propertyName, oldValue, newValue);
        this.index = index;
    }

    public int getIndex() { return index; }
} 
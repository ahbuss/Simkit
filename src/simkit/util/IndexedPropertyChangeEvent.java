package simkit.util;
import java.beans.PropertyChangeEvent;
/**
 *  Event to support indexed property changes.
 *  @see simkit.PropertyChangeDispatcher
 *  @author Arnold Buss
 *  @version $Id: IndexedPropertyChangeEvent.java 476 2003-12-09 00:27:33Z jlruck $
**/
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {

/**
* The index of the changed property.
**/
    private int index;

/**
* Creates a new event.
* @param source The Object that fired the change event.
* @param propertyName The name of the indexed property.
* @param oldValue The value of the property before this change.
* @param newValue The value of the property after this change.
* @param index The index of the property that changed.
**/
    public IndexedPropertyChangeEvent(Object source, String propertyName,
        Object oldValue, Object newValue, int index) {
        super(source, propertyName, oldValue, newValue);
        this.index = index;
    }

/**
* The index of the changed property.
**/
    public int getIndex() { return index; }
} 

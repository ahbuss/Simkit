package simkit.util;

import java.beans.PropertyChangeEvent;

/**
 * @version $Id$
 * @author ahbuss
 */
public class PropertyChangeEventX extends PropertyChangeEvent {

    private Object[] extraData;
    
    /**
     * 
     * @param source Source object of PropertyChangeEventX
     * @param propertyName Name of property
     * @param oldValue old value
     * @param newValue new value
     * @param extraData additional data to be carried with this event
     */
    public PropertyChangeEventX(Object source, String propertyName,
            Object oldValue, Object newValue, Object... extraData) {
        super(source, propertyName, oldValue, newValue);
        this.extraData = extraData;
    }

    /**
     * @return the extraData
     */
    public Object[] getExtraData() {
        return extraData.clone();
    }

    /**
     * 
     * @param index given index of extra data to return
     * @return extra data at given index
     * @throws ArrayIndexOutOfBoundsException if index &lt; extraData.length - 1
     */
    public Object getExtraData(int index) {
        return extraData[index];
    }
    
    /**
     * 
     * @return number of objects in extra data array
     */
    public int getExtraDataCount() {
        return extraData.length;
    }
}

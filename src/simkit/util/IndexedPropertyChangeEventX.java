package simkit.util;

/**
 * This allows the user to add additional data to the event to be used by
 * a listener
 * @author ahbuss
 */
public class IndexedPropertyChangeEventX extends java.beans.IndexedPropertyChangeEvent {
    
    private Object[] extraData;
    
    /**
     * 
     * @param source Given source
     * @param propertyName Given property name
     * @param oldValue Given old value
     * @param newValue Given new value
     * @param index Given index
     * @param extraData Optional additional data
     */
    public IndexedPropertyChangeEventX(
            Object source, String propertyName, Object oldValue, 
            Object newValue, int index, Object... extraData) {
        super(source, propertyName, oldValue, newValue, index);
    }
    
    /**
     * 
     * @return The number of extra data objects in this instance
     */
    public int getExtraDataCount() {
        return extraData.length;
    }
    
    /**
     * 
     * @param index given index
     * @return extra data at the given index
     * @throws ArrayIndexOutOfBoundsException if index &lt; number of data elements
     */
    public Object getExtraData(int index) {
        return this.extraData[index];
    }

    /**
     * @return the extraData
     */
    public Object[] getExtraData() {
        return extraData.clone();
    }
    
}

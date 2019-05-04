package simkit.util;

import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;

/**
 *
 * @author ahbuss
 * @param <T>
 * @param <S>
 */
public class StateChangeEvent<T, S> extends PropertyChangeEvent {

    private static final Logger LOGGER = Logger.getLogger(StateChangeEvent.class.getName());

    private final T key;
    
    private final Object[] extraData;

    /**
     *
     * @param key Can be used for indices or for arbitrary objects (such as enums)
     * @param source Source object
     * @param stateName name of state
     * @param oldValue the old value (may be null)
     * @param newValue the new value (shouldn't be null)
     * @param extraData Optional "extra" data, sometimes used for data loggers
     */
    public StateChangeEvent(T key, Object source, String stateName, S oldValue, S newValue, Object... extraData) {
        super(source, stateName, oldValue, newValue);
        this.key = key;
        this.extraData = extraData.clone();
    }

    /**
     * @return the key
     */
    public T getKey() {
        return key;
    }

    /**
     * @return (shallow copy of) the extraData
     */
    public Object[] getExtraData() {
        return extraData.clone();
    }
    

}

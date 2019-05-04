package simkit.util;

/**
 * Interface for listening to StateChangeEvents
 *
 * @author ahbuss
 * @param <T> The key for heard StateChangeEvents; may be ignored
 * @param <S> The type of the state in the StateChangeEvent
 */
public interface StateChangeListener<T, S> {

    /**
     * Respond to the given StateChangeEvent
     * @param evt Given StateChangeEvent
     */
    public void stateChange(StateChangeEvent<T, S> evt);
}

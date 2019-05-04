package simkit.util;

/**
 *
 * @author ahbuss
 */
public interface StateChangeSource  {
    
    public <T, S> void addPropertyChangeListener(StateChangeListener<T, S> listener);
    
    public <T, S> void addPropertyChangeListener(String property, StateChangeListener<T, S> listener);
    
    public <T, S> void removePropertyChangeListener(StateChangeListener<T, S> listener);
    
    public <T, S> void firePropertyChange(StateChangeListener<T, S> evt);
    
    public <T, S> void firePropertyChange(T key, Object source, String propertyName, S oldValue, S newValue);
}

package simkit.test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import simkit.util.StateChangeListener;
import simkit.util.StateChangeSource;
/**
 *
 * @author ahbuss
 */
public class TestSimpleStateChangeSource implements StateChangeSource {

    private static final Logger LOGGER = Logger.getLogger(TestSimpleStateChangeSource.class.getName());

    private Map<Object, StateChangeListener<Object, Object>> listeners;
    
    public TestSimpleStateChangeSource() {
        this.listeners = new HashMap<>();
    }
    
    @Override
    public <T, S> void addPropertyChangeListener(StateChangeListener<T, S> listener) {
    }

    @Override
    public <T, S> void addPropertyChangeListener(String property, StateChangeListener<T, S> listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T, S> void removePropertyChangeListener(StateChangeListener<T, S> listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T, S> void firePropertyChange(StateChangeListener<T, S> evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T, S> void firePropertyChange(T key, Object source, String propertyName, S oldValue, S newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

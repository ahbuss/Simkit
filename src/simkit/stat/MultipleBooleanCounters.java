package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Listens for Boolean properties.  Creates BooleanCounters on the fly
 * and keeps track of each one separately.  The property names should
 * be unique.
 *
 * @version $Id$
 * @author  ahbuss
 */
public class MultipleBooleanCounters implements PropertyChangeListener {
    
    protected Map counters;
    
    public MultipleBooleanCounters() {
        counters = new HashMap();
    }
    
    /**
     * If this is a Boolean-valued PropertyChangeEvent, update value of
     * BooleanCounter for that name.  If none exists, create one on the fly.
     * @param e Heard PropertyChangeEvent
     */    
    public void propertyChange(PropertyChangeEvent e) {
        Object value = e.getNewValue();
        if (value instanceof Boolean) {
            String name = e.getPropertyName();
            BooleanCounter counter = (BooleanCounter) counters.get(name);
            if (counter == null) {
                counter = new BooleanCounter(name);
                counters.put(name, counter);
            }
            counter.propertyChange(e);
        }
    }
    
    /**
     * Reset all existing BooleanCounters
     */    
    public void reset() {
        for (Iterator i = counters.keySet().iterator(); i.hasNext(); ) {
            ((BooleanCounter) counters.get(i.next())).reset();
        }
    }
    
    /**
     * Clear (empty) all created Boolean counters
     */    
    public void clear() {
        counters.clear();
    }
    
    /**
     * Returns copy of Map and of counters
     * @return Copy of all counters
     */    
    public Map getCounters() {
        Map copy = new HashMap(counters.size());
        for (Iterator i = counters.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            BooleanCounter value = (BooleanCounter) counters.get(key);
            copy.put(key, new BooleanCounter(value));
        }
        return copy;
    }
    
    /**
     *
     * @param name Name of counter desired
     * @return Copy of BooleanCounter for property name, or null if none exists
     */    
    public BooleanCounter getCounter(String name) {
        BooleanCounter counter = (BooleanCounter) counters.get(name);
        if (counter!= null) {
            counter = new BooleanCounter(counter);
        }
        return counter;
    }
    
    /**
     *
     * @return List of all Counters
     */    
    public String toString() {
        StringBuffer buf = new StringBuffer("MultipleBooleanCounter");
        for (Iterator i = counters.keySet().iterator(); i.hasNext(); ) {
            buf.append(System.getProperty("line.separator"));
            buf.append('\t');
            buf.append(counters.get(i.next()));
        }
        return buf.toString();
    }
    
}

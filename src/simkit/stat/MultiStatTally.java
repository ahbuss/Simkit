package simkit.stat;

import simkit.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Listens for PropertyChangeEvents that are associated with state changes
 * to be collected in a Tally manner.  A new SimpleStatsTally will be 
 * created on-the-fly if necessary.
 *
 * @author  Arnold Buss
 */
public class MultiStatTally implements PropertyChangeListener {
    
    private Map allStats;
    
    public MultiStatTally() {
        allStats = new TreeMap();
    }
    
    public void reset() {
        allStats.clear();
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        Object value = evt.getNewValue();
        if (value instanceof Number) {
            newObservation(evt.getPropertyName(), ((Number) value).doubleValue());
        }
        else if (value instanceof Boolean) {
            newObservation(evt.getPropertyName(), ((Boolean) value).booleanValue() );
        }
        else {
            try {
                newObservation(evt.getPropertyName(), Double.parseDouble(value.toString()));
            } catch (NumberFormatException e) {}
        }
    }
    
    protected void newObservation(String name, double value) {
        SimpleStatsTally sst = (SimpleStatsTally) allStats.get(name);
        if (sst == null) {
            sst = createNewStat(name);
        }
        sst.newObservation(value);
    }
    
    protected void newObservation(String name, boolean value) {
        newObservation(name, value ? 1.0 : 0.0);
    }
    
    protected SimpleStatsTally createNewStat(String name) {
        SimpleStatsTally newSST = new SimpleStatsTally(name);
        allStats.put(name, newSST);
        return newSST;
    }
    
    public SimpleStatsTally getStats(String name) {
        SimpleStatsTally sst = (SimpleStatsTally) allStats.get(name);
        return (SimpleStatsTally) sst.clone();
    }
    
    public SimpleStatsTally[] getAllStats() {
        SimpleStatsTally[] sst = new SimpleStatsTally[allStats.size()];
        return sst;
    }
    
    public String[] getNames() {
        return (String[]) allStats.keySet().toArray(new String[0]);
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("MultiStatTally");
        for (Iterator i = allStats.keySet().iterator(); i.hasNext(); ) {
            buf.append(SimEntity.NL);
            buf.append(allStats.get(i.next()));
        }
        return buf.toString();
    }
}

package simkit.stat;

import simkit.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Listens for PropertyChangeEvents that are associated with state changes
 * to be collected in a Tally manner.  A new SimpleStatsTally and a
 * new SimpleStatsTimeVarying will be created on-the-fly if necessary.
 * Thus, the user will have a choice as to whether to see Tally or
 * Time-Varying statistics.  Typically, of course, only one will
 * make any sense.
 *
 * @author  Arnold Buss
 */
public class MultiStat implements PropertyChangeListener {
    
    private Map tallyStats;
    private Map timeVaryingStats;
    
    public MultiStat() {
        tallyStats = new TreeMap();
        timeVaryingStats = new TreeMap();
    }
    
    public void reset() {
        tallyStats.clear();
        timeVaryingStats.clear();
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
        SimpleStatsTally sst = (SimpleStatsTally) tallyStats.get(name);
        if (sst == null) {
            sst = createNewTallyStat(name);
        }
        sst.newObservation(value);
        SimpleStatsTimeVarying sstv = (SimpleStatsTimeVarying) timeVaryingStats.get(name);
        if (sstv == null) {
            sstv = createNewTimeVaryingStat(name);
        }
        sstv.newObservation(value);
    }
    
    protected void newObservation(String name, boolean value) {
        newObservation(name, value ? 1.0 : 0.0);
    }
    
    protected SimpleStatsTally createNewTallyStat(String name) {
        SimpleStatsTally newSST = new SimpleStatsTally(name);
        tallyStats.put(name, newSST);
        return newSST;
    }
    
    protected SimpleStatsTimeVarying createNewTimeVaryingStat(String name) {
        SimpleStatsTimeVarying newSSTV = new SimpleStatsTimeVarying(name);
        timeVaryingStats.put(name, newSSTV);
        return newSSTV;
    }
    
    public SimpleStatsTally getTallyStats(String name) {
        SimpleStatsTally sst = (SimpleStatsTally) tallyStats.get(name);
        return (SimpleStatsTally) sst.clone();
    }
    
    public SimpleStatsTally[] getAllTallyStats() {
        SimpleStatsTally[] sst = (SimpleStatsTally[]) tallyStats.values().toArray(new SimpleStatsTally[0]);
        for (int i = 0; i < sst.length; ++i) {
            sst[i] = (SimpleStatsTally) sst[i].clone();
        }        
        return sst;
    }
    
    public SimpleStatsTimeVarying[] getAllTimeVaryingStats() {
        SimpleStatsTimeVarying[] sstv = 
            (SimpleStatsTimeVarying[]) timeVaryingStats.values().toArray(new SimpleStatsTimeVarying[0]);
        for (int i = 0; i < sstv.length; ++i) {
            sstv[i] = (SimpleStatsTimeVarying) sstv[i].clone();
        }        
        return sstv;
    }
    
    public String[] getNames() {
        return (String[]) tallyStats.keySet().toArray(new String[0]);
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("MultiStatTally");
        buf.append(SimEntity.NL);
        buf.append("Tally Statistics:");
        for (Iterator i = tallyStats.keySet().iterator(); i.hasNext(); ) {
            buf.append(SimEntity.NL);
            buf.append(tallyStats.get(i.next()));
        }
        buf.append(SimEntity.NL);
        buf.append("Time Varying Statistics:");
        for (Iterator i = timeVaryingStats.keySet().iterator(); i.hasNext(); ) {
            buf.append(SimEntity.NL);
            buf.append(timeVaryingStats.get(i.next()));
        }
        
        return buf.toString();
    }
}

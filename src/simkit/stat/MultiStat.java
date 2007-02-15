package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import simkit.SimEntity;

/**
 * Listens for PropertyChangeEvents that are associated with state changes
 * to be collected in a Tally or Time-Varying manner.  A new SimpleStatsTally 
 * and a new SimpleStatsTimeVarying will be created on-the-fly for each 
 * unique property name.
 * Thus, the user will have a choice as to whether to see Tally or
 * Time-Varying statistics.  Typically, of course, only one will
 * make any sense.
 *
 * @author ahbuss
 * @version $Id$
 */
public class MultiStat implements PropertyChangeListener {
    
/**
* A container for the SimpleStatsTallies.
**/
    private Map<String, SimpleStatsTally> tallyStats;

/**
* A container for the SimpleStatsTimeVarying.
**/
    private Map<String, SimpleStatsTimeVarying> timeVaryingStats;
    
/**
* Creates a new empty MultiStat.
**/
    public MultiStat() {
        tallyStats = new TreeMap<String, SimpleStatsTally>();
        timeVaryingStats = new TreeMap<String, SimpleStatsTimeVarying>();
    }
    
/**
* Resets this MultiStat to its original empty state.
**/
    public void reset() {
        tallyStats.clear();
        timeVaryingStats.clear();
    }
    
/**
* Notifies this MultiStat of a property change. Adds the observation to 
* both a SimpleStatsTally and a SimpleStatsTimeVarying, creating them if
* this is the first observation for the given property.
**/
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
    
/**
* Adds the observation to 
* both a SimpleStatsTally and a SimpleStatsTimeVarying, creating them if
* this is the first observation for the given property.
**/
    protected void newObservation(String name, double value) {
        SimpleStatsTally sst = tallyStats.get(name);
        if (sst == null) {
            sst = createNewTallyStat(name);
        }
        sst.newObservation(value);
        SimpleStatsTimeVarying sstv = timeVaryingStats.get(name);
        if (sstv == null) {
            sstv = createNewTimeVaryingStat(name);
        }
        sstv.newObservation(value);
    }
    
/**
* Adds the observation to 
* both a SimpleStatsTally and a SimpleStatsTimeVarying, creating them if
* this is the first observation for the given property.
**/
    protected void newObservation(String name, boolean value) {
        newObservation(name, value ? 1.0 : 0.0);
    }
    
/**
* Creates a new SimpleStatsTally for the given property name and adds it to 
* the Map.
**/
    protected SimpleStatsTally createNewTallyStat(String name) {
        SimpleStatsTally newSST = new SimpleStatsTally(name);
        tallyStats.put(name, newSST);
        return newSST;
    }
    
/**
* Creates a new SimpleStatsTimeVarying for the given property name and adds it to 
* the Map.
**/
    protected SimpleStatsTimeVarying createNewTimeVaryingStat(String name) {
        SimpleStatsTimeVarying newSSTV = new SimpleStatsTimeVarying(name);
        timeVaryingStats.put(name, newSSTV);
        return newSSTV;
    }
    
/**
* Returns a copy of the SimpleStatsTally for the given property name. Returns
* <code>null<code/> if the property cannot be found.
**/
    public SimpleStatsTally getTallyStats(String name) {
        SimpleStatsTally sst = tallyStats.get(name);
        if (sst == null) {
           return null;
        } else {
           return (SimpleStatsTally) sst.clone();
        }
    }
    
/**
* Returns an array containing a copy of all of the SimpleStatsTallies.
**/
    public SimpleStatsTally[] getAllTallyStats() {
        SimpleStatsTally[] sst = (SimpleStatsTally[]) tallyStats.values().toArray(new SimpleStatsTally[0]);
        for (int i = 0; i < sst.length; ++i) {
            sst[i] = (SimpleStatsTally) sst[i].clone();
        }        
        return sst;
    }
    
/**
* Returns a copy of the SimpleStatsTimeVarying for the given property name.
* Returns <code>null<code/> if the property cannot be found.
**/
    public SimpleStatsTimeVarying getTimeVaryingStats(String name) {
        SimpleStatsTimeVarying sst = timeVaryingStats.get(name);
        if (sst == null) {
           return null;
        } else {
           return (SimpleStatsTimeVarying) sst.clone();
        }
    }
/**
* Returns an array containing a copy of all of the SimpleStatsTimeVarying.
**/
    public SimpleStatsTimeVarying[] getAllTimeVaryingStats() {
        SimpleStatsTimeVarying[] sstv = 
            (SimpleStatsTimeVarying[]) timeVaryingStats.values().toArray(new SimpleStatsTimeVarying[0]);
        for (int i = 0; i < sstv.length; ++i) {
            sstv[i] = (SimpleStatsTimeVarying) sstv[i].clone();
        }        
        return sstv;
    }
    
/**
* Returns an array containing the names of all of the properties for which
* this MultiStat has collected data.
**/
    public String[] getNames() {
        return tallyStats.keySet().toArray(new String[0]);
    }
    
/**
* Returns a String containing information about the data contained in this MultiStat.
**/
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

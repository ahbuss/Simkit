package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Instances will listen for PropertyChangeEvents for a specific state name
 * but possibly coming from different sources. This will keep separate
 * Tally statistics for each source, as well as global statistics for all.
 * @author ahbuss
 */
public class TallyStatsBySource extends SimpleStatsTally {

    protected Map<Object, SimpleStatsTally> allStats;

    /**
     * Instantiate a TallyStatsBySource for state given by stateName
     * @param stateName Given name of state to listen for
     */
    public TallyStatsBySource(String stateName) {
        super(stateName);
    }

    /**
     * Clears all statistics
     */
    @Override
    public void reset() {
        if (allStats == null) {
            allStats = new HashMap<>();
        }
        allStats.clear();
    }
    
    /**
     * If propertyName is the one we are listening for, update the global
     * tally. If this is the first state transition from this source,
     * instantiate a SimpleStatsTally and add it to allStats. Regardless,
     * update the SimpleStatsTally for the source of the state transition.
     * @param evt Given PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (getName().equals(evt.getPropertyName())) {
            super.propertyChange(evt);
            Object source = evt.getSource();
            SimpleStatsTally stat = allStats.get(source);
            if (stat == null) {
                stat = new SimpleStatsTally(getName());
                stat.reset();
                allStats.put(source, stat);
            }
            if (evt.getNewValue() instanceof Number) {
                stat.newObservation((Number) evt.getNewValue());
            } else if (evt.getNewValue() instanceof Boolean) {
                stat.newObservation((Boolean) evt.getNewValue());
            }
        }
    }

    /**
     * 
     * @return a shallow copy of all SimpleStatsTally instances
     */
    public Map<Object, SimpleStatsTally> getAllStats() {
        return new HashMap<>(allStats);
    }
    
    /**
     * 
     * @param source Given source of state transitions
     * @return copy of SimpleStatsTally object for this source, or null
     * if none heard yet
     */
    public SimpleStatsTally getStatsFor(Object source) {
        SimpleStatsTally statsFor = allStats.get(source);
        return statsFor != null ? new SimpleStatsTally(statsFor) : null;
    }

}

package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import simkit.SimEntityBase;
import simkit.stat.SimpleStatsTally;

/**
 * @version $Id: MultipleEndingStateStatTally.java 1031 2007-08-21 17:07:58Z ahbuss $
 * @author ahbuss
 */
public class MultipleEndingStateStatTally extends SimEntityBase implements 
        PropertyChangeListener {
    
    protected Map<String, Number> lastStateValue;
    
    protected Map<String, SimpleStatsTally> lastStateValueStat;
    
    
    public MultipleEndingStateStatTally() {
        lastStateValue = new HashMap<String, Number>();
        lastStateValueStat = new HashMap<String, SimpleStatsTally>();
    }

    public void reset() {
        super.reset();
        lastStateValue.clear();
    }

    public void doRun() {
    }
    
    public void deepReset() {
        this.reset();
        lastStateValueStat.clear();
    }
    
    public void doEndIterationEvent() {
        for (String property : lastStateValue.keySet()) {
            double lastValue = getLastStateValueFor(property);
            if (!Double.isNaN(lastValue)) {
                SimpleStatsTally stat = lastStateValueStat.get(property);
                if (stat == null) {
                    stat = new SimpleStatsTally(property);
                    lastStateValueStat.put(property, stat);
                }
                stat.newObservation(lastValue);
            }
        }
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        if (java.lang.Number.class.isAssignableFrom(e.getNewValue().getClass())) {
            lastStateValue.put(e.getPropertyName(), (Number) e.getNewValue());
        }
    }

    public Map<String, Number> getLastStateValue() {
        return new HashMap<String, Number>(lastStateValue);
    }

    public Map<String, SimpleStatsTally> getLastStateValueStat() {
        return new HashMap<String, SimpleStatsTally>(lastStateValueStat);
    }
    
    public double getLastStateValueFor(String property) {
        Number lastValue = lastStateValue.get(property);
        return lastValue != null ? lastValue.doubleValue() : Double.NaN;
    }
}
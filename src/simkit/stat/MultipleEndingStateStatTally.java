package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import simkit.SimEntityBase;

/**
 * 
 * @author ahbuss
 */
public class MultipleEndingStateStatTally extends SimEntityBase implements 
        PropertyChangeListener {
    
    protected Map<String, Number> lastStateValue;
    
    protected Map<String, SimpleStatsTally> lastStateValueStat;
    
    
    public MultipleEndingStateStatTally() {
        lastStateValue = new HashMap<>();
        lastStateValueStat = new HashMap<>();
    }

    @Override
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
        for (String localProperty : lastStateValue.keySet()) {
            double lastValue = getLastStateValueFor(localProperty);
            if (!Double.isNaN(lastValue)) {
                SimpleStatsTally stat = lastStateValueStat.get(localProperty);
                if (stat == null) {
                    stat = new SimpleStatsTally(localProperty);
                    lastStateValueStat.put(localProperty, stat);
                }
                stat.newObservation(lastValue);
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (java.lang.Number.class.isAssignableFrom(e.getNewValue().getClass())) {
            lastStateValue.put(e.getPropertyName(), (Number) e.getNewValue());
        }
    }

    public Map<String, Number> getLastStateValue() {
        return new HashMap<>(lastStateValue);
    }

    public Map<String, SimpleStatsTally> getLastStateValueStat() {
        return new HashMap<>(lastStateValueStat);
    }
    
    public double getLastStateValueFor(String property) {
        Number lastValue = lastStateValue.get(property);
        return lastValue != null ? lastValue.doubleValue() : Double.NaN;
    }
}
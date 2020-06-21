package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static java.lang.Double.NaN;
import java.util.HashMap;
import java.util.Map;
import simkit.Named;
import static simkit.SimEventScheduler.NL;

/**
 *
 * @author ahbuss
 */
public class ObjectMultiTimeVaryingStat extends SimpleStatsTimeVarying implements ObjectIndexedStatistics, PropertyChangeListener {

    private final Map<Object, SampleStatistics> allSampleStat;

    public ObjectMultiTimeVaryingStat() {
        this.allSampleStat = new HashMap<>();        
    }
    
    public ObjectMultiTimeVaryingStat(String property) {
        this();
        setName(property);
    }

    @Override
    public void newObservation(double value, Object source) {
        if (source instanceof Named) {
            if (allSampleStat.containsKey(source)) {
                allSampleStat.get(source).newObservation(value);
            } else {
                SimpleStatsTimeVarying stat = new SimpleStatsTimeVarying(getName());
                allSampleStat.put(source, stat);
                stat.newObservation(value);
            }
            this.newObservation(value);
        }
    }

    @Override
    public void newObservation(Number newObs, Object source) {
        newObservation(newObs.doubleValue(), source);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(getName())) {
            newObservation((Number) evt.getNewValue(), evt.getSource());
        }
    }

    @Override
    public double getMean(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getMean();
        } else {
            return NaN;
        }
    }

    @Override
    public double getVariance(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getVariance();
        } else {
            return NaN;
        }
    }

    @Override
    public double getStandardDeviation(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getStandardDeviation();
        } else {
            return NaN;
        }
    }

    @Override
    public int getCount(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getCount();
        } else {
            return 0;
        }
    }

    @Override
    public double getMinObs(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getMinObs();
        } else {
            return NaN;
        }

    }

    @Override
    public double getMaxObs(Object source) {
        if (allSampleStat.containsKey(source)) {
            return allSampleStat.get(source).getMaxObs();
        } else {
            return NaN;
        }

    }

    @Override
    public Map<Object, Double> getAllMean() {
        Map<Object, Double> allMean = new HashMap<>();
        for (Object key : allMean.keySet()) {
            allMean.put(key, allSampleStat.get(key).getMean());
        }
        return allMean;
    }

    @Override
    public Map<Object, Double> getAllVariance() {
        Map<Object, Double> allVariance = new HashMap<>();
        for (Object key : allVariance.keySet()) {
            allVariance.put(key, allSampleStat.get(key).getVariance());
        }
        return allVariance;
    }

    @Override
    public Map<Object, Double> getAllStandardDeviation() {
        Map<Object, Double> allStandardDeviation = new HashMap<>();
        for (Object key : allStandardDeviation.keySet()) {
            allStandardDeviation.put(key, allSampleStat.get(key).getStandardDeviation());
        }
        return allStandardDeviation;
    }

    @Override
    public Map<Object, Integer> getAllCount() {
        Map<Object, Integer> allMean = new HashMap<>();
        for (Object key : allMean.keySet()) {
            allMean.put(key, allSampleStat.get(key).getCount());
        }
        return allMean;

    }

    @Override
    public Map<Object, Double> getAllMinObs() {
        Map<Object, Double> allMinObs = new HashMap<>();
        for (Object key : allMinObs.keySet()) {
            allMinObs.put(key, allSampleStat.get(key).getMinObs());
        }
        return allMinObs;
    }

    @Override
    public Map<Object, Double> getAllMaxObs() {
        Map<Object, Double> allMaxObs = new HashMap<>();
        for (Object key : allMaxObs.keySet()) {
            allMaxObs.put(key, allSampleStat.get(key).getMaxObs());
        }
        return allMaxObs;
    }

    /**
     * @return the allSampleStat
     */
    public Map<Object, SampleStatistics> getAllSampleStat() {
        return new HashMap<>(allSampleStat);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getName()).append(' ').append(getSamplingType());
        for (Object key : allSampleStat.keySet()) {
            builder.append(NL).append(((Named) key).getName()).append(':').append(' ').append(((SimpleStatsTimeVarying) allSampleStat.get(key)).getDataLine());
        }
        return builder.toString();
    }

}

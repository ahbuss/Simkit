package simkit.stat;

import static java.lang.Double.NaN;
import java.util.LinkedHashMap;
import java.util.Map;
import simkit.SimEntity;
import static simkit.SimEventScheduler.NL;

/**
 *
 * @author ahbuss
 */
public class ObjectMultiTallyStat extends SimpleStatsTally implements ObjectIndexedStatistics {

    private Map<Object, SimpleStatsTally> stats;

    public ObjectMultiTallyStat() {
        super();
        stats = new LinkedHashMap<>();
    }

    public ObjectMultiTallyStat(String name) {
        this();
        this.setName(name);
    }

    @Override
    public void reset() {
        super.reset();
        stats.clear();
    }

    @Override
    public void newObservation(double value, Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (!stats.containsKey(source)) {
            statsForMe = new SimpleStatsTally(getName());
            stats.put(source, statsForMe);
        }
        statsForMe.newObservation(value);
    }

    @Override
    public void newObservation(Number newObs, Object source) {
        newObservation(newObs.doubleValue(), source);
    }

    @Override
    public double getMean(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getMean();
        } else {
            return NaN;
        }
    }

    @Override
    public double getVariance(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getVariance();
        } else {
            return NaN;
        }

    }

    @Override
    public double getStandardDeviation(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getStandardDeviation();
        } else {
            return NaN;
        }
    }

    @Override
    public int getCount(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public double getMinObs(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getMinObs();
        } else {
            return NaN;
        }
    }

    @Override
    public double getMaxObs(Object source) {
        SimpleStatsTally statsForMe = stats.get(source);
        if (statsForMe != null) {
            return statsForMe.getMaxObs();
        } else {
            return NaN;
        }
    }

    @Override
    public Map<Object, Double> getAllMean() {
        Map<Object, Double> allMeans = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allMeans.put(source, stats.get(source).getMean());
        }
        return allMeans;
    }

    @Override
    public Map<Object, Double> getAllVariance() {
        Map<Object, Double> allVar = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allVar.put(source, stats.get(source).getVariance());
        }
        return allVar;
    }

    @Override
    public Map<Object, Double> getAllStandardDeviation() {
        Map<Object, Double> allStdDev = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allStdDev.put(source, stats.get(source).getStandardDeviation());
        }
        return allStdDev;
    }

    @Override
    public Map<Object, Integer> getAllCount() {
        Map<Object, Integer> allCount = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allCount.put(source, stats.get(source).getCount());
        }
        return allCount;
    }

    @Override
    public Map<Object, Double> getAllMinObs() {
        Map<Object, Double> allMin = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allMin.put(source, stats.get(source).getMinObs());
        }
        return allMin;
    }

    @Override
    public Map<Object, Double> getAllMaxObs() {
        Map<Object, Double> allMax = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allMax.put(source, stats.get(source).getMaxObs());
        }
        return allMax;
    }

    @Override
    public Map<Object, SampleStatistics> getAllSampleStat() {
        Map<Object, SampleStatistics> allStats = new LinkedHashMap<>();
        for (Object source : stats.keySet()) {
            allStats.put(source, new SimpleStatsTally(stats.get(source)));
        }
        return allStats;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(' ').append('(').append(getSamplingType()).append(')');
        for (Object source: stats.keySet()) {
            if (source instanceof SimEntity) {
                builder.append(NL).append(((SimEntity)source).getName());
                builder.append(stats.get(source).getDataLine());
            }
        }
        return builder.toString();
    }

}

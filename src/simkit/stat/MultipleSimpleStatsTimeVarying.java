package simkit.stat;

import java.util.TreeMap;
import java.util.Iterator;
import simkit.util.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;

public class MultipleSimpleStatsTimeVarying extends SimpleStatsTimeVarying implements IndexedSampleStatistics {

    private TreeMap indexedStats;

    public MultipleSimpleStatsTimeVarying() {
        super();
        indexedStats = new TreeMap();
    }

    public MultipleSimpleStatsTimeVarying(String name) {
        this();
        setName(name);
    }

    public void newObservation(double x, int index) {
        this.newObservation(x);
        Integer key = new Integer(index);
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(key);
        if (stat == null) {
            stat = new SimpleStatsTimeVarying();
            stat.setName(this.getName());
            indexedStats.put(key, stat);
        }
        stat.newObservation(x);
    }

    public void newObservation(Number num, int index) {
        this.newObservation(num.doubleValue(), index);
    }

    public double getMean(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMean();  
    }
    public double getVariance(int index) {
        SimpleStatsTally stat = (SimpleStatsTally) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getVariance();  
    }
    public double getStandardDeviation(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getStandardDeviation();  
    }
    public int getCount(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? 0 : stat.getCount();  
    }
    public double getMinObs(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMinObs();  
    }
    public double getMaxObs(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMaxObs();  
    }

    public double[] getAllMean() {
        double[] means = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            means = new double[length];
            for (int i = 0; i < means.length; i++) {
                means[i] = getMean(i);
            }
        }
        return means;
    }

    public double[] getAllVariance() {
        double[] variance = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            variance = new double[length];
            for (int i = 0; i < variance.length; i++) {
                variance[i] = getVariance(i);
            }
        }
        return variance;
    }

    public double[] getAllStandardDeviation() {
        double[] std = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            std = new double[length];
            for (int i = 0; i < std.length; i++) {
                std[i] = getStandardDeviation(i);
            }
        }
        return std;
    }

    public SampleStatistics[] getAllSampleStat() {
        SampleStatistics[] allStats = null;
        SimpleStatsTimeVarying temp;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            allStats = new SampleStatistics[length];
            for (int i = 0; i < allStats.length; i++) {
                temp = (SimpleStatsTimeVarying) indexedStats.get(new Integer(i));
                allStats[i] = temp != null ? (SampleStatistics) temp.clone() :
                    new SimpleStatsTimeVarying(this.getName());
            }
        }
        return allStats;
    }

    public double[] getAllMaxObs() {
        double[] max = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            max = new double[length];
            for (int i = 0; i < max.length; i++) {
                max[i] = getMaxObs(i);
            }
        }
        return max;
    }

    public double[] getAllMinObs() {
        double[] min = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            min = new double[length];
            for (int i = 0; i < min.length; i++) {
                min[i] = getMinObs(i);
            }
        }
        return min;
    }

    public int[] getAllCount() {
        int[] count = null;
        if (indexedStats.size() > 0) {
            int high = ((Integer) indexedStats.lastKey()).intValue();
            int length = high + 1;
            count = new int[length];
            for (int i = 0; i < count.length; i++) {
                count[i] = getCount(i);
            }
        }
        return count;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (this.getName().equals(e.getPropertyName()) &&
            (e instanceof IndexedPropertyChangeEvent) ) {
            if (e.getNewValue() instanceof Number) {
                this.newObservation((Number) e.getNewValue(),
                    ((IndexedPropertyChangeEvent) e).getIndex() );
            }
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getName());
        buf.append(' ');
        buf.append('(');
        buf.append(getSamplingType());
        buf.append(')');
        buf.append(EOL);
        for (Iterator i = indexedStats.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(key);
            buf.append(key);
            buf.append(' ');
            buf.append(' ');
            buf.append(stat.getDataLine());
            buf.append(EOL);
        }
        buf.append("--------------------------------------------------------");
        buf.append(EOL);
        buf.append("   ");
        buf.append(this.getDataLine());
        
        return buf.toString();
    }
} 
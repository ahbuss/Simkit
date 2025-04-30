package simkit.stat;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.TreeMap;

/**
 * A class to collect Tally statistics for an array of properties.
 * <p>
 * The getters inherited from <code>SimpleStatsTally</code> will return
 * statistics for all of the properties, independent of index.</p>
 *
 * @version $Id: TruncatingMultipleSimpleStatsTally.java 1329 2014-07-12
 * 00:21:53Z ahbuss $
 *
 */
public class TruncatingMultipleSimpleStatsTally extends SimpleStatsTally implements IndexedSampleStatistics {

    /**
     * Holds the collection of statistics.
     */
    private TreeMap<Integer, SimpleStatsTally> indexedStats;

    private int truncationPoint;

    /**
     * Creates a new instance with the default name. Note: The name can be
     * changed later by calling <CODE>setName</CODE>.
     *
     * @param truncationPoint truncation point
     */
    public TruncatingMultipleSimpleStatsTally(int truncationPoint) {
        super();
        setTruncationPoint(truncationPoint);
        indexedStats = new TreeMap<>();
    }

    /**
     * Creates a new instance with the given name.
     *
     * @param name The name of the property to collect statistics on.
     * @param truncationPoint truncation point
     */
    public TruncatingMultipleSimpleStatsTally(String name, int truncationPoint) {
        this(truncationPoint);
        setName(name);
    }

    @Override
    public void newObservation(double x, int index) {
        this.newObservation(x);
        SimpleStatsTally stat = indexedStats.get(index);
        if (stat == null) {
            stat = new TruncatingSimpleStatsTally(getTruncationPoint());
            stat.setName(this.getName());
            indexedStats.put(index, stat);
        }
        stat.newObservation(x);
    }

    @Override
    public void newObservation(Number num, int index) {
        this.newObservation(num.doubleValue(), index);
    }

    @Override
    public double getMean(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMean();
    }

    @Override
    public double getVariance(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getVariance();
    }

    @Override
    public double getStandardDeviation(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getStandardDeviation();
    }

    @Override
    public int getCount(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? 0 : stat.getCount();
    }

    @Override
    public double getMinObs(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMinObs();
    }

    @Override
    public double getMaxObs(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMaxObs();
    }

    @Override
    public double[] getAllMean() {
        double[] means = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            means = new double[length];
            for (int i = 0; i < means.length; i++) {
                means[i] = getMean(i);
            }
        }
        return means;
    }

    @Override
    public double[] getAllVariance() {
        double[] variance = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            variance = new double[length];
            for (int i = 0; i < variance.length; i++) {
                variance[i] = getVariance(i);
            }
        }
        return variance;
    }

    @Override
    public double[] getAllStandardDeviation() {
        double[] std = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            std = new double[length];
            for (int i = 0; i < std.length; i++) {
                std[i] = getStandardDeviation(i);
            }
        }
        return std;
    }

    @Override
    public SampleStatistics[] getAllSampleStat() {
        SampleStatistics[] allStats = null;
        SimpleStatsTally temp;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            allStats = new SampleStatistics[length];
            for (int i = 0; i < allStats.length; i++) {
                temp = (SimpleStatsTally) indexedStats.get(i);
                allStats[i] = temp != null ? (SampleStatistics) temp.clone()
                        : new SimpleStatsTally(this.getName());
            }
        }
        return allStats;
    }

    @Override
    public double[] getAllMaxObs() {
        double[] max = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            max = new double[length];
            for (int i = 0; i < max.length; i++) {
                max[i] = getMaxObs(i);
            }
        }
        return max;
    }

    @Override
    public double[] getAllMinObs() {
        double[] min = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            min = new double[length];
            for (int i = 0; i < min.length; i++) {
                min[i] = getMinObs(i);
            }
        }
        return min;
    }

    @Override
    public int[] getAllCount() {
        int[] cnt = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            cnt = new int[length];
            for (int i = 0; i < cnt.length; i++) {
                cnt[i] = getCount(i);
            }
        }
        return cnt;
    }

    /**
     * If the PropertyChangeEvent contains the indexed property associated with
     * this statistic then record a new observation for the index and value
     * contained in the event.
     * <P>
     * Note: <CODE>MultipleSimpleStatsTally</CODE> cannot be reset by an event
     * like <CODE>AbstractSimpleStats</CODE>.</P>
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (this.getName().equals(e.getPropertyName())
                && (e instanceof IndexedPropertyChangeEvent)) {
            if (e.getNewValue() instanceof Number) {
                this.newObservation((Number) e.getNewValue(),
                        ((IndexedPropertyChangeEvent) e).getIndex());
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (indexedStats == null) {
            return;
        }
        for (SimpleStatsTally stats : indexedStats.values()) {
            stats.reset();
        }
    }

    @Override
    public int getIndexCount() {
        return indexedStats.size();
    }

    /**
     * Produces a String containing the name, SamplingType, and DataLines for
     * all of the properties.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.getName());
        buf.append(' ');
        buf.append('(');
        buf.append(getSamplingType());
        buf.append(')');
        buf.append(EOL);
        for (Integer key : indexedStats.keySet()) {
            SimpleStatsTally stat = indexedStats.get(key);
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

    public int getTruncationPoint() {
        return truncationPoint;
    }

    public void setTruncationPoint(int truncationPoint) {
        this.truncationPoint = truncationPoint;
    }
}

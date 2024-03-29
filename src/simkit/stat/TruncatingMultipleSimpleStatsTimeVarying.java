package simkit.stat;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class to collect time varying statistics for an array of properties. The
 * statistics calculated are time weighted. Automatically gets the time of the
 * observations from Schedule.
 * <P>
 * Getting the mean, variance, or standard deviation will cause a new
 * observation to be added to account for the time since the last observation,
 * therefore the value returned by <CODE>getCount</CODE> may not correspond to
 * the number of times <CODE>newObservation</CODE> has been called.
 * </P>
 * The getters inherited from <code>SimplStatsTimeVarying</code> will return
 * statistics for all observations of the property independent of index.
 *
 * @version $Id: TruncatingMultipleSimpleStatsTimeVarying.java 1329 2014-07-12
 * 00:21:53Z ahbuss $
*
 */
public class TruncatingMultipleSimpleStatsTimeVarying extends SimpleStatsTimeVarying implements IndexedSampleStatistics {

    /**
     * Holds the table of indexed statistics.
     */
    protected SortedMap<Integer, SimpleStatsTimeVarying> indexedStats;

    private double truncationPoint;

    /**
     * Creates a new instance with the default name. The name can be changed
     * later by calling <CODE>setName</CODE>
     * @param truncationPoint given truncation point
     */
    public TruncatingMultipleSimpleStatsTimeVarying(double truncationPoint) {
        super();
        indexedStats = new TreeMap<>();
    }

    /**
     * Creates a new instance with the given name.
     *
     * @param name The name of the array of properties for which statistics will
     * be collected.
     * @param truncationPoint given truncation point
     */
    public TruncatingMultipleSimpleStatsTimeVarying(String name, double truncationPoint) {
        this(truncationPoint);
        setName(name);
    }

    @Override
    public void newObservation(double x, int index) {
        this.newObservation(x);
        SimpleStatsTimeVarying stat = indexedStats.get(index);
        if (stat == null) {
            stat = new TruncatingSimpleStatsTimeVarying(getTruncationPoint());
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
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMean();
    }

    @Override
    public double getVariance(int index) {
        SimpleStatsTimeVarying stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getVariance();
    }

    @Override
    public double getStandardDeviation(int index) {
        SimpleStatsTimeVarying stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getStandardDeviation();
    }

    @Override
    public int getCount(int index) {
        SimpleStatsTimeVarying stat = indexedStats.get(index);
        return (stat == null) ? 0 : stat.getCount();
    }

    @Override
    public double getMinObs(int index) {
        SimpleStatsTimeVarying stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMinObs();
    }

    @Override
    public double getMaxObs(int index) {
        SimpleStatsTimeVarying stat = indexedStats.get(index);
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
        double[] var = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            var = new double[length];
            for (int i = 0; i < var.length; i++) {
                var[i] = getVariance(i);
            }
        }
        return var;
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
        SimpleStatsTimeVarying temp;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            allStats = new SampleStatistics[length];
            for (int i = 0; i < allStats.length; i++) {
                temp = indexedStats.get(i);
                    allStats[i] = temp != null ? (SampleStatistics) temp.clone()
                            : new SimpleStatsTimeVarying(this.getName());
            }
        }
        return allStats;
    }

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

// Javadoc inherited.
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

// Javadoc inherited.
    public int[] getAllCount() {
        int[] count = null;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            count = new int[length];
            for (int i = 0; i < count.length; i++) {
                count[i] = getCount(i);
            }
        }
        return count;
    }

    /**
     * If the PropertyChangeEvent contains the indexed property associated with
     * this statistic then record a new observation for the index and value
     * contained in the event.
     * <P>
     * Note: <CODE>MultipleSimpleStatsTimeVarying</CODE> cannot be reset by an
     * event like <CODE>AbstractSimpleStats</CODE>.</P>
*
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (this.getName().equals(e.getPropertyName())
                && (e instanceof IndexedPropertyChangeEvent)) {
            if (e.getNewValue() instanceof Number) {
                this.newObservation((Number) e.getNewValue(),
                        ((IndexedPropertyChangeEvent) e).getIndex());
            } else if (e.getNewValue() instanceof Boolean) {
                this.newObservation(
                        (((Boolean) e.getNewValue()).booleanValue() ? 1 : 0),
                        ((IndexedPropertyChangeEvent) e).getIndex());
            }
        }
    }

//Javadoc inherited.
    public void reset() {
        super.reset();
        if (indexedStats == null) {
            return;
        }
        for (SimpleStatsTimeVarying stats : indexedStats.values()) {
            stats.reset();
        }
    }

    /**
     * Empties HashMap completely.
     */
    public void clear() {
        indexedStats.clear();
    }

    /**
     * Produces a String containing the name, SamplingType, and DataLines for
     * all of the properties.
*
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.getName());
        buf.append(' ');
        buf.append('(');
        buf.append(getSamplingType());
        buf.append(')');
        buf.append(EOL);
        for (Integer key : indexedStats.keySet()) {
            SimpleStatsTimeVarying stat = indexedStats.get(key);
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

    public double getTruncationPoint() {
        return truncationPoint;
    }

    public void setTruncationPoint(double truncationPoint) {
        this.truncationPoint = truncationPoint;
    }
}

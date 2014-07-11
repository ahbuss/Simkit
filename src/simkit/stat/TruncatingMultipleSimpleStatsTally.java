package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.TreeMap;

import simkit.util.IndexedPropertyChangeEvent;

/**
* A class to collect Tally statistics for an array of properties.
* <p>The getters inherited from <code>SimpleStatsTally</code> will
* return statistics for all of the properties, independent of index.</p>
* 
* @version $Id$
**/
public class TruncatingMultipleSimpleStatsTally extends SimpleStatsTally implements IndexedSampleStatistics {

/**
* Holds the collection of statistics.
**/
    private TreeMap<Integer, SimpleStatsTally> indexedStats;

    private int truncationPoint;
    
/**
* Creates a new instance with the default name. Note: The name
* can be changed later by calling <CODE>setName</CODE>.
**/
    public TruncatingMultipleSimpleStatsTally(int truncationPoint) {
        super();
        setTruncationPoint(truncationPoint);
        indexedStats = new TreeMap<Integer, SimpleStatsTally>();
    }

/**
* Creates a new instance with the given name.
* @param name The name of the property to collect statistics on.
**/
    public TruncatingMultipleSimpleStatsTally(String name, int truncationPoint) {
        this(truncationPoint);
        setName(name);
    }
    
//Javadoc inherited.
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

//Javadoc inherited.
    public void newObservation(Number num, int index) {
        this.newObservation(num.doubleValue(), index);
    }

//Javadoc inherited.
    public double getMean(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMean();  
    }
//Javadoc inherited.
    public double getVariance(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getVariance();  
    }
//Javadoc inherited.
    public double getStandardDeviation(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getStandardDeviation();  
    }
//Javadoc inherited.
    public int getCount(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? 0 : stat.getCount();  
    }
//Javadoc inherited.
    public double getMinObs(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMinObs();  
    }
//Javadoc inherited.
    public double getMaxObs(int index) {
        SimpleStatsTally stat = indexedStats.get(index);
        return (stat == null) ? Double.NaN : stat.getMaxObs();  
    }

//Javadoc inherited.
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

//Javadoc inherited.
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

//Javadoc inherited.
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

//Javadoc inherited.
    public SampleStatistics[] getAllSampleStat() {
        SampleStatistics[] allStats = null;
        SimpleStatsTally temp;
        if (indexedStats.size() > 0) {
            int high = indexedStats.lastKey();
            int length = high + 1;
            allStats = new SampleStatistics[length];
            for (int i = 0; i < allStats.length; i++) {
                temp = (SimpleStatsTally) indexedStats.get(new Integer(i));
                allStats[i] = temp != null ? (SampleStatistics) temp.clone() :
                    new SimpleStatsTally(this.getName());
            }
        }
        return allStats;
    }

//Javadoc inherited.
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

//Javadoc inherited.
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

//Javadoc inherited.
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
* If the PropertyChangeEvent contains the indexed property associated
* with this statistic then record a new observation for the index and value
* contained in the event.
* <P>Note: <CODE>MultipleSimpleStatsTally</CODE> cannot be reset by an event like
* <CODE>AbstractSimpleStats</CODE>.</P>
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (this.getName().equals(e.getPropertyName()) &&
            (e instanceof IndexedPropertyChangeEvent) ) {
            if (e.getNewValue() instanceof Number) {
                this.newObservation((Number) e.getNewValue(),
                    ((IndexedPropertyChangeEvent) e).getIndex() );
            }
        }
    }
    
//Javadoc inherited.
    public void reset() {
        super.reset();
        if (indexedStats == null) { return; }
        for (SimpleStatsTally stats : indexedStats.values()) {
            stats.reset();
        }
    }
/**
* Produces a String containing the name, SamplingType, and DataLines for
* all of the properties.
**/
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

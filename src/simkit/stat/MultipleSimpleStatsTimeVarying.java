package simkit.stat;

import java.util.TreeMap;
import java.util.Iterator;
import simkit.util.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;

/**
* A class to collect time varying statistics for an array of properties. 
* The statistics calculated are time weighted. Automatically gets the
* time of the observations from Schedule.
* <P>
* Getting the mean, variance, or standard deviation will cause
* a new observation to be added to account for the time since the last
* observation, therefore the value returned by <CODE>getCount</CODE> may not
* correspond to the number of times <CODE>newObservation</CODE> has been called.
* </P>
* @version $Id$
**/
public class MultipleSimpleStatsTimeVarying extends SimpleStatsTimeVarying implements IndexedSampleStatistics {

/**
* Holds the table of indexed statistics.
**/
    private TreeMap indexedStats;

/**
* Creates a new instance with the default name. The name can be
* changed later by calling <CODE>setName</CODE>
**/
    public MultipleSimpleStatsTimeVarying() {
        super();
        indexedStats = new TreeMap();
    }

/**
* Creates a new instance with the given name.
* @param name The name of the array of properties for which statistics
* will be collected.
**/
    public MultipleSimpleStatsTimeVarying(String name) {
        this();
        setName(name);
    }

// Javadoc inherited.
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

// Javadoc inherited.
    public void newObservation(Number num, int index) {
        this.newObservation(num.doubleValue(), index);
    }

// Javadoc inherited.
    public double getMean(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMean();  
    }
// Javadoc inherited.
    public double getVariance(int index) {
        SimpleStatsTally stat = (SimpleStatsTally) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getVariance();  
    }
// Javadoc inherited.
    public double getStandardDeviation(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getStandardDeviation();  
    }
// Javadoc inherited.
    public int getCount(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? 0 : stat.getCount();  
    }
// Javadoc inherited.
    public double getMinObs(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMinObs();  
    }
// Javadoc inherited.
    public double getMaxObs(int index) {
        SimpleStatsTimeVarying stat = (SimpleStatsTimeVarying) indexedStats.get(new Integer(index));
        return (stat == null) ? Double.NaN : stat.getMaxObs();  
    }

// Javadoc inherited.
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

// Javadoc inherited.
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

// Javadoc inherited.
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

// Javadoc inherited.
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

// Javadoc inherited.
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

// Javadoc inherited.
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

// Javadoc inherited.
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

/**
* If the PropertyChangeEvent contains the indexed property associated
* with this statistic then record a new observation for the index and value
* contained in the event.
* <P>Note: <CODE>MultipleSimpleStatsTimeVarying</CODE> cannot be reset by an event like
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
        for (Iterator i = indexedStats.values().iterator(); i.hasNext(); ) {
            ((SimpleStatsTimeVarying) i.next()).reset();
        }
    }

/**
* Produces a String containing the name, SamplingType, and DataLines for
* all of the properties.
**/
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

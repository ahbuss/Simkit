package simkit.stat;

import java.text.DecimalFormat;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 *  Basic class for collecting tally statistics as produced by a typical
 *  simulation model, that is, one observation at a time.  The algorithm is
 *  not the naive one, keeping the sum, sum of squares, etc., but rather
 *  keeps the mean and variance themselves.  They are updated by a more stable
 *  algorithm that is less prone to overflow, particularly for the sum of
 *  squares.  All counters have approximately the same order of magnitude as the
 *  quantities they are estimating.
 *
 *  <P> A frequent use is as a PropertyChangeListener.  In this capacity, an instance
 *  listens for a single property name and adds a new observation if the newValue
 *  is of type <CODE>Number</CODE> 
 *  @author Arnold Buss
 *  @version $Date$
**/
public class SimpleStatsTally implements SampleStatistics, Cloneable {

    private static String DEFAULT_NAME = "%unnamed%";
    private static String DEFAULT_FORMAT = " 0.0000;-0.0000";

    private int count;
    private double mean;
    private double variance;
    private double minObs;
    private double maxObs;
    private double diff;

    private String name;
    private DecimalFormat df;

/**
 *  Construct a SimpleStatsTally with the default name.  Note:  The name can
 *  be set after instantiation using setName().
**/
    public SimpleStatsTally() {
        this(DEFAULT_NAME);
    }
/**
 *  Construct a SimpleStatsTally with the name <CODE>name</CODE>
 *  @param name The property name that will be listened too.
**/
    public SimpleStatsTally(String name) {
        this.setFormat(DEFAULT_FORMAT);
        this.setName(name);
        this.reset();
    }
/**
 *  Update counters with a new observation.
 *  @param x The new (primitive) ovservation.
**/
    public void newObservation(double x) {
        minObs = (x < minObs) ? x : minObs;
        maxObs = (x > maxObs) ? x : maxObs;

        count++;
        diff = x - mean;
        variance += (count == 1) ? 0.0 : diff * diff / count - 1.0 / (count - 1) * variance;
        mean += diff / count;
    }
/**
 *  Update counters with a new observation - just invokes primitive newObservation()
 *  with <CODE>x.doubleValue()</CODE>
 *  @param x The new (Number) ovservation.
**/
    public void newObservation(Number x) { this.newObservation( x.doubleValue() ); }

    public double getMinObs() { return minObs; }
    public double getMaxObs() { return maxObs; }
    public double getMean() { return mean; }
    public double getVariance() { return variance; }
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }
    public int getCount() { return count; } 

    public void setFormat(String formatString) {
        df = new DecimalFormat(formatString);
    }

// Implements Named interface

    public void setName(String newName) { this.name = newName; }
    public String getName() { return name; }

    public SamplingType getSamplingType() { return SamplingType.TALLY; }

    public void setSamplingType(SamplingType type) {  }
/**
 *  When a PropetyChangeEvent is heard, if the propertyName matches the name of
 *  this instance and the newValue is of type Number, invoke
 *  newObservation(Number).
 *  @param event The PropertyChangeEvent heard.
**/
    public void propertyChange(PropertyChangeEvent event) {
        if (this.getName().equals(event.getPropertyName()) ){
            Object value = event.getNewValue();
            if (value instanceof Number) {
                newObservation((Number) value);
            }
        }
    }
/**
 *  Reset all counters to 0, except max/min which are set to minus and plus
 *  infinity, respectively.
**/
    public void reset() {
        count = 0;
        diff = 0.0;
        mean = 0.0;
        variance = 0.0;
        minObs = Double.POSITIVE_INFINITY;
        maxObs = Double.NEGATIVE_INFINITY;
    }
/**
 *  @return a clone of this object with all counters identical.
**/
    public synchronized Object clone() {
        SimpleStatsTally clone = new SimpleStatsTally();
        clone.count = this.count;
        clone.diff = this.diff;
        clone.maxObs = this.maxObs;
        clone.minObs = this.minObs;
        clone.mean = this.mean;
        clone.name = this.name;
        clone.variance = this.variance;
        return clone;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getName());
        buf.append(' ');
        buf.append('(');
        buf.append(this.getSamplingType());
        buf.append(")");
        buf.append(EOL);
        return buf.toString() + getDataLine();
   }
/**
 *  @return A single line of space-separated, formated data containing: count,
 *          min, max, mean, variance, std deviation
**/
    public String getDataLine() {
        StringBuffer buf = new StringBuffer();        
        buf.append(getCount());
        buf.append(' ');
        buf.append(df.format(getMinObs()));
        buf.append(' ');
        buf.append(df.format(getMaxObs()));
        buf.append(' ');
        buf.append(df.format(getMean()));
        buf.append(' ');
        buf.append(df.format(getVariance()));
        buf.append(' ');
        buf.append(df.format(getStandardDeviation()));

        return buf.toString();
    }

}
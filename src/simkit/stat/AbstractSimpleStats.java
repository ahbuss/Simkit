package simkit.stat;

import java.text.NumberFormat;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Abstract base class implementation of the SampleStatistics interface.
 * <P>
 * This class contains a skeletal implementation for collecting new
 * observations that tracks the minimum and maximum values, along with the
 * number of observations. Classes extending AbstractSimpleStatistics will
 * need to implement the logic for calculating the mean and standard deviation.
 * </P>
 * @author  ahbuss
 * @version $Id$
 */
public abstract class AbstractSimpleStats implements SampleStatistics, Cloneable {
    
/**
* The default name for SampleStatistics. "%unnamed%"
**/
    protected  static String DEFAULT_NAME = "%unnamed%";

    /**
     */
    public static final NumberFormat DEFAULT_NUMBER_FORMAT ;
    
    static {
        DEFAULT_NUMBER_FORMAT = NumberFormat.getInstance();
        DEFAULT_NUMBER_FORMAT.setMinimumFractionDigits(3);
    }
    
/**
* The total number of observations recorded.
**/
    protected int count;

/**
* The smallest observation recorded.
**/
    protected double minObs;

/**
* The largest observation recorded.
**/
    protected double maxObs;

/**
* The name of the property that this SampleStatistic will
* collect statistics on.
**/
    protected  String name;

/**
* The DecimalFormat used for Strings.
**/
    protected NumberFormat numberFormat;
    
/**
* Creates a new instance with the default name "%unnamed%"
**/
    public AbstractSimpleStats() {
        this(DEFAULT_NAME);
    }

/**
* Creates a new instance with the given name.
**/    
    public AbstractSimpleStats(String name) {
        this.setName(name);
        numberFormat = DEFAULT_NUMBER_FORMAT;
    }

// Javadoc inherited from SampleStatistics.    
    /**
     * @return The current running maximum observation.
     */
    public double getMaxObs() { return maxObs; }
    
// Javadoc inherited from SampleStatistics.    
    /**
     * @return The current running minimum observation.
     */
    public double getMinObs() { return minObs; }
    
// Javadoc inherited from SampleStatistics.    
    /**
     * @return The current number of observations.
     */
    public int getCount() { return count; }
    
    /**
     * Resets/initializes all relevant counters.
     */
    public void reset() {
        count = 0;
        maxObs = Double.NEGATIVE_INFINITY;
        minObs = Double.POSITIVE_INFINITY;
    }
    
    /**
     * Adds new observation and update counters (primitive).
     * @param newObs The new (primitive) observation
     */
    public void newObservation(double newObs) {
        count++;
        if (newObs < minObs) { minObs = newObs; }
        if (newObs > maxObs) { maxObs = newObs; }
    }
    
    /**
     * Add new observation and update counters - normally will just call
     * newObservation(newObs.doubleValue()).
     * @param newObs The new (Number) observation
     */
    public void newObservation(Number newObs) {
        this.newObservation(newObs.doubleValue());
    }
    
/**
* If the PropertyChangeEvent contains the property this SampleStatistic
* is associated with, record the value as a new observation.
* <P>If the name of the property is "reset" and the value of
* the property is the name of the property that this SampleStatistic
* is associated with, then reset this SampleStatistic.
* </P>
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(name)) {
            Object newObs = e.getNewValue();
            if (newObs instanceof Number) {
                newObservation((Number)newObs);
            }
            else if (newObs instanceof Boolean) {
                newObservation( (Boolean) newObs);
            }
        }
        else if (e.getPropertyName().equals("reset") &&
        this.getName().equals(e.getNewValue())) {
            this.reset();
        }

    }
    
    /**
     * Sets the Sampling type.
     * This should normally have no effect, since each class should only implement
     * one SamplingType.
     * @param type The SamplingType (TALLY or TIME_VARYING).
     */
    public void setSamplingType(SamplingType type) {
    }
    
    /**
     * @param numberFormat the new DecimalFormat instance
     */
    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

// Javadoc inherited from SampleStatistics.
    public void newObservation(Boolean newObs) {
        newObservation(newObs.booleanValue());
    }
    
    /**
     * Add a new Observation of type <code>boolean</code>.  It is expected that
     * "true" corresponds to '1' and "false" to 0.
     * @param newObs the boolean observation
     */
    public void newObservation(boolean newObs) {
        newObservation( newObs ? 1.0 : 0.0 );
    }
    /**
     * The name of the Property that this SampleStatistic is associated with.
     * @return The Property name associated with the SampleStatistics instance
     */
    public String getName() { return name; }
    
    /**
     * The name of the Property that this SampleStatistic is associated with.
     * @param name The Property name associated with the SampleStatistics instance
     */
    public void setName(String name) { this.name = name; }
    /**
     *  Clone this instance
     *  @return A copy of this object with identical state.
     */
    @Override
    public synchronized Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw(new RuntimeException(e));
        }
        //return null;
    }
    
/**
* Return a String containing the Name, SamplingType, and the DataLine
* for this SampleStatistic.
* @see #getDataLine()
**/
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getName());
        buf.append(' ');
        buf.append('(');
        buf.append(this.getSamplingType());
        buf.append(")");
        buf.append(EOL);
        return buf.toString() + getDataLine();
    }
    /**
     * Returns the data line consisting of the count, min, max,
     * mean, variance, and standard deviation.
     *  @return A single line of space-separated, formatted data containing: count,
     *          min, max, mean, variance, std deviation
     **/
    public String getDataLine() {
        StringBuilder buf = new StringBuilder();
        buf.append(getCount());
        buf.append(' ');
        buf.append(numberFormat.format(getMinObs()));
        buf.append(' ');
        buf.append(numberFormat.format(getMaxObs()));
        buf.append(' ');
        buf.append(numberFormat.format(getMean()));
        buf.append(' ');
        buf.append(numberFormat.format(getVariance()));
        buf.append(' ');
        buf.append(numberFormat.format(getStandardDeviation()));
        
        return buf.toString();
    }
    
}

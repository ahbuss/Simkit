/*
 * AbstractSimpleStats.java
 *
 * Created on November 21, 2001, 2:14 PM
 */

package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;

/**
 *
 * @author  ahbuss
 * @version
 */
public abstract class AbstractSimpleStats implements SampleStatistics, Cloneable {
    
    protected  static String DEFAULT_NAME = "%unnamed%";
    protected static String DEFAULT_FORMAT = " 0.0000;-0.0000";
    
    protected int count;
    protected double minObs;
    protected double maxObs;
    protected  String name;
    protected DecimalFormat df;
    
    public AbstractSimpleStats() {
        this(DEFAULT_NAME);
    }
    
    public AbstractSimpleStats(String name) {
        this.setName(name);
        this.setFormat(DEFAULT_FORMAT);
        this.reset();
    }
    
    /**
     * @return The currrent running maximum observation.
     */
    public double getMaxObs() { return maxObs; }
    
    /**
     * @return The currrent running minumum observation.
     */
    public double getMinObs() { return minObs; }
    
    /**
     * @return The current number of observations.
     */
    public int getCount() { return count; }
    
    /**
     * Should reset/initialize all relevant counters.
     */
    public void reset() {
        count = 0;
        maxObs = Double.NEGATIVE_INFINITY;
        minObs = Double.POSITIVE_INFINITY;
    }
    
    /**
     * Add new observation and update counters (primitive).
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
     * This should normally have no effect, since each class should only implement
     * one SamplingType.
     * @param type The SamplingType (TALLY or TIME_VARYING).
     */
    public void setSamplingType(SamplingType type) {
    }
    
    /**
     * @param format The <CODE>DecimalFormat</CODE> String for default reporting
     */
    public void setFormat(String format) { df = new DecimalFormat(format); }
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
     * @return The Property name associated with the SampleStatistics instance
     */
    public String getName() { return name; }
    
    /**
     * @param The Property name associated with the SampleStatistics instance
     */
    public void setName(String name) { this.name = name; }
    /**
     *  Clone this instance
     *  @return A copy of this object with identical state.
     */
    public synchronized Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
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

package simkit.stat;

import simkit.Named;
import java.text.NumberFormat;

/**
 * Common interface for classes collecting statistics. Note that they
 * necessarily implement <CODE>PropertyChangeListener</CODE> so they can respond
 * to data fired in <CODE>PropertyChangeEvent</CODE>s.
 *
 * @version $Id$
 * @author ahbuss
 */
public interface SampleStatistics extends java.beans.PropertyChangeListener, Named {

    /**
     * String for line separator on the platform we are running on.
     */
    public static final String EOL = System.getProperty("line.separator", "\n");

    /**
     * Should reset/initialize all relevant counters.
     */
    public void reset();

    /**
     * Add new observation and update counters (primitive).
     *
     * @param newObs The new (primitive) observation
     */
    public void newObservation(double newObs);

    /**
     * Add new observation and update counters - normally will just call
     * newObservation(newObs.doubleValue()).
     *
     * @param newObs The new (Number) observation
     */
    public void newObservation(Number newObs);

    /**
     * Add a new Observation of type <code>boolean</code>. It is expected that
     * "true" corresponds to '1' and "false" to 0.
     *
     * @param newObs the boolean observation
     */
    public void newObservation(boolean newObs);

    /**
     * Add a new Observation of type <code>Boolean</code>. It is expected that
     * "true" corresponds to '1' and "false" to 0.
     *
     * @param newObs the Boolean observation
     */
    public void newObservation(Boolean newObs);

    /**
     * Gets the current value of the mean.
     *
     * @return The current mean.
     */
    public double getMean();

    /**
     * Gets the current value of the variance.
     *
     * @return The current variance.
     */
    public double getVariance();

    /**
     * Gets the current value of the standard deviation.
     *
     * @return The current standard deviation. Normally will return
     * Math.sqrt(getVariance());
     */
    public double getStandardDeviation();

    /**
     * Gets the number of observations so far.
     *
     * @return The current number of observations.
     */
    public int getCount();

    /**
     * Gets the minimum observed value so far.
     *
     * @return The current running minimum observation.
     */
    public double getMinObs();

    /**
     * Gets the maximum observed value so far.
     *
     * @return The current running maximum observation.
     */
    public double getMaxObs();

    /**
     * Sets the NumberFormat
     *
     * @param numberFormat the new DecimalFormat instance
     */
    public void setNumberFormat(NumberFormat numberFormat);

    /**
     * Sets the sampling type to either TALLY or TIME_VARYING. This should
     * normally have no effect, since each class should only implement one
     * SamplingType.
     *
     * @param type The SamplingType (TALLY or TIME_VARYING).
     */
    public void setSamplingType(SamplingType type);

    /**
     * Returns the SamplingType for this SampleStatistics.
     *
     * @return The SamplingType for the class. 
     */
    public SamplingType getSamplingType();

    @Override
    public String getName();

    @Override
    public void setName(String name);
}

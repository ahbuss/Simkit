package simkit.stat;
/**
 *  Common interface for classes collecting statistics.  Note that they necessarily
 *  implement <CODE>PropertyChangeListener</CODE> so they can respond to
 *  data fired in <CODE>PropertyChangeEvent</CODE>s.
 *  @author Arnold Buss
 *  @version $Date$
**/
public interface SampleStatistics extends java.beans.PropertyChangeListener {
/**
 *   String for line separator on the platform we are running on.
**/
    public static final String EOL = System.getProperty("line.separator", "\n");

/**
 *  Should reset/initialize all relevant counters.
**/
    public void reset();
/**
 *  Add new observation and update counters (primitive).
 *  @param newObs The new (primitive) observation
**/
    public void newObservation(double newObs);
/**
 *  Add new observation and update counters - normally will just call
 *  newObservation(newObs.doubleValue()).
 *  @param newObs The new (Number) observation
**/
    public void newObservation(Number newObs);
/**
 * Add a new Observation of type <code>boolean</code>.  It is expected that
 * "true" corresponds to '1' and "false" to 0.
 * @param newObs the boolean observation
 */    
    public void newObservation(boolean newObs);
    
/**
 * Add a new Observation of type <code>Boolean</code>.  It is expected that
 * "true" corresponds to '1' and "false" to 0.
 * @param newObs the boolean observation
 */    
    public void newObservation(Boolean newObs);
    
/**
 *  @return The current mean.
**/
    public double getMean();
/**
 *  @return The current variance.
**/
    public double getVariance();
/**
 *  @return The current standard deviation.  Normally will return
 *          Math.sqrt(getVariance());
**/
    public double getStandardDeviation();
/**
 *  @return The current number of observations.
**/
    public int getCount();
/**
 *  @return The currrent running minumum observation.
**/
    public double getMinObs();
/**
 *  @return The currrent running maximum observation.
**/
    public double getMaxObs();
/**
 *  @param format The <CODE>DecimalFormat</CODE> String for default reporting
**/
    public void setFormat(String format);
/**
 *  This should normally have no effect, since each class should only implement
 *  one SamplingType.
 *  @param type The SamplingType (TALLY or TIME_VARYING).
**/
    public void setSamplingType(SamplingType type);
/**
 *  @return The SamplingType for the class. 
**/
    public SamplingType getSamplingType();
/**
 *  @return The Property name associated with the SampleStatistics instance
 */
    public String getName();
/**
 *  @param name The property name associated with the SampleStatistics instance
 */    
    public void setName(String name);
}

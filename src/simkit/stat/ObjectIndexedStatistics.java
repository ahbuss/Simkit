package simkit.stat;

import java.util.Map;

/**
 *
 * @author ahbuss
 */
public interface ObjectIndexedStatistics {
        /**
     * Adds a new observation for the given member of the collection of
     * properties associated with this SampleStatistic.
     *
     * @param value The current value of the property.
     * @param source
*
     */
    public void newObservation(double value, Object source);

    /**
     * Adds a new observation for the given member of the collection of
     * properties associated with this SampleStatistic.
     *
     * @param newObs The new observation
     * @param source
*
     */
    public void newObservation(Number newObs, Object source);

    /**
     *
     * @param source
     * @return the current mean of the given property of given index.
     */
    public double getMean(Object source);

    /**
     *
     * @param source
     * @return the current variance of the given property of given index.
     */
    public double getVariance(Object source);

    /**
     *
     * @param source
     * @return the current standard deviation of the given index.
     */
    public double getStandardDeviation(Object source);

    /**
     *
     * @param source
     * @return the number of observations for the given property and index
     */
    public int getCount(Object source);

    /**
     *
     * @param source
     * @return the smallest value of the given property with given index.
     */
    public double getMinObs(Object source);

    /**
     *
     * @param source
     * @return the largest value of the given property with given index.
     */
    public double getMaxObs(Object source);

    /**
     * Gets the current mean of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return A Map containing the means.
     */
    public Map<Object, Double> getAllMean();

    /**
     * Gets the current variance of all of the properties in the indexed
     * property associated with this statistic.
     *
     * @return A Map containing the variances.
     */
    public Map<Object, Double>  getAllVariance();

    /**
     * Gets the current standard deviation of all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return A Map containing the standard deviations.
     */
    public Map<Object, Double>  getAllStandardDeviation();

    /**
     * Gets the current number of observations for all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return A Map containing the numbers of observations.
     */
    public Map<Object, Integer> getAllCount();

    /**
     * Gets the minimum value of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return A Map containing the minimums.
     */
    public Map<Object, Double>  getAllMinObs();

    /**
     * Gets the maximum value of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return A Map containing the maximums.
     */
    public Map<Object, Double> getAllMaxObs();

    /**
     * Gets the underlying SampleStatistics for all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return Map containing the SampleStatistics.
     */
    public Map<Object, SampleStatistics> getAllSampleStat();
    
}

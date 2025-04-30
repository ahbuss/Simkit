package simkit.stat;

/**
 * An interface for classes that collect statistics on indexed properties.
 *
 * @version $Id: IndexedSampleStatistics.java 1083 2008-06-11 20:41:21Z kastork
 * $
*
 */
public interface IndexedSampleStatistics extends SampleStatistics {

    /**
     * Adds a new observation for the given member of the collection of
     * properties associated with this SampleStatistic.
     *
     * @param value The current value of the property.
     * @param index The index of the property.
*
     */
    public void newObservation(double value, int index);

    /**
     * Adds a new observation for the given member of the collection of
     * properties associated with this SampleStatistic.
     *
     * @param newObs The new observation
     * @param index The index of the property.
*
     */
    public void newObservation(Number newObs, int index);

    /**
     *
     * @param index The index of the property.
     * @return the current mean of the given property of given index.
     */
    public double getMean(int index);

    /**
     *
     * @param index The index of the property.
     * @return the current variance of the given property of given index.
     */
    public double getVariance(int index);

    /**
     *
     * @param index The index of the property.
     * @return the current standard deviation of the given index.
     */
    public double getStandardDeviation(int index);

    /**
     *
     * @param index The index of the property.
     * @return the number of observations for the given property and index
     */
    public int getCount(int index);

    /**
     *
     * @param index The index of the property.
     * @return the smallest value of the given property with given index.
     */
    public double getMinObs(int index);

    /**
     *
     * @param index The index of the property.
     * @return the largest value of the given property with given index.
     */
    public double getMaxObs(int index);

    /**
     * Gets the current mean of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return An array containing the means.
     */
    public double[] getAllMean();

    /**
     * Gets the current variance of all of the properties in the indexed
     * property associated with this statistic.
     *
     * @return An array containing the variances.
     */
    public double[] getAllVariance();

    /**
     * Gets the current standard deviation of all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return An array containing the standard deviations.
     */
    public double[] getAllStandardDeviation();

    /**
     * Gets the current number of observations for all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return An array containing the numbers of observations.
     */
    public int[] getAllCount();

    /**
     * Gets the minimum value of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return An array containing the minimums.
     */
    public double[] getAllMinObs();

    /**
     * Gets the maximum value of all of the properties in the indexed property
     * associated with this statistic.
     *
     * @return An array containing the maximums.
     */
    public double[] getAllMaxObs();

    /**
     * Gets the underlying SampleStatistics for all of the properties in the
     * indexed property associated with this statistic.
     *
     * @return An array containing the SampleStatistics.
     */
    public SampleStatistics[] getAllSampleStat();
    
    /**
     * 
     * @return the number of distinct indices.
     */
    public int getIndexCount();
}

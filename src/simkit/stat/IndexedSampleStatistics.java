package simkit.stat;

/**
* An interface for classes that collect statistics on indexed properties.
**/
public interface IndexedSampleStatistics extends SampleStatistics {

/**
* Adds a new observation for the given member of the collection of 
* properties associated with this SampleStatistic.
* @param value The current value of the property.
* @param index The index of the property.
**/
    public void newObservation(double value, int index);

/**
* Adds a new observation for the given member of the collection of 
* properties associated with this SampleStatistic.
* @param value The current value of the property.
* @param index The index of the property.
**/
    public void newObservation(Number newObs, int index);

/**
* Gets the current mean of the given property.
* @param index The index of the property.
**/
    public double getMean(int index);

/**
* Gets the current variance of the given property.
* @param index The index of the property.
**/
    public double getVariance(int index);

/**
* Gets the current standard deviation of the given property.
* @param index The index of the property.
**/
    public double getStandardDeviation(int index);

/**
* Gets the number of observations for the given property.
* @param index The index of the property.
**/
    public int getCount(int index);

/**
* Gets the smallest value of the given property.
* @param index The index of the property.
**/
    public double getMinObs(int index);

/**
* Gets the largest value of the given property.
* @param index The index of the property.
**/
    public double getMaxObs(int index);

/**
* Gets the current mean of all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the means.
**/
    public double[] getAllMean();

/**
* Gets the current variance of all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the variances.
**/
    public double[] getAllVariance();

/**
* Gets the current standard deviation of all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the standard deviations.
**/
    public double[] getAllStandardDeviation();

/**
* Gets the current number of observations for all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the numbers of observations.
**/
    public int[] getAllCount();

/**
* Gets the minimum value of all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the minimums.
**/
    public double[] getAllMinObs();

/**
* Gets the maximum value of all of the properties in 
* the indexed property associated with this statistic.
* @return An array containing the maximums.
**/
    public double[] getAllMaxObs();

/**
* Gets the underlying SampleStatistics for all
* of the properties in the indexed property associated
* with this statistic.
* @return An array containing the SampleStatistics.
**/
    public SampleStatistics[] getAllSampleStat();
}

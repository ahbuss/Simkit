/*
 * Distribution.java
 *
 * Created on September 19, 2001, 4:01 PM
 */

package simkit.random;

/**
 * An interface for Classes that describe a random variable distribution.
 *
 * @author  Arnold Buss
 * 
 */
public interface Distribution extends simkit.Named, Cloneable {

    /**
     * 
     * @return the mean of this distribution.
     */
    public double getMean();
    
    /**
     * 
     * @param mean the mean of this distribution.
     */
    public void setMean(double mean);
    
    /**
     * 
     * @return the variance of this distribution.
     */
    public double getVariance();
    
    /**
     * 
     * @param variance the variance of this distribution.
     */
    public void setVariance(double variance);
    
    /**
     * 
     * @return the standard deviation of this distribution.
     */
    public double getStandardDeviation();
    
    /**
     * 
     * @param standardDeviation the standard deviation for this distribution.
     */
    public void setStandardDeviation(double standardDeviation);
    
    /**
     * 
     * @return the median for this distribution.
     */
    public double getMedian();
    
    /**
     * 
     * @return the minimum value possible for this distribution.
     */
    public double getMinimum();
    
    /**
     * 
     * @return the maximum value possible for this distribution.
     */
    public double getMaximum();
    
    /**
     * 
     * @param params the parameters necessary to define this distribution.
     * The meaning of these is determined by the implementation.
     */
    public void setCannonicalParameters(Object... params);
    
    /**
     * 
     * @return an array containing the parameters necessary to define this 
     * distribution. The meaning of these is determined by the implementation.
     */
    public Object[] getCannonicalParameters();
    
    /**
     * 
     * @param params the parameters necessary to define this distribution.
     * The meaning of these is determined by the implementation.
     */
    public void setParameters(Object... params);
    
    /**
     * 
     * @return an array containing the parameters necessary to define this 
     * distribution. The meaning of these is determined by the implementation.
     */
    public Object[] getParameters();
    
    /**
     * 
     * @param x Given value
     * @return the value of the Cumulative Distribution Function for the given
     * value of this distribution.
     */
    public double cdf(double x);
    
    /**
     * 
     * @param x Given value
     * @return the value of the complementary cumulative distribution function 
     * for the given value of this distribution.
     */
    public double ccdf(double x);
}

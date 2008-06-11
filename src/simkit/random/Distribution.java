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
 * @version $Id$
 */
public interface Distribution extends simkit.Named, Cloneable {

/**
* Returns the mean of this distribution.
**/
    public double getMean();
    
/**
* Sets the mean of this distribution.
**/
    public void setMean(double mean);
    
/**
* Returns the variance of this distribution.
**/
    public double getVariance();
    
/**
* Sets the variance of this distribution.
**/
    public void setVariance(double variance);
    
/**
* Gets the standard deviation of this distribution.
**/
    public double getStandardDeviation();
    
/**
* Sets the standard deviation for this distribution.
**/
    public void setStandardDeviation(double std);
    
/**
* Returns the median for this distribution.
**/
    public double getMedian();
    
/**
* Returns the minimum value possible for this distribution.
**/
    public double getMinimum();
    
/**
* Returns the maximum value possible for this distribution.
**/
    public double getMaximum();
    
/**
* Sets the parameters necessary to define this distribution.
* The meaning of these is determined by the implementation.
**/
    public void setCannonicalParameters(Object[] params);
    
/**
* Returns an array containing the parameters necessary to define this distribution.
* The meaning of these is determined by the implementation.
**/
    public Object[] getCannonicalParameters();
    
/**
* Sets the parameters necessary to define this distribution.
* The meaning of these is determined by the implementation.
**/
    public void setParameters(Object[] params);
    
/**
* Returns an array containing the parameters necessary to define this distribution.
* The meaning of these is determined by the implementation.
**/
    public Object[] getParameters();
    
/**
* Returns the value of the Cumulative Distribution Function for the given
* value of this distribution.
**/
    public double cdf(double x);
    
/**
* Returns the value of the complementary cumulative distribution function for the given
* value of this distribution.
**/
    public double ccdf(double x);
}

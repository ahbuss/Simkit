/*
 * ContinuousDistribution.java
 *
 * Created on September 19, 2001, 4:32 PM
 */

package simkit.random;

/**
 * An interface for Classes that describe a continuous random variable distribution.
 * 
 * @author  Arnold Buss
 * 
 */
public interface ContinuousDistribution extends Distribution{
    
    /**
     * 
     * @param x given value
     * @return the value of the probability density function at the given value.
     */
    public double pdf(double x);
    
}


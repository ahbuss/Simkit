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
 * @version $Id: ContinuousDistribution.java 443 2003-11-14 00:13:03Z jlruck $
 */
public interface ContinuousDistribution extends Distribution{
    
/**
* Returns the value of the probability density function at the given value.
**/
    public double pdf(double x);
    
}


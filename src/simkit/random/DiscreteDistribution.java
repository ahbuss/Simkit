/*
 * DiscreteDistribution.java
 *
 * Created on September 19, 2001, 4:42 PM
 */

package simkit.random;

/**
 * An interface for Classes that describe a descrete random variable.
 * 
 * @author  Arnold Buss
 * @version $Id$
 */
public interface DiscreteDistribution extends Distribution {
    
/**
* Returns the value of the probability mass function at the given value.
**/
    public double pmf(double x);
        
}


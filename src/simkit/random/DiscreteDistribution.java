/*
 * DiscreteDistribution.java
 *
 * Created on September 19, 2001, 4:42 PM
 */

package simkit.random;

/**
 * An interface for Classes that describe discrete random variables.
 * 
 * @author  Arnold Buss
 * @version $Id: DiscreteDistribution.java 466 2003-11-20 01:09:07Z jlruck $
 */
public interface DiscreteDistribution extends Distribution {
    
/**
* Returns the value of the probability mass function at the given value.
**/
    public double pmf(double x);
        
}


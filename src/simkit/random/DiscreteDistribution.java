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
 * 
 */
public interface DiscreteDistribution extends Distribution {
    
    /**
     * 
     * @param x given value
     * @return the value of the probability mass function at the given value.
     */
    public double pmf(double x);
        
}

